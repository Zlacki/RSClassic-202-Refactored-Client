package net.swiftpk.client.cache;

import java.util.ArrayList;
import net.swiftpk.client.bzip.BZip2Decompressor;

public class Archive {
	private ArrayList<ByteArray> files = new ArrayList<>();
	byte[] finalBuffer;
	private int totalFiles;
	private ArrayList<Integer> identifiers = new ArrayList<>();
	private ArrayList<Integer> decompressedSizes = new ArrayList<>();
	private ArrayList<Integer> compressedSizes = new ArrayList<>();
	private ArrayList<Integer> startOffsets = new ArrayList<>();
	boolean compressedAsWhole;

	public Archive(byte abyte0[]) {
		if(abyte0 == null) {
			return;
		}
		Stream stream = new Stream(abyte0);
		int decompressedSize = stream.readU24BitInt();
		int compressedSize = stream.readU24BitInt();
		if(compressedSize != decompressedSize) { // we need to decompress
			byte abyte1[] = new byte[decompressedSize];
			BZip2Decompressor.decompressBuffer(abyte1, decompressedSize,
					abyte0, compressedSize, 6);
			finalBuffer = abyte1;
			stream = new Stream(finalBuffer);
			compressedAsWhole = true;
		} else {
			finalBuffer = abyte0;
			compressedAsWhole = false;
		}
		totalFiles = stream.readShort();
		int offset = stream.caret + totalFiles * 10; // file info at beginning
														// is 10 bytes per file,
														// and we want to start
														// file byteBuffer immediately
														// after
		for(int l = 0; l < totalFiles; l++) {
			identifiers.add(stream.readInt()); // 4
			decompressedSizes.add(stream.readU24BitInt());// 3
			compressedSizes.add(stream.readU24BitInt()); // + 3
			startOffsets.add(offset); // ____
			offset += compressedSizes.get(l); // 10
			files.add(new ByteArray(getFileAt(l)));
		}
	}

	public final byte[] getFileAt(int at) {
		byte dataBuffer[] = new byte[decompressedSizes.get(at)];
		if(!compressedAsWhole) {
			BZip2Decompressor.decompressBuffer(dataBuffer, decompressedSizes
					.get(at), finalBuffer, compressedSizes.get(at),
					startOffsets.get(at));
		} else {
			System.arraycopy(finalBuffer, startOffsets.get(at), dataBuffer, 0,
					decompressedSizes.get(at));
		}
		return dataBuffer;
	}

	public byte[] getFile(int identifier) {
		for(int k = 0; k < totalFiles; k++)
			if(identifiers.get(k) == identifier) {
				return getFileAt(k);
			}
		return null;
	}

	public int getFileTest(int index) {
		return identifiers.get(index);
	}

	public int getIdentifierAt(int at) {
		return identifiers.get(at);
	}

	public int getDecompressedSize(int at) {
		return decompressedSizes.get(at);
	}

	public int getTotalFiles() {
		return totalFiles;
	}

	public byte[] getFile(String identStr) {
		int identifier = 0;
		identStr = identStr.toUpperCase();
		for(int j = 0; j < identStr.length(); j++) {
			identifier = (identifier * 61 + identStr.charAt(j)) - 32;
		}
		return getFile(identifier);
	}

	public static int getHash(String s) {
		int identifier = 0;
		s = s.toUpperCase();
		for(int j = 0; j < s.length(); j++)
			identifier = (identifier * 61 + s.charAt(j)) - 32;
		return identifier;
	}

	public void renameFile(int index, int newName) {
		identifiers.set(index, newName);
	}

	public void updateFile(int index, byte[] data) {
		files.get(index).setBytes(data);
	}

	public int indexOf(String name) {
		return indexOf(getHash(name));
	}

	public int indexOf(int hash) {
		return identifiers.indexOf(hash);
	}

	public void removeFile(int index) {
		files.remove(index);
		identifiers.remove(index);
		compressedSizes.remove(index);
		decompressedSizes.remove(index);
		totalFiles--;
	}

	public void addFile(int identifier, byte[] data) {
		if(indexOf(identifier) != -1) {
			updateFile(indexOf(identifier), data);
		} else {
			identifiers.add(identifier);
			decompressedSizes.add(data.length);
			compressedSizes.add(0);
			files.add(new ByteArray(data));
			totalFiles++;
		}
	}

	public void addFileAt(int at, int identifier, byte[] data) {
		identifiers.add(at, identifier);
		decompressedSizes.add(at, data.length);
		compressedSizes.add(at, 0);
		files.add(at, new ByteArray(data));
		totalFiles++;
	}
}
