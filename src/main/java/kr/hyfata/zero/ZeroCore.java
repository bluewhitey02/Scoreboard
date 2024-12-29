package kr.hyfata.zero;

import kr.hyfata.zero.scoreboard.ZeroScoreBoard;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZeroCore extends JavaPlugin {

    @Override
    public void onEnable() {
        new ZeroScoreBoard(this);
        getLogger().info("Zero Scoreboard has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
