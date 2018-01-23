package pl.eldzi.superspleef.game;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import pl.eldzi.core.util.BlockUtils;
import pl.eldzi.core.util.MapUtil;
import pl.eldzi.superspleef.Main;

public class Corner {
	private Location a, b;

	public Corner(Location a, Location b) {
		this.a = a;
		this.b = b;
	}

	public void removeWall() {
		ArrayList<Block> blocks = BlockUtils.getInBoundingBox(a, b);
		for (Block b : blocks) {
			Main.getInst().getBlockRestore().Add(b, 0, (byte) 0, System.currentTimeMillis() * 5);
			Main.getInst().getMapUtil().quickChangeBlockAt(b.getLocation(), Material.AIR);
		}
	}

	public Location getA() {
		return a;
	}

	public Location getB() {
		return b;
	}

	public Location getMiddle() {
		Location a = getA().clone();
		Location b = getB().clone();
		Location spawn = a.add(a.subtract(b).multiply(0.5));
		spawn.setY(MapUtil.getHighestBlock(spawn.getWorld(), spawn.getBlockX(), spawn.getBlockZ()));
		return spawn;
	}

}
