package net.swiftpk.client.gfx.components;

import java.awt.Rectangle;
import net.swiftpk.client.gfx.GraphicalComponent;
import net.swiftpk.client.gfx.action.Action;

public class CheckBox extends GraphicalComponent {
	private String text;
	private boolean selected = false;

	public CheckBox(int x, int y) {
		setBoundarys(new Rectangle(x, y, 15, 15));
		this.setAction((int x1, int y1, int button) -> {
			selected = !selected;
		});
	}

	@Override
	public final void setBoundarys(Rectangle bounds) {
		super.setBoundarys(bounds);
	}
	
	@Override
	public final void setAction(Action a) {
		super.setAction(a);
	}

	public boolean isSelected() {
		return selected;
	}

	@Override
	public void render() {
		if (!visible)
			return;
		mc.surface.drawBox(getX(), getY(), getWidth() + 1, getHeight() + 1,
				this.getBoarder());
		mc.surface.drawBoxAlpha(getX() + 1, getY() + 1, 14, 14,
				selected ? convertToJag(255, 0, 0) : getFill(), 100);
		if (text != null) {
			mc.surface.drawString(text, getX() + 20, getY() + getHeight() - 3,
					3, this.convertToJag(48, 244, 255));
		}
	}

	public void setSelected(boolean value) {
		selected = value;
	}

	public void setText(String text) {
		this.text = text;
	}
}