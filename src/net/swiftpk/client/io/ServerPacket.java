package net.swiftpk.client.io;

/**
 * An immutable packet object.
 */
public class ServerPacket {
	/**
	 * Whether this packet is without the standard packet header
	 */
	private final boolean bare;
	/**
	 * The current index into the payload buffer for reading
	 */
	private int caret = 1;
	/**
	 * The payload
	 */
	private final byte[] pData;
	/**
	 * The ID of the packet
	 */
	private final int pID;
	/**
	 * The length of the payload
	 */
	private final int pLength;

	/**
	 * Creates a new packet with the specified parameters. The packet is
	 * considered not to be a bare packet.
	 *
	 * @param pID
	 *            The ID of the packet
	 * @param pData
	 *            The payload the packet
	 * @param length
	 */
	public ServerPacket(int pID, byte[] pData, int length) {
		this.pID = pID;
		this.pData = pData;
		this.pLength = length;
		this.bare = false;
	}

	/**
	 * Creates a new packet with the specified parameters.
	 *
	 * @param pID
	 *            The ID of the packet
	 * @param pData
	 *            The payload of the packet
	 * @param bare
	 *            Whether this packet is bare, which means that it does not
	 *            include the standard packet header
	 */
	public ServerPacket(int pID, byte[] pData, boolean bare) {
		this.pID = pID;
		this.pData = pData;
		this.pLength = pData.length;
		this.bare = bare;
	}

	/**
	 * Returns the entire payload byteBuffer of this packet.
	 *
	 * @return The payload <code>byte</code> array
	 */
	public byte[] getData() {
		return pData;
	}

	/**
	 * Returns the packet ID.
	 *
	 * @return The packet ID
	 */
	public int getId() {
		return pID;
	}

	/**
	 * Returns the length of the payload of this packet.
	 *
	 * @return The length of the packet's payload
	 */
	public int getLength() {
		return pLength;
	}

	/**
	 * Returns the remaining payload byteBuffer of this packet.
	 *
	 * @return The payload <code>byte</code> array
	 */
	public byte[] getRemainingData() {
		byte[] data = new byte[pLength - caret];
		for(int i = 0; i < data.length; i++) {
			data[i] = pData[i + caret];
		}
		caret += data.length;
		return data;
	}

	/**
	 * Checks if this packet is considered to be a bare packet, which means that
	 * it does not include the standard packet header (ID and length values).
	 *
	 * @return Whether this packet is a bare packet
	 */
	public boolean isBare() {
		return bare;
	}

	/**
	 * Reads the next <code>byte</code> fromnet.swiftpk.server.util.Logger.err(e.getMessage()); the payload.
	 *
	 * @return A <code>byte</code>
	 */
	public byte readByte() {
		return pData[caret++];
	}

	public void readBytes(byte[] buf, int off, int len) {
		for(int i = 0; i < len; i++) {
			buf[off + i] = pData[caret++];
		}
	}

	public byte[] readBytes(int length) {
		byte[] data = new byte[length];
		try {
			for(int i = 0; i < length; i++) {
				data[i] = pData[i + caret];
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		caret += length;
		return data;
	}

	/**
	 * Reads the next <code>int</code> from the payload.
	 *
	 * @return An <code>int</code>
	 */
	public int readInt() {
		return ((pData[caret++] & 0xff) << 24)
				| ((pData[caret++] & 0xff) << 16)
				| ((pData[caret++] & 0xff) << 8) | (pData[caret++] & 0xff);
	}

	/**
	 * Reads the next <code>long</code> from the payload.
	 *
	 * @return A <code>long</code>
	 */
	public long readLong() {
		return (long) (pData[caret++] & 0xff) << 56
				| ((long) (pData[caret++] & 0xff) << 48)
				| ((long) (pData[caret++] & 0xff) << 40)
				| ((long) (pData[caret++] & 0xff) << 32)
				| ((long) (pData[caret++] & 0xff) << 24)
				| ((long) (pData[caret++] & 0xff) << 16)
				| ((long) (pData[caret++] & 0xff) << 8)
				| (pData[caret++] & 0xff);
	}

	/**
	 * Reads the next <code>short</code> from the payload.
	 *
	 * @return A <code>short</code>
	 */
	public short readShort() {
		return (short) ((short) ((pData[caret++] & 0xff) << 8) | (short) (pData[caret++] & 0xff));
	}

	/**
	 * Reads the string which is formed by the unread portion of the payload.
	 *
	 * @return A <code>String</code>
	 */
	public String readString() {
		return readString(pLength - caret);
	}

	/**
	 * Reads a string of the specified length from the payload.
	 *
	 * @param length
	 *            The length of the string to be read
	 * @return A <code>String</code>
	 */
	public String readString(int length) {
		String rv = new String(pData, caret, length);
		caret += length;
		return rv;
	}

	public int remaining() {
		return pData.length - caret;
	}

	/**
	 * Skips the specified number of bytes in the payload.
	 *
	 * @param x
	 *            The number of bytes to be skipped
	 */
	public void skip(int x) {
		caret += x;
	}

	/**
	 * Returns this packet in string form.
	 *
	 * @return A <code>String</code> representing this packet
	 */
	@Override
	public String toString() {
		String packet = "id = " + pID + " length = " + pLength + " byteBuffer =";
		for(int x = 0; x < pLength; x++) {
			packet += " " + pData[x];
		}
		return packet;
	}
}
