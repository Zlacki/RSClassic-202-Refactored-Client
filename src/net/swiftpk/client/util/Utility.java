package net.swiftpk.client.util;

import net.swiftpk.client.bzip.DataFileDecrypter;

import java.io.*;

public class Utility {
	public static String formatString(String s) {
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);
			if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
				sb.append(c);
			else
				sb.append('_');
		}
		return sb.toString();
	}

	public static String formatIPAddress(int i) {
		return (i >> 24 & 0xff) + "." + (i >> 16 & 0xff) + "." +
				(i >> 8 & 0xff) + "." + (i & 0xff);
	}

	public static int getBitMask(byte[] byteArray, int offset, int length) {
		int bitOffset = offset >> 3;
		int bitMod = 8 - (offset & 7);
		int i1 = 0;
		for(; length > bitMod; bitMod = 8) {
			i1 += (byteArray[bitOffset++] & BASE_LENGTH_ARRAY[bitMod]) << length -
					bitMod;
			length -= bitMod;
		}
		if(length == bitMod) {
			i1 += byteArray[bitOffset] & BASE_LENGTH_ARRAY[bitMod];
		} else {
			i1 += byteArray[bitOffset] >> bitMod - length &
					BASE_LENGTH_ARRAY[length];
		}
		return i1;
	}

	public static int getSignedInteger(byte[] data, int i) {
		if(getUnsignedByte(data[i] & 0xff) < 128) {
			return data[i];
		} else {
			return getUnsignedByte((data[i] & 0xff) - 128 << 24) +
					getUnsignedByte((data[i + 1] & 0xff) << 16) +
					getUnsignedByte((data[i + 2] & 0xff) << 8) + getUnsignedByte(data[i + 3] & 0xff);
		}
	}

	public static int getSignedShort(byte[] data, int offset) {
		int j = getUnsignedByte(data[offset]) * 256 +
				getUnsignedByte(data[offset + 1]);
		if(j > 32767) {
			j -= 0x10000;
		}
		return j;
	}

	public static int getUnsignedInteger(byte abyte0[], int i) {
		return ((abyte0[i] & 0xff) << 24) + ((abyte0[i + 1] & 0xff) << 16) + ((abyte0[i + 2] & 0xff) << 8) + (abyte0[i + 3] & 0xff);
	}

	public static long getUnsignedLong(byte[] data, int offset) {
		return ((getUnsignedInteger(data, offset) & 0xffffffffL) << 32) +
				(getUnsignedInteger(data, offset + 4) & 0xffffffffL);
	}

	public static int getUnsignedByte(int b) {
		return b & 0xff;
	}

	public static int getUnsignedShort(byte[] data, int offset) {
		return ((data[offset] & 0xff) << 8) + (data[offset + 1] & 0xff);
	}

	public static byte[] unpackConfigArchiveEntry(String archiveEntryName, int offset, byte[] cacheFileData) {
		return method361(archiveEntryName, offset, cacheFileData, null);
	}

	public static String base37Decode(long usernameHash) {
		if(usernameHash < 0L) {
			return "invalid_name";
		}
		String username = "";
		while(usernameHash != 0L) {
			int i = (int) (usernameHash % 37L);
			usernameHash /= 37L;
			if(i == 0) {
				username = " " + username;
			} else if(i < 27) {
				if(usernameHash % 37L == 0L) {
					username = (char) ((i + 65) - 1) + username;
				} else {
					username = (char) ((i + 97) - 1) + username;
				}
			} else {
				username = (char) ((i + 48) - 27) + username;
			}
		}
		return username;
	}

	public static int getSoundFileStartOffset(String soundFileName, byte[] soundFilesData) {
		int dataLength = getUnsignedShort(soundFilesData, 0);
		int desiredSoundFileHash = 0;
		soundFileName = soundFileName.toUpperCase();
		for(int caret = 0; caret < soundFileName.length(); caret++) {
			desiredSoundFileHash = (desiredSoundFileHash * 61 + soundFileName.charAt(caret)) - 32;
		}
		int l = 2 + dataLength * 10;
		for(int i1 = 0; i1 < dataLength; i1++) {
			int curSoundFileHash = (soundFilesData[i1 * 10 + 2] & 0xff) * 0x1000000 +
					(soundFilesData[i1 * 10 + 3] & 0xff) * 0x10000 +
					(soundFilesData[i1 * 10 + 4] & 0xff) * 256 +
					(soundFilesData[i1 * 10 + 5] & 0xff);
			int k1 = (soundFilesData[i1 * 10 + 9] & 0xff) * 0x10000 +
					(soundFilesData[i1 * 10 + 10] & 0xff) * 256 +
					(soundFilesData[i1 * 10 + 11] & 0xff);
			if(curSoundFileHash == desiredSoundFileHash) {
				return l;
			}
			l += k1;
		}
		return 0;
	}

	public static int getSoundFileEndOffset(String soundFileName, byte[] soundFilesData) {
		int dataLength = getUnsignedShort(soundFilesData, 0);
		int desiredSoundFileHash = 0;
		soundFileName = soundFileName.toUpperCase();
		for(int caret = 0; caret < soundFileName.length(); caret++)
			desiredSoundFileHash = (desiredSoundFileHash * 61 + soundFileName.charAt(caret)) - 32;
		for(int caret = 0; caret < dataLength; caret++) {
			int curSoundFileHash = (soundFilesData[caret * 10 + 2] & 0xff) * 0x1000000 +
					(soundFilesData[caret * 10 + 3] & 0xff) * 0x10000 +
					(soundFilesData[caret * 10 + 4] & 0xff) * 256 +
					(soundFilesData[caret * 10 + 5] & 0xff);
			int curHashedSoundFileLength = (soundFilesData[caret * 10 + 6] & 0xff) * 0x10000 +
					(soundFilesData[caret * 10 + 7] & 0xff) * 256 +
					(soundFilesData[caret * 10 + 8] & 0xff);
			if(curSoundFileHash == desiredSoundFileHash)
				return curHashedSoundFileLength;
		}
		return 0;
	}

	public static byte[] method361(String archiveEntryName, int caret, byte[] cacheFileData,
								   byte[] abyte1) {
		int j = (cacheFileData[0] & 0xff) * 256 + (cacheFileData[1] & 0xff);
		int k = 0;
		archiveEntryName = archiveEntryName.toUpperCase();
		for(int l = 0; l < archiveEntryName.length(); l++) {
			k = (k * 61 + archiveEntryName.charAt(l)) - 32;
		}
		int i1 = 2 + j * 10;
		for(int j1 = 0; j1 < j; j1++) {
			int k1 = (cacheFileData[j1 * 10 + 2] & 0xff) * 0x1000000 +
					(cacheFileData[j1 * 10 + 3] & 0xff) * 0x10000 +
					(cacheFileData[j1 * 10 + 4] & 0xff) * 256 +
					(cacheFileData[j1 * 10 + 5] & 0xff);
			int l1 = (cacheFileData[j1 * 10 + 6] & 0xff) * 0x10000 +
					(cacheFileData[j1 * 10 + 7] & 0xff) * 256 +
					(cacheFileData[j1 * 10 + 8] & 0xff);
			int i2 = (cacheFileData[j1 * 10 + 9] & 0xff) * 0x10000 +
					(cacheFileData[j1 * 10 + 10] & 0xff) * 256 +
					(cacheFileData[j1 * 10 + 11] & 0xff);
			if(k1 == k) {
				if(abyte1 == null) {
					abyte1 = new byte[l1 + caret];
				}
				if(l1 != i2) {
					DataFileDecrypter.unpackData(abyte1, l1, cacheFileData, i2, i1, archiveEntryName);
				} else {
					for(int j2 = 0; j2 < l1; j2++) {
						abyte1[j2] = cacheFileData[i1 + j2];
					}
				}
				return abyte1;
			}
			i1 += i2;
		}
		return null;
	}

	public static final void readFromPath(String path, byte[] abyte0, int length)
			throws IOException {
		InputStream inputstream = streamFromPath(path);
		try(DataInputStream datainputstream = new DataInputStream(inputstream)) {
			try {
				datainputstream.readFully(abyte0, 0, length);
			} catch(EOFException _ex) {
			}
		}
	}

	public static final int readInt(byte[] abyte0, int i) {
		return ((abyte0[i] & 0xff) << 24) | ((abyte0[i + 1] & 0xff) << 16) |
				((abyte0[i + 2] & 0xff) << 8) | (abyte0[i + 3] & 0xff);
	}

	public static final InputStream streamFromPath(String path) throws IOException {
		Object obj = new BufferedInputStream(new FileInputStream(path));
		return ((InputStream) (obj));
	}

	public static final long base37Encode(String s) {
		String s1 = "";
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
				s1 += c;
			} else if(c >= 'A' && c <= 'Z') {
				s1 += (char) ((c + 97) - 65);
			} else {
				s1 += ' ';
			}
		}
		s1 = s1.trim();
		if(s1.length() > 12) {
			s1 = s1.substring(0, 12);
		}
		long l = 0L;
		for(int j = 0; j < s1.length(); j++) {
			char c1 = s1.charAt(j);
			l *= 37L;
			if(c1 >= 'a' && c1 <= 'z') {
				l += (1 + c1) - 97;
			} else if(c1 >= '0' && c1 <= '9') {
				l += (27 + c1) - 48;
			}
		}
		return l;
	}

	private static final int[] BASE_LENGTH_ARRAY = { 0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f,
													 0x7f, 0xff, 0x1ff, 0x3ff, 0x7ff, 0xfff, 0x1fff, 0x3fff, 0x7fff,
													 0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff,
													 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff,
													 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };
}
