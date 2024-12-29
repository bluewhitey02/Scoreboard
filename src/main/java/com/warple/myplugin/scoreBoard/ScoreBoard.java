package com.warple.myplugin.scoreBoard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ScoreBoard implements Listener {

    private final VaultLoader plugin;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Economy econ;

    public ScoreBoard(VaultLoader plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("정보", Criteria.DUMMY, Component.text("서버 정보").color(NamedTextColor.YELLOW));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        new BukkitRunnable() {
            @Override
            public void run() {
                updateScoreboard();
            }
        }.runTaskTimer(plugin, 0, 20);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setScoreboard(scoreboard);
        updateScoreboard();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        scoreboard.resetScores(player.getName()); // 플레이어 퇴장 시 스코어보드에서 제거
        updateScoreboard();
    }

    private void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            objective.getScore((OfflinePlayer) Component.text("플레이어:").color(NamedTextColor.GREEN)).setScore(Bukkit.getOnlinePlayers().size());
            objective.getScore((OfflinePlayer) Component.text("이름: " + player.getName()).color(NamedTextColor.AQUA)).setScore(0);
            if (econ != null) {
                double balance = econ.getBalance(player);
                objective.getScore((OfflinePlayer) Component.text("돈: " + balance).color(NamedTextColor.GOLD)).setScore(0);
            }
        }
    }
}