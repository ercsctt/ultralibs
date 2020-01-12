package uk.co.ericscott.ultralibs.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardProvider{

    List<String> provide(Player player);

}
