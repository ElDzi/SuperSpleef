
package pl.eldzi.superspleef.game;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import pl.eldzi.core.util.PlatformBlocks;
import pl.eldzi.core.util.list.ElDziList;
import pl.eldzi.superspleef.Main;

public class BorderPlatform implements Platform {

	Location s;
	public ElDziList<Block> blocks = new ElDziList<>();

	public BorderPlatform(Location srodek) {
		s = srodek;

		int x = (int) s.getX();
		int y = (int) s.getY();
		int z = (int) s.getZ();

		// krzy¿
		Block b = s.getWorld().getBlockAt(x - 1, y, z);
		Block c = s.getWorld().getBlockAt(x + 1, y, z);
		Block d = s.getWorld().getBlockAt(x, y, z - 1);
		Block e = s.getWorld().getBlockAt(x, y, z + 1);
		// naro¿nik
		Block sr = s.getWorld().getBlockAt(x, y, z);
		Block f = s.getWorld().getBlockAt(x + 1, y, z + 1);
		Block g = s.getWorld().getBlockAt(x + 1, y, z - 1);
		Block h = s.getWorld().getBlockAt(x - 1, y, z - 1);
		Block i = s.getWorld().getBlockAt(x - 1, y, z + 1);
		blocks.add(b, c, d, e, f, g, h, i, sr);
		for (Block ff : blocks) {
			if (ff.getType() != Material.AIR) {
				Main.getInst().getBlockRestore().Add(b, 0, (byte) 0, System.currentTimeMillis() * 5);
			}
		}
		build(Material.STAINED_CLAY.getId(), PlatformBlocks.GREEN.toByte());
		new PlatformTimer(this).runTaskTimer(Main.getInst(), 20, 20);
	}

	@Override
	public void build(int id, byte data) {
		for (Block b : blocks) {

			b.setTypeIdAndData(id, data, false);
			b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

		}

	}

	@Override
	public void remove() {
		for (Block b : blocks) {
			b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
			b.setType(Material.AIR);

		}

	}
}
