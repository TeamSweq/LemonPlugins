package lemon.mobs;

import org.bukkit.plugin.java.JavaPlugin;

public class LemonMobs extends JavaPlugin {
	public void onEnable(){
		MobUtils.init(this);
		EntityManager.init(this);
	}
	public void onDisable(){
		
	}
}
