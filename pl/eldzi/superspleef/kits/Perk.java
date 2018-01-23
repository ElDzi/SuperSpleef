package pl.eldzi.superspleef.kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import pl.eldzi.core.util.updater.UpdateEvent;
import pl.eldzi.superspleef.Main;

public abstract class Perk implements Listener {
	private String name;
	private ItemStack item;
	private Item drop;
	private int slot;
	private Player player;
	private boolean used;

	public Perk(String name, ItemStack i, int slot, Player p) {
		this.name = name;
		item = i;
		this.player = p;
		this.slot = slot;
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInst());
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public Item getDrop() {
		return drop;
	}

	public int getSlot() {
		return slot;
	}

	public abstract void onRightClick(PlayerInteractEvent e);

	public abstract void onLeftClick(PlayerInteractEvent e);

	public abstract void onDrop(PlayerDropItemEvent e);

	public abstract void onPickup(PlayerPickupItemEvent e);

	public abstract void onUpdate(UpdateEvent e);

	public ItemStack getItem() {
		return item;
	}

	public String getName() {
		return name;
	}
}
