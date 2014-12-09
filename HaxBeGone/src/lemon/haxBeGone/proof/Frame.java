package lemon.haxBeGone.proof;

import java.util.ArrayList;
import java.util.List;

public class Frame {
	private List<Proof> proofs;
	public Frame(){
		proofs = new ArrayList<Proof>();
	}
	public Frame(Frame frame){
		this();
		for(Proof proof: frame.getProofs()){
			proofs.add(proof.getClone());
		}
	}
	public void addProof(Proof proof){
		proofs.add(proof);
	}
	public void delProof(Proof proof){
		proofs.remove(proof);
	}
	public List<Proof> getProofs(String type){
		List<Proof> list = new ArrayList<Proof>();
		for(Proof proof: proofs){
			if(proof.getType().equalsIgnoreCase(type)){
				list.add(proof);
			}
		}
		return list;
	}
	public List<Proof> getProofs(){
		return proofs;
	}
}
