package lemon.haxBeGone.proof;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class ProofRecorder implements Runnable {
	private static JavaPlugin plugin;
	private static List<ProofRecorderListener> listeners;
	private ProofBuilder proof;
	private UUID player;
	private int taskId;
	private int timesLeftToRecord;
	private boolean isRunning;
	public static void init(JavaPlugin plugin){
		ProofRecorder.plugin = plugin;
		listeners = new ArrayList<ProofRecorderListener>();
	}
	public static void registerListener(ProofRecorderListener listener){
		listeners.add(listener);
	}
	private static ProofRecorder onStopRecorder(ProofRecorder recorder){
		for(ProofRecorderListener listener: listeners){
			recorder = listener.onStopRecord(recorder);
		}
		recorder.restartProof();
		return recorder;
	}
	public ProofRecorder(UUID player) {
		//What's the difference between runTaskTimerAsynchronysly and not?
		this.player = player;
		proof = new ProofBuilder();
		taskId = -1;
		timesLeftToRecord = -1;
		isRunning = false;
	}
	public boolean startRecorder(int interval, int timesToRecord){
		if((!plugin.getServer().getOfflinePlayer(player).isOnline())||isRecording()){
			return false;
		}
		proof.setInterval(interval);
		timesLeftToRecord = timesToRecord;
		taskId = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, interval).getTaskId();
		isRunning = true;
		return true;
	}
	public void setTimesToRecord(int timesToRecord){
		timesLeftToRecord = timesToRecord;
	}
	public int getInterval(){
		return proof.getInterval();
	}
	public void run() {
		if(timesLeftToRecord!=-1&&timesLeftToRecord<=0){
			stopRecorder();
			return;
		}
		Frame frame = new Frame();
		frame.addProof(new PlayerProof(plugin.getServer().getPlayer(player)));
		proof.addFrame(frame);
		timesLeftToRecord--;
	}
	public boolean isRecording(){
		return isRunning;
	}
	public boolean stopRecorder(){
		if(taskId==-1){
			return false;
		}
		plugin.getServer().getScheduler().cancelTask(taskId);
		ProofRecorder recorder = ProofRecorder.onStopRecorder(this);
		//START COPY
		this.proof = new ProofBuilder(recorder.proof);
		this.player = recorder.getPlayer();
		this.timesLeftToRecord = recorder.timesLeftToRecord;
		//END COPY
		taskId = -1;
		isRunning = false;
		return true;
	}
	public void restartProof(){
		proof.restartProof();
	}
	public ProofTimeline getProof(){
		return proof.build();
	}
	public UUID getPlayer(){
		return player;
	}
}
