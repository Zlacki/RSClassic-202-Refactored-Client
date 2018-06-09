package net.swiftpk.client.packethandler;

import net.swiftpk.client.entity.Actor;
import net.swiftpk.client.io.ServerPacket;
import net.swiftpk.client.util.Utility;
import net.swiftpk.client.mudclient;

public class PlayerPositionUpdate implements PacketHandler {

	@Override
	public int getPacketOpcode() {
		return 145;
	}

	@Override
	public void handlePacket(mudclient<?> client, ServerPacket p) {
		client.knownPlayerCount = client.playerCount;
		System.arraycopy(client.playerArray, 0, client.knownPlayers, 0, client.knownPlayerCount);
		int currentOffset = 8;
		client.regionX = Utility.getBitMask(p.getData(), currentOffset, 11);
		currentOffset += 11;
		client.regionY = Utility.getBitMask(p.getData(), currentOffset, 13);
		currentOffset += 13;
		int mobSprite = Utility.getBitMask(p.getData(), currentOffset, 4);
		currentOffset += 4;
		boolean sectionLoaded = client.loadSection(client.regionX, client.regionY);
		client.regionX -= client.areaX;
		client.regionY -= client.areaY;
		int mapEnterX = client.regionX * 128 + 64;
		int mapEnterY = client.regionY * 128 + 64;
		if(sectionLoaded) {
			client.ourPlayer.waypointCurrent = 0;
			client.ourPlayer.waypointEndSprite = 0;
			client.ourPlayer.currentX = client.ourPlayer.waypointsX[0] = mapEnterX;
			client.ourPlayer.currentY = client.ourPlayer.waypointsY[0] = mapEnterY;
		}
		client.playerCount = 0;
		// System.out.println("added ourselves ("+ourPlayerServerIndex+") to the players");
		client.ourPlayer = client.makePlayer(client.ourPlayerServerIndex, mapEnterX,
				mapEnterY, mobSprite, client.userGroup);
		int newPlayerCount = Utility.getBitMask(p.getData(), currentOffset, 8);
		currentOffset += 8;
		for(int currentNewPlayer = 0; currentNewPlayer < newPlayerCount; currentNewPlayer++) {
			// Actor lastMob = getLastPlayer(Utility.getBitMask(byteBuffer,
			// currentOffset, 16));
			// currentOffset += 16;
			Actor lastMob = client.knownPlayers[currentNewPlayer + 1];
			if(lastMob != null && lastMob.name == null) {
				client.streamClass.addNewFrame(84);
				client.streamClass.addShort(lastMob.serverIndex);
				client.streamClass.formatCurrentFrame();
			}
			int nextPlayer = Utility.getBitMask(p.getData(), currentOffset, 1);
			currentOffset++;
			if(nextPlayer != 0) {
				int waypointsLeft = Utility.getBitMask(p.getData(),
						currentOffset, 1); // 2
				currentOffset++;
				if(waypointsLeft == 0) {
					int currentNextSprite = Utility.getBitMask(p.getData(),
							currentOffset, 3); // 3
					currentOffset += 3;
					if(lastMob == null)
						continue;
					int currentWaypoint = lastMob.waypointCurrent;
					int newWaypointX = lastMob.waypointsX[currentWaypoint];
					int newWaypointY = lastMob.waypointsY[currentWaypoint];
					if(currentNextSprite == 2 || currentNextSprite == 1
							|| currentNextSprite == 3)
						newWaypointX += 128;
					if(currentNextSprite == 6 || currentNextSprite == 5
							|| currentNextSprite == 7)
						newWaypointX -= 128;
					if(currentNextSprite == 4 || currentNextSprite == 3
							|| currentNextSprite == 5)
						newWaypointY += 128;
					if(currentNextSprite == 0 || currentNextSprite == 1
							|| currentNextSprite == 7)
						newWaypointY -= 128;
					lastMob.nextSprite = currentNextSprite;
					lastMob.waypointCurrent = currentWaypoint = (currentWaypoint + 1) % 10;
					lastMob.waypointsX[currentWaypoint] = newWaypointX;
					lastMob.waypointsY[currentWaypoint] = newWaypointY;
				} else {
					int needsNextSprite = Utility.getBitMask(p.getData(),
							currentOffset, 4);
					if((needsNextSprite & 0xc) == 12) {
						currentOffset += 2;
						continue;
					}
					if(lastMob == null)
						continue;
					lastMob.nextSprite = Utility.getBitMask(p.getData(),
							currentOffset, 4);
					currentOffset += 4;
					// if(lastMob.name != null)
					// System.out.println("["+lastMob.name+"] nextSprite="+needsNextSprite);
				}
			}
			client.playerArray[client.playerCount++] = lastMob;
		}
		int mobCount = 0;
		while(currentOffset + 24 < p.getLength() * 8) {
			int mobIndex = Utility.getBitMask(p.getData(), currentOffset, 11);
			currentOffset += 11;
			int areaMobX = Utility.getBitMask(p.getData(), currentOffset, 5);
			currentOffset += 5;
			if(areaMobX > 15)
				areaMobX -= 32;
			int areaMobY = Utility.getBitMask(p.getData(), currentOffset, 5);
			currentOffset += 5;
			if(areaMobY > 15)
				areaMobY -= 32;
			int mobArrayMobID = Utility.getBitMask(p.getData(), currentOffset,
					4);
			currentOffset += 4;
			int addIndex = Utility.getBitMask(p.getData(), currentOffset, 1);
			currentOffset++;
			int mobX = (client.regionX + areaMobX) * 128 + 64;
			int mobY = (client.regionY + areaMobY) * 128 + 64;
			client.makePlayer(mobIndex, mobX, mobY, mobArrayMobID, 0);
			if(addIndex == 0)
				client.mobArrayIndexes[mobCount++] = mobIndex;
			// System.out.println("Player "+mobIndex+" in combat");
		}
		if(mobCount > 0) {
			client.streamClass.addNewFrame(83);
			client.streamClass.addShort(mobCount); /* Keep track of mobs */
			for(int i = 0; i < mobCount; i++) {
				Actor a = client.mobArray[client.mobArrayIndexes[i]];
				client.streamClass.addShort(a.serverIndex);
				client.streamClass.addShort(a.mobIntUnknown); // appearanceID?
			}
			client.streamClass.formatCurrentFrame();
		}
	}

}
