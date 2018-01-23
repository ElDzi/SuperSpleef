package pl.eldzi.superspleef.game;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.eldzi.core.util.list.ElDziList;
import pl.eldzi.superspleef.game.Game.GameState;

public class GameTimer extends BukkitRunnable {
	private Game g;
	private int t;
	private ElDziList<Integer> nums;

	public GameTimer(Game game) {
		g = game;
		t = 60 * 5;
		nums = new ElDziList<>();
		nums.add(1, 2, 3, 4, 5, 10);
		for (int k = 0; k < t; k += 30)
			nums.add(k);
		g.setGameState(GameState.BATTLE);
		g.msgTitle("&cGRA", "&eWYSTARTOWALA");
		for (Player p : g.getPlayers()) {
			p.setScoreboard(g.getScoreboard().getScoreboard());
			p.teleport(g.getConfig().getPlayerSpawn());
			g.equip(p);
		}
		for (Entity e : g.getConfig().getPlayerSpawn().getWorld().getEntities()) {
			if (!(e instanceof Player))
				e.remove();
		}
	}

	public int getT() {
		return t;
	}

	@Override
	public void run() {
		if (t == 0) {
			g.stop();
			cancel();
		} else if (t > 0) {
			if (nums.contains(t))
				msg("&cGra zakonczy sie za " + t + " sekund!");

		}

		if (g.getWinner() != null) {
			g.stop();
			cancel();
		}
		t--;
	}

	public void msg(String msg) {
		g.broadcast(msg);
	}
}
