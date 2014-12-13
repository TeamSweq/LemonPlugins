package lemon.mobs.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lemon.mobs.EntityHandler;
import lemon.mobs.EntityHandlerInit;
import lemon.mobs.EntityReceiveLevelEvent;
import net.minecraft.server.v1_7_R4.EntityGolem;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityMonster;
import net.minecraft.server.v1_7_R4.EntitySnowman;
import net.minecraft.server.v1_7_R4.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_7_R4.PathfinderGoalAvoidPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftSnowman;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SnowmanHandler implements EntityHandler {
	private static List<Class<? extends EntityLiving>> targets;
	static{
		targets = new ArrayList<Class<? extends EntityLiving>>();
		targets.add(EntityHuman.class);
	}
	@EntityHandlerInit
	public static void onInit(JavaPlugin plugin){
		plugin.getServer().getPluginManager().registerEvents(new Listener(){
			@EventHandler(priority=EventPriority.MONITOR)
			public void onReceiveLevel(EntityReceiveLevelEvent event){
				if(event.getEntity() instanceof Snowman){
					Snowman snowman = (Snowman)event.getEntity();
					snowman.addPotionEffect(
							new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE,
									(int) Math.floor(event.getLevel()/10D), false), true);
					EntitySnowman ent = ((CraftSnowman)snowman).getHandle();
					updateSnowmanGoals(ent);
				}
			}
		}, plugin);
	}
	private static void updateSnowmanGoals(EntitySnowman snowman){
		try{
		Field f1 = EntityLiving.class.getDeclaredField("goalSelector");
	    f1.setAccessible(true);
	    PathfinderGoalSelector gselector = (PathfinderGoalSelector)f1.get(snowman);
	    
	    Field f2 = EntityLiving.class.getDeclaredField("targetSelector");
	    f2.setAccessible(true);
	    PathfinderGoalSelector tselector = (PathfinderGoalSelector)f2.get(snowman);
	    
	    Field f3 = PathfinderGoalSelector.class.getDeclaredField("a");
	    f3.setAccessible(true);
	    ((List<?>)f3.get(gselector)).clear();
	    ((List<?>)f3.get(tselector)).clear();
	    
	    int i = 1;
	    for (Class<? extends EntityLiving> c : targets)
	    {
	      if (((c.isAssignableFrom(EntityHuman.class)) || (c.isAssignableFrom(EntityMonster.class)) || 
	        (c.isAssignableFrom(EntityGolem.class)))) {
	        gselector.a(i, new PathfinderGoalAvoidPlayer(snowman, c, 8f, 0.23F, 0.4F));
	      }
	      tselector.a(2, new PathfinderGoalNearestAttackableTarget(snowman, c, 16, true, false));//16f before 0
	    }
	    i++;
	    gselector.a(i++, new PathfinderGoalArrowAttack(snowman, 0.25F, 20, 10.0F));
	    gselector.a(i++, new PathfinderGoalRandomStroll(snowman, 0.2F));
	    gselector.a(i++, new PathfinderGoalLookAtPlayer(snowman, EntityHuman.class, 6.0F));
	    gselector.a(i++, new PathfinderGoalRandomLookaround(snowman));
	    tselector.a(1, new PathfinderGoalHurtByTarget(snowman, false));
	    
	    f1.set(snowman, gselector);
	    f2.set(snowman, tselector);
		}catch(Exception ex){
	    	ex.printStackTrace();
	    }
	}
}
