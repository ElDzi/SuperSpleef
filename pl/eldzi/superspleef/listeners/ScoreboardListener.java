package pl.eldzi.superspleef.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import pl.eldzi.core.util.MathUtils;
import pl.eldzi.core.util.scoreboard.GameScoreboard;
import pl.eldzi.core.util.updater.UpdateEvent;
import pl.eldzi.core.util.updater.UpdateType;
import pl.eldzi.superspleef.game.Game;
import pl.eldzi.superspleef.game.Game.GameState;
import pl.eldzi.superspleef.manager.PlayerManager;

public class ScoreboardListener implements Listener {
	private Game g;

	public ScoreboardListener(Game g) {
		this.g = g;
	}

	public final ChatColor[] colors = new ChatColor[] { ChatColor.RED, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
			ChatColor.DARK_AQUA, ChatColor.AQUA, ChatColor.GREEN, ChatColor.YELLOW };

	public ChatColor getRandom() {
		return colors[MathUtils.r(colors.length)];
	}

	@EventHandler
	public void onUpdate(UpdateEvent e) {
		if (e.getType() != UpdateType.FASTER)
			return;
		g.setScoreboard(new GameScoreboard(g, "&c&lSUPERSPLEEF"));
		GameScoreboard gs = g.getScoreboard();
		ChatColor c = getRandom();
		if (g.getGameState() == GameState.LOBBY) {
			if (g.getLt() != null) {
				gs.addLine(c + "✘✘✘✘✘✘");
				gs.addBlankSpace();
				gs.addLine("&cArena wystartuje");
				gs.addLine(" &aza " + g.getLt().getT());
				gs.addBlankSpace();
				gs.addLine(c + "✘✘✘✘✘✘");
				for (Player p : g.getPlayers())
					p.setScoreboard(gs.getScoreboard());
			} else {
				gs.addLine(c + "✘✘✘✘✘✘");
				gs.addBlankSpace();
				gs.addLine("&cArena wystartuje");
				gs.addLine(" &aWkrotce...");
				gs.addBlankSpace();
				gs.addLine(c + "✘✘✘✘✘✘");
				for (Player p : g.getPlayers())
					p.setScoreboard(gs.getScoreboard());
			}
		} else if (g.getGameState() == GameState.BATTLE) {
			for (Player p : g.getPlayers()) {
				GameScoreboard gsa = new GameScoreboard(g, "&cGra " + g.getName());
				gsa.addLine(c + "✘✘✘✘✘✘");
				gsa.addBlankSpace();
				gsa.addLine("&cDo konca");
				gsa.addLine(" &a" + g.secToEnd() + " sek");
				gsa.addBlankSpace();
				gsa.addLine("&cPozostalo");
				gsa.addLine(" &a" + g.getPlayers().size() + " graczy");
				gsa.addBlankSpace();
				gsa.addLine("&cZniszczyles");
				gsa.addLine(" &a" + PlayerManager.getPlayer(p).getBrokenBlock() + " blokow");
				gsa.addBlankSpace();
				gsa.addLine(c + "✘✘✘✘✘✘");
				p.setScoreboard(gsa.getScoreboard());
			}
		} else {
			gs.addLine(c + "✘✘✘✘✘✘");
			gs.addBlankSpace();
			if (g.getWinner() != null) {
				gs.addLine("&cZwyciezyl");

				gs.addLine(" " + g.getWinner().getName() + "");
			}
			gs.addBlankSpace();
			gs.addLine(getRandom() + "Gratulacje!");
			gs.addLine(c + "✘✘✘✘✘✘");
			for (Player p : g.getPlayers()) {
				p.setScoreboard(gs.getScoreboard());
			}
		}
	}
}
