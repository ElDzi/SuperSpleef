package pl.eldzi.superspleef;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.reflect.ClassPath;

import pl.eldzi.core.miniplugins.moreevents.MoreEvents;
import pl.eldzi.core.miniplugins.rollback.BlockRestore;
import pl.eldzi.core.plugin.ElDziPlugin;
import pl.eldzi.core.util.ArmorStandUtils;
import pl.eldzi.core.util.BlockUtils;
import pl.eldzi.core.util.EntityUtils;
import pl.eldzi.core.util.FireworkUtils;
import pl.eldzi.core.util.InventoryUtil;
import pl.eldzi.core.util.MapUtil;
import pl.eldzi.core.util.PlayerUtils;
import pl.eldzi.core.util.TextBottomUtils;
import pl.eldzi.core.util.TitleUtils;
import pl.eldzi.core.util.commands.Command;
import pl.eldzi.core.util.commands.CommandManager;
import pl.eldzi.core.util.list.ElDziList;
import pl.eldzi.core.util.noteapi.SongPlayer;
import pl.eldzi.core.util.updater.Updater;
import pl.eldzi.superspleef.commands.CreateCommand;
import pl.eldzi.superspleef.commands.JoinCommands;
import pl.eldzi.superspleef.game.Game;
import pl.eldzi.superspleef.manager.GameManager;
import pl.eldzi.superspleef.manager.GamesUtils;

public class Main extends JavaPlugin {

	private static Main inst;
	private BlockUtils blockUtils;
	private EntityUtils entityUtils;
	private pl.eldzi.core.util.FireworkUtils fireworkUtils;
	private InventoryUtil inventoryUtil;
	private MapUtil mapUtil;
	private TextBottomUtils bottomUtils;
	private TitleUtils titleUtils;

	public BlockUtils getBlockUtils() {
		return blockUtils;
	}

	public EntityUtils getEntityUtils() {
		return entityUtils;
	}

	public pl.eldzi.core.util.FireworkUtils getFireworkUtils() {
		return fireworkUtils;
	}

	public InventoryUtil getInventoryUtil() {
		return inventoryUtil;
	}

	public MapUtil getMapUtil() {
		return mapUtil;
	}

	public TextBottomUtils getBottomUtils() {
		return bottomUtils;
	}

	public TitleUtils getTitleUtils() {
		return titleUtils;
	}

	public PlayerUtils getPlayerUtils() {
		return playerUtils;
	}

	private PlayerUtils playerUtils;
	private ArmorStandUtils armorStandUtils;

	public static Main getInst() {
		return inst;
	}

	@Override
	public void onLoad() {
		inst = this;
	}

	private BlockRestore br;

	public BlockRestore getBlockRestore() {
		return br;
	}

	private ElDziList<ElDziPlugin> plugins = new ElDziList<>();

	public static void sendServer(Player p, String name) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(name);
		p.sendPluginMessage(Main.getInst(), "BungeeCord", out.toByteArray());
	}

	@Override
	public void onEnable() {
		try {
			loadUtils();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			System.out.println("[ElDziCore] Plugin zostaje wylaczony poprzez niezgodnosc z wersja silnika!");
			getServer().getPluginManager().disablePlugin(this);
		}
		// Blood b = new Blood(this);
		// plugins.add(b);
		br = new BlockRestore(this);
		plugins.add(br);
		MoreEvents ev = new MoreEvents();
		plugins.add(ev);
		Config.reloadConfig();
		GamesUtils.loadGames();
		new Updater().runTaskTimer(this, 0, 1);
		registerCommand(new CreateCommand());
		registerCommand(new JoinCommands());
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		try {
			registerListener("pl.eldzi.superspleef.listeners");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Entity e : Bukkit.getWorlds().get(0).getEntities()) {
			if (e.getType() != EntityType.PLAYER)
				e.remove();
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			Main.sendServer(p, "lobby");
		}
	}

	public ArmorStandUtils getArmorStandUtils() {
		return armorStandUtils;
	}

	private int k = 0;
	public static HashMap<String, ArrayList<SongPlayer>> playingSongs = new HashMap<String, ArrayList<SongPlayer>>();
	public HashMap<String, Byte> playerVolume = new HashMap<String, Byte>();

	public static boolean isReceivingSong(Player p) {
		return ((Main.playingSongs.get(p.getName()) != null) && (!Main.playingSongs.get(p.getName()).isEmpty()));
	}

	public static void stopPlaying(Player p) {
		if (Main.playingSongs.get(p.getName()) == null) {
			return;
		}
		for (SongPlayer s : Main.playingSongs.get(p.getName())) {
			s.removePlayer(p);
		}
	}

	public static void setPlayerVolume(Player p, byte volume) {
		inst.playerVolume.put(p.getName(), volume);
	}

	public static byte getPlayerVolume(Player p) {
		Byte b = inst.playerVolume.get(p.getName());
		if (b == null) {
			b = 100;
			inst.playerVolume.put(p.getName(), b);
		}
		return b;
	}

	@Override
	public void onDisable() {

		getBlockRestore().RestoreAll();
		try {
			synchronized (GameManager.getGames()) {
				for (Game g : GameManager.getGames()) {
					for (Player p : g.getPlayers())
						g.leave(p);
					g.stopGame(false);
					GameManager.getGames().remove(g);
				}
			}
		} catch (Exception e) {

		}
		Iterator<ElDziPlugin> k = plugins.iterator();
		while (k.hasNext())
			k.next().onDisable();
		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTasks(this);
	}

	private String version;

	public String getVersion() {
		return version;
	}

	private TextBottomUtils utils;

	public TextBottomUtils getTextBottomUtils() {
		return utils;
	}

	public void registerListener(Listener l) {
		getServer().getPluginManager().registerEvents(l, this);
	}

	public void registerCommand(Command c) {
		CommandManager.register(c);
	}

	private void registerListener(String packages) throws Exception {
		ClassPath path = ClassPath.from(getClassLoader());
		for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive(packages)) {
			Class clazz = Class.forName(info.getName(), true, getClassLoader());
			if (Listener.class.isAssignableFrom(clazz)) {
				if (hasNoArgConstructor(clazz)) {
					Object o = clazz.newInstance();
					registerListener((Listener) o);
				}
			}

		}
	}

	private boolean hasNoArgConstructor(Class<?> klass) {
		for (Constructor c : klass.getDeclaredConstructors()) {
			if (c.getParameterTypes().length == 0)
				return true;
		}
		return false;
	}

	private void loadUtils() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		version = Bukkit.getServer().getClass().getPackage().getName()
				.substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);
		armorStandUtils = (ArmorStandUtils) Class.forName("pl.eldzi.core.util." + version + ".ArmorStandUtils")
				.newInstance();
		blockUtils = (BlockUtils) Class.forName("pl.eldzi.core.util." + version + ".BlockUtils").newInstance();
		entityUtils = (EntityUtils) Class.forName("pl.eldzi.core.util." + version + ".EntityUtils").newInstance();
		fireworkUtils = (FireworkUtils) Class.forName("pl.eldzi.core.util." + version + ".FireworkUtils").newInstance();
		inventoryUtil = (InventoryUtil) Class.forName("pl.eldzi.core.util." + version + ".InventoryUtil").newInstance();
		mapUtil = (MapUtil) Class.forName("pl.eldzi.core.util." + version + ".MapUtil").newInstance();
		bottomUtils = (TextBottomUtils) Class.forName("pl.eldzi.core.util." + version + ".TextBottomUtils")
				.newInstance();
		titleUtils = (TitleUtils) Class.forName("pl.eldzi.core.util." + version + ".TitleUtils").newInstance();
		playerUtils = (PlayerUtils) Class.forName("pl.eldzi.core.util." + version + ".PlayerUtils").newInstance();
		utils = (TextBottomUtils) Class.forName("pl.eldzi.core.util." + version + ".TextBottomUtils").newInstance();
		System.out.println("[ElDziCore] Zaladowalem wersje " + version);

	}
}
