package pl.eldzi.superspleef.commands;

import org.bukkit.entity.Player;

import pl.eldzi.core.util.commands.PlayerCommand;
import pl.eldzi.superspleef.game.Game;
import pl.eldzi.superspleef.game.Game.GameState;
import pl.eldzi.superspleef.manager.GameManager;
import pl.eldzi.superspleef.manager.GamesUtils;

public class JoinCommands extends PlayerCommand {

	public JoinCommands() {
		super("join", "Dolaczanie do areny", "/join <arena>", "join.gracz", "dolacz", "wejdz");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if (args.length != 1) {
			for (Game g : GameManager.getGames()) {
				if (g.getGameState() == GameState.LOBBY)
					msg(p, "&cArena &e&l\"" + g.getName() + "\"&c jest dostepna!");
			}
			return msg(p, "&cWpisz /join <arena>, aby dolaczyc!");
		}
		String arena = args[0];
		if (!GamesUtils.containByName(arena))
			return msg(p, "&aPodana arena nie istnieje!");
		if (GamesUtils.getGameByPlayer(p) != null)
			return msg(p, "&cJestes na arenie!");
		Game g = GamesUtils.getByName(arena);
		if (g == null)
			return msg(p, "&cArena nie istnieje!");
		if (g.getGameState() != GameState.LOBBY)
			return msg(p, "&cArena nie jest w trybie lobby!");

		g.join(p);
		return true;
	}
}
