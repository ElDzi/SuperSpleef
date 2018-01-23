package pl.eldzi.superspleef.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import pl.eldzi.core.util.updater.UpdateEvent;
import pl.eldzi.core.util.updater.UpdateType;
import pl.eldzi.superspleef.game.Game;
import pl.eldzi.superspleef.game.Game.GameState;
import pl.eldzi.superspleef.manager.GameManager;
import pl.eldzi.superspleef.manager.GamesUtils;

public class CornerListener implements Listener {
	public static HashMap<Player, Location> point1 = new HashMap<Player, Location>();
	public static HashMap<Player, Location> point2 = new HashMap<Player, Location>();

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		Action a = e.getAction();
		if (a == Action.RIGHT_CLICK_BLOCK || a == Action.LEFT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			if (p.getItemInHand().getTypeId() == Material.DIAMOND_HOE.getId()) {
				if (a == Action.LEFT_CLICK_BLOCK) {
					point1.put(p, b.getLocation());
					p.sendMessage(ChatColor.GREEN + "Zaznaczyles punkt pierwszy!");
				}

				if (a == Action.RIGHT_CLICK_BLOCK) {
					point2.put(p, p.getLocation());
					p.sendMessage(ChatColor.GREEN + "Zaznaczyle punkt drugi!");
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onMOTD(ServerListPingEvent e) {
		Game g = GameManager.getGames().get(0);
		if (g == null)
			return;
		String target = g.getMax() + "@" + g.getGameState().name();
		e.setMotd(target);
	}

	@EventHandler
	public void asyncJoin(AsyncPlayerPreLoginEvent e) {
		Game g = GameManager.getGames().get(0);
		if (g.getGameState() != GameState.LOBBY)
			e.disallow(Result.KICK_OTHER, ChatColor.RED + "Gra jest juz rozpoczeta!");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Game g = GameManager.getGames().get(0);
		if (g == null) {
			GamesUtils.loadGames();
			g = GameManager.getGames().get(0);
		}
		g.join(e.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Game g = GameManager.getGames().get(0);
		if (g == null)
			return;
		g.leave(e.getPlayer());
	}

	@EventHandler
	public void onGameLoad(UpdateEvent e) {
		if (e.getType() != UpdateType.SEC)
			return;

		if (GameManager.getGames().size() <= 0) {
			System.out.println("Laduje areny!");
			GamesUtils.loadGames();
		}
	}

}
