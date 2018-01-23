package pl.eldzi.superspleef.game;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.eldzi.core.util.WorldUtils;

public class GameConfig {
	private FileConfiguration config;
	private File file;
	private boolean firstRun;
	Map<String, Object> values = new HashMap<>();

	public GameConfig(File f, Game g, boolean fr) {
		file = f;
		config = YamlConfiguration.loadConfiguration(f);
		firstRun = fr;
		if (!firstRun) {
			if (config.isSet("hub")) {
				values.put("hub", config.getString("hub"));
				values.put("lobby", config.getString("lobby"));
				values.put("playerspawn", config.getString("playerspawn"));
				values.put("min", config.getInt("min"));
				values.put("max", config.getInt("max"));
				values.put("gamecorner", config.getString("gamecorner"));
				save();
			} else {
				values.put("hub", WorldUtils.locToStr(WorldUtils.getSpawn()));
				values.put("playerspawn", WorldUtils.locToStr(WorldUtils.getSpawn()));
				values.put("lobby", WorldUtils.locToStr(WorldUtils.getSpawn()));
				values.put("min", 4);
				values.put("min", 16);
				values.put("gamecorner",
						WorldUtils.locToStr(WorldUtils.getSpawn()) + "@" + WorldUtils.locToStr(WorldUtils.getSpawn()));
				setDefaultValues(getConfig(), values);
				save();
			}

		} else {
			values.put("hub", WorldUtils.locToStr(WorldUtils.getSpawn()));
			values.put("lobby", WorldUtils.locToStr(WorldUtils.getSpawn()));
			values.put("playerspawn", WorldUtils.locToStr(WorldUtils.getSpawn()));
			values.put("min", 4);
			values.put("max", 16);
			values.put("gamecorner",
					WorldUtils.locToStr(WorldUtils.getSpawn()) + "@" + WorldUtils.locToStr(WorldUtils.getSpawn()));
			setDefaultValues(getConfig(), values);
			save();
		}

	}

	public void setDefaultValues(FileConfiguration config, Map<String, Object> configParams) {
		if (config == null)
			return;
		for (final Entry<String, Object> e : configParams.entrySet())
			if (!config.contains(e.getKey()))
				config.set(e.getKey(), e.getValue());
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Location getHub() {
		return WorldUtils.strToLoc((String) values.get("hub"));
	}

	public Location getLobbyEnd() {
		return WorldUtils.strToLoc((String) values.get("lobby"));
	}

	public Location getPlayerSpawn() {
		return WorldUtils.strToLoc((String) values.get("playerspawn"));
	}

	public void setPlayerSpawn(Location l) {
		config.set("playerspawn", WorldUtils.locToStr(l));
		save();
		values.put("playerspawn", config.getString("playerspawn"));
	}

	public void setLobby(Location l) {
		config.set("lobby", WorldUtils.locToStr(l));
		save();
		values.put("lobby", config.getString("lobby"));
	}

	public void setHub(Location l) {
		config.set("hub", WorldUtils.locToStr(l));
		save();
		values.put("hub", config.getString("hub"));
	}

	public void setMin(int l) {
		config.set("min", l);
		save();
		values.put("min", config.getInt("min"));
	}

	public void setMax(int l) {
		config.set("max", l);
		save();
		values.put("max", config.getInt("max"));
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public Corner getCorner() {
		String c = (String) values.get("gamecorner");
		String[] locs = c.split("@");
		Corner corner = new Corner(WorldUtils.strToLoc(locs[0]), WorldUtils.strToLoc(locs[1]));
		return corner;
	}

	public void setCorner(Location a, Location b) {
		String k = WorldUtils.locToStr(a) + "@" + WorldUtils.locToStr(b);
		config.set("gamecorner", k);
		save();
		values.put("gamecorner", config.getInt("gamecorner"));
	}

	public File getFile() {
		return file;
	}

	public int getMin() {
		return (int) values.get("min");
	}

	public int getMax() {
		return (int) values.get("max");
	}
}
