package net.swiftpk.client.gfx.components;

import net.swiftpk.client.gfx.GraphicalComponent;

public class DrawArc extends GraphicalComponent {

	@Override
	public void render() {
		mc.surface.rounded_rectangle(100, 100, 100, 100, 50000, 100);
	}

}
