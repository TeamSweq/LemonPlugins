package lemon.haxBeGone.detection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import lemon.haxBeGone.util.SqlUtils;

public class DetectionStorage {
	private static HashMap<UUID, List<Detection>> detections;
	private DetectionStorage(){}
	public static void init(){
		detections = new HashMap<UUID, List<Detection>>();
	}
	public static void save(){
		Connection connection = SqlUtils.createConnection("198.52.227.34", "3306", "9942", "9942", "7fb0ba7ff2");
		PreparedStatement createDetectionTable = SqlUtils.prepareStatement(connection, 
				"CREATE TABLE IF NOT EXIST Detections("
				+ "id int NOT NULL AUTO_INCREMENT"
				+ "player varchar(36) NOT NULL"
				+ "type int NOT NULL"
				+ "level int NOT NULL"
				+ "interval int NOT NULL"
				+ ")");
		SqlUtils.sendStatement(connection, createDetectionTable);
		for(UUID player: detections.keySet()){
			for(Detection detection: detections.get(player)){
				PreparedStatement addDetection = SqlUtils.prepareStatement(connection, 
						"REPLACE INTO Detections(id, player, type, level) VALUES("
						+ "NULL, ?, ?, ?, ?"
						+ ")");
				SqlUtils.setValues(addDetection, player.toString(), detection.getType().getId(), detection.getLevel(), detection.getProof().getInterval());
				SqlUtils.sendStatement(connection, addDetection);
				PreparedStatement createSubTable = SqlUtils.prepareStatement(connection, 
						"CREATE TABLE IF NOT EXIST Detection_?("
						+ ""
						+ ")");
				SqlUtils.sendStatement(connection, createSubTable);
			}
		}
		SqlUtils.closeConnection(connection);
	}
	public static Detection[] getDetections(UUID player){
		return detections.get(player).toArray(new Detection[]{});
	}
	public static void addDetection(UUID player, Detection detection){
		List<Detection> detections = DetectionStorage.detections.get(player);
		if(detections==null){
			detections = new ArrayList<Detection>();
		}
		detections.add(detection);
		DetectionStorage.detections.put(player, detections);
	}
	public static List<Detection> delDetection(UUID player){
		return detections.remove(player);
	}
	public static void clearDetections(){
		detections.clear();
	}
}
