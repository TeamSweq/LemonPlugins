package lemon.haxBeGone.check;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpeedCheck extends Check implements Listener, Runnable {
	//PlayerVelocityEvent or by Timer?
	private HashMap<UUID, Location> players;
	private List<UUID> hackers;
	public void init(JavaPlugin plugin){
		/*players = new HashMap<String, Location>();
		hackers = new ArrayList<String>();
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 1);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);*/
	}
	public void run(){
		/*for(Player p: Bukkit.getServer().getOnlinePlayers()){
			p.sendMessage("Location of "+p.getName()+": "+players.get(p.getName()).subtract(p.getLocation()));
			players.put(p.getName(), p.getLocation());
		}*/
	}
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onTeleport(PlayerTeleportEvent event){
		players.put(event.getPlayer().getUniqueId(), event.getTo());
	}
	public boolean check(UUID player, boolean remove){
		boolean hasPlayer = hackers.contains(player);
		if(remove){
			hackers.remove(player);
		}
		return hasPlayer;
	}
	public String getType(){
		return "Speed";
	}
}
