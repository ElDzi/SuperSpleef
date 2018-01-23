package pl.eldzi.superspleef.game;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.eldzi.core.util.FireworkUtil;
import pl.eldzi.core.util.MathUtils;
import pl.eldzi.superspleef.Main;
import pl.eldzi.superspleef.game.Game.GameState;

public class EndTimer extends BukkitRunnable {
	private Game g;
	private int t;

	public EndTimer(Game game) {
		g = game;
		t = 10;
		g.rollback();
		for (Player p : g.getPlayers()) {
			p.teleport(g.getConfig().getLobbyEnd());

		}
		g.setGameState(GameState.STOPING);
	}

	public final ChatColor[] colors = new ChatColor[] { ChatColor.RED, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
			ChatColor.DARK_AQUA, ChatColor.AQUA, ChatColor.GREEN, ChatColor.YELLOW };

	public ChatColor getRandom() {
		return colors[MathUtils.r(colors.length)];
	}

	@Override
	public void run() {
		if (t > 0) {
			for (Player p : g.getPlayers()) {
				Main.getInst().getFireworkUtils()
						.detonateFirework(FireworkUtil.launchRandomFirework(p.getEyeLocation().add(0, 0.3, 0)));
			}
			if (g.getWinner() != null) {
				g.msgTitle(getRandom() + "ZWYCIEZA", getRandom() + "DRUZYNA " + g.getWinner().getName());

			}
		}
		if (t == 0) {
			g.stopGame(true);
			cancel();
			return;
		}
		t--;
	}
}
