package lemon.haxBeGone.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.plugin.java.JavaPlugin;

public class ReflectionUtils {
	private static JavaPlugin plugin;
	private static boolean debug;
	
	private ReflectionUtils(){}
	
	public static void init(JavaPlugin plugin){
		init(plugin, false);
	}
	
	public static void init(JavaPlugin plugin, boolean debug){
		ReflectionUtils.plugin = plugin;
		ReflectionUtils.debug = debug;
	}
	
	public static Field getPrivateField(Class<?> clazz, String fieldName){
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			if(debug){
				plugin.getLogger().warning("Failed to get private field");
				e.printStackTrace();
			}
			return null;
		}
	}
	public static Object getPrivateField(Class<?> clazz, String fieldName, Object object){
		Field field = getPrivateField(clazz, fieldName);
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			if(debug){
				plugin.getLogger().warning("Failed to get private field");
				e.printStackTrace();
			}
			return null;
		} finally {
			field.setAccessible(false);
		}
	}
	public static Field getPublicField(Class<?> clazz, String fieldName){
		try {
			return clazz.getField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			if(debug){
				plugin.getLogger().warning("Failed to get public field");
				e.printStackTrace();
			}
			return null;
		}
	}
	public static Object getPublicField(Class<?> clazz, String fieldName, Object object){
		Field field = getPublicField(clazz, fieldName);
		try {
			return field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			if(debug){
				plugin.getLogger().warning("Failed to get public field");
				e.printStackTrace();
			}
			return null;
		}
	}
	public static boolean setPublicField(Object object, String fieldName, Object value){
		Field field = ReflectionUtils.getPublicField(object.getClass(), fieldName);
		try {
			field.set(object, value);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			if(debug){
				plugin.getLogger().warning("Failed to set field");
				e.printStackTrace();
			}
			return false;
		}
	}
	public static boolean setPrivateField(Object object, String fieldName, Object value){
		Field field = ReflectionUtils.getPrivateField(object.getClass(), fieldName);
		field.setAccessible(true);
		try {
			field.set(object, value);
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			if(debug){
				plugin.getLogger().warning("Failed to set field");
				e.printStackTrace();
			}
			return false;
		} finally {
			field.setAccessible(false);
		}
	}
	public static void setPubilcModifier(Object object, String fieldName, int modifier){
		Field field = ReflectionUtils.getPublicField(object.getClass(), fieldName);
		ReflectionUtils.setPrivateField(field, "modifiers", field.getModifiers() & ~modifier);
	}
	public static void setPrivateModifier(Object object, String fieldName, int modifier){
		Field field = ReflectionUtils.getPrivateField(object.getClass(), fieldName);
		ReflectionUtils.setPrivateField(field, "modifiers", field.getModifiers() & ~modifier);
	}
	public static Method getPublicMethod(Class<?> clazz, String name, Class<?>... args){
		try {
			return clazz.getMethod(name, args);
		} catch (NoSuchMethodException | SecurityException e) {
			if(debug){
				plugin.getLogger().warning("Failed to get public method");
				e.printStackTrace();
			}
			return null;
		}
	}
	public static Object invokePublicMethod(Method method, Object object, Object... args){
		try {
			return method.invoke(object, args);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			if(debug){
				plugin.getLogger().warning("Failed to invoke method");
				e.printStackTrace();
			}
			return null;
		}
	}
	public static Object invokePrivateMethod(Method method, Object object, Object... args){ //Used to be private
		try {
			method.setAccessible(true);
			return method.invoke(object, args);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			if(debug){
				plugin.getLogger().warning("Failed to invoke method");
				e.printStackTrace();
			}
			return null;
		} finally {
			method.setAccessible(false);
		}
	}
	public static Object getInstance(Class<?> clazz, Object... args){
		int numOfParams = 0;
		if(args!=null){
			numOfParams = args.length;
		}
		for(Constructor<?> constructor: clazz.getConstructors()){
			if(constructor.getParameterTypes().length==numOfParams){
				try {
					return constructor.newInstance(args);
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					if(debug){
						plugin.getLogger().warning("Failed to create instance!");
						e.printStackTrace();
					}
					return null;
				}
			}
		}
		return null;
	}
	public static Class<?> getClass(String packageName, String className){
		Class<?> clazz = null;
		try {
			clazz = Class.forName(packageName+"."+className);
		} catch (ClassNotFoundException e) {
			if(debug){
				plugin.getLogger().warning("Cannot find class");
				e.printStackTrace();
			}
			return null;
		}
		return clazz;
	}
	public static <T> T castObject(Class<T> clazz, Object object){
		return clazz.cast(object);
	}
}
