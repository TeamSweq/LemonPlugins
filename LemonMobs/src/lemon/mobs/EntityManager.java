package lemon.mobs;

import java.util.ArrayList;
import java.util.List;

import lemon.mobs.handlers.CreeperHandler;
import lemon.mobs.handlers.SilverfishHandler;
import lemon.mobs.handlers.SkeletonHandler;
import lemon.mobs.handlers.SpiderHandler;
import lemon.mobs.handlers.ZombieHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class EntityManager implements Listener{
	private static List<EntityHandler> handlers;
	public static void init(LemonMobs plugin){
		plugin.getServer().getPluginManager().registerEvents(new EntityManager(), plugin);
		handlers = new ArrayList<EntityHandler>();
		handlers.add(new ZombieHandler());
		handlers.add(new SkeletonHandler());
		handlers.add(new SpiderHandler());
		handlers.add(new CreeperHandler());
		handlers.add(new SilverfishHandler());
		for(EntityHandler handler: handlers){
			handler.init(plugin);
		}
	}
	
	/*@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event){
		if(event.getSpawnReason()==SpawnReason.NATURAL||event.getSpawnReason()==SpawnReason.CHUNK_GEN){
			event.setCancelled(true);
			EntityType type = event.getEntityType();
			Biome biome = event.getLocation().getWorld().getBiome(event.getLocation().getBlockX(), event.getLocation().getBlockZ());
			type = calcRandomRatio(biome, type);
			event.getLocation().getWorld().spawnEntity(event.getLocation(), type);
		}
	}
	
	private EntityType calcRandomRatio(Biome biome, EntityType defaultType){
		List<Integer> ratios = new ArrayList<Integer>();
		int whole = 0;
		for(EntityHandler handler: handlers){
			for(SpawnRatio ratio: handler.getRatios()){
				for(Biome b: ratio.getBiomes()){
					if(b==biome){
						whole+=ratio.getRatio();
						ratios.add(ratio.getRatio());
					}
				}
			}
		}
		int random = (int) (Math.random()*whole);
		for(EntityHandler handler: handlers){
			for(SpawnRatio ratio: handler.getRatios()){
				for(Biome b: ratio.getBiomes()){
					if(b==biome){
						if(random<=ratio.getRatio()){
							return handler.getType();
						}
						random-=ratio.getRatio();
					}
				}
			}
		}
		return defaultType;
	}*/
	
	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
	public void onMobSpawn(CreatureSpawnEvent event){
		double level = -1;
		SpawnReason reason = event.getSpawnReason();
		if(reason==SpawnReason.CHUNK_GEN||reason==SpawnReason.NATURAL||reason==SpawnReason.SPAWNER_EGG||reason==SpawnReason.DISPENSE_EGG||reason==SpawnReason.SLIME_SPLIT){
			if(event.getEntity() instanceof Monster){
				level = MobUtils.getLevel(event.getLocation());
			}else{
				level = 0.5+Math.random();
			}
		}
		if(reason==SpawnReason.BUILD_IRONGOLEM||reason==SpawnReason.BUILD_SNOWMAN){
			level = 1;
		}
		if(reason==SpawnReason.SPAWNER){
			level = (int)(MobUtils.getLevel(event.getLocation())*(Math.random()+0.5));
		}
		if(reason==SpawnReason.BREEDING){
			double maleLvl = 0;
			double femaleLvl = 0;
			if(maleLvl+femaleLvl<=0){
				maleLvl = 1;
			}
			level = MobUtils.getLevel(maleLvl, femaleLvl);
		}
		if(reason==SpawnReason.EGG){
			level = 1;
		}
		MobUtils.setMetadata(event.getEntity(), "Level", level);
		MobUtils.nameMob(event.getEntity(), MobUtils.getName(event.getEntityType())+ChatColor.GOLD+" [Level "+Math.round(level)+"]");
	}
}
