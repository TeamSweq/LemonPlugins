package lemon.haxBeGone.util;

import java.lang.reflect.Method;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NmsUtils {
	private static JavaPlugin plugin;
	
	private NmsUtils(){}
	public static void init(JavaPlugin plugin){
		NmsUtils.plugin = plugin;
	}
	public static Class<?> getNmsClass(String name){
		return ReflectionUtils.getClass(getNmsPackageName(), name);
	}
	public static Class<?> getObcClass(String name){
		return ReflectionUtils.getClass(getObcPackageName(), name);
	}
	public static Object getNmsInstance(String name, Object... args){
		Class<?> clazz = getNmsClass(name);
		if(clazz==null){
			plugin.getLogger().warning("Failed to get class");
		}
		return ReflectionUtils.getInstance(clazz, args);
	}
	public static Object getObcClass(String name, Object... args){
		Class<?> clazz = getObcClass(name);
		if(clazz==null){
			plugin.getLogger().warning("Failed to get class");
		}
		return ReflectionUtils.getInstance(clazz, args);
	}
	public static boolean sendPacket(Object packet, Player player){
		Object entityPlayer = ReflectionUtils.invokePublicMethod(ReflectionUtils.getPublicMethod(player.getClass(), "getHandle"), player);
		Object playerConnection;
		try {
			playerConnection = ReflectionUtils.getPublicField(entityPlayer.getClass(), "playerConnection").get(entityPlayer);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			plugin.getLogger().warning("Failed to get player connection");
			e.printStackTrace();
			return false;
		}
		Method method = ReflectionUtils.getPublicMethod(playerConnection.getClass(), "sendPacket", packet.getClass().getSuperclass());
		ReflectionUtils.invokePublicMethod(method, playerConnection, packet);
		return true;
	}
	public static String getNmsPackageName(){
		return "net.minecraft.server."+getVersion();
	}
	public static String getObcPackageName(){
		return "org.bukkit.craftbukkit."+getVersion();
	}
	public static String getVersion(){
		return plugin.getServer().getClass().getPackage().getName().replace('.', ',').split(",")[3];
	}
}
