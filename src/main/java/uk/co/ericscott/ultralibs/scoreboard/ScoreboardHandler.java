package uk.co.ericscott.ultralibs.scoreboard;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ScoreboardHandler implements Listener
{

    private final Map<UUID, BukkitTask> tasks;
    private ScoreboardProvider provider;
    private String title;

    private final Plugin plugin;

    public ScoreboardHandler(Plugin plugin){
        this.plugin = plugin;

        tasks = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public ScoreboardHandler setProvider(ScoreboardProvider provider){
        this.provider = provider;
        return this;
    }

    public ScoreboardHandler setTitle(String title){
        this.title = title;

        for(Player player : tasks.keySet().stream().map(i -> plugin.getServer().getPlayer(i)).collect(Collectors.toList())){
            Scoreboard scoreboard = player.getScoreboard();
            Objective objective = scoreboard.getObjective("board");

            if(objective != null){
                objective.setDisplayName(title);
            }
        }

        return this;
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Scoreboard scoreboard;

        if((scoreboard = player.getScoreboard()).equals(plugin.getServer().getScoreboardManager().getMainScoreboard())){
            player.setScoreboard(scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard());
        }

        Objective objective = scoreboard.getObjective("board");
        if(objective != null) objective.unregister();
        objective = scoreboard.registerNewObjective("board", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(title);

        for(int i = 0; 15 > i; i++){
            String teamName = "board-" + i;

            Team team = scoreboard.getTeam(teamName);
            if(team != null){
                team.unregister();
            }

            team = scoreboard.registerNewTeam(teamName);
            String identifier = new String(new char[]{ChatColor.COLOR_CHAR, ChatColor.values()[i].getChar(), ChatColor.COLOR_CHAR, ChatColor.RESET.getChar()});

            team.setDisplayName(identifier);
            team.addEntry(identifier);
        }

        Scoreboard finalScoreboard = scoreboard;
        Objective finalObjective = objective;

        BukkitTask task = new BukkitRunnable(){

            @Override
            public void run(){
                if(!player.isOnline()){
                    cancel();
                    return;
                }

                if(provider == null){
                    finalScoreboard.getEntries().forEach(finalScoreboard::resetScores);
                    return;
                }

                List<String> lines = Lists.reverse(provider.provide(player));

                for(int score = 0; 15 > score; score++){
                    Team team = finalScoreboard.getTeam("board-" + score);
                    String identifier = team.getDisplayName();

                    if(lines.size() > score){
                        String line = lines.get(score);

                        if (line.length() > 16){
                            int splitAt = line.charAt(15) == ChatColor.COLOR_CHAR ? 15 : 16;
                            String prefix = line.substring(0, splitAt);
                            String suffix = ChatColor.getLastColors(prefix) + line.substring(splitAt);

                            if(!team.getPrefix().equals(prefix)){
                                team.setPrefix(prefix);
                            }

                            if(!team.getSuffix().equals(suffix)){
                                if(suffix.length() > 16) {
                                    team.setSuffix(suffix.substring(0, 15));
                                } else {
                                    team.setSuffix(suffix);
                                }
                            }
                        } else {
                            if(!team.getPrefix().equals(line)){
                                team.setPrefix(line);
                            }

                            if(!team.getSuffix().isEmpty()){
                                team.setSuffix("");
                            }
                        }

                        finalObjective.getScore(identifier).setScore(score + 1);
                    }else if(finalScoreboard.getScores(identifier).isEmpty()){
                        break;
                    }else{
                        finalScoreboard.resetScores(identifier);
                        team.setPrefix("");
                        if(!team.getSuffix().isEmpty()){
                            team.setSuffix("");
                        }
                    }
                }

            }

        }.runTaskTimerAsynchronously(plugin, 2L, 2L);

        tasks.put(player.getUniqueId(), task);
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event){
        BukkitTask task = tasks.remove(event.getPlayer().getUniqueId());
        if(task != null){
            task.cancel();
        }
    }

}
