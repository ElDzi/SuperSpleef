package pl.eldzi.superspleef.game;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import pl.eldzi.core.items.ItemBuilder;
import pl.eldzi.core.util.F;
import pl.eldzi.core.util.MathUtils;
import pl.eldzi.core.util.PlatformBlocks;
import pl.eldzi.core.util.PlayerUtils;
import pl.eldzi.core.util.Sounds;
import pl.eldzi.core.util.VectorUtils;
import pl.eldzi.core.util.list.ElDziList;
import pl.eldzi.core.util.scoreboard.GameScoreboard;
import pl.eldzi.superspleef.Main;
import pl.eldzi.superspleef.listeners.GameListener;
import pl.eldzi.superspleef.listeners.ScoreboardListener;
import pl.eldzi.superspleef.manager.GameManager;
import pl.eldzi.superspleef.manager.GamesUtils;
import pl.eldzi.superspleef.manager.PlayerManager;

public class Game {

	private ElDziList<Player> players = new ElDziList<>();
	private GameState gameState;

	private Location hub, spawn;
	private GameConfig config;
	private FileConfiguration con;

	public enum GameState {
		LOBBY, BATTLE, RESTART, RESULT, STOPING;
	}

	public ElDziList<Player> getPlayers() {
		return players;
	}

	public Player getRandom() {
		if (getPlayers().size() == 1)
			return getPlayers().get(0);
		return getPlayers().get(MathUtils.r(getPlayers().size()));
	}

	public void setScoreboard(GameScoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	private String chest_drop;
	private String name;

	public String getName() {
		return name;
	}

	public Game(String name) {
		this.name = name;
		setGameState(GameState.RESTART);
		File f = new File(Main.getInst().getDataFolder() + "/game-files/");
		File n = new File(Main.getInst().getDataFolder() + "/game-files/" + name.toLowerCase() + ".yml");
		config = new GameConfig(n, this, !n.exists());
		config.save();
		max = getConfig().getMax();
		con = config.getConfig();
		min = config.getMin();
		hub = config.getHub();
		spawn = config.getPlayerSpawn();
		scoreboard = new GameScoreboard(this, "");
		setGameState(GameState.LOBBY);
		Bukkit.getPluginManager().registerEvents(new GameListener(this), Main.getInst());
		Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this), Main.getInst());
		GameManager.getGames().add(this);
	}

	public String getChestDrop() {
		return chest_drop;
	}

	public GameConfig getConfig() {
		return config;
	}

	private int max, min;

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getCurrently() {
		return players.size();
	}

	public void rollback() {
		Main.getInst().getBlockRestore().RestoreAll();
	}

	public void addBlock(Block b) {
		Main.getInst().getBlockRestore().Add(b, 0, (byte) 0, System.currentTimeMillis() * 5);
	}

	private GameScoreboard scoreboard;

	public GameScoreboard getScoreboard() {
		return scoreboard;
	}

	public void join(Player p) {
		players.add(p);
		p.getInventory().clear();
		p.teleport(hub);
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(false);
		p.setMaxHealth(20);
		p.setHealth(20);
		p.setFoodLevel(20);
		for (PotionEffect ef : p.getActivePotionEffects())
			p.removePotionEffect(ef.getType());
		p.playSound(p.getEyeLocation(), Sounds.ORB_PICKUP.bukkitSound(), 2f, 1f);
		if (getCurrently() >= min) {
			if (lt == null)
				start();
		}
	}

	private LobbyTimer lt;

	public LobbyTimer getLt() {
		return lt;
	}

	private void start() {
		lt = new LobbyTimer(this);
		lt.runTaskTimer(Main.getInst(), 20, 20);

	}

	private GameTimer gameTimer;

	public int secToEnd() {
		if (gameTimer != null)
			return gameTimer.getT();
		else
			return -1;
	}

	public void startGame() {
		gameTimer = new GameTimer(this);
		gameTimer.runTaskTimer(Main.getInst(), 20, 20);
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public GameState getGameState() {
		return gameState;
	}

	private Player winner;

	public Player getWinner() {
		return winner;

	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public void leave(Player p) {
		p.getInventory().clear();

		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(false);
		p.setMaxHealth(20);
		p.setHealth(20);
		p.setFoodLevel(20);
		PlayerManager.getPlayer(p).reset();
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		p.teleport(config.getLobbyEnd());

		players.remove(p);
		if (getWinner() != null) {
			stop();
		}
		broadcast("&cGracz " + p.getName() + " wypadl poza mape! Pozostalo " + (players.size() - 1) + " graczy!");
		Main.getInst().sendServer(p, "lobby");
	}

	public void broadcast(String message) {
		String m = F.sys("SuperSpleef", message);
		for (Player p : players)
			PlayerUtils.sendMsg(p, m);
	}

	public void msg(Player p, String message) {
		String m = F.sys("SuperSpleef", message);
		pl.eldzi.core.util.PlayerUtils.sendMsg(p, m);
	}

	public void msgTitle(String msg1, String msg2) {
		for (Player p : getPlayers()) {
			p.sendTitle(PlayerUtils.fixColor(msg1), PlayerUtils.fixColor(msg2));
		}
	}

	public void stop() {
		if (gameTimer != null) {
			gameTimer.cancel();
			gameTimer = null;
			new EndTimer(this).runTaskTimer(Main.getInst(), 20, 20);
		}
	}

	public void stopGame(boolean restart) {
		try {
			if (!getPlayers().isEmpty()) {
				for (Player p : getPlayers()) {
					leave(p);
				}
			}
		} catch (Exception e) {
			rollback();
			GameManager.getGames().remove(this);
			GamesUtils.loadGames();
		}
		rollback();
		GameManager.getGames().remove(this);
		GamesUtils.loadGames();

	}

	public void changeBlock(Block b) {
		if (b.getType() == Material.AIR)
			return;
		if (Main.getInst().getBlockRestore().Contains(b)) {
			if (b.getData() == PlatformBlocks.GREEN.toByte()) {
				b.setTypeIdAndData(Material.STAINED_CLAY.getId(), PlatformBlocks.ORANGE.toByte(), true);
			} else if (b.getData() == PlatformBlocks.ORANGE.toByte()) {
				b.setTypeIdAndData(Material.STAINED_CLAY.getId(), PlatformBlocks.RED.toByte(), true);
			} else if (b.getData() == PlatformBlocks.RED.toByte()) {
				b.setType(Material.AIR);
			}
		} else {
			addBlock(b);
			b.setTypeIdAndData(Material.STAINED_CLAY.getId(), PlatformBlocks.GREEN.toByte(), true);
		}
	}

	public void equip(Player p) {
		ItemStack i = new ItemBuilder(Material.GOLD_SPADE).setTitle(PlayerUtils.fixColor("&cDESTRUKTOR")).build();
		ItemStack platformHelper = new ItemBuilder(Material.getMaterial(2262))
				.setTitle(PlayerUtils.fixColor("&cBlokHelper")).addEnchantment(Enchantment.SILK_TOUCH, 20).build();
		ItemStack jumper = new ItemBuilder(Material.SLIME_BLOCK).setTitle(PlayerUtils.fixColor("&cJumper"))
				.addEnchantment(Enchantment.SILK_TOUCH, 20).build();
		p.getInventory().setItem(0, i);
		p.getInventory().setItem(8, platformHelper);
		p.getInventory().setItem(7, jumper);
	}

}
