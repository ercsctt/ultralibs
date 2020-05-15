package uk.co.ericscott.ultralibs.redis;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import uk.co.ericscott.ultralibs.redis.annotation.RedisHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* Credit to Ptech for this cool redis messenger
*/

@Getter
public class RedisMessenger {

    private JedisPool jedisPool;
    private JavaPlugin plugin;
    private Set<Object> listeners = new HashSet<>();

    private Gson gson;

    public RedisMessenger(JavaPlugin plugin, String host, int port, int timeout, String password) {
        this.plugin = plugin;
        this.gson = new Gson();

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(20);

        if(password == null) {
            // no password specified
            jedisPool = new JedisPool(config, host, port, timeout);
        }else{
            jedisPool = new JedisPool(config, host, port, timeout, password);
        }
    }

    public RedisMessenger(JavaPlugin plugin, String host, int port, int timeout) {
        this(plugin, host, port, timeout, null);
    }

    public void initialize() {

        //In order to avoid subscribing twice to the same redis channel, we add a simple set.
        Set<String> subscribedChannels = Sets.newHashSet();
        Map<String, ImmutablePair<Object, Method>> map = Maps.newHashMap();

        //We schedule an asynchronous task to handle our subscriptions.
        listeners.forEach(listener -> {
            //After looping through each listener, we get that listener's methods, and try to find where the RedisHandler annotation is used, we add that to a set.
            Set<Method> methods = getMethodsOfAnnotation(listener.getClass(), RedisHandler.class);

            for (Method method : methods) {
                //For each of these sets, we get the redis handler, check if we're already subscribed, if not, we subscribe to the channel.
                RedisHandler handler = method.getAnnotation(RedisHandler.class);
                if (!subscribedChannels.contains(handler.value())) {
                    map.put(handler.value(), new ImmutablePair<>(listener, method));
                    subscribedChannels.add(handler.value());
                }
            }
        });

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {

                        if(map.containsKey(channel)){
                            ImmutablePair<Object, Method> pair = map.get(channel);
                            try {
                                pair.getValue().invoke(pair.getKey(), deserialize(message));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, subscribedChannels.toArray(new String[0]));
            }
        });
    }

    public void send(String channel, Map<String, Object> message, boolean async) {
        if (!async) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, serialize(message));
            }
        } else {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try (Jedis jedis = jedisPool.getResource())
                {
                    jedis.publish(channel, serialize(message));
                }
            });
        }
    }

    public void send(String channel, Map<String, Object> message) {
        send(channel, message, true);
    }

    public void registerListeners(Object... objects) {
        for (Object object : objects) {
            getListeners().add(object);
        }
    }

    private Set<Method> getMethodsOfAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public Map<String, Object> deserialize(String string) {
        return gson.fromJson(string, new HashMap<String, Object>().getClass());
    }

    public String serialize(Map<String, Object> map) {
        return gson.toJson(map);
    }
}
