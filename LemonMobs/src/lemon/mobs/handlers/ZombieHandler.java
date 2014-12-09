package lemon.mobs.handlers;

import lemon.mobs.EntityHandler;
import lemon.mobs.LemonMobs;
import lemon.mobs.MobUtils;
import lemon.mobs.SpawnRatio;

import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZombieHandler extends EntityHandler implements Listener{
	
	@Override
	public void init(LemonMobs plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onTarget(EntityTargetLivingEntityEvent event){
		if(event.getEntityType()==EntityType.ZOMBIE){
			TargetReason reason = event.getReason();
			Zombie zombie = (Zombie) event.getEntity();
			if(reason==TargetReason.CLOSEST_PLAYER||
					reason==TargetReason.DEFEND_VILLAGE||
					reason==TargetReason.OWNER_ATTACKED_TARGET||
					reason==TargetReason.PIG_ZOMBIE_TARGET||
					reason==TargetReason.RANDOM_TARGET||
					reason==TargetReason.TARGET_ATTACKED_ENTITY||
					reason==TargetReason.TARGET_ATTACKED_OWNER){
				if((!zombie.isBaby())&&(!zombie.isVillager())){
					if(MobUtils.getMetadata(zombie, "Level")!=null){
						double level = MobUtils.getMetadata(zombie, "Level").asDouble();
						level/=20;
						zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, (int)level, true), true);
					}
				}
			}
			if(reason==TargetReason.TARGET_DIED||
					reason==TargetReason.FORGOT_TARGET){
				zombie.removePotionEffect(PotionEffectType.SPEED);
			}
		}
	}
	
	@Override
	public EntityType getType() {
		return EntityType.ZOMBIE;
	}

	@Override
	public SpawnRatio[] getRatios() {
		return new SpawnRatio[]{new SpawnRatio(Biome.DESERT, Integer.MAX_VALUE)};
	}
}
