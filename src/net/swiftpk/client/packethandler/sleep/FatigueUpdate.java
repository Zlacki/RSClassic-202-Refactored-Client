package net.swiftpk.client.packethandler.sleep;

import net.swiftpk.client.io.ServerPacket;
import net.swiftpk.client.mudclient;
import net.swiftpk.client.packethandler.PacketHandler;

public class FatigueUpdate implements PacketHandler {

	@Override
	public int getPacketOpcode() {
		return 244;
	}

	@Override
	public void handlePacket(mudclient<?> client, ServerPacket p) {
		client.fatigue = p.readShort();
	}
}
