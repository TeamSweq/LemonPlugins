package lemon.haxBeGone.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

public class SqlUtils {
	private static JavaPlugin plugin;
	private static boolean debug;
	
	private SqlUtils(){}
	
	public static void init(JavaPlugin plugin){
		init(plugin, false);
	}
	public static void init(JavaPlugin plugin, boolean debug){
		SqlUtils.plugin = plugin;
		SqlUtils.debug = debug;
		if(ReflectionUtils.getClass("com.mysql.jdbc", "Driver")==null){
			if(debug){
				plugin.getLogger().warning("Failed to get JDBC Driver");
			}
		}
	}
	public static Connection createConnection(String hostname, String port, String database, String username, String password){
		try {
			return DriverManager.getConnection("jdbc:mysql://"+hostname+"/"+database+"?user="+username+"&password="+password);
		} catch (SQLException e) {
			if(debug){
				plugin.getLogger().warning("Failed to establish connection");
				e.printStackTrace();
			}
		}
		return null;
	}
	public static void closeConnection(Connection connection){
		if(connection!=null){
			try {
				connection.close();
			} catch (SQLException e) {
				plugin.getLogger().warning("Failed to close connection");
				e.printStackTrace();
			}
		}
	}
	public static PreparedStatement prepareStatement(Connection connection, String line){
		try {
			return connection.prepareStatement(line);
		} catch (SQLException e) {
			if(debug){
				plugin.getLogger().warning("Failed to prepare PreparedStatement");
				e.printStackTrace();
			}
			return null;
		}
	}
	public static boolean setValues(PreparedStatement statement, Object... values){
		if(values!=null){
			for(int i=0;i<values.length;++i){
				try {
					statement.setObject(i, values[i]);
				} catch (SQLException e) {
					if(debug){
						plugin.getLogger().warning("Failed to set statement: "+i+" to "+values[i].toString());
						e.printStackTrace();
					}
					return false;
				}
			}
		}
		return true;
	}
	public static int sendStatement(Connection connection, PreparedStatement statement){
		try {
			return statement.executeUpdate();
		} catch (SQLException e) {
			if(debug){
				plugin.getLogger().warning("Failed to send statement");
				e.printStackTrace();
			}
			return -1;
		}
	}
	public static ResultSet sendQuery(Connection connection, PreparedStatement statement){
		try {
			return statement.executeQuery();
		} catch (SQLException e) {
			if(debug){
				plugin.getLogger().warning("Failed to send query");
				e.printStackTrace();
			}
			return null;
		}
	}
}
