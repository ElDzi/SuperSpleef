package pl.eldzi.superspleef.manager;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.eldzi.superspleef.Main;
import pl.eldzi.superspleef.game.Game;

public class GamesUtils {
	public static boolean containByName(String a) {
		File f = new File(Main.getInst().getDataFolder() + "/game-files/");
		if (f.listFiles() == null)
			return false;
		if (f.listFiles().length == 0)
			return false;
		for (File m : f.listFiles()) {
			if (!m.getName().endsWith(".yml"))
				continue;
			String name = m.getName().split("\\.")[0];
			if (name.equalsIgnoreCase(a))
				return true;
		}
		return false;
	}

	public static void runNew(String name) {
		try {
			Bukkit.getScheduler().runTaskLater(Main.getInst(), new BukkitRunnable() {

				@Override
				public void run() {
					Game g = new Game(name);
				}
			}, 20 * 3);
		} catch (Exception e) {

		}
	}

	public static boolean containGame(Player p) {
		for (Game g : GameManager.getGames())
			if (g.getPlayers().contains(p))
				return true;
		return false;
	}

	public static Game getGameByPlayer(Player p) {
		for (Game g : GameManager.getGames())
			if (g.getPlayers().contains(p))
				return g;
		return null;

	}

	public static Game getByName(String t) {
		for (Game g : GameManager.getGames())
			if (g.getName().equalsIgnoreCase(t))
				return g;
		return null;
	}

	public static void loadGames() {
		File f = new File(Main.getInst().getDataFolder() + "/game-files/");
		File[] files = f.listFiles();
		if (files == null) {
			return;
		}
		for (File m : files) {
			if (!m.getName().endsWith(".yml")) {
				continue;
			}
			String name = m.getName().split("\\.")[0];

			if (!containByName(name))
				continue;
			Game g = new Game(name);
			System.out.println("LADOWANIE " + name + " !");
		}
	}

}
