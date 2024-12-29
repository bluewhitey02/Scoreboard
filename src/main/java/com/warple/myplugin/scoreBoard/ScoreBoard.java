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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScoreBoard implements Listener {

    private final VaultLoader plugin;
    private final Economy econ;
    private final Map<Player, Scoreboard> playerScoreboards = new HashMap<>();

    public ScoreBoard(VaultLoader plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        new BukkitRunnable() {
            @Override
            public void run() {
                updateScoreboardsAsync();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createScoreboard(player);
        updateOnlinePlayersCount();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerScoreboards.remove(player);
        updateOnlinePlayersCount();
    }

    private void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("정보", Criteria.DUMMY, Component.text("서버 정보").color(NamedTextColor.YELLOW));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        playerScoreboards.put(player, scoreboard);

        Bukkit.getScheduler().runTask(plugin, () -> player.setScoreboard(scoreboard));

        updatePlayerInfo(player, objective);
    }

    private void updateScoreboardsAsync() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = playerScoreboards.get(player);
            if (scoreboard != null) {
                Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
                if (objective != null) {
                    updatePlayerInfo(player, objective);
                }
            }
        }
    }

    private void updateOnlinePlayersCount() {
        for (Player p : Bukkit.getOnlinePlayers()){
            Scoreboard scoreboard = playerScoreboards.get(p);
            if (scoreboard != null) {
                Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
                if (objective != null) {
                    updatePlayerInfo(p, objective);
                }
            }
        }
    }

    private void updatePlayerInfo(Player player, Objective objective) {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String playerName = player.getName();
        double balance = econ != null ? econ.getBalance(player) : 0.0;

        for(String entry : Objects.requireNonNull(objective.getScoreboard()).getEntries()){
            objective.getScoreboard().resetScores(entry);
        }

        objective.getScore((OfflinePlayer) Component.text("플레이어:").color(NamedTextColor.GREEN)).setScore(onlinePlayers);
        objective.getScore((OfflinePlayer) Component.text("이름: " + playerName).color(NamedTextColor.AQUA)).setScore(0);
        objective.getScore((OfflinePlayer) Component.text("돈: " + balance).color(NamedTextColor.GOLD)).setScore(-1);
    }
}