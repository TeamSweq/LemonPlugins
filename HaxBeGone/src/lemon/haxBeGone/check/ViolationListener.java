package lemon.haxBeGone.check;

import java.util.UUID;

public interface ViolationListener {
	public void onViolate(UUID player, String type);
}
