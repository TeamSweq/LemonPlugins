package lemon.haxBeGone.detection;

import lemon.haxBeGone.proof.ProofTimeline;

public class Detection {
	private DetectionType type;
	private int level;
	private ProofTimeline proof;
	public Detection(DetectionType type, int level, ProofTimeline proof){
		this.type = type;
		this.level = level;
		this.proof = proof;
	}
	public Detection setType(DetectionType type){
		this.type = type;
		return this;
	}
	public DetectionType getType(){
		return type;
	}
	public Detection setLevel(int level){
		this.level = level;
		return this;
	}
	public Detection setProof(ProofTimeline proof){
		this.proof = proof;
		return this;
	}
	public int getLevel(){
		return level;
	}
	public ProofTimeline getProof(){
		return proof;
	}
}
