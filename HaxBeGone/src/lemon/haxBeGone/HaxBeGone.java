package lemon.haxBeGone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import lemon.haxBeGone.check.Check;
import lemon.haxBeGone.check.FlyCheck;
import lemon.haxBeGone.check.ViolationListener;
import lemon.haxBeGone.detection.Detection;
import lemon.haxBeGone.detection.DetectionStorage;
import lemon.haxBeGone.detection.DetectionType;
import lemon.haxBeGone.proof.ProofRecorder;
import lemon.haxBeGone.proof.ProofRecorderListener;
import lemon.haxBeGone.util.SqlUtils;

import org.bukkit.plugin.java.JavaPlugin;

public class HaxBeGone extends JavaPlugin implements ViolationListener, ProofRecorderListener {
	private HashMap<UUID, List<String>> violations;
	private HashMap<UUID, ProofRecorder> recorders;
	public void onEnable(){
		violations = new HashMap<UUID, List<String>>();
		recorders = new HashMap<UUID, ProofRecorder>();
		SqlUtils.init(this);
		DetectionStorage.init();
		ProofRecorder.init(this);
		ProofRecorder.registerListener(this);
		Check.initListeners();
		Check.registerListener(this);
		//new SpeedCheck().init(this);
		new FlyCheck().init(this);
	}
	public void onDisable(){
		this.getLogger().info("Saving Detections");
		DetectionStorage.save();
		this.getLogger().info("Save Complete");
	}
	public void onViolate(UUID player, String type) {
		List<String> playerViolations = violations.get(player);
		ProofRecorder recorder = recorders.get(player);
		if(recorder==null){
			recorder = new ProofRecorder(player);
		}
		if(playerViolations==null){
			playerViolations = new ArrayList<String>();
		}
		playerViolations.add(type);
		if(!recorder.isRecording()){
			recorder.startRecorder(1, 600); //30 seconds = 30*20 ticks=600
		}else{
			recorder.setTimesToRecord(600);
		}
		violations.put(player, playerViolations);
		recorders.put(player, recorder);
	}
	public ProofRecorder onStopRecord(ProofRecorder recorder) {
		List<String> playerViolations = violations.get(recorder.getPlayer());
		String hackType = getMode(playerViolations).toUpperCase();
		DetectionStorage.addDetection(recorder.getPlayer(), new Detection(DetectionType.valueOf(hackType), (int)(Math.random()*100), recorder.getProof()));
		return recorder;
	}
	public int getViolationsCount(UUID player, String type){
		int count = 0;
		if(violations.get(player)==null){
			return 0;
		}
		for(String violationType: violations.get(player)){
			if(violationType.equalsIgnoreCase(type)){
				count++;
			}
		}
		return count;
	}
	private String getMode(List<String> list){ //If there is more than one, the most recent will be used
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(String string: list){
			if(!map.containsKey(string)){
				map.put(string, 1);
			}else{
				map.put(string, map.get(string)+1);
			}
		}
		String mode = null;
		int max = 0;
		for(String string: list){
			if(map.get(string)>=max){
				mode = string;
				max = map.get(string);
			}
		}
		return mode;
	}
}
