package lemon.mobs.handlers;

import lemon.mobs.EntityHandler;
import lemon.mobs.EntityHandlerInit;
import lemon.mobs.EntityReceiveLevelEvent;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SkeletonHandler implements EntityHandler {
	@EntityHandlerInit
	public static void onInit(JavaPlugin plugin){
		plugin.getServer().getPluginManager().registerEvents(new Listener(){
			@EventHandler
			public void onReceiveLevel(EntityReceiveLevelEvent event){
				if(event.getEntity() instanceof Skeleton){
					Skeleton skeleton = (Skeleton)event.getEntity();
					skeleton.addPotionEffect(
							new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE,
									(int) Math.floor(event.getLevel()/30D), false), true);
					ItemStack bow = new ItemStack(Material.BOW);
					bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, (int) (event.getLevel()/20D));
					bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, (int) (event.getLevel()/50D));
					skeleton.getEquipment().setItemInHand(bow);
					skeleton.getEquipment().setItemInHandDropChance((float)(0.025D*(Math.random()+0.5D)));
				}
			}
		}, plugin);
	}
}