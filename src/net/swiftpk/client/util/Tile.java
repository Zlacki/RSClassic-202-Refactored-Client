package net.swiftpk.client.util;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A representation of one tile within our world map
 */
public class Tile {
	/**
	 * Create a new tile from raw byteBuffer packed into the given ByteBuffer
	 * @param in
	 * @return
	 * @throws java.io.IOException
	 */
	public static Tile unpack(ByteBuffer in) throws IOException {
		if (in.remaining() < 10) {
			throw new IOException("Provided buffer too short");
		}
		Tile tile = new Tile();

		tile.groundElevation = in.get();
		tile.groundTexture = in.get();
		tile.groundOverlay = in.get();
		tile.roofTexture = in.get();
		tile.horizontalWall = in.get();
		tile.verticalWall = in.get();
		tile.diagonalWalls = in.getInt();

		return tile;
	}

	/**
	 * The elevation of this tile
	 */
	public byte groundElevation = 0;

	/**
	 * The texture ID of this tile
	 */
	public byte groundTexture = 0;

	/**
	 * The texture ID of the roof of this tile
	 */
	public byte roofTexture = 0;

	/**
	 * The texture ID of any horizontal wall on this tile
	 */
	public byte horizontalWall = 0;

	/**
	 * The texture ID of any vertical wall on this tile
	 */
	public byte verticalWall = 0;

	/**
	 * The ID of any diagonal walls on this tile
	 */
	public int diagonalWalls = 0;

	/**
	 * The overlay texture ID
	 */
	public byte groundOverlay = 0;

	/**
	 * Writes the Tile raw byteBuffer into a ByteBuffer
	 * @return
     */
	public ByteBuffer pack() {
		ByteBuffer out = ByteBuffer.allocate(10);

		out.put(groundElevation);
		out.put(groundTexture);
		out.put(groundOverlay);
		out.put(roofTexture);

		out.put(horizontalWall);
		out.put(verticalWall);
		out.putInt(diagonalWalls);

		out.flip();
		return out;
	}
}
