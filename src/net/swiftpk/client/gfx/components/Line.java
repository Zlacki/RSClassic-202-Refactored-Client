package net.swiftpk.client.gfx.components;

import net.swiftpk.client.gfx.GraphicalComponent;

public class Line extends GraphicalComponent {

	private final int x, y, endx;

	public Line(int x, int y, int endx) {
		this.x = x;
		this.y = y;
		this.endx = endx;
	}

	@Override
	public void render() {
		if (!visible)
			return;
		mc.surface.drawLineX(x, y, endx, this.convertToJag(0, 0, 0));
	}
}