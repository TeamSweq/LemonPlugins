package lemon.mobs.handlers;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lemon.mobs.EntityHandler;
import lemon.mobs.EntityHandlerInit;
import lemon.mobs.EntityReceiveLevelEvent;

public class CreeperHandler implements EntityHandler {
	private static Map<UUID, Double> creepers;
	@EntityHandlerInit
	public static void onInit(JavaPlugin plugin){
		plugin.getServer().getPluginManager().registerEvents(new Listener(){
			@EventHandler(priority=EventPriority.MONITOR)
			public void onReceiveLevel(EntityReceiveLevelEvent event){
				if(event.getEntity() instanceof Creeper){
					Creeper creeper = (Creeper)event.getEntity();
					creeper.addPotionEffect(
							new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE,
									(int) Math.floor(event.getLevel()/5D), false), true);
					creepers.put(creeper.getUniqueId(), event.getLevel());
				}
			}
			@EventHandler
			public void onCreeperExplode(EntityExplodeEvent event){
				if(event.getEntityType()==EntityType.CREEPER){
					if(creepers.containsKey(event.getEntity().getUniqueId())){
						event.setCancelled(true);
						double level = creepers.get(event.getEntity().getUniqueId());
						event.getLocation().getWorld().createExplosion(event.getLocation(), (float)(4D+(level/12.5D)), level>=50D);
					}
				}
			}
			@EventHandler
			public void onDeath(EntityDeathEvent event){
				if(event.getEntityType()==EntityType.CREEPER){
					creepers.remove(event.getEntity().getUniqueId());
				}
			}
		}, plugin);
	}
}
