package lemon.mobs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lemon.mobs.handlers.CreeperHandler;
import lemon.mobs.handlers.EndermanHandler;
import lemon.mobs.handlers.SilverfishHandler;
import lemon.mobs.handlers.SkeletonHandler;
import lemon.mobs.handlers.SnowmanHandler;
import lemon.mobs.handlers.SpiderHandler;
import lemon.mobs.handlers.ZombieHandler;

import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

public class LemonMobs extends JavaPlugin implements Listener {
	private Map<Class<? extends EntityHandler>, Map<String, List<Method>>> handlers;
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		handlers = new HashMap<Class<? extends EntityHandler>, Map<String, List<Method>>>();
		addHandler(ZombieHandler.class);
		addHandler(SkeletonHandler.class);
		addHandler(SpiderHandler.class);
		addHandler(EndermanHandler.class);
		addHandler(SnowmanHandler.class);
		addHandler(CreeperHandler.class);
		addHandler(SilverfishHandler.class);
		callHandlers("INIT", this);
	}
	public void addHandler(Class<? extends EntityHandler> handler){
		Map<String, List<Method>> methods = new HashMap<String, List<Method>>();
		for(Method method: handler.getMethods()){
			for(Annotation annotation: method.getAnnotations()){
				if(annotation.annotationType().getPackage().equals(LemonMobs.class.getPackage())){
					String annotationType = annotation.annotationType().getSimpleName();
					annotationType = annotationType.substring("ENTITYHANDLER".length(), annotationType.length());
					annotationType = annotationType.toUpperCase();
					List<Method> existing = methods.get(annotationType);
					if(existing==null){
						existing = new ArrayList<Method>();
					}
					existing.add(method);
					methods.put(annotationType, existing);
				}
			}
		}
		handlers.put(handler, methods);
	}
	public void callHandlers(String annotation, Object... objects){
		for(Class<? extends EntityHandler> handler: handlers.keySet()){
			if(handlers.get(handler).get(annotation)!=null){
				for(Method method: handlers.get(handler).get(annotation)){
					ReflectionUtil.invokePublicMethod(method, null, objects);
				}
			}
		}
	}
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onMobSpawn(CreatureSpawnEvent event){
		double level = -1;
		SpawnReason reason = event.getSpawnReason();
		if(reason==SpawnReason.CHUNK_GEN||
				reason==SpawnReason.NATURAL||
				reason==SpawnReason.SPAWNER_EGG||
				reason==SpawnReason.DISPENSE_EGG||
				reason==SpawnReason.SLIME_SPLIT){
			if(event.getEntity() instanceof Monster){
				level = MobUtil.getLevel(event.getLocation());
			}else{
				level = 0.5+Math.random();
			}
		}
		if(reason==SpawnReason.BUILD_IRONGOLEM||reason==SpawnReason.BUILD_SNOWMAN){
			level = 1;
		}
		if(reason==SpawnReason.SPAWNER){
			level = MobUtil.getLevel(event.getLocation())*(Math.random()+0.5D);
		}
		if(reason==SpawnReason.BREEDING){
			//TODO
			double maleLvl = 0;
			double femaleLvl = 0;
			if(maleLvl+femaleLvl<=0){
				maleLvl = 1;
			}
			level = MobUtil.getLevel(maleLvl, femaleLvl);
		}
		if(reason==SpawnReason.EGG){
			level = 1;
		}
		EntityReceiveLevelEvent e = new EntityReceiveLevelEvent(event.getEntity(), level);
		this.getServer().getPluginManager().callEvent(e);
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityReceiveLevel(EntityReceiveLevelEvent event){
		MobUtil.setName(event.getEntity(), MobUtil.getFormattedName(MobUtil.getName(event.getEntity().getType()), event.getLevel()), true);
	}
}
