package pl.eldzi.superspleef.kits;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import pl.eldzi.core.util.list.ElDziList;

public class PerkUtils {
	public static void registerPerks()
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		String package_ = "pl.eldzi.core.game.kits.list";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = package_.replace("\\.", "\\/");
		Enumeration<URL> res = classLoader.getResources(path);
		ElDziList<File> dirs = new ElDziList<>();
		while (res.hasMoreElements())
			dirs.add(new File((res.nextElement().getFile())));
		ElDziList<Class> perks = new ElDziList<>();
		for (File f : dirs) {
			if (!f.exists())
				continue;

			File[] files = f.listFiles();
			for (File fi : files) {
				if (fi.isDirectory())
					continue;
				if (fi.getName().endsWith(".class"))
					perks.add(Class.forName(package_ + "." + fi.getName().substring(0, fi.getName().length() - 6)));
			}
		}

		for (Class c : perks) {
			if (!Perk.class.isAssignableFrom(c))
				continue;
			Perk p = (Perk) c.newInstance();
			PerksListener.getPerks().add(p);
		}
	}
}
