package pl.eldzi.superspleef.game;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import pl.eldzi.core.items.ItemBuilder;
import pl.eldzi.core.util.ItemUtils;
import pl.eldzi.core.util.PlayerUtils;
import pl.eldzi.superspleef.manager.GamesUtils;
import pl.eldzi.superspleef.manager.PlayerManager;
import pl.eldzi.superspleef.players.GamePlayer;

public class RegenerationTimer extends BukkitRunnable {
	ItemStack platformHelper = new ItemBuilder(Material.getMaterial(2262))
			.setTitle(PlayerUtils.fixColor("&cBlokHelper")).addEnchantment(Enchantment.SILK_TOUCH, 20).build();
	ItemStack jumper = new ItemBuilder(Material.SLIME_BLOCK).setTitle(PlayerUtils.fixColor("&cJumper"))
			.addEnchantment(Enchantment.SILK_TOUCH, 20).build();
	private Player p;
	private ItemStack item;
	private int t;
	private GamePlayer gamePl;

	public RegenerationTimer(Player p, ItemStack cur) {
		this.p = p;
		this.item = cur;
		gamePl = PlayerManager.getPlayer(p);
		if (item == null)
			cancel();
		if (ItemUtils.isSameItem(item, platformHelper))
			t = 20;
		else if (ItemUtils.isSameItem(cur, jumper))
			t = 10;
	}

	@Override
	public void run() {
		if (p == null) {
			cancel();
			return;
		}
		if (GamesUtils.getGameByPlayer(p) == null) {
			if (ItemUtils.isSameItem(item, platformHelper)) {
				p.getInventory().setItem(8, null);
			} else if (ItemUtils.isSameItem(item, jumper)) {
				jumper.setAmount(t);
				p.getInventory().setItem(7, null);
			}
			cancel();

			return;
		}
		if (t >= 0) {
			if (ItemUtils.isSameItem(item, platformHelper)) {
				platformHelper.setAmount(t);
				p.getInventory().setItem(8, platformHelper);
			} else if (ItemUtils.isSameItem(item, jumper)) {
				jumper.setAmount(t);
				p.getInventory().setItem(7, jumper);
			}
		}
		if (t == -1) {
			if (ItemUtils.isSameItem(item, platformHelper)) {
				platformHelper.setAmount(1);
				p.getInventory().setItem(8, platformHelper);
				PlayerUtils.sendMsg(p, "&cBlokHelper jest juz dostepny!");
				gamePl.setBlokHelper(true);
			} else if (ItemUtils.isSameItem(item, jumper)) {
				jumper.setAmount(1);
				p.getInventory().setItem(7, jumper);
				PlayerUtils.sendMsg(p, "&cJumper jest juz dostepny!");
				gamePl.setJumper(true);
			}
		}
		t--;
	}

}
