package net.swiftpk.client.gfx.components;

import java.awt.Rectangle;
import net.swiftpk.client.gfx.GraphicalComponent;

public class Button extends GraphicalComponent {

	private String text;

	public boolean selected = false;

	public Button(Rectangle bounds) {
		setBoundarys(bounds);
	}

	@Override
	public final void setBoundarys(Rectangle bounds) {
		super.setBoundarys(bounds);
	}

	@Override
	public void render() {
		if (!visible)
			return;
		mc.surface.drawBox(getX(), getY(), getWidth() + 1, getHeight() + 1,
				hovering ? this.getFillHovering() : this.getBoarder());

		mc.surface.drawBoxAlpha(getX() + 1, getY() + 1, getWidth() - 1,
				getHeight() - 1, this.getFill(), getOpaque());

		if (text != null) {
			mc.surface.drawString(text, getX() + 6, getY() + getHeight() - 5,
					3, this.convertToJag(48, 244, 255));
		}

	}

	public void setText(String text) {
		this.text = text;
	}
}