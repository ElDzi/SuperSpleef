package pl.eldzi.superspleef.kits;

import org.bukkit.inventory.ItemStack;

import pl.eldzi.core.util.ItemUtils;
import pl.eldzi.core.util.list.ElDziList;

public class PerksListener {

	private static ElDziList<Perk> perks = new ElDziList<>();

	public static ElDziList<Perk> getPerks() {
		return perks;
	}

	public static boolean isPerk(ItemStack is) {
		for (Perk p : perks)
			if (ItemUtils.isSameItem(is, p.getItem()))
				return true;
		return false;
	}
}
