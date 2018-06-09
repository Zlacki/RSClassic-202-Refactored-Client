package net.swiftpk.client.gfx.components;

import java.awt.Rectangle;
import net.swiftpk.client.gfx.GraphicalComponent;

public class GameFrame extends GraphicalComponent {

	private final String title;

	public GameFrame(String title, Rectangle bounds) {
		this.title = title;
		this.setBoundarys(bounds);
	}
	
	@Override
	public final void setBoundarys(Rectangle bounds) {
		super.setBoundarys(bounds);
	}

	@Override
	public void render() {
		if (this.visible) {
			mc.surface.drawBox(getX(), getY(), getWidth(), 12, 190);
			mc.surface.drawBoxAlpha(getX(), getY() + 12, getWidth(),
					getHeight(), this.getFill(), this.getOpaque());

			mc.surface.drawString(title, getX() + 1, getY() + 10, 1, 0xffffff);
			/*
			 * int k3 = 0xffffff; if (mc.mouseX > getX() + 305 && mc.mouseY >=
			 * getY() && mc.mouseX < getX() + 385 && mc.mouseY < getY() + 12) {
			 * k3 = 0xff0000; } mc.surface.drawBoxTextRight("Close window",
			 * getX() + 380, getY() + 10, 1, k3);
			 */
		}
	}

}
