package net.swiftpk.client.gfx.uis;

import net.swiftpk.client.gfx.GraphicalComponent;
import net.swiftpk.client.gfx.GraphicalOverlay;
import net.swiftpk.client.gfx.components.DrawArc;
import net.swiftpk.client.mudclient;

public class ChatUI extends GraphicalOverlay {

	public ChatUI(mudclient<?> mc) {
		super(mc);
		add(new DrawArc());
	}
	
	@Override
	public final boolean add(GraphicalComponent... comp) {
		return super.add(comp);
	}

}
