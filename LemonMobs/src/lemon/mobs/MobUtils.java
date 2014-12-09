package lemon.mobs;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

public class MobUtils {
	private MobUtils(){}
	private static LemonMobs plugin;
	public static void init(LemonMobs plugin){
		MobUtils.plugin = plugin;
	}
	public static void setMetadata(Metadatable entity, String key, Object value){
		entity.setMetadata(key, new FixedMetadataValue(plugin, value));
	}
	public static MetadataValue getMetadata(Metadatable entity, String key){
		for(MetadataValue value: entity.getMetadata(key)){
			if(value.getOwningPlugin().equals(plugin)){
				return value;
			}
		}
		return null;
	}
	public static void removeMetadata(Metadatable entity, String key){
		entity.removeMetadata(key, plugin);
	}
	public static String nameMob(LivingEntity entity, String name){
		return nameMob(entity, name, true);
	}
	public static String nameMob(LivingEntity entity, String name, boolean visible){
		String oldTag = entity.getCustomName();
		entity.setCustomName(name);
		if(visible){
			showNametag(entity);
		}
		return oldTag;
	}
	public static void showNametag(LivingEntity entity){
		entity.setCustomNameVisible(true);
	}
	public static void hideNametag(LivingEntity entity){
		entity.setCustomNameVisible(false);
	}
	public static double getLevel(Location location){
		if(location.getWorld().getEnvironment()==Environment.NORMAL){
			double x = Math.max(Math.abs(location.getX())-100D, 0D);
			double y = Math.max(Math.abs(location.getY())-100D, 0D);
			double distance = Math.sqrt((x*x)+(y*y))*((Math.random()/2)+0.75);
			return Math.pow(distance, 0.4256251)+1;
		}
		if(location.getWorld().getEnvironment()==Environment.NETHER){
			double x = location.getX()*8;
			double y = location.getY()*8;
			double distance = Math.sqrt((x*x)+(y*y))*((Math.random()/2)+0.75);
			return Math.pow(distance, 0.4256251)+1;
		}
		if(location.getWorld().getEnvironment()==Environment.THE_END){
			return 1;
		}
		plugin.getLogger().warning("Failed to calculate environment");
		return -1;
	}
	public static double getLevel(double father, double mother){
		return ((father+mother)/2)-(Math.abs(father-mother)*Math.random())+((Math.random()+(1/(father+mother)))*Math.random());
	}
	public static String getName(EntityType type){
		String formatted = type.toString();
		formatted = formatted.charAt(0)+formatted.substring(1).toLowerCase();
		if(type==EntityType.PIG_ZOMBIE){
			formatted = "Pigman";
		}
		if(type==EntityType.CAVE_SPIDER){
			formatted = "Cave Spider";
		}
		if(type==EntityType.MAGMA_CUBE){
			formatted = "Magma Cube";
		}
		if(type==EntityType.IRON_GOLEM){
			formatted = "Iron Golem";
		}
		if(type==EntityType.MUSHROOM_COW){
			formatted = "Mooshroom";
		}
		return formatted;
	}
}
