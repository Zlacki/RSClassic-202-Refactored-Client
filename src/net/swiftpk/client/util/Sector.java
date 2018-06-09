package net.swiftpk.client.util;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Sector {
	/**
	 * Create a new Sector from raw byteBuffer packed into the given ByteBuffer
	 * @param in
	 * @return
	 * @throws java.io.IOException
	 */
	public static Sector unpack(ByteBuffer in) throws IOException {
		int length = Sector.WIDTH * Sector.HEIGHT;
		if (in.remaining() < (10 * length)) {
			throw new IOException("Provided buffer too short");
		}
		Sector sector = new Sector();

		for (int i = 0; i < length; i++) {
			sector.setTile(i, Tile.unpack(in));
		}

		return sector;
	}

	/**
	 * The width of a sector
	 */
	public static final short WIDTH = 48;

	/**
	 * The height of a sector
	 */
	public static final short HEIGHT = 48;

	/**
	 * An array containing all the tiles within this Sector
	 */
	private final Tile[] tiles;

	/**
	 * Creates a new Sector full of blank tiles
	 */
	public Sector() {
		tiles = new Tile[Sector.WIDTH * Sector.HEIGHT];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile();
		}
	}

	/**
	 * Gets the Tile at the given index
	 * @param i
	 * @return
	 */
	public Tile getTile(int i) {
		return tiles[i];
	}

	/**
	 * Gets the Tile at the given coords
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getTile(int x, int y) {
		return getTile(x * Sector.WIDTH + y);
	}

	/**
	 * Writes the Sector raw byteBuffer into a ByteBuffer
	 * @return
	 * @throws java.io.IOException
	 */
	public ByteBuffer pack() throws IOException {
		ByteBuffer out = ByteBuffer.allocate(10 * tiles.length);

		for(Tile tile : tiles) {
			out.put(tile.pack());
		}

		out.flip();
		return out;
	}

	/**
	 * Sets the the Tile at the given coords
	 * @param x
	 * @param y
	 * @param t
	 */
	public void setTile(int x, int y, Tile t) {
		setTile(x * Sector.WIDTH + y, t);
	}

	/**
	 * Sets the Tile at the given index
	 * @param i
	 * @param t
	 */
	public void setTile(int i, Tile t) {
		tiles[i] = t;
	}
}
