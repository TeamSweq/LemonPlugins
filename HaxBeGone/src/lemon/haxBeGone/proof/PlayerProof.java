package lemon.haxBeGone.proof;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerProof extends Proof {
	private UUID name;
	private Location location;
	private int foodLevel;
	private float saturation;
	private float exhaustion;
	private PotionEffect[] effects;
	private float walkSpeed;
	private float flySpeed;
	private boolean isFlying;
	private boolean isSneaking;
	private boolean isSprinting;
	private ItemStack[] armor;
	private ItemStack[] contents;
	private PlayerProof(){}
	public PlayerProof(Player player){
		this.setUniqueId(player.getUniqueId());
		this.setLocation(player.getLocation().clone());
		this.setFoodLevel(player.getFoodLevel());
		this.setSaturation(player.getSaturation());
		this.setExhaustion(player.getExhaustion());
		this.setWalkSpeed(player.getWalkSpeed());
		this.setFlySpeed(player.getFlySpeed());
		this.setFlying(player.isFlying());
		this.setSneaking(player.isSneaking());
		this.setSprinting(player.isSprinting());
		this.setEffects(player.getActivePotionEffects().toArray(new PotionEffect[]{}));
		this.setArmor(player.getInventory().getArmorContents());
		this.setContents(player.getInventory().getContents());
	}
	public String toString(){
		return "[Name: "+name+
				"Location: "+location+
				"IsSprinting: "+isSprinting+"]";
	}
	public String getType(){
		return "Player";
	}
	public UUID getUniqueId() {
		return name;
	}
	public void setUniqueId(UUID name) {
		this.name = name;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public int getFoodLevel() {
		return foodLevel;
	}
	public void setFoodLevel(int foodLevel) {
		this.foodLevel = foodLevel;
	}
	public float getSaturation() {
		return saturation;
	}
	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}
	public float getExhaustion() {
		return exhaustion;
	}
	public void setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
	}
	public PotionEffect[] getEffects() {
		return effects;
	}
	public void setEffects(PotionEffect[] effects) {
		this.effects = effects;
	}
	public float getWalkSpeed() {
		return walkSpeed;
	}
	public void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
	public float getFlySpeed() {
		return flySpeed;
	}
	public void setFlySpeed(float flySpeed) {
		this.flySpeed = flySpeed;
	}
	public boolean isFlying() {
		return isFlying;
	}
	public void setFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}
	public boolean isSneaking() {
		return isSneaking;
	}
	public void setSneaking(boolean isSneaking) {
		this.isSneaking = isSneaking;
	}
	public boolean isSprinting() {
		return isSprinting;
	}
	public void setSprinting(boolean isSprinting) {
		this.isSprinting = isSprinting;
	}
	@Override
	public Proof getClone() {
		PlayerProof proof = new PlayerProof();
		proof.setUniqueId(getUniqueId());
		proof.setLocation(getLocation().clone());
		proof.setFoodLevel(getFoodLevel());
		proof.setSaturation(getSaturation());
		proof.setExhaustion(getExhaustion());
		PotionEffect[] newEffects = new PotionEffect[effects.length];
		for(int i=0;i<effects.length;++i){
			newEffects[i] = new PotionEffect(effects[i].getType(), effects[i].getDuration(), effects[i].getAmplifier(), effects[i].isAmbient());
		}
		proof.setEffects(newEffects);
		proof.setWalkSpeed(getWalkSpeed());
		proof.setFlySpeed(getFlySpeed());
		proof.setFlying(isFlying());
		proof.setSneaking(isSneaking());
		proof.setSprinting(isSprinting());
		ItemStack[] newArmor = new ItemStack[armor.length];
		for(int i=0;i<armor.length;++i){
			newArmor[i] = armor[i].clone();
		}
		proof.setArmor(newArmor);
		ItemStack[] newContents = new ItemStack[contents.length];
		for(int i=0;i<contents.length;++i){
			newContents[i] = contents[i].clone();
		}
		proof.setContents(newContents);
		return proof;
	}
	public ItemStack[] getArmor() {
		return armor;
	}
	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}
	public ItemStack[] getContents() {
		return contents;
	}
	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}
}
