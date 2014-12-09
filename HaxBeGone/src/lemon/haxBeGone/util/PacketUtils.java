package lemon.haxBeGone.util;

import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class PacketUtils {
	private PacketUtils(){}
	
	public static Object getLivingEntitySpawnPacket(int id, EntityType type, Location location, Object dataWatcher){
		Object packet = NmsUtils.getNmsInstance("PacketPlayOutSpawnEntityLiving");
		ReflectionUtils.setPrivateField(packet, "a", (int)id);
		ReflectionUtils.setPrivateField(packet, "b", (int)WorldUtils.getId(type));
		ReflectionUtils.setPrivateField(packet, "c", (int)Math.floor(location.getBlockX()*32.0D));
		ReflectionUtils.setPrivateField(packet, "d", (int)Math.floor(location.getBlockY()*32.0D));
		ReflectionUtils.setPrivateField(packet, "e", (int)Math.floor(location.getBlockZ()*32.0D));
		ReflectionUtils.setPrivateField(packet, "f", (int)0);
		ReflectionUtils.setPrivateField(packet, "g", (int)0);
		ReflectionUtils.setPrivateField(packet, "h", (int)0);
		ReflectionUtils.setPrivateField(packet, "i", (byte)0);
		ReflectionUtils.setPrivateField(packet, "j", (byte)0);
		ReflectionUtils.setPrivateField(packet, "k", (byte)0);
		ReflectionUtils.setPrivateField(packet, "l", dataWatcher);
		return packet;
	}
	
	public static Object getEntitySpawnPacket(int id, EntityType type, Location location){
		Object packet = NmsUtils.getNmsInstance("PacketPlayOutSpawnEntity");
		ReflectionUtils.setPrivateField(packet, "a", (int)id);
		ReflectionUtils.setPrivateField(packet, "b", (int)Math.floor(location.getBlockX()*32.0D));
		ReflectionUtils.setPrivateField(packet, "c", (int)Math.floor(location.getBlockY()*32.0D));
		ReflectionUtils.setPrivateField(packet, "d", (int)Math.floor(location.getBlockZ()*32.0D));
		ReflectionUtils.setPrivateField(packet, "e", (int)0); //Unknown
		ReflectionUtils.setPrivateField(packet, "f", (int)0); //Unknown
		ReflectionUtils.setPrivateField(packet, "g", (int)0); //Unknown
		ReflectionUtils.setPrivateField(packet, "h", (byte)0);
		ReflectionUtils.setPrivateField(packet, "i", (byte)0);
		ReflectionUtils.setPrivateField(packet, "j", (int)WorldUtils.getId(type));
		ReflectionUtils.setPrivateField(packet, "k", (int)0); //Unknown
		return packet;
	}
	
	public static Object getEntityDestroyPacket(int id){
		Object packet = NmsUtils.getNmsInstance("PacketPlayOutEntityDestroy");
		ReflectionUtils.setPrivateField(packet, "a", new int[]{id});
		return packet;
	}
	
	public static Object getEntityMetadataPacket(int id, Object dataWatcher){
		Object packet = NmsUtils.getNmsInstance("PacketPlayOutEntityMetadata");
		ReflectionUtils.setPrivateField(packet, "a", id);
		Method method = ReflectionUtils.getPublicMethod(dataWatcher.getClass(), "c");
		ReflectionUtils.setPrivateField(packet, "b", ReflectionUtils.invokePublicMethod(method, dataWatcher));
		return packet;
	}
	
	public static Object getEntityAttachPacket(int entity, int entity2){
		Object packet = NmsUtils.getNmsInstance("PacketPlayOutAttachEntity");
		ReflectionUtils.setPrivateField(packet, "a", (int)0);
		ReflectionUtils.setPrivateField(packet, "b", entity);
		ReflectionUtils.setPrivateField(packet, "c", entity2);
		return packet;
	}
	
	public static Object getEntityEffectPacket(int entity, int effect, int amplifier, int duration){
		if(duration>Short.MAX_VALUE){
			duration = Short.MAX_VALUE;
		}
		Object packet = NmsUtils.getNmsInstance("PacketPlayOutEntityEffect");
		ReflectionUtils.setPrivateField(packet, "a", entity);
		ReflectionUtils.setPrivateField(packet, "b", (byte)(effect & 255));
		ReflectionUtils.setPrivateField(packet, "c", (byte)(amplifier & 255));
		ReflectionUtils.setPrivateField(packet, "d", (short)duration);
		return packet;
	}
}
