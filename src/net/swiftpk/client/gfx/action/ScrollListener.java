package net.swiftpk.client.gfx.action;

public interface ScrollListener {

	void onScrollUpdate(int index);

	// 0 down - 1 up
    void scrolling(int type);

}
