package lemon.haxBeGone.check;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class KillAuraCheck extends Check {
	
	@Override
	public void init(JavaPlugin plugin) {
		
	}
	
	@Override
	public boolean check(UUID player, boolean remove) {
		return false;
	}
	
	@Override
	public String getType() {
		return null;
	}
}
