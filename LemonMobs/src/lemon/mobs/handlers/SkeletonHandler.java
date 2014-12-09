package lemon.mobs.handlers;

import lemon.mobs.EntityHandler;
import lemon.mobs.LemonMobs;
import lemon.mobs.MobUtils;
import lemon.mobs.SpawnRatio;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class SkeletonHandler extends EntityHandler implements Listener {
	
	@Override
	public void init(LemonMobs plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onSpawnSkeleton(CreatureSpawnEvent event){
		if(event.getEntityType()==EntityType.SKELETON){
			Skeleton skeleton = (Skeleton) event.getEntity();
			ItemStack bow = new ItemStack(Material.BOW);
			double mobLevel = MobUtils.getMetadata(event.getEntity(), "Level").asDouble();
			int powerLevel = (int) (mobLevel/20);
			if(powerLevel>0){
				bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, (int) (mobLevel/20));
			}
			if(mobLevel>=100){
				bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
			}
			skeleton.getEquipment().setItemInHand(bow);
			skeleton.getEquipment().setItemInHandDropChance(Math.max((float)(0.025*Math.random()), 0f));
		}
	}
	
	@Override
	public EntityType getType() {
		return EntityType.SKELETON;
	}

	@Override
	public SpawnRatio[] getRatios() {
		return new SpawnRatio[]{new SpawnRatio(1)};
	}
}
