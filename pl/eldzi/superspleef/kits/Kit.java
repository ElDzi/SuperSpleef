package pl.eldzi.superspleef.kits;

import org.bukkit.inventory.ItemStack;

import pl.eldzi.core.util.list.ElDziList;

public class Kit {
	public Kit(String name, Perk[] perks, ItemStack[] items) {
		this.name = name;
		this.perks.add(perks);
		this.items.add(items);
	}

	private String name;
	private ElDziList<Perk> perks = new ElDziList<>();
	private ElDziList<ItemStack> items = new ElDziList<>();

	public String getName() {

		return name;
	}
}
