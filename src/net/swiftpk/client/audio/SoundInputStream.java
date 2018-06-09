package net.swiftpk.client.audio;

public class SoundInputStream  {
	private byte dataArray[];
	private int length;
	private int offset;

    public SoundInputStream() {  }

	public void loadSoundFile(byte[] data, int dataOffset, int dataLength) {
		dataArray = data;
		offset = dataOffset;
		length = dataOffset + dataLength;
	}

	public int read() {
		byte[] data = new byte[1];
		read(data, 0, 1);
		return data[0];
	}

	public int read( byte[] fileData, int fileStartOffset, int fileEndOffset) {
		for(int fileCurrentOffset = 0; fileCurrentOffset < fileEndOffset; fileCurrentOffset++)
			if(offset < length)
                fileData[fileStartOffset + fileCurrentOffset] = dataArray[offset++];
			else
                fileData[fileStartOffset + fileCurrentOffset] = -1;
		return fileEndOffset;
	}
}
