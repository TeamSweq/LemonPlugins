package lemon.mobs.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import lemon.mobs.EntityHandler;
import lemon.mobs.LemonMobs;
import lemon.mobs.SpawnRatio;

public class BlazeHandler extends EntityHandler implements Listener {
	@Override
	public void init(LemonMobs plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onFireballHit(ProjectileHitEvent event){
		if(event.getEntity().getShooter() instanceof Blaze){
			System.out.println("KABAM");
			shootFireballs(event.getEntity().getLocation());
		}
	}
	private void shootFireballs(Location location){
		Fireball fireball = (Fireball) location.getWorld().spawnEntity(location, EntityType.FIREBALL);
		fireball.setIsIncendiary(true);
		
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getPlayer().getItemInHand().getType()==Material.STICK){
			
		}
	}
	@Override
	public EntityType getType() {
		return EntityType.BLAZE;
	}
	@Override
	public SpawnRatio[] getRatios() {
		return null;
	}
}
