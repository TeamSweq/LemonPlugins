package lemon.mobs.handlers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lemon.mobs.EntityHandler;
import lemon.mobs.LemonMobs;
import lemon.mobs.MobUtils;
import lemon.mobs.SpawnRatio;

public class CreeperHandler extends EntityHandler implements Listener, Runnable {
	
	private List<Creeper> creepers;
	
	@Override
	public void init(LemonMobs plugin) {
		creepers = new CopyOnWriteArrayList<Creeper>();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 1);
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onCreeperSpawn(CreatureSpawnEvent event){
		if(event.getEntityType()==EntityType.CREEPER&&(MobUtils.getMetadata(event.getEntity(), "Level").asDouble()>=0)){
			creepers.add((Creeper)event.getEntity());
		}
	}
	
	@Override
	public void run(){
		for(Creeper creeper: creepers){
			if((!(creeper.isValid()))||creeper.isDead()){
				creepers.remove(creeper);
				continue;
			}
			if(creeper.getVelocity().getX()==0&&creeper.getVelocity().getZ()==0&&(!(creeper.isPowered()))){
				creeper.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true), true);
			}else{
				creeper.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
	}
	
	@Override
	public SpawnRatio[] getRatios() {
		return null;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.CREEPER;
	}
}
