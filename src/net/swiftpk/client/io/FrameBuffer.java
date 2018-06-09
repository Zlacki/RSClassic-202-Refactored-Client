package net.swiftpk.client.io;

import java.io.IOException;

public class FrameBuffer {

    public void closeStream() {
    }

    private void readBytes(int len, byte[] buf) throws IOException {
        readInputStream(len, 0, buf);
    }

    public void formatCurrentFrame() {
		int frameLength = currentFrameOffset - currentBufferOffset - 2;
		if (frameLength >= 160) {
			byteBuffer[currentBufferOffset] = (byte) (160 + frameLength / 256);
			byteBuffer[currentBufferOffset + 1] = (byte) (frameLength & 0xff);
		} else {
			byteBuffer[currentBufferOffset] = (byte) frameLength;
			currentFrameOffset--;
			byteBuffer[currentBufferOffset + 1] = byteBuffer[currentFrameOffset];
		}
		if(maximumByteBufferSize <= 10000) {
            int frameOpcode = byteBuffer[currentBufferOffset + 2] & 0xff;
            timesFrameWasCreated[frameOpcode]++;
            accumulatedFrameLength[frameOpcode] += currentFrameOffset - currentBufferOffset;
        }
/*
        if(anInt529 != 8)
            currentFrameOffset++;
        int j = currentFrameOffset - currentBufferOffset - 2;
        byteBuffer[currentBufferOffset] = (byte) (j >> 8);
        byteBuffer[currentBufferOffset + 1] = (byte) j;
        if(anInt549 <= 10000) {
            int k = byteBuffer[currentBufferOffset + 2] & 0xff;
            timesFrameWasCreated[k]++;
            accumulatedFrameLength[k] += currentFrameOffset - currentBufferOffset;
        }*/
        currentBufferOffset = currentFrameOffset;
    }

    public void addByte(int i) {
        byteBuffer[currentFrameOffset++] = (byte)i;
    }

    public void addLong(long l) {
        addInt((int)(l >> 32));
        addInt((int)(l & -1L));
    }

    public void addInt(int i) {
        byteBuffer[currentFrameOffset++] = (byte)(i >> 24);
        byteBuffer[currentFrameOffset++] = (byte)(i >> 16);
        byteBuffer[currentFrameOffset++] = (byte)(i >> 8);
        byteBuffer[currentFrameOffset++] = (byte)i;
    }

    public boolean containsData() {
        return currentBufferOffset > 0;
    }

    public void addShort(int i) {
        byteBuffer[currentFrameOffset++] = (byte)(i >> 8);
        byteBuffer[currentFrameOffset++] = (byte)i;
    }

    private int readByteB() throws IOException {
        return readInputStream();
    }

    public int readFrameIntoBuffer(byte[] buffer) {
        try {
            frameDecodeAttempts++;
            if (maxFrameDecodeAttempts > 0 && frameDecodeAttempts > maxFrameDecodeAttempts) {
            	error = true;
				errorText = "time-out";
				maxFrameDecodeAttempts += maxFrameDecodeAttempts;
				return 0;
            }
            if (frameLength == 0 && inputStreamAvailable() >= 2) {
                    frameLength = readInputStream();
                    if (frameLength >= 160) frameLength = (frameLength - 160) * 256 + readInputStream();
            }
            if (frameLength > 0 && inputStreamAvailable() >= frameLength) {
                    if (frameLength >= 160) {
                            readBytes(frameLength, buffer);
                    } else {
                            buffer[frameLength - 1] = (byte) readInputStream();
                            if (frameLength > 1) readBytes(frameLength - 1, buffer);
                    }
                    int bytesRead = frameLength;
                    frameLength = 0;
                    frameDecodeAttempts = 0;
                    return bytesRead;
            }
        }
        catch(IOException ioexception) {
            error = true;
            errorText = ioexception.getMessage();
        }
        return 0;
    }

    public void readInputStream(int i, int j, byte abyte0[]) throws IOException {
    }

    public int inputStreamAvailable() throws IOException {
        return 0;
    }

    public void formatCurrentFrameAndFlushBuffer() throws IOException {
        formatCurrentFrame();
        flushBufferIfCountOver(0);
    }

    public long readLong() throws IOException {
        long l = readShort();
        long l1 = readShort();
        long l2 = readShort();
        long l3 = readShort();
        return (l << 48) + (l1 << 32) + (l2 << 16) + l3;
    }

    private int readShort() throws IOException {
        int i = readByteB();
        int j = readByteB();
        return i * 256 + j;
    }

    public void addBytes(byte[] buffer, int i, int j) {
        for(int k = 0; k < j; k++)
            byteBuffer[currentFrameOffset++] = buffer[i + k];
    }

    public void flushBufferIfCountOver(int framesRequiredBeforeFlush) throws IOException {
        if(error) {
            currentBufferOffset = 0;
            currentFrameOffset = 3;
            error = false;
            throw new IOException(errorText);
        }
        bufferedFrameCount++;
        if(bufferedFrameCount < framesRequiredBeforeFlush)
            return;
        if(currentBufferOffset > 0) {
            bufferedFrameCount = 0;
            writeToOutputBuffer(byteBuffer, 0, currentBufferOffset);
        }
        currentBufferOffset = 0;
        currentFrameOffset = 3;
    }

	public void addString(String s) {
		System.arraycopy(s.getBytes(), 0, byteBuffer, currentFrameOffset, s.length());
        currentFrameOffset += s.length();
    }

    public void writeToOutputBuffer(byte abyte0[], int i, int j) throws IOException {  }

    public void addNewFrame(int opcode) {
        /*
         * This seems to force a flush of the buffer
         * if the start of this frame is getting close
         * to the maximum socket buffer size.
         */
        if(currentBufferOffset > (maximumByteBufferSize * 4) / 5)
            try {
                flushBufferIfCountOver(0);
            } catch(IOException ioexception) {
                error = true;
                errorText = ioexception.getMessage();
            }
        if(byteBuffer == null)
            byteBuffer = new byte[maximumByteBufferSize];
        byteBuffer[currentBufferOffset + 2] = (byte)opcode;
        byteBuffer[currentBufferOffset + 3] = 0;
        currentFrameOffset = currentBufferOffset + 3;
    }

    public int readInputStream() throws IOException {
        return 0;
    }

    FrameBuffer() {
        currentFrameOffset = 3;
        errorText = "";
        maximumByteBufferSize = 5000;
        error = false;
    }

    private int frameLength;
    private int frameDecodeAttempts;
    public int maxFrameDecodeAttempts;
    private int currentBufferOffset;
    private int currentFrameOffset;
    public byte[] byteBuffer;
    private static int[] timesFrameWasCreated = new int[256];
    String errorText;
    private int maximumByteBufferSize;
    private int bufferedFrameCount;
    private static int[] accumulatedFrameLength = new int[256];
    public boolean error;
}
