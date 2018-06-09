package net.swiftpk.client.packethandler;

import net.swiftpk.client.mudclient;
import net.swiftpk.client.io.ServerPacket;

public interface PacketHandler {
	int getPacketOpcode();

	void handlePacket(mudclient<?> client, ServerPacket p);
}
