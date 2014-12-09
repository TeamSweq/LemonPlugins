package lemon.haxBeGone.proof;

import java.util.ArrayList;
import java.util.List;

public class ProofTimeline {
	private List<Frame> frames;
	private int interval;
	protected ProofTimeline(){
		frames = new ArrayList<Frame>();
		interval = 1;
	}
	protected ProofTimeline(ProofTimeline timeline){
		frames = new ArrayList<Frame>();
		this.interval = timeline.getInterval();
		for(Frame frame: timeline.getFrames()){
			frames.add(new Frame(frame));
		}
	}
	/*protected void init(){
		frames = new ArrayList<Frame>();
		interval = 1;
	}*/
	protected void addFrame(Frame frame){
		frames.add(frame);
	}
	protected void clearFrames(){
		frames.clear();
	}
	/**
	 * 
	 * @param interval in ticks
	 */
	protected void setInterval(int interval){
		this.interval = interval;
	}
	public Frame getFrame(int index){
		return frames.get(index);
	}
	public Frame[] getFrames(){
		return frames.toArray(new Frame[]{});
	}
	public int getInterval(){
		return interval;
	}
}
