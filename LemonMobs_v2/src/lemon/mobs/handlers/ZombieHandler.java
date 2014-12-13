package lemon.mobs.handlers;

import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lemon.mobs.EntityHandler;
import lemon.mobs.EntityHandlerInit;
import lemon.mobs.EntityReceiveLevelEvent;

public class ZombieHandler implements EntityHandler {
	@EntityHandlerInit
	public static void onInit(JavaPlugin plugin){
		plugin.getServer().getPluginManager().registerEvents(new Listener(){
			@EventHandler(priority=EventPriority.MONITOR)
			public void onReceiveLevel(EntityReceiveLevelEvent event){
				if(event.getEntity() instanceof Zombie){
					Zombie zombie = (Zombie)event.getEntity();
					zombie.addPotionEffect(
							new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,
									(int) Math.floor(event.getLevel()/20D), false), true);
					zombie.addPotionEffect(
							new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE,
									(int) Math.floor(event.getLevel()/20D), false), true);
				}
			}
		}, plugin);
	}
}
