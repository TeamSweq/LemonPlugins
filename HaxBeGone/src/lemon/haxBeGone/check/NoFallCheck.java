package lemon.haxBeGone.check;

import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class NoFallCheck extends Check implements Listener {
	
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