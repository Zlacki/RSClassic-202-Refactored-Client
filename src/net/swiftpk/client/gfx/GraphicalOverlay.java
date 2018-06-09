package net.swiftpk.client.gfx;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import net.swiftpk.client.mudclient;

public class GraphicalOverlay {

	private final List<GraphicalComponent> components = new ArrayList<>();

	private final mudclient<?> mc;

	public boolean visible = false;

	public boolean menu = false;

	public GraphicalOverlay(mudclient<?> mc) {
		this.mc = mc;
	}

	public boolean add(GraphicalComponent... comp) {
		for (GraphicalComponent c : comp) {
			c.mc = mc;
			components.add(c);
		}
		return true;
	}

	private boolean doAction(int x, int y, int button, GraphicalComponent comp) {
		if (comp.getBoundarys() != null
				&& comp.getBoundarys().contains(new Point(x, y))) {
			for (GraphicalComponent c : comp.getComponents()) {
				c.hovering = c.getBoundarys().contains(new Point(x, y));
				if (c.hoveringCallback != null)
					c.hoveringCallback.hovering();
				if (c.action != null && button != 0
						&& c.getBoundarys().contains(new Point(x, y))) {
					c.action.action(x, y, button);
					return true;
				}
			}
			comp.hovering = true;
			if (comp.hoveringCallback != null)
				comp.hoveringCallback.hovering();
			if (comp.action != null && button != 0) {
				comp.action.action(x, y, button);
				return true;
			}
			return false;
		} else {
			comp.hovering = false;
			comp.getComponents().stream().filter((c) -> (!c.getBoundarys().contains(new Point(x, y)))).forEach((c) -> {
				c.hovering = false;
			});
		}
		return false;
	}

	public List<GraphicalComponent> getComponents() {
		return components;
	}

	public boolean isMenu() {
		return menu;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean onAction(int x, int y, int button) {
		return components.stream().filter((comp) -> !(!comp.visible && !comp.isDragging())).anyMatch((comp) -> (!comp.isDragging() && doAction(x, y, button, comp)));
	}

	public boolean onComponent(int x, int y, GraphicalComponent comp) {
		return comp.getBoundarys() != null
				&& comp.getBoundarys().contains(new Point(x, y));
	}

	public void onRender() {
		if (visible) {
			components.stream().forEach((comp) -> {
				comp.onRender();
			});
		}
	}

	public void onResize(int width, int height) {
		components.stream().forEach((comp) -> {
			comp.onResize(width, height);
		});
	}

	public void setMenu(boolean menu) {
		this.menu = menu;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}