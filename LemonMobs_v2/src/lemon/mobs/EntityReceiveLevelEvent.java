package lemon.mobs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntityReceiveLevelEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final LivingEntity entity;
	private double level;
	
	public EntityReceiveLevelEvent(LivingEntity entity, double level) {
		this.entity = entity;
		this.level = level;
	}
	public LivingEntity getEntity(){
		return entity;
	}
	public void setLevel(double level){
		this.level = level;
	}
	public double getLevel() {
		return level;
	}
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
