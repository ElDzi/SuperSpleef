package pl.eldzi.superspleef.players;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.eldzi.core.items.ItemBuilder;
import pl.eldzi.core.util.PlayerUtils;
import pl.eldzi.superspleef.Main;
import pl.eldzi.superspleef.game.Game;
import pl.eldzi.superspleef.game.RegenerationTimer;
import pl.eldzi.superspleef.manager.GamesUtils;
import pl.eldzi.superspleef.manager.PlayerManager;

public class GamePlayer {
	private Player player;
	private Game game;

	public GamePlayer(Player p) {
		this.player = p;
		game = GamesUtils.getGameByPlayer(p);
		PlayerManager.getPlayers().add(this);
		reset();
	}

	private boolean blokHelper, jumper;
	private ItemStack platformHelper = new ItemBuilder(Material.getMaterial(2262))
			.setTitle(PlayerUtils.fixColor("&cBlokHelper")).addEnchantment(Enchantment.SILK_TOUCH, 20).build();
	private ItemStack jumperi = new ItemBuilder(Material.SLIME_BLOCK).setTitle(PlayerUtils.fixColor("&cJumper"))
			.addEnchantment(Enchantment.SILK_TOUCH, 20).build();

	public void setBlokHelper(boolean blokHelper) {
		this.blokHelper = blokHelper;
	}

	public void setJumper(boolean jumper) {
		this.jumper = jumper;
	}

	public boolean isBlokHelper() {
		return blokHelper;
	}

	public boolean isJumper() {
		return jumper;
	}

	public Player getPlayer() {
		return player;
	}

	public Game getGame() {
		return game;
	}

	public void useJumper() {
		setJumper(false);
		new RegenerationTimer(getPlayer(), jumperi).runTaskTimer(Main.getInst(), 20, 20);
	}

	public void useBlockHelper() {
		setBlokHelper(false);
		new RegenerationTimer(getPlayer(), platformHelper).runTaskTimer(Main.getInst(), 20, 20);

	}

	public int getBrokenBlock() {
		return brokenBlock;
	}

	public void setBrokenBlock(int brokenBlock) {
		this.brokenBlock = brokenBlock;
	}

	private int brokenBlock;

	public void reset() {
		brokenBlock = 0;
		setBlokHelper(true);
		setJumper(true);

	}
}
