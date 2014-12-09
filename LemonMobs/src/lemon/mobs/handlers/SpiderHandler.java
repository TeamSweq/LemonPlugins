package lemon.mobs.handlers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lemon.mobs.EntityHandler;
import lemon.mobs.LemonMobs;
import lemon.mobs.MobUtils;
import lemon.mobs.SpawnRatio;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SpiderHandler extends EntityHandler implements Listener, Runnable {
	private List<Spider> spiders;
	private List<Item> items;
	@Override
	public void init(LemonMobs plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 1);
		spiders = new CopyOnWriteArrayList<Spider>();
		items = new CopyOnWriteArrayList<Item>();
	}
	@EventHandler
	public void onTarget(EntityTargetEvent event){
		if(event.getEntityType()==EntityType.SPIDER||event.getEntity() instanceof Spider){
			TargetReason reason = event.getReason();
			Spider spider = (Spider) event.getEntity();
			if(reason==TargetReason.CLOSEST_PLAYER||
					reason==TargetReason.DEFEND_VILLAGE||
					reason==TargetReason.OWNER_ATTACKED_TARGET||
					reason==TargetReason.PIG_ZOMBIE_TARGET||
					reason==TargetReason.RANDOM_TARGET||
					reason==TargetReason.TARGET_ATTACKED_ENTITY||
					reason==TargetReason.TARGET_ATTACKED_OWNER){
				if(MobUtils.getMetadata(spider, "ThrowTime")==null){
					MobUtils.setMetadata(spider, "ThrowTime", (int)(Math.random()*100));
					spiders.add(spider);
				}
			}
		}
	}/*
	@EventHandler
	public void onTarget(EntityTargetLivingEntityEvent event){
		System.out.println("TARGET EVENT - "+event.getEntityType());
		if(event.getEntityType()==EntityType.SPIDER||event.getEntity() instanceof Spider){
			System.out.println("SPIDER");
			TargetReason reason = event.getReason();
			Spider spider = (Spider) event.getEntity();
			if(reason==TargetReason.CLOSEST_PLAYER||
					reason==TargetReason.DEFEND_VILLAGE||
					reason==TargetReason.OWNER_ATTACKED_TARGET||
					reason==TargetReason.PIG_ZOMBIE_TARGET||
					reason==TargetReason.RANDOM_TARGET||
					reason==TargetReason.TARGET_ATTACKED_ENTITY||
					reason==TargetReason.TARGET_ATTACKED_OWNER){
				MobUtils.setMetadata(spider, "ThrowTime", (int)(Math.random()*100));
				spiders.add(spider);
				System.out.println("TARGET");
			}
			if(reason==TargetReason.TARGET_DIED||
					reason==TargetReason.FORGOT_TARGET){
				spider.removeMetadata("ThrowTime", plugin);
				spiders.remove(spider);
				System.out.println("REMOVE");
			}
		}
	}*/
	@Override
	public void run() {
		for(Spider spider: spiders){
			if((!(spider.isValid()))||spider.isDead()||spider.getTarget()==null){
				MobUtils.removeMetadata(spider, "ThrowTime");
				spiders.remove(spider);
				continue;
			}
			int timeLeft = MobUtils.getMetadata(spider, "ThrowTime").asInt();
			timeLeft--;
			if(timeLeft>0){
				MobUtils.setMetadata(spider, "ThrowTime", timeLeft);
			}else{
				MobUtils.setMetadata(spider, "ThrowTime", (int)(Math.random()*100));
				Item item = spider.getWorld().dropItem(spider.getLocation(), new ItemStack(Material.WEB));
				pullToLocation(item, spider.getTarget().getLocation());
				items.add(item);
			}
		}
		for(Item i: items){
			if(i.getLocation().getY()<0){
				items.remove(i);
				i.remove();
			}
			if(i.getVelocity().getY()==0){
				items.remove(i);
				i.remove();
				i.getWorld().getBlockAt(i.getLocation()).breakNaturally();
				i.getWorld().getBlockAt(i.getLocation()).setType(Material.WEB);
			}
		}
	}
	public void pullToLocation(Item item, Location location){
		double t = item.getLocation().distance(location);
		double vx = (1.0D + 0.07000000000000001D * t) * (location.getX() - item.getLocation().getX()) / t;
		double vz = (1.0D + 0.07000000000000001D * t) * (location.getZ() - item.getLocation().getZ()) / t;
		Vector velocity = item.getVelocity();
		velocity.setX(vx);
		velocity.setZ(vz);
		item.setVelocity(velocity);
	}
	@EventHandler(priority=EventPriority.HIGH)
	public void onPickupWeb(PlayerPickupItemEvent event){
		if(items.contains(event.getItem())){
			event.setCancelled(true);
		}
	}
	@Override
	public EntityType getType() {
		return EntityType.SPIDER;
	}
	@Override
	public SpawnRatio[] getRatios() {
		return null;
	}
}
