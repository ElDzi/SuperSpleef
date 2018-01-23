package pl.eldzi.superspleef.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.eldzi.core.items.ItemBuilder;
import pl.eldzi.core.util.InventoryUtils;
import pl.eldzi.core.util.PlayerUtils;
import pl.eldzi.core.util.commands.PlayerCommand;
import pl.eldzi.superspleef.game.Game;
import pl.eldzi.superspleef.listeners.CornerListener;
import pl.eldzi.superspleef.manager.GamesUtils;

public class CreateCommand extends PlayerCommand {
	public CreateCommand() {
		super("superspleefsetup", "creating arena", "/superspleefsetup <args>", "superspleefsetup.setup", "spleef",
				"ss");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("create")) {
				String name = args[1];
				if (GamesUtils.containByName(name))
					return msg(p, "&6Arena o takiej nazwie juz istnieje!");
				Game g = new Game(name.toLowerCase());
				InventoryUtils.insert(p, new ItemBuilder(Material.DIAMOND_HOE).setTitle("&cZaznacz teren!").build());
				return msg(p, "&6Stworzylem arene!");
			} else if (args[0].equalsIgnoreCase("sethub")) {
				String name = args[1];
				if (!GamesUtils.containByName(name))
					return msg(p, "Arena o takiej nazwie nie istnieje!");
				Game g = new Game(name.toLowerCase());
				g.getConfig().setHub(p.getLocation());
				return msg(p, "&6Stworzylem hub dla areny " + name + "!");
			} else if (args[0].equalsIgnoreCase("setlobby")) {
				String name = args[1];
				if (!GamesUtils.containByName(name))
					return msg(p, "&6Arena o takiej nazwie nie istnieje!");
				Game g = new Game(name.toLowerCase());
				g.getConfig().setLobby(p.getLocation());
				return msg(p, "&6Stworzylem lobby dla areny " + name + "!");
			} else if (args[0].equalsIgnoreCase("setspawn")) {
				String name = args[1];
				if (!GamesUtils.containByName(name))
					return msg(p, "&6Arena o takiej nazwie nie istnieje!");
				Game g = new Game(name.toLowerCase());

				g.getConfig().setPlayerSpawn(p.getLocation());
				return msg(p, "&6Ustawiles spawn dla graczy!");

			} else if (args[0].equalsIgnoreCase("setcorner")) {
				String name = args[1];
				if (!GamesUtils.containByName(name))
					return msg(p, "&6Arena o takiej nazwie nie istnieje!");
				Game g = new Game(name.toLowerCase());
				Location a = CornerListener.point1.get(p);
				Location b = CornerListener.point2.get(p);
				if (a == null)
					return msg(p, "&6Pierwszy punkt jest nullem!");
				if (b == null)
					return msg(p, "&6Drugi punkt jest nullem!");
				if (a.getWorld().getName() != b.getWorld().getName())
					return msg(p, "&6Punkty musza byc w tym samym punkcie!!");
				g.getConfig().setCorner(a, b);
				return msg(p, "&cPomyslnie ustawiono!");
			}

		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("setmin")) {
				String name = args[1];
				int c = Integer.parseInt(args[2]);
				if (!GamesUtils.containByName(name))
					return msg(p, "&6Arena o takiej nazwie nie istnieje!");
				Game g = new Game(name.toLowerCase());
				g.getConfig().setMin(c);
				return msg(p, "&6Ustawiles min graczy!");
			} else if (args[0].equalsIgnoreCase("setmax")) {
				String name = args[1];
				int c = Integer.parseInt(args[2]);
				if (!GamesUtils.containByName(name))
					return msg(p, "&6Arena o takiej nazwie nie istnieje!");
				Game g = new Game(name.toLowerCase());
				g.getConfig().setMax(c);
				return msg(p, "&6Ustawiles max graczy!");
			} else {
				return help(p);
			}
		} else {
			return help(p);
		}
		return help(p);

	}

	@Override
	public boolean msg(CommandSender s, String m) {
		PlayerUtils.sendMsg(s, m);
		return true;
	}

	public boolean help(Player p) {
		msg(p, "&6=-=-=-= SuperSpleef =-=-=-=");
		msg(p, "&6/sqs create <arena>");
		msg(p, "&6/sqs setmin <arena> <int>");
		msg(p, "&6/sqs setmax <arena> <int>");
		msg(p, "&6/sqs sethub <arena>");
		msg(p, "&6/sqs setspawn <arena>");
		msg(p, "&6/sqs setlobby <arena>");
		msg(p, "&6/sqs setspawn <arena>");
		msg(p, "&6/sqs setcorner <arena>");
		return true;
	}

}
