package lemon.chat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
//import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;


public class LemonChat extends JavaPlugin implements Listener {
	private Set<String> groupNames;
	private HashMap<String, UUID[]> groups;
	private HashMap<String, String> chatFormat;
	private HashMap<String, String> loginFormat;
	private HashMap<String, String> quitFormat;
	//private HashMap<String, String> achievementFormat;
	private HashMap<String, String> color;
	private HashMap<String, String[]> mute;
	private HashMap<String, Integer> muteTime;
	private HashMap<String, Long> muted;
	private long defaultMuteTime;
	public void onEnable(){
		groupNames = new HashSet<String>();
		groups = new HashMap<String,UUID[]>();
		chatFormat = new HashMap<String, String>();
		loginFormat = new HashMap<String, String>();
		quitFormat = new HashMap<String, String>();
		color = new HashMap<String, String>();
		mute = new HashMap<String, String[]>();
		muteTime = new HashMap<String, Integer>();
		muted = new HashMap<String, Long>();
		defaultMuteTime = 0;
		this.getLogger().info("Loading Config...");
		loadConfig();
		this.getLogger().info("Loading Complete");
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	@SuppressWarnings("deprecation") //TODO
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = null;
		if(sender instanceof Player){
			player = (Player)sender;
		}
		if(cmd.getName().equalsIgnoreCase("mute")){
			if(args.length<=0){
				return false;
			}
			String[] players = null;
			String playerToMute = args[0].toLowerCase();
			if(player!=null){
				players = mute.get(getGroup(player.getUniqueId()));
				if(players==null){
					players = new String[]{};
				}
			}else{
				players = groupNames.toArray(new String[]{});
			}
			//UUID FETCHER?
			if(this.getServer().getPlayerExact(playerToMute)!=null){
				if(Arrays.asList(players).contains(getGroup(this.getServer().getPlayerExact(playerToMute).getUniqueId()))){
					long time = Math.min(defaultMuteTime, muteTime.get(getGroup(player.getUniqueId())));
					if(args.length>=2){
						try{
							time = parseToMilliseconds(args[1]);
						}catch(NumberFormatException ex){
							sender.sendMessage("Parsing Failed! Muting default time..");
						}
					}
					muted.put(playerToMute, System.currentTimeMillis()+time);
					sender.sendMessage("Muted player "+playerToMute+" for "+time+" milliseconds");
				}else{
					sender.sendMessage("You can't mute that person!");
				}
			}else{
				sender.sendMessage("Cannot find player!");
			}
			return true;
		}
		if(player!=null){
			if(!player.getName().equalsIgnoreCase("awesomelemonade")){
				player.sendMessage(ChatColor.RED+"You aren't awesomelemonade!");
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("lemonchat")){
			if(args.length>=1){
				if(args[0].equalsIgnoreCase("reload")){
					reloadConfig();
					loadConfig();
					sender.sendMessage(ChatColor.GREEN+"Reload Successful!");
					return true;
				}
			}
		}
		return false;
	}
	public void loadConfig(){
		chatFormat.clear();
		loginFormat.clear();
		quitFormat.clear();
		color.clear();
		ScoreboardManager sManager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = sManager.getMainScoreboard();
		for(Team team: scoreboard.getTeams()){
			team.unregister();
		}
		List<String> groups = this.getConfig().getStringList("Groups");
		for(String group: groups){
			List<String> players = this.getConfig().getStringList(group+".Players");
			UUID[] uuids = new UUID[players.size()];
			for(int i=0;i<players.size();++i){
				uuids[i] = UUID.fromString(players.get(i));
			}
			this.groups.put(group, uuids);
			groupNames.add(group);
			chatFormat.put(group, this.getConfig().getString(group+".ChatFormat"));
			loginFormat.put(group, this.getConfig().getString(group+".LoginFormat"));
			quitFormat.put(group, this.getConfig().getString(group+".QuitFormat"));
			String[] muteCmds = parse(this.getConfig().getString(group+".Mute"));
			mute.put(group, muteCmds);
			muteTime.put(group, this.getConfig().getInt(group+".MuteTime"));
			color.put(group, addColor(this.getConfig().getString(group+".Color")));
			Team team = scoreboard.registerNewTeam(group);
			for(UUID player: uuids){
				team.addPlayer(Bukkit.getOfflinePlayer(player));
			}
			team.setPrefix(addColor(color.get(group)));
			team.setAllowFriendlyFire(true);
		}
		chatFormat.put("DEFAULT", this.getConfig().getString("DEFAULT.ChatFormat"));
		loginFormat.put("DEFAULT", this.getConfig().getString("DEFAULT.LoginFormat"));
		quitFormat.put("DEFAULT", this.getConfig().getString("DEFAULT.QuitFormat"));
		color.put("DEFAULT", addColor(this.getConfig().getString("DEFAULT.Color")));
		defaultMuteTime = parseToMilliseconds(this.getConfig().getString("DEFAULT.MuteTime"));
		for(String group: groupNames){
			String[] muteCmds = mute.get(group);
			Set<String> mutes = new HashSet<String>();
			if(muteCmds!=null){
				for(String cmd: muteCmds){
					if(cmd.equalsIgnoreCase("All")){
						mutes.addAll(groupNames);
						mutes.add("DEFAULT");
						continue;
					}
					if(cmd.equalsIgnoreCase("-All")){
						mutes.clear();
						continue;
					}
					if(cmd.startsWith("-")){
						mutes.remove(cmd.substring(1));
					}else{
						mutes.add(cmd);
					}
				}
			}
			mute.remove(group);
			mute.put(group, mutes.toArray(new String[]{}));
		}
	}
	public String[] parse(String string){
		if(string==null){
			return null;
		}
		int index = -1;
		int index2 = -1;
		Set<String> strings = new HashSet<String>();
		while(((index=string.indexOf("["))!=-1)&&(((index2=string.indexOf("]"))!=-1))){
			String s = string.substring(index+1, index2);
			if(s.charAt(0)=='"'&&s.charAt(s.length()-1)=='"'){
				s = s.substring(1, s.length()-1);
			}
			strings.add(s);
			string = string.substring(index2+1);
		}
		return strings.toArray(new String[]{});
	}
	public long parseToMilliseconds(String string){
		char unit = string.charAt(string.length()-1);
		try{
			long milliseconds = Long.parseLong(string.substring(0, string.length()-1));
			if(unit=='s'){
				milliseconds*=1000l;
			}
			if(unit=='m'){
				milliseconds*=60000l;
			}
			if(unit=='h'){
				milliseconds*=3600000l;
			}
			if(unit=='d'){
				milliseconds*=86400000l;
			}
			if(unit=='y'){
				milliseconds*=315360000000l;
			}
			return milliseconds;
		}catch(NumberFormatException ex){
			return -1;
		}
	}
	public String getGroup(UUID player){
		for(String group: groupNames){
			UUID[] players = groups.get(group);
			if(players!=null){
				for(UUID uuid: players){
					if(player.equals(uuid)){
						return group;
					}
				}
			}
		}
		return "DEFAULT";
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		event.setCancelled(true);
		if(muted.containsKey(event.getPlayer().getName().toLowerCase())){
			if(System.currentTimeMillis()>muted.get(event.getPlayer().getName().toLowerCase())){
				muted.remove(event.getPlayer().getName().toLowerCase());
			}else{
				event.getPlayer().sendMessage("You are muted for "+(muted.get(event.getPlayer().getName().toLowerCase())-System.currentTimeMillis())+" more milliseconds!");
				return;
			}
		}
		String line = chatFormat.get(getGroup(event.getPlayer().getUniqueId()));
		if(line==null){
			line = chatFormat.get("DEFAULT");
			if(line==null){
				line = "Chat Message Failed - Please Contact Server Admin";
			}
		}
		line = addColor(line);
		line = line.replace("%p", event.getPlayer().getName());
		line = line.replace("%m", event.getMessage());
		broadcast(line);
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		event.setJoinMessage(null);
		String line = loginFormat.get(getGroup(event.getPlayer().getUniqueId()));
		if(line==null){
			line = loginFormat.get("DEFAULT");
			if(line==null){
				line = "Login Message Failed - Please Contact Server Admin";
			}
		}
		line = addColor(line);
		line = line.replace("%p", event.getPlayer().getName());
		broadcast(line);
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		event.setQuitMessage(null);
		String line = quitFormat.get(getGroup(event.getPlayer().getUniqueId()));
		if(line==null){
			line = quitFormat.get("DEFAULT");
			if(line==null){
				line = "Quit Message Failed - Please Contact Server Admin";
			}
		}
		line = addColor(line);
		line = line.replace("%p", event.getPlayer().getName());
		broadcast(line);
	}
	//@EventHandler
	//public void onAchievement(PlayerAchievementAwardedEvent event){
		//TODO
	//}
	public void broadcast(String msg){
		for(Player p: this.getServer().getOnlinePlayers()){
			p.sendMessage(msg);
		}
		this.getLogger().info(msg);
	}
	public String addColor(String s){
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
