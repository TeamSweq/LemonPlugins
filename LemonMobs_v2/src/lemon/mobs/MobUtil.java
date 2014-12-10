package lemon.mobs;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;

public class MobUtil {

	public static double getLevel(Location location){
		if(location.getWorld().getEnvironment()==Environment.NORMAL){
			double x = Math.max(Math.abs(location.getX())-100D, 0D);
			double z = Math.max(Math.abs(location.getZ())-100D, 0D);
			double distance = Math.sqrt((x*x)+(z*z))*((Math.random()/2)+0.75);
			return Math.pow(distance, 0.5)+1;
		}
		if(location.getWorld().getEnvironment()==Environment.NETHER){
			double x = Math.abs(location.getX()*8);
			double z = Math.abs(location.getZ()*8);
			double distance = Math.sqrt((x*x)+(z*z))*((Math.random()/2)+0.75);
			return Math.pow(distance, 0.5)+1;
		}
		if(location.getWorld().getEnvironment()==Environment.THE_END){
			return 1;
		}
		return -1;
	}public static double getLevel(double father, double mother){
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
