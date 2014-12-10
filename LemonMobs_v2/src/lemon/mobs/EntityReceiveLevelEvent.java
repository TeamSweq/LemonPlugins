package lemon.mobs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityReceiveLevelEvent extends EntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private final double level;
	
	public EntityReceiveLevelEvent(LivingEntity entity, double level) {
		super(entity);
		this.level = level;
	}
	public LivingEntity getEntity(){
		return (LivingEntity)super.getEntity();
	}
	public double getLevel() {
		return level;
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
