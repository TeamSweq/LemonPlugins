package lemon.mobs;

import org.bukkit.block.Biome;

public class SpawnRatio {
	private final Biome[] biomes;
	private final int ratio;
	public SpawnRatio(Biome[] biomes, int ratio){
		this.biomes = biomes;
		this.ratio = ratio;
	}
	public SpawnRatio(Biome[] biomes){
		this(biomes, 1);
	}
	public SpawnRatio(Biome biome, int ratio){
		this(new Biome[]{biome}, ratio);
	}
	public SpawnRatio(Biome biome){
		this(biome, 1);
	}
	public SpawnRatio(int ratio){
		this(Biome.values(), ratio);
	}
	public SpawnRatio(){
		this(Biome.values(), 1);
	}
	public Biome[] getBiomes() {
		return biomes;
	}
	public int getRatio() {
		return ratio;
	}
}
