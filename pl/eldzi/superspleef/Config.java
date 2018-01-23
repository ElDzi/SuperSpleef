
package pl.eldzi.superspleef;

import java.lang.reflect.Field;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	static {
	}

	public static void loadConfig() {
		try {
			Main.getInst().saveDefaultConfig();
			final FileConfiguration c = Main.getInst().getConfig();
			for (final Field f : Config.class.getFields())
				if (c.isSet("config." + f.getName().toLowerCase().replace("_", ".").replace("$", "-")))
					f.set(null, c.get("config." + f.getName().toLowerCase().replace("_", ".").replace("$", "-")));
				else
					saveConfig();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void reloadConfig() {
		Main.getInst().reloadConfig();
		loadConfig();
		saveConfig();
	}

	public static void saveConfig() {
		try {
			final FileConfiguration c = Main.getInst().getConfig();
			for (final Field f : Config.class.getFields())
				c.set("config." + f.getName().toLowerCase().replace("_", ".").replace("$", "-"), f.get(null));
			Main.getInst().saveConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
