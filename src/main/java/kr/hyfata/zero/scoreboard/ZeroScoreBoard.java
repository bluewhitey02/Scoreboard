package kr.hyfata.zero.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class ZeroScoreBoard {
    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public ZeroScoreBoard(JavaPlugin plugin) {
        getServer().getPluginManager().registerEvents(new ScoreboardListener(this), plugin);

        getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (FastBoard board : this.boards.values()) {
                updateBoard(board);
            }
        }, 0, 20);
    }

    public void createScoreboard(Player player) {
        FastBoard board = new FastBoard(player);
        board.updateTitle("§eZERO RPG");

        this.boards.put(player.getUniqueId(), board);
    }

    public void removeScoreboard(Player player) {
        FastBoard board = this.boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }

    private void updateBoard(FastBoard board) {
        board.updateLines(
                "",
                "§7온라인 플레이어: " + getServer().getOnlinePlayers().size(),
                "",
                "Kills: " + board.getPlayer().getStatistic(Statistic.PLAYER_KILLS),
                ""
        );
    }
}
