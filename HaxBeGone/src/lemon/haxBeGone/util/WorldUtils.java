package lemon.haxBeGone.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldUtils {
	private static JavaPlugin plugin;
	private WorldUtils(){}
	public static void init(JavaPlugin plugin){
		WorldUtils.plugin = plugin;
	}
	public static WorldCreator createWorld(String name){
		WorldCreator creator = WorldCreator.name(name);
		return creator;
	}
	public static WorldCreator createWorld(String name, Environment environment){
		WorldCreator creator = createWorld(name);
		creator.environment(environment);
		return creator;
	}
	public static WorldCreator createWorld(String name, Environment environment, long seed){
		WorldCreator creator = createWorld(name, environment);
		creator.seed(seed);
		return creator;
	}
	public static World createWorld(WorldCreator creator){
		return plugin.getServer().createWorld(creator);
	}
	public static void addPopulator(World world, BlockPopulator populator){
		world.getPopulators().add(populator);
	}
	public static boolean spawnFallingBlock(Location location, Material material){
		Class<?> blockClass = NmsUtils.getNmsClass("Block");
		Method blockMethod = ReflectionUtils.getPublicMethod(blockClass, "e", int.class);
		Object block = ReflectionUtils.invokePublicMethod(blockMethod, null, WorldUtils.getId(material));
		Class<?> craftWorldClass = NmsUtils.getObcClass("CraftWorld");
		Object craftWorld = ReflectionUtils.castObject(craftWorldClass, location.getWorld());
		Object world = ReflectionUtils.getPrivateField(craftWorldClass, "world", craftWorld);
		Class<?> worldClass = NmsUtils.getNmsClass("World");
		world = ReflectionUtils.castObject(worldClass, world);
		Object entity = NmsUtils.getNmsInstance("EntityFallingBlock", world,
				location.getX(),
				location.getY(),
				location.getZ(),
				block);
		ReflectionUtils.setPublicField(entity, "b", 1);
		Class<?> entityClass = NmsUtils.getNmsClass("Entity");
		Method addEntity = ReflectionUtils.getPublicMethod(worldClass, "addEntity", entityClass);
		Boolean success = (Boolean)ReflectionUtils.invokePublicMethod(addEntity, world, entity);
		return success;
	}
	public static Set<Biome> getAverageBiome(Chunk chunk){
		Map<Biome, Integer> biomes = new HashMap<Biome, Integer>();
		for(int x=0;x<16;++x){
			for(int z=0;z<16;++z){
				Biome biome = chunk.getChunkSnapshot().getBiome(x, z);
				int current = 0;
				if(biomes.containsKey(biome)){
					current = biomes.get(biome);
				}
				biomes.put(biome, current+1);
			}
		}
		Set<Biome> biomeSet = new HashSet<Biome>();
		int max = 0;
		for(Biome biome: biomes.keySet()){
			if(max==biomes.get(biome)){
				biomeSet.add(biome);
			}
			if(max<biomes.get(biome)){
				max = biomes.get(biome);
				biomeSet.clear();
				biomeSet.add(biome);
			}
		}
		return biomeSet;
	}
	public static int getId(Material material){
		Method getId = ReflectionUtils.getPublicMethod(material.getClass(), "getId");
		return (int)ReflectionUtils.invokePublicMethod(getId, material);
	}
	public static int getId(EntityType type){
		Method getId = ReflectionUtils.getPublicMethod(type.getClass(), "getTypeId");
		return (int)ReflectionUtils.invokePublicMethod(getId, type);
	}
	public static Material getMaterial(int id){
		Method getMaterial = ReflectionUtils.getPublicMethod(Material.class, "getMaterial", int.class);
		return (Material)ReflectionUtils.invokePublicMethod(getMaterial, null, id);
	}
}
