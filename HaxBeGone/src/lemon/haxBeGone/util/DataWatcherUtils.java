package lemon.haxBeGone.util;

import java.lang.reflect.Method;

public class DataWatcherUtils {
	private DataWatcherUtils(){}
	
	public static void setOnFire(Object dataWatcher){
		write(dataWatcher, 0, (Byte)(byte)0x01);
	}
	
	public static void setInvisible(Object dataWatcher){
		write(dataWatcher, 0, (Byte)(byte)0x20);
	}
	
	public static void setHealth(Object dataWatcher, float health){
		write(dataWatcher, 6, (Float)(float)health);
	}
	
	public static void setNameTag(Object dataWatcher, String name){
		write(dataWatcher, 10, (String)name);
	}
	
	public static void showNameTag(Object dataWatcher, boolean show){
		write(dataWatcher, 11, (Byte)(byte)(show?1:0));
	}
	
	public static void setAge(Object dataWatcher, int i){
		write(dataWatcher, 12, (Integer)i);
	}
	
	public static Object getDataWatcher(){
		return NmsUtils.getNmsInstance("DataWatcher", new Object[]{null});
	}
	public static void write(Object dataWatcher, int i, Object object){
		Method method = ReflectionUtils.getPublicMethod(dataWatcher.getClass(), "a", int.class, Object.class);
		ReflectionUtils.invokePublicMethod(method, dataWatcher, i, object);
	}
}
