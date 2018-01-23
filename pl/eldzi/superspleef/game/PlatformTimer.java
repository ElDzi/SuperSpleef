package pl.eldzi.superspleef.game;

import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import pl.eldzi.core.util.PlatformBlocks;

public class PlatformTimer extends BukkitRunnable {
	private Platform pla;

	public PlatformTimer(Platform platform) {
		this.pla = platform;
		t = 3;
	}

	int t;

	@Override
	public void run() {
		if (t == 3) {
			pla.build(Material.STAINED_CLAY.getId(), PlatformBlocks.YELLOW.toByte());

		} else if (t == 2) {
			pla.build(Material.STAINED_CLAY.getId(), PlatformBlocks.ORANGE.toByte());
		} else if (t == 1) {
			pla.build(Material.STAINED_CLAY.getId(), PlatformBlocks.RED.toByte());
		} else if (t <= 0) {
			pla.remove();
			this.cancel();
			return;
		}
		t--;
	}

}
