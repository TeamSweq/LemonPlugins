package lemon.mobs.handlers;

import org.bukkit.entity.EntityType;

import lemon.mobs.EntityHandler;
import lemon.mobs.LemonMobs;
import lemon.mobs.SpawnRatio;

public class SnowmanHandler extends EntityHandler {
	@Override
	public void init(LemonMobs plugin) {
		
	}
	@Override
	public EntityType getType() {
		return EntityType.SNOWMAN;
	}
	@Override
	public SpawnRatio[] getRatios() {
		return null;
	}
}
