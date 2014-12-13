package lemon.mobs.handlers;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lemon.mobs.EntityHandler;
import lemon.mobs.EntityHandlerInit;
import lemon.mobs.EntityReceiveLevelEvent;

public class SilverfishHandler implements EntityHandler {
	private static Map<UUID, Double> silverfishes;
	@EntityHandlerInit
	public static void onInit(JavaPlugin plugin){
		plugin.getServer().getPluginManager().registerEvents(new Listener(){
			@EventHandler(priority=EventPriority.MONITOR)
			public void onReceiveLevel(EntityReceiveLevelEvent event){
				if(event.getEntity() instanceof Silverfish){
					Silverfish silverfish = (Silverfish)event.getEntity();
					silverfish.addPotionEffect(
							new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE,
									(int) Math.floor(event.getLevel()/15D), false), true);
					silverfishes.put(silverfish.getUniqueId(), event.getLevel());
				}
			}
			@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
			public void onDamage(EntityDamageByEntityEvent event){
				if(event.getDamager().getType()==EntityType.SILVERFISH){
					if(event.getEntity() instanceof LivingEntity){
						LivingEntity defender = (LivingEntity)event.getEntity();
						double level = silverfishes.get(event.getDamager().getUniqueId());
						defender.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int)level, 0, false), true);
						defender.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int)level, 0, false), true);
						defender.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)(level*1.35), (int)(level/30D), false), true);
					}
				}
			}
			@EventHandler
			public void onDeath(EntityDeathEvent event){
				if(event.getEntityType()==EntityType.SILVERFISH){
					silverfishes.remove(event.getEntity().getUniqueId());
				}
			}
		}, plugin);
	}
}
