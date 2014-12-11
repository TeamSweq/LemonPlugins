package lemon.mobs.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lemon.mobs.EntityHandler;
import lemon.mobs.EntityHandlerInit;
import lemon.mobs.EntityReceiveLevelEvent;

public class SpiderHandler implements EntityHandler {
	private static Map<UUID, Double> spiders;
	static{
		spiders = new HashMap<UUID, Double>();
	}
	@EntityHandlerInit
	public static void onInit(JavaPlugin plugin){
		plugin.getServer().getPluginManager().registerEvents(new Listener(){
			@EventHandler
			public void onReceiveLevel(EntityReceiveLevelEvent event){
				if(event.getEntity() instanceof Spider){
					Spider spider = (Spider)event.getEntity();
					spider.addPotionEffect(
							new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE,
									(int) Math.floor(event.getLevel()/10D), false), true);
					spiders.put(spider.getUniqueId(), event.getLevel());
				}
			}
			@EventHandler
			public void onDamage(EntityDamageByEntityEvent event){
				if(event.getDamager().getType()==EntityType.SPIDER){
					if(event.getEntity() instanceof LivingEntity){
						LivingEntity defender = (LivingEntity)event.getEntity();
						int duration = (int) (spiders.get(event.getDamager().getUniqueId())*12D);
						int amplifier = (int) (spiders.get(event.getDamager().getUniqueId())/30D);
						defender.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, amplifier, false), true);
					}
				}
			}
			@EventHandler
			public void onDeath(EntityDeathEvent event){
				if(event.getEntityType()==EntityType.SPIDER){
					spiders.remove(event.getEntity().getUniqueId());
				}
			}
		}, plugin);
	}
}
