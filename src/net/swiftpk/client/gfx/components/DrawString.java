package net.swiftpk.client.gfx.components;

import java.awt.Rectangle;
import net.swiftpk.client.gfx.GraphicalComponent;

public class DrawString extends GraphicalComponent {
	private String text;
	private final boolean centered;

	public DrawString(String text, boolean centered, Rectangle bounds) {
		this.text = text;
		this.centered = centered;
		this.setBoundarys(bounds);
	}
	
	@Override
	public final void setBoundarys(Rectangle bounds) {
		super.setBoundarys(bounds);
	}

	@Override
	public void render() {
		if (visible) {
			if (centered) {
				mc.surface.drawStringCentered(text, getX(), getY() + 10, 1,
						hovering ? this.getFillHovering() : this.getFill());
			} else
				mc.surface.drawString(text, getX(), getY() + 10, 1,
						hovering ? this.getFillHovering() : this.getFill());
		}
	}

	public void setColor(int color) {
		this.setFill(color);
	}

	public void setText(String text) {
		this.text = text;
	}

}
