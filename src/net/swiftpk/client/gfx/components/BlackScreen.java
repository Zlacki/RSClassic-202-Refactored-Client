package net.swiftpk.client.gfx.components;

import net.swiftpk.client.gfx.GraphicalComponent;

public class BlackScreen extends GraphicalComponent {

	@Override
	public void render() {
		mc.surface.blackScreen();
	}

}
