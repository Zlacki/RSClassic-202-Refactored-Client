package net.swiftpk.client.gfx;

import java.awt.Component;
import net.swiftpk.client.mudclient;

public final class SurfaceSprite extends Surface {

	public mudclient<?> client;

	public SurfaceSprite(int width, int height, int k, Component component) {
		super(width, height, k, component);
	}

	@Override
	public final void spriteClipping(int x, int y, int k, int l, int index,
			int j1, int k1) {
		if (index >= 50000) {
			client.method71(x, y, k, l, index - 50000, j1, k1);
			return;
		}
		if (index >= 40000) {
			client.method68(x, y, k, l, index - 40000, j1, k1);
			return;
		}
		if (index >= 20000) {
			client.processNPCs(x, y, k, l, index - 20000, j1, k1);
			return;
		}
		if (index >= 5000) {
			client.processPlayers(x, y, k, l, index - 5000, j1, k1);
		} else {
			super.spriteClip1(x, y, k, l, index);
		}
	}
}
