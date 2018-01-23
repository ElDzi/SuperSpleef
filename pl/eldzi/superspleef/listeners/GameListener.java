package pl.eldzi.superspleef.listeners;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import pl.eldzi.core.items.ItemBuilder;
import pl.eldzi.core.miniplugins.moreevents.PlayerDamageEvent;
import pl.eldzi.core.miniplugins.moreevents.PlayerJumpEvent;
import pl.eldzi.core.util.ItemUtils;
import pl.eldzi.core.util.PlayerUtils;
import pl.eldzi.core.util.Sounds;
import pl.eldzi.core.util.updater.UpdateEvent;
import pl.eldzi.core.util.updater.UpdateType;
import pl.eldzi.superspleef.Main;
import pl.eldzi.superspleef.game.BorderPlatform;
import pl.eldzi.superspleef.game.Game;
import pl.eldzi.superspleef.game.Game.GameState;
import pl.eldzi.superspleef.manager.GamesUtils;
import pl.eldzi.superspleef.manager.PlayerManager;
import pl.eldzi.superspleef.players.GamePlayer;

public class GameListener implements Listener {
	private Game g;

	public GameListener(Game g) {
		this.g = g;
	}

	private boolean isGame(Player p) {
		Game g = GamesUtils.getGameByPlayer(p);
		if (g == null)
			return false;
		if (g.getPlayers().contains(p))
			return true;
		return false;
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!isGame(p))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		Player p = (Player) e.getEntity();
		if (!isGame(p))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (!isGame(p))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onJump(PlayerJumpEvent e) {
		Player p = e.getPlayer();
		if (!isGame(p))
			return;
		Vector jump = p.getLocation().getDirection().multiply(0.1).setY(1.1);
		p.setVelocity(p.getVelocity().add(jump));
	}

	private ItemStack d = new ItemBuilder(Material.GOLD_SPADE).setTitle(PlayerUtils.fixColor("&cDESTRUKTOR")).build();
	private ItemStack p = new ItemBuilder(Material.getMaterial(2262)).setTitle(PlayerUtils.fixColor("&cBlokHelper"))
			.addEnchantment(Enchantment.SILK_TOUCH, 20).build();
	ItemStack j = new ItemBuilder(Material.SLIME_BLOCK).setTitle(PlayerUtils.fixColor("&cJumper"))
			.addEnchantment(Enchantment.SILK_TOUCH, 20).build();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (!isGame(p))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!isGame(p))
			return;
		Game g = GamesUtils.getGameByPlayer(p);
		GamePlayer gP = PlayerManager.getPlayer(p);
		ItemStack is = e.getItem();
		Action a = e.getAction();
		e.setCancelled(true);
		if (a == Action.LEFT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			if (b == null)
				return;
			if (!PlayerUtils.isInRegion(g.getConfig().getCorner().getA(), g.getConfig().getCorner().getB(),
					b.getLocation()))
				return;
			System.out.println(DyeColor.getByDyeData(b.getData()).name());
			g.changeBlock(b);
			PlayerManager.getPlayer(p).setBrokenBlock(PlayerManager.getPlayer(p).getBrokenBlock() + 1);
			return;
		} else {
			if (ItemUtils.isSameItem(is, d)) {
				Snowball k = p.launchProjectile(Snowball.class);
				k.setShooter(p);
				k.setMetadata("owner", new FixedMetadataValue(Main.getInst(), p.getName()));
				p.getWorld().playSound(p.getEyeLocation(), Sounds.CHICKEN_EGG_POP.bukkitSound(), 1f, 1f);
			} else if (ItemUtils.isSameItem(is, this.p)) {
				if (gP.isBlokHelper()) {
					Location under = p.getLocation().clone().add(0, -1, 0);
					p.setVelocity(new Vector(0, 0, 0));
					BorderPlatform bp = new BorderPlatform(under);
					gP.useBlockHelper();
					PlayerUtils.sendMsg(p, "&cUzyles blok helpera!");

				} else {
					PlayerUtils.sendMsg(p,
							"&cNie mozesz tego uzyc przez " + p.getInventory().getItem(8).getAmount() + " sekund!");
				}
			} else if (ItemUtils.isSameItem(is, j)) {
				if (gP.isJumper()) {
					p.setVelocity(new Vector(0, 1, 0));
					gP.useJumper();
					PlayerUtils.sendMsg(p, "&cUzyles jumpera!");
				} else {
					PlayerUtils.sendMsg(p,
							"&cNie mozesz tego uzyc przez " + p.getInventory().getItem(7).getAmount() + " sekund!");
				}
			}

		}
	}

	@EventHandler
	public void onUpdate(UpdateEvent e) {
		if (e.getType() != UpdateType.SEC)
			return;
		if (g.getGameState() == GameState.BATTLE) {
			if (g.getPlayers().size() == 1) {
				Player winner = g.getPlayers().get(0);
				g.setWinner(winner);
			}
		}
		if (g.getPlayers() == null)
			return;
		if (g.getPlayers().isEmpty())
			return;
		try {
			for (Player p : g.getPlayers()) {
				if (!PlayerUtils.isInRegion(g.getConfig().getCorner().getA(), g.getConfig().getCorner().getB(),
						p.getLocation())) {
					if (g.getGameState() == GameState.BATTLE) {
						g.leave(p);
						g.msg(p, "&cSpadles!");
						g.broadcast("&cGracz " + p.getName() + " wypadl poza mape!");
					} else {
						p.teleport(g.getConfig().getPlayerSpawn());
					}
				}
			}
		} catch (Exception ae) {

		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (!isGame(p))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (!isGame(p))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		Block b = e.getHitBlock();
		Projectile p = e.getEntity();
		if (!p.hasMetadata("owner"))
			return;
		if (b == null)
			return;
		if (!PlayerUtils.isInRegion(g.getConfig().getCorner().getA(), g.getConfig().getCorner().getB(),
				b.getLocation()))
			return;
		if (p.getShooter() instanceof Player) {
			Player pl = (Player) p.getShooter();
			PlayerManager.getPlayer(pl).setBrokenBlock(PlayerManager.getPlayer(pl).getBrokenBlock() + 1);
			g.changeBlock(b);
			return;
		}
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onDamage(PlayerDamageEvent e) {
		Player p = e.getPlayer();
		if (!isGame(p))
			return;
		e.setCancelled(true);
	}

}
