package net.swiftpk.client.loader.various;

public interface ProgressCallback {
	void onComplete(byte[] paramArrayOfByte);

	void update(int paramInt1, int paramInt2);
}
