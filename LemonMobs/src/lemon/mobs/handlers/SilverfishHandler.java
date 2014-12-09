package lemon.mobs.handlers;

import lemon.mobs.EntityHandler;
import lemon.mobs.LemonMobs;
import lemon.mobs.MobUtils;
import lemon.mobs.SpawnRatio;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SilverfishHandler extends EntityHandler implements Listener{
	@Override
	public void init(LemonMobs plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onSpawnSilverfish(CreatureSpawnEvent event){
		if(event.getEntityType() == EntityType.SILVERFISH){
			double level = MobUtils.getMetadata(event.getEntity(), "Level").asDouble();
			event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, (int)(level/(100/3)), true), true);
		}
	}
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Silverfish){
			if(event.getEntity() instanceof Player){
				Player player = (Player)event.getEntity();
				double level = MobUtils.getMetadata(event.getDamager(), "Level").asDouble();
				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int)(level/3), 0, false), true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)((level/4)*Math.random()), 0, false), true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int)((level/6)*Math.random()), 0, false), true);
			}
		}
	}
	@Override
	public EntityType getType() {
		return EntityType.SILVERFISH;
	}
	@Override
	public SpawnRatio[] getRatios() {
		return null;
	}
}
