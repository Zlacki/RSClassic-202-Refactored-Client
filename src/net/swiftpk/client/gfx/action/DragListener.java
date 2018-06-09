package net.swiftpk.client.gfx.action;

public interface DragListener {

	boolean onDragging(int startX, int startY);

	void stopDragging();

}
