package lemon.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LemonTools extends JavaPlugin implements Listener {
	public void onEnable(){
		Connection connection = SqlUtils.createConnection("198.52.227.34", "3306", "9942", "9942", "7fb0ba7ff2");
		PreparedStatement statement = SqlUtils.prepareStatement(connection, "CREATE TABLE IF NOT EXIST players("
				+ "id int NOT NULL AUTO_INCREMENT"
				+ "uuid varchar(36) NOT NULL"
				+ "rank varchar(3)"
				+ "PRIMARY KEY(id)"
				+ ")");
		SqlUtils.sendStatement(connection, statement);
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		return false;
	}
	public void onJoin(){
		
	}
}