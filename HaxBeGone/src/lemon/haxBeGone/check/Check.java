package lemon.haxBeGone.check;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Check {
	private static List<ViolationListener> listeners;
	public static void initListeners(){
		listeners = new ArrayList<ViolationListener>();
	}
	public static boolean registerListener(ViolationListener listener){
		return listeners.add(listener);
	}
	public static boolean unRegisterListener(ViolationListener listener){
		return listeners.remove(listener);
	}
	
	public void violate(UUID player){
		for(ViolationListener listener: listeners){
			listener.onViolate(player, getType());
		}
	}
	
	public abstract void init(JavaPlugin plugin);
	public boolean check(UUID player){
		return check(player, false);
	}
	public abstract boolean check(UUID player, boolean remove);
	public abstract String getType();
}
