package lemon.haxBeGone.proof;


public class ProofBuilder {
	private ProofTimeline proof;
	public ProofBuilder(){
		restartProof();
	}
	public ProofBuilder(ProofBuilder builder){
		this.proof = new ProofTimeline(builder.build());
	}
	public void addFrame(Frame frame){
		proof.addFrame(frame);
	}
	public void clearFrames(){
		proof.clearFrames();
	}
	public void setInterval(int interval){
		proof.setInterval(interval);
	}
	public int getInterval(){
		return proof.getInterval();
	}
	public ProofTimeline build(){
		return proof;
	}
	public void restartProof(){
		proof = new ProofTimeline();
	}
}
