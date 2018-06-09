package net.swiftpk.client.gfx.uis.various;

import java.util.ArrayList;
import java.util.List;

import net.swiftpk.client.mudclient;
import net.swiftpk.client.gfx.GraphicalOverlay;
import net.swiftpk.client.gfx.uis.BankUI;
import net.swiftpk.client.gfx.uis.ChatUI;

public class GameUIs {
	public static void reload() {
		overlay.clear();
		overlay.add(new BankUI(mudclient.getInstance()));
		overlay.add(new ChatUI(mudclient.getInstance()));
	}

	public static List<GraphicalOverlay> overlay = new ArrayList<>();

	static {
		reload();
	}

}
