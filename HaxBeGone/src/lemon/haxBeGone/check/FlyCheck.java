package lemon.haxBeGone.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyCheck extends Check implements Runnable, Listener {
	private HashMap<UUID, List<Direction>> players;
	private HashMap<UUID, Location> locations;
	private List<UUID> hackers;
	@Override
	public void init(JavaPlugin plugin) {
		players = new HashMap<UUID, List<Direction>>();
		locations = new HashMap<UUID, Location>();
		hackers = new ArrayList<UUID>();
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 1);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@Override
	public void run() {
		for(Player p: Bukkit.getOnlinePlayers()){
			if(!locations.containsKey(p.getUniqueId())){
				locations.put(p.getUniqueId(), p.getLocation());
			}
			if(p.isFlying()){
				players.remove(p.getUniqueId());
			}else{
				if(isOnGround(p)){
					players.remove(p.getUniqueId());
				}else{
					List<Direction> directions = players.get(p.getUniqueId());
					if(directions==null){
						directions = new ArrayList<Direction>();
					}
					Direction currentDir = null;
					if(p.getLocation().getY()>locations.get(p.getUniqueId()).getY()){ //UP
						currentDir = Direction.UP;
					}
					if(p.getLocation().getY()<locations.get(p.getUniqueId()).getY()){ //DOWN
						currentDir = Direction.DOWN;
					}
					if(currentDir!=null){
						if(directions.size()==0){
							directions.add(currentDir);
							players.put(p.getUniqueId(), directions);
						}else if(directions.get(directions.size()-1)!=currentDir){
							directions.add(currentDir);
							players.put(p.getUniqueId(), directions);
						}
					}
					if(isAlternating(directions)&&directions.size()>=3){
						Bukkit.broadcastMessage(p.getName()+" HACKS");
						violate(p.getUniqueId());
						players.remove(p.getUniqueId());
						//HACKS!
					}
				}
			}
			locations.put(p.getUniqueId(), p.getLocation());
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			players.remove(((Player)event.getEntity()).getUniqueId());
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onTeleport(PlayerTeleportEvent event){
		players.remove(event.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onGmChange(PlayerGameModeChangeEvent event){
		players.remove(event.getPlayer().getUniqueId());
	}
	
	private boolean isOnGround(Player player){
		for(Material material: Material.values()){
			if(material==Material.AIR){
				continue;
			}
			if(isSurroundingMaterial(player, material)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isSurroundingMaterial(Player player, Material material){
		for(int i=-1;i<=1;++i){
			for(int j=-1;j<=1;++j){
				for(int k=-1;k<=1;++k){
					if(player.getLocation().add(i, j, k).getBlock().getType()==material){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean isAlternating(List<Direction> directions){
		if(directions.size()==0){
			return true;
		}
		Direction current = directions.get(0);
		for(int i=1;i<directions.size();++i){
			if(directions.get(i)==current){
				return false;
			}
			current = directions.get(i);
		}
		return true;
	}
	
	@Override
	public boolean check(UUID player, boolean remove) {
		boolean hasPlayer = hackers.contains(player);
		if(remove){
			hackers.remove(player);
		}
		return hasPlayer;
	}
	
	@Override
	public String getType() {
		return "Fly";
	}
	private enum Direction{
		UP, DOWN;
	}
}
