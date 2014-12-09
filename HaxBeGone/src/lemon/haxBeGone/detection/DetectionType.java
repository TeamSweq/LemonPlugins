package lemon.haxBeGone.detection;

public enum DetectionType {
	FLY(0),
	SPEED(1),
	FORCEFIELD(2),
	REACH(3),
	FASTBUILD(4),
	ANTIKNOCKBACK(5),
	AUTOCRITS(6),
	SNEAK(7),
	BLOCK(8),
	CUSTOM(9);
	private final int id;
	private DetectionType(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}
}
