package lemon.haxBeGone.proof;

public class TestProof extends Proof {
	private String message;
	public TestProof(String message){
		this.message = message;
	}
	
	@Override
	public String getType() {
		return "Test";
	}
	public String toString() {
		return message;
	}
	@Override
	public Proof getClone() {
		return new TestProof(new String(message));
	}
}
