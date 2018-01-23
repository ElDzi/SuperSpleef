package pl.eldzi.superspleef.manager;

import org.bukkit.entity.Player;

import pl.eldzi.core.util.list.ElDziList;
import pl.eldzi.superspleef.players.GamePlayer;

public class PlayerManager {
	private static ElDziList<GamePlayer> players = new ElDziList<>();

	public static ElDziList<GamePlayer> getPlayers() {
		return players;
	}

	public static GamePlayer getPlayer(Player pa) {
		for (GamePlayer p : players)
			if (p.getPlayer().getName().equalsIgnoreCase(pa.getName()))
				return p;
		return new GamePlayer(pa);
	}

	public static boolean containPlayer(Player p) {
		for (GamePlayer pa : players)
			if (p.getName().equalsIgnoreCase(pa.getPlayer().getName()))
				return true;
		return false;
	}

	public static void removePlayer(Player p) {
		if (!containPlayer(p))
			return;
		players.remove(getPlayer(p));
	}
}
