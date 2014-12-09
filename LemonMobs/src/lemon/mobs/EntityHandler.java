package lemon.mobs;

import org.bukkit.entity.EntityType;

public abstract class EntityHandler {
	
	public abstract SpawnRatio[] getRatios();
	
	public abstract void init(LemonMobs plugin);
	public abstract EntityType getType();
}
