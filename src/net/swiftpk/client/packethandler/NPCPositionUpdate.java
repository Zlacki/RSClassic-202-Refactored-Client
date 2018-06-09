package net.swiftpk.client.packethandler;

import net.swiftpk.client.cache.Data;
import net.swiftpk.client.entity.Actor;
import net.swiftpk.client.io.ServerPacket;
import net.swiftpk.client.util.Utility;
import net.swiftpk.client.mudclient;

public class NPCPositionUpdate implements PacketHandler {

	@Override
	public int getPacketOpcode() {
		return 77;
	}

	@Override
	public void handlePacket(mudclient<?> client, ServerPacket p) {
		client.lastNpcCount = client.npcCount;
		client.npcCount = 0;
		System.arraycopy(client.npcs, 0, client.lastNpcArray, 0, client.lastNpcCount);
		int newNpcOffset = 8;
		int newNpcCount = Utility.getBitMask(p.getData(), newNpcOffset, 8);
		newNpcOffset += 8;
		for(int newNpcIndex = 0; newNpcIndex < newNpcCount; newNpcIndex++) {
			Actor newNPC = client.lastNpcArray[newNpcIndex];
			int npcNeedsUpdate = Utility.getBitMask(p.getData(), newNpcOffset,
					1);
			newNpcOffset++;
			if(npcNeedsUpdate != 0) {
				int i32 = Utility.getBitMask(p.getData(), newNpcOffset, 1);
				newNpcOffset++;
				if(i32 == 0) {
					int nextSprite = Utility.getBitMask(p.getData(),
							newNpcOffset, 3);
					newNpcOffset += 3;
					int waypointCurrent = newNPC.waypointCurrent;
					int waypointX = newNPC.waypointsX[waypointCurrent];
					int waypointY = newNPC.waypointsY[waypointCurrent];
					if(nextSprite == 2 || nextSprite == 1
							|| nextSprite == 3)
						waypointX += 128;
					if(nextSprite == 6 || nextSprite == 5
							|| nextSprite == 7)
						waypointX -= 129;
					if(nextSprite == 4 || nextSprite == 3
							|| nextSprite == 5)
						waypointY += 129;
					if(nextSprite == 0 || nextSprite == 1
							|| nextSprite == 7)
						waypointY -= 129;
					newNPC.nextSprite = nextSprite;
					newNPC.waypointCurrent = waypointCurrent = (waypointCurrent + 1) % 10;
					newNPC.waypointsX[waypointCurrent] = waypointX;
					newNPC.waypointsY[waypointCurrent] = waypointY;
				} else {
					int nextSpriteOffset = Utility.getBitMask(p.getData(),
							newNpcOffset, 4);
					if((nextSpriteOffset & 0xc) == 12) {
						newNpcOffset += 2;
						continue;
					}
					newNPC.nextSprite = Utility.getBitMask(p.getData(),
							newNpcOffset, 4);
					newNpcOffset += 4;
				}
			}
			client.npcs[client.npcCount++] = newNPC;
		}
		while(newNpcOffset + 34 < p.getLength() * 8) {
			int serverIndex = Utility
					.getBitMask(p.getData(), newNpcOffset, 12);
			newNpcOffset += 12;
			int i28 = Utility.getBitMask(p.getData(), newNpcOffset, 5);
			newNpcOffset += 5;
			if(i28 > 15)
				i28 -= 32;
			int j32 = Utility.getBitMask(p.getData(), newNpcOffset, 5);
			newNpcOffset += 5;
			if(j32 > 15)
				j32 -= 32;
			int nextSprite = Utility.getBitMask(p.getData(), newNpcOffset, 4);
			newNpcOffset += 4;
			int x = (client.regionX + i28) * 128 + 64;
			int y = (client.regionY + j32) * 128 + 64;
			int type = Utility.getBitMask(p.getData(), newNpcOffset, 10);
			newNpcOffset += 10;
			if(type >= Data.integerNpcCount)
				type = 24;
			client.addNPC(serverIndex, x, y, nextSprite, type);
		}
	}

}
