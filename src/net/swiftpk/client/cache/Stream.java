package net.swiftpk.client.cache;

public final class Stream {
	public Stream(byte abyte0[]) {
		buffer = abyte0;
		caret = 0;
	}

	public void writeByte(int i) {
		buffer[caret++] = (byte) i;
	}

	public void writeShort(int i) {
		buffer[caret++] = (byte) (i >> 8);
		buffer[caret++] = (byte) i;
	}

	public void writeLEShort(boolean flag, int i) {
		buffer[caret++] = (byte) i;
		buffer[caret++] = (byte) (i >> 8);
	}

	public void write24BitInt(int i) {
		buffer[caret++] = (byte) (i >> 16);
		buffer[caret++] = (byte) (i >> 8);
		buffer[caret++] = (byte) i;
	}

	public void writeInt(int i) {
		buffer[caret++] = (byte) (i >> 24);
		buffer[caret++] = (byte) (i >> 16);
		buffer[caret++] = (byte) (i >> 8);
		buffer[caret++] = (byte) i;
	}

	public void writeLong(long l) {
		buffer[caret++] = (byte) (int) (l >> 56);
		buffer[caret++] = (byte) (int) (l >> 48);
		buffer[caret++] = (byte) (int) (l >> 40);
		buffer[caret++] = (byte) (int) (l >> 32);
		buffer[caret++] = (byte) (int) (l >> 24);
		buffer[caret++] = (byte) (int) (l >> 16);
		buffer[caret++] = (byte) (int) (l >> 8);
		buffer[caret++] = (byte) (int) l;
	}

	public void writeString(String s) {
		// s.getBytes(0, s.length(), buffer, caret);
		for(byte b : s.getBytes())
			buffer[caret++] = b;
		// caret += s.length();
		buffer[caret++] = 10;
	}

	public void writeBytes(byte abyte0[], int i, int j) {
		for(int k = j; k < j + i; k++)
			buffer[caret++] = abyte0[k];
	}

	public int readUByte() {
		return buffer[caret++] & 0xff;
	}

	public byte read() {
		return buffer[caret++];
	}

	public int readShort() {
		caret += 2;
		return ((buffer[caret - 2] & 0xff) << 8) + (buffer[caret - 1] & 0xff);
	}

	public int readU24BitInt() {
		caret += 3;
		return ((buffer[caret - 3] & 0xff) << 16)
				+ ((buffer[caret - 2] & 0xff) << 8)
				+ (buffer[caret - 1] & 0xff);
	}

	public int readInt() {
		caret += 4;
		return ((buffer[caret - 4] & 0xff) << 24)
				+ ((buffer[caret - 3] & 0xff) << 16)
				+ ((buffer[caret - 2] & 0xff) << 8)
				+ (buffer[caret - 1] & 0xff);
	}

	public long readLong() {
		long l = (long) readInt() & 0xffffffffL;
		long l1 = (long) readInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String readStringIndex() {
		String s;
		for(s = ""; buffer[caret] != 0; )
			s = s + (char) buffer[caret++];
		caret++;
		return s;
	}

	public String readString() {
		int i = caret;
		while(buffer[caret] != 10)
			caret++;
		return new String(buffer, i, caret - i - 1);
	}

	public int readSmart2() {
		int i = buffer[caret] & 0xff;
		if(i < 128)
			return readUByte();
		else
			return readShort() - 32768;
	}

	public int readSmart() {
		int i = buffer[caret] & 0xff;
		if(i < 128)
			return readUByte() - 64;
		else
			return readShort() - 49152;
	}

	public void writeSmart(int i) {
		if(i < 63 && i > -64)
			writeByte(i + 64);
		if(i < 16384 && i >= -16384) {
			writeShort(i + 49152);
		}
	}

	public void setShort(int val, int offset) {
		buffer[offset++] = (byte) (val >> 8);
		buffer[offset++] = (byte) val;
		if(caret < offset + 2)
			caret += 2;
	}

	public byte[] buffer = new byte[5000];
	public int caret;
	public int anInt1407;
	public static boolean aBoolean1418;
}
