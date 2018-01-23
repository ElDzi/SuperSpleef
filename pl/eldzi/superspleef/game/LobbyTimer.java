package pl.eldzi.superspleef.game;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.eldzi.core.util.FireworkUtil;
import pl.eldzi.core.util.list.ElDziList;
import pl.eldzi.superspleef.Main;

public class LobbyTimer extends BukkitRunnable {
	private Game g;
	private int t;
	private ElDziList<Integer> nums;

	public LobbyTimer(Game game) {
		g = game;
		t = 20;
		nums = new ElDziList<>();
		nums.add(1, 2, 3, 4);
		for (int k = 0; k < 1000; k += 5)
			nums.add(k);
	}

	public int getT() {
		return t;
	}

	@Override
	public void run() {
		if (t == 0) {
			g.startGame();
			cancel();
		} else if (t > 0) {
			if (nums.contains(t))
				msg("&cGra wystartuje za " + t + " sekund!");
			for (Player p : g.getPlayers()) {
				p.setExp(0);
				p.setLevel(t);
			}
			if (t % 2 == 0) {
				Main.getInst().getFireworkUtils().detonateFirework(
						FireworkUtil.launchRandomFirework(g.getRandom().getLocation().clone().add(0, 1.4, 0)));
			}
		}
		t--;
	}

	public void msg(String msg) {
		g.broadcast(msg);
	}
}
