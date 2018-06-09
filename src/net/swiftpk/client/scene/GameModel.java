package net.swiftpk.client.scene;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import net.swiftpk.client.util.Utility;

public class GameModel {

	private static int base64Alphabet[];

	private static int sine11[];

	private static int sine9[];

	static {
		sine9 = new int[512];
		sine11 = new int[2048];

		base64Alphabet = new int[256];
		for (int i = 0; i < 256; i++) {
			sine9[i] = (int) (Math.sin(i * 0.02454369D) * 32768D);
			sine9[i + 256] = (int) (Math.cos(i * 0.02454369D) * 32768D);
		}

		for (int j = 0; j < 1024; j++) {
			sine11[j] = (int) (Math.sin(j * 0.00613592315D) * 32768D);
			sine11[j + 1024] = (int) (Math.cos(j * 0.00613592315D) * 32768D);
		}

		for (int j1 = 0; j1 < 10; j1++)
			base64Alphabet[48 + j1] = j1;

		for (int k1 = 0; k1 < 26; k1++)
			base64Alphabet[65 + k1] = k1 + 10;

		for (int l1 = 0; l1 < 26; l1++)
			base64Alphabet[97 + l1] = l1 + 36;

		base64Alphabet[163] = 62;
		base64Alphabet[36] = 63;
	}

	public boolean aSceneBoolean;

	private boolean autoCommit;

	private int baseX;

	private int baseY;

	private int baseZ;

	private int dataOffset;

	public int depth;

	private int diameter;

	private int faceBoundBottom[];

	private int faceBoundFar[];

	private int faceBoundLeft[];

	private int faceBoundNear[];

	private int faceBoundRight[];

	private int faceBoundTop[];

	public int faceCameraNormalMagnitude[];

	public int faceCameraNormalScale[];

	public int faceCount;

	public int faceFillBack[];

	public int faceFillFront[];

	public int faceIntensity[];

	public int faceNormalX[];

	public int faceNormalY[];

	public int faceNormalZ[];

	private int faces[][];

	public byte faceSpriteType[];

	public int faceTag[];

	public int faceVerticeCount[];

	public int faceVertices[][];

	public boolean isolated;

	public int key;

	protected int lightAmbience;

	protected int lightDiffuse;

	private int lightDirectionMagnitude;

	private int lightDirectionX;

	private int lightDirectionY;

	private int lightDirectionZ;
	private int magic;
	private int maxFaces;
	public int maxVertices;
	private int orientationPitch;
	private int orientationRoll;
	private int orientationYaw;
	public boolean pickable;
	public boolean projected;
	private int scaleFX;
	private int scaleFY;
	private int scaleFZ;
	private int shearXY;
	private int shearXZ;
	private int shearYX;
	private int shearYZ;
	private int shearZX;
	private int shearZY;
	private int transformKind;
	public int transformState;
	public boolean transparent;
	public boolean unlit;
	public byte vertexAmbience[];
	public int vertexCameraX[];
	public int vertexCameraY[];
	public int vertexCameraZ[];
	public int vertexIntensity[];
	public int vertexTransformedX[];
	public int vertexTransformedY[];
	public int vertexTransformedZ[];
	public int vertexViewX[];
	public int vertexViewY[];
	public int vertexX[];
	public int vertexY[];
	public int vertexZ[];
	public int verticeCount;
	public boolean visible;
	public int x1;
	public int x2;
	public int y1;
	public int y2;
	public int z1;
	public int z2;

    public byte[] gzDecompress(byte[] b) throws IOException {
        GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(b));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = gzi.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        out.close();
        return out.toByteArray();
    }

	public GameModel(byte[] data, boolean unused) {
/*		try {
			byteBuffer = gzDecompress(byteBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		int offset = 0;
		transformState = 1;
		visible = true;
		aSceneBoolean = false;
		transparent = false;
		key = -1;
		autoCommit = false;
		isolated = false;
		unlit = false;
		pickable = false;
		projected = false;
		magic = 0xbc614e;
		diameter = 0xbc614e;
		lightDirectionX = 180;
		lightDirectionY = 155;
		lightDirectionZ = 95;
		lightDirectionMagnitude = 256;
		lightDiffuse = 512;
		lightAmbience = 32;
		int j = Utility.getUnsignedShort(data, offset);
		offset += 2;
		int k = Utility.getUnsignedShort(data, offset);
		offset += 2;
		allocate(j, k);
		faces = new int[k][1];
		for (int l = 0; l < j; l++) {
			vertexX[l] = Utility.getSignedShort(data, offset);
			offset += 2;
		}

		for (int i1 = 0; i1 < j; i1++) {
			vertexY[i1] = Utility.getSignedShort(data, offset);
			offset += 2;
		}

		for (int j1 = 0; j1 < j; j1++) {
			vertexZ[j1] = Utility.getSignedShort(data, offset);
			offset += 2;
		}

		verticeCount = j;
		for (int k1 = 0; k1 < k; k1++)
			faceVerticeCount[k1] = data[offset++] & 0xff;

		for (int l1 = 0; l1 < k; l1++) {
			faceFillFront[l1] = Utility.getSignedShort(data, offset);
			offset += 2;
			if (faceFillFront[l1] == 32767)
				faceFillFront[l1] = magic;
		}

		for (int i2 = 0; i2 < k; i2++) {
			faceFillBack[i2] = Utility.getSignedShort(data, offset);
			offset += 2;
			if (faceFillBack[i2] == 32767)
				faceFillBack[i2] = magic;
		}

		for (int j2 = 0; j2 < k; j2++) {
			int k2 = data[offset++] & 0xff;
			if (k2 == 0)
				faceIntensity[j2] = 0;
			else
				faceIntensity[j2] = magic;
		}

		for (int l2 = 0; l2 < k; l2++) {
			faceVertices[l2] = new int[faceVerticeCount[l2]];
			for (int i3 = 0; i3 < faceVerticeCount[l2]; i3++)
				if (j < 256) {
					faceVertices[l2][i3] = data[offset++] & 0xff;
				} else {
					faceVertices[l2][i3] = Utility.getUnsignedShort(data,
							offset);
					offset += 2;
				}

		}

		faceCount = k;
		transformState = 1;
	}

	public GameModel(GameModel[] pieces, int count) {
		transformState = 1;
		visible = true;
		aSceneBoolean = false;
		transparent = false;
		key = -1;
		autoCommit = false;
		isolated = false;
		unlit = false;
		pickable = false;
		projected = false;
		magic = 0xbc614e;
		diameter = 0xbc614e;
		lightDirectionX = 180;
		lightDirectionY = 155;
		lightDirectionZ = 95;
		lightDirectionMagnitude = 256;
		lightDiffuse = 512;
		lightAmbience = 32;
		merge(pieces, count, true);
	}

	public GameModel(GameModel[] pieces, int count, boolean autoCommit,
			boolean isolated, boolean unlit, boolean pickable) {
		transformState = 1;
		visible = true;
		aSceneBoolean = false;
		transparent = false;
		key = -1;
		projected = false;
		magic = 0xbc614e;
		diameter = 0xbc614e;
		lightDirectionX = 180;
		lightDirectionY = 155;
		lightDirectionZ = 95;
		lightDirectionMagnitude = 256;
		lightDiffuse = 512;
		lightAmbience = 32;
		this.autoCommit = autoCommit;
		this.isolated = isolated;
		this.unlit = unlit;
		this.pickable = pickable;
		merge(pieces, count, false);
	}

	public GameModel(int verticeCount, int faceCount) {
		transformState = 1;
		visible = true;
		aSceneBoolean = false;
		transparent = false;
		key = -1;
		autoCommit = false;
		isolated = false;
		unlit = false;
		pickable = false;
		projected = false;
		magic = 0xbc614e;
		diameter = 0xbc614e;
		lightDirectionX = 180;
		lightDirectionY = 155;
		lightDirectionZ = 95;
		lightDirectionMagnitude = 256;
		lightDiffuse = 512;
		lightAmbience = 32;
		allocate(verticeCount, faceCount);
		faces = new int[faceCount][1];
		for (int k = 0; k < faceCount; k++)
			faces[k][0] = k;

	}

	public GameModel(int verticeCount, int faceCount, boolean autoCommit,
			boolean isolated, boolean unlit, boolean pickable, boolean projected) {
		transformState = 1;
		visible = true;
		aSceneBoolean = false;
		transparent = false;
		key = -1;
		magic = 0xbc614e;
		diameter = 0xbc614e;
		lightDirectionX = 180;
		lightDirectionY = 155;
		lightDirectionZ = 95;
		lightDirectionMagnitude = 256;
		lightDiffuse = 512;
		lightAmbience = 32;
		this.autoCommit = autoCommit;
		this.isolated = isolated;
		this.unlit = unlit;
		this.pickable = pickable;
		this.projected = projected;
		allocate(verticeCount, faceCount);
	}

	public GameModel(String path) {
		transformState = 1;
		visible = true;
		aSceneBoolean = false;
		transparent = false;
		key = -1;
		autoCommit = false;
		isolated = false;
		unlit = false;
		pickable = false;
		projected = false;
		magic = 0xbc614e;
		diameter = 0xbc614e;
		lightDirectionX = 180;
		lightDirectionY = 155;
		lightDirectionZ = 95;
		lightDirectionMagnitude = 256;
		lightDiffuse = 512;
		lightAmbience = 32;
		byte[] data;
		try {
			java.io.InputStream inputStream = Utility.streamFromPath(path);
			try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
				data = new byte[3];
				dataOffset = 0;
				for (int i = 0; i < 3; )
					i += dataInputStream.read(data, i, 3 - i);
				int sz = readBase64(data);
				data = new byte[sz];
				dataOffset = 0;
				for (int j = 0; j < sz; )
					j += dataInputStream.read(data, j, sz - j);
			}
		} catch (IOException _ex) {
			verticeCount = 0;
			faceCount = 0;
			return;
		}
		int verticeCount1 = readBase64(data);
		int faceCount1 = readBase64(data);
		allocate(verticeCount1, faceCount1);
		faces = new int[faceCount1][];
		for (int j3 = 0; j3 < verticeCount1; j3++) {
			int x = readBase64(data);
			int y = readBase64(data);
			int z = readBase64(data);
			vertexAt(x, y, z);
		}

		for (int k3 = 0; k3 < faceCount1; k3++) {
			int count = readBase64(data);
			int front = readBase64(data);
			int back = readBase64(data);
			int l2 = readBase64(data);
			lightDiffuse = readBase64(data);
			lightAmbience = readBase64(data);
			int gouraud = readBase64(data);
			int vs[] = new int[count];
			for (int l3 = 0; l3 < count; l3++)
				vs[l3] = readBase64(data);

			int ai1[] = new int[l2];
			for (int i4 = 0; i4 < l2; i4++)
				ai1[i4] = readBase64(data);

			int j4 = makeFace(count, vs, front, back);
			faces[k3] = ai1;
			if (gouraud == 0)
				faceIntensity[j4] = 0;
			else
				faceIntensity[j4] = magic;
		}

		transformState = 1;
	}

	public int addVertex(int x, int y, int z) {
		if (verticeCount >= maxVertices) {
			return -1;
		} else {
			vertexX[verticeCount] = x;
			vertexY[verticeCount] = y;
			vertexZ[verticeCount] = z;
			return verticeCount++;
		}
	}

	private void allocate(int verticeCount, int faceCount) {
		vertexX = new int[verticeCount];
		vertexY = new int[verticeCount];
		vertexZ = new int[verticeCount];
		vertexIntensity = new int[verticeCount];
		vertexAmbience = new byte[verticeCount];
		faceVerticeCount = new int[faceCount];
		faceVertices = new int[faceCount][];
		faceFillFront = new int[faceCount];
		faceFillBack = new int[faceCount];
		faceIntensity = new int[faceCount];
		faceCameraNormalScale = new int[faceCount];
		faceCameraNormalMagnitude = new int[faceCount];
		if (!projected) {
			vertexCameraX = new int[verticeCount];
			vertexCameraY = new int[verticeCount];
			vertexCameraZ = new int[verticeCount];
			vertexViewX = new int[verticeCount];
			vertexViewY = new int[verticeCount];
		}
		if (!pickable) {
			faceSpriteType = new byte[faceCount];
			faceTag = new int[faceCount];
		}
		if (autoCommit) {
			vertexTransformedX = vertexX;
			vertexTransformedY = vertexY;
			vertexTransformedZ = vertexZ;
		} else {
			vertexTransformedX = new int[verticeCount];
			vertexTransformedY = new int[verticeCount];
			vertexTransformedZ = new int[verticeCount];
		}
		if (!unlit || !isolated) {
			faceNormalX = new int[faceCount];
			faceNormalY = new int[faceCount];
			faceNormalZ = new int[faceCount];
		}
		if (!isolated) {
			faceBoundLeft = new int[faceCount];
			faceBoundRight = new int[faceCount];
			faceBoundBottom = new int[faceCount];
			faceBoundTop = new int[faceCount];
			faceBoundNear = new int[faceCount];
			faceBoundFar = new int[faceCount];
		}
		this.faceCount = 0;
		this.verticeCount = 0;
		maxVertices = verticeCount;
		maxFaces = faceCount;
		baseX = baseY = baseZ = 0;
		orientationYaw = orientationPitch = orientationRoll = 0;
		scaleFX = scaleFY = scaleFZ = 256;
		shearXY = shearXZ = shearYX = shearYZ = shearZX = shearZY = 256;
		transformKind = 0;
	}

	public void apply() {
		if (transformState == 2) {
			transformState = 0;
			for (int vertex = 0; vertex < verticeCount; vertex++) {
				vertexTransformedX[vertex] = vertexX[vertex];
				vertexTransformedY[vertex] = vertexY[vertex];
				vertexTransformedZ[vertex] = vertexZ[vertex];
			}

			x1 = y1 = z1 = 0xff676981;
			diameter = x2 = y2 = z2 = 0x98967f;
			return;
		}
		if (transformState == 1) {
			transformState = 0;
			for (int vertex = 0; vertex < verticeCount; vertex++) {
				vertexTransformedX[vertex] = vertexX[vertex];
				vertexTransformedY[vertex] = vertexY[vertex];
				vertexTransformedZ[vertex] = vertexZ[vertex];
			}

			if (transformKind >= 2)
				applyRotation(orientationYaw, orientationPitch, orientationRoll);
			if (transformKind >= 3)
				applyScale(scaleFX, scaleFY, scaleFZ);
			if (transformKind >= 4)
				applyShear(shearXY, shearXZ, shearYX, shearYZ, shearZX, shearZY);
			if (transformKind >= 1)
				applyTranslate(baseX, baseY, baseZ);
			computeBounds();
			relight();
		}
	}

	private void applyRotation(int yaw, int roll, int pitch) {
		for (int vertex = 0; vertex < verticeCount; vertex++) {
			if (pitch != 0) {
				int sin = sine9[pitch];
				int cos = sine9[pitch + 256];
				int x = vertexTransformedY[vertex] * sin
						+ vertexTransformedX[vertex] * cos >> 15;
				vertexTransformedY[vertex] = vertexTransformedY[vertex] * cos
						- vertexTransformedX[vertex] * sin >> 15;
				vertexTransformedX[vertex] = x;
			}
			if (yaw != 0) {
				int sin = sine9[yaw];
				int cos = sine9[yaw + 256];
				int y = vertexTransformedY[vertex] * cos
						- vertexTransformedZ[vertex] * sin >> 15;
				vertexTransformedZ[vertex] = vertexTransformedY[vertex] * sin
						+ vertexTransformedZ[vertex] * cos >> 15;
				vertexTransformedY[vertex] = y;
			}
			if (roll != 0) {
				int sin = sine9[roll];
				int cos = sine9[roll + 256];
				int x = vertexTransformedZ[vertex] * sin
						+ vertexTransformedX[vertex] * cos >> 15;
				vertexTransformedZ[vertex] = vertexTransformedZ[vertex] * cos
						- vertexTransformedX[vertex] * sin >> 15;
				vertexTransformedX[vertex] = x;
			}
		}

	}

	private void applyScale(int faceX, int faceY, int faceZ) {
		for (int vertex = 0; vertex < verticeCount; vertex++) {
			vertexTransformedX[vertex] = vertexTransformedX[vertex] * faceX >> 8;
			vertexTransformedY[vertex] = vertexTransformedY[vertex] * faceY >> 8;
			vertexTransformedZ[vertex] = vertexTransformedZ[vertex] * faceZ >> 8;
		}

	}

	private void applyShear(int xy, int xz, int yx, int yz, int zx, int zy) {
		for (int vertex = 0; vertex < verticeCount; vertex++) {
			if (xy != 0)
				vertexTransformedX[vertex] += vertexTransformedY[vertex] * xy >> 8;
			if (xz != 0)
				vertexTransformedZ[vertex] += vertexTransformedY[vertex] * xz >> 8;
			if (yx != 0)
				vertexTransformedX[vertex] += vertexTransformedZ[vertex] * yx >> 8;
			if (yz != 0)
				vertexTransformedY[vertex] += vertexTransformedZ[vertex] * yz >> 8;
			if (zx != 0)
				vertexTransformedZ[vertex] += vertexTransformedX[vertex] * zx >> 8;
			if (zy != 0)
				vertexTransformedY[vertex] += vertexTransformedX[vertex] * zy >> 8;
		}

	}

	private void applyTranslate(int x, int y, int z) {
		for (int vertex = 0; vertex < verticeCount; vertex++) {
			vertexTransformedX[vertex] += x;
			vertexTransformedY[vertex] += y;
			vertexTransformedZ[vertex] += z;
		}

	}

	public void clear() {
		faceCount = 0;
		verticeCount = 0;
	}

	public void commit() {
		apply();
		for (int i = 0; i < verticeCount; i++) {
			vertexX[i] = vertexTransformedX[i];
			vertexY[i] = vertexTransformedY[i];
			vertexZ[i] = vertexTransformedZ[i];
		}

		baseX = baseY = baseZ = 0;
		orientationYaw = orientationPitch = orientationRoll = 0;
		scaleFX = scaleFY = scaleFZ = 256;
		shearXY = shearXZ = shearYX = shearYZ = shearZX = shearZY = 256;
		transformKind = 0;
	}

	private void computeBounds() {
		x1 = y1 = z1 = 0xf423f;
		diameter = x2 = y2 = z2 = 0xfff0bdc1;
		for (int face = 0; face < faceCount; face++) {
			int[] vertice = faceVertices[face];
			int vertex = vertice[0];
			int count = faceVerticeCount[face];
			int x11;
			int x12 = x11 = vertexTransformedX[vertex];
			int y11;
			int y12 = y11 = vertexTransformedY[vertex];
			int z11;
			int z12 = z11 = vertexTransformedZ[vertex];
			for (int j = 0; j < count; j++) {
				vertex = vertice[j];
				if (vertexTransformedX[vertex] < x11)
					x11 = vertexTransformedX[vertex];
				else if (vertexTransformedX[vertex] > x12)
					x12 = vertexTransformedX[vertex];
				if (vertexTransformedY[vertex] < y11)
					y11 = vertexTransformedY[vertex];
				else if (vertexTransformedY[vertex] > y12)
					y12 = vertexTransformedY[vertex];
				if (vertexTransformedZ[vertex] < z11)
					z11 = vertexTransformedZ[vertex];
				else if (vertexTransformedZ[vertex] > z12)
					z12 = vertexTransformedZ[vertex];
			}

			if (!isolated) {
				faceBoundLeft[face] = x11;
				faceBoundRight[face] = x12;
				faceBoundBottom[face] = y11;
				faceBoundTop[face] = y12;
				faceBoundNear[face] = z11;
				faceBoundFar[face] = z12;
			}
			if (x12 - x11 > diameter)
				diameter = x12 - x11;
			if (y12 - y11 > diameter)
				diameter = y12 - y11;
			if (z12 - z11 > diameter)
				diameter = z12 - z11;
			if (x11 < this.x1)
				this.x1 = x11;
			if (x12 > this.x2)
				this.x2 = x12;
			if (y11 < this.y1)
				this.y1 = y11;
			if (y12 > this.y2)
				this.y2 = y12;
			if (z11 < this.z1)
				this.z1 = z11;
			if (z12 > this.z2)
				this.z2 = z12;
		}

	}

	public GameModel copy() {
		GameModel[] pieces = new GameModel[1];
		pieces[0] = this;
		GameModel model = new GameModel(pieces, 1);
		model.depth = depth;
		model.transparent = transparent;
		return model;
	}

	public GameModel copy(boolean autoCommit, boolean isolated, boolean unlit,
			boolean pickable) {
		GameModel[] pieces = new GameModel[1];
		pieces[0] = this;
		GameModel model = new GameModel(pieces, 1, autoCommit, isolated, unlit,
				pickable);
		model.depth = depth;
		return model;
	}

	public void copyLighting(GameModel model, int srcVertice[],
			int verticeCount, int faceCount) {
		int destVertice[] = new int[verticeCount];
		for (int inVertex = 0; inVertex < verticeCount; inVertex++) {
			int outVertex = destVertice[inVertex] = model.vertexAt(
					vertexX[srcVertice[inVertex]],
					vertexY[srcVertice[inVertex]],
					vertexZ[srcVertice[inVertex]]);
			model.vertexIntensity[outVertex] = vertexIntensity[srcVertice[inVertex]];
			model.vertexAmbience[outVertex] = vertexAmbience[srcVertice[inVertex]];
		}

		int outFace = model.makeFace(verticeCount, destVertice,
				faceFillFront[faceCount], faceFillBack[faceCount]);
		if (!model.pickable && !pickable)
			model.faceTag[outFace] = faceTag[faceCount];
		model.faceIntensity[outFace] = faceIntensity[faceCount];
		model.faceCameraNormalScale[outFace] = faceCameraNormalScale[faceCount];
		model.faceCameraNormalMagnitude[outFace] = faceCameraNormalMagnitude[faceCount];
	}

	public void copyPosition(GameModel model) {
		orientationYaw = model.orientationYaw;
		orientationPitch = model.orientationPitch;
		orientationRoll = model.orientationRoll;
		baseX = model.baseX;
		baseY = model.baseY;
		baseZ = model.baseZ;
		determineTransformKind();
		transformState = 1;
	}

	private void determineTransformKind() {
		if (shearXY != 256 || shearXZ != 256 || shearYX != 256
				|| shearYZ != 256 || shearZX != 256 || shearZY != 256) {
			transformKind = 4;
			return;
		}
		if (scaleFX != 256 || scaleFY != 256 || scaleFZ != 256) {
			transformKind = 3;
			return;
		}
		if (orientationYaw != 0 || orientationPitch != 0
				|| orientationRoll != 0) {
			transformKind = 2;
			return;
		}
		if (baseX != 0 || baseY != 0 || baseZ != 0) {
			transformKind = 1;
		} else {
			transformKind = 0;
		}
	}

	public void light() {
		if (unlit)
			return;
		int divisor = lightDiffuse * lightDirectionMagnitude >> 8;
		for (int face = 0; face < faceCount; face++)
			if (faceIntensity[face] != magic)
				faceIntensity[face] = (faceNormalX[face] * lightDirectionX
						+ faceNormalY[face] * lightDirectionY + faceNormalZ[face]
						* lightDirectionZ)
						/ divisor;

		int[] normalX = new int[verticeCount];
		int[] normalY = new int[verticeCount];
		int[] normalZ = new int[verticeCount];
		int[] normalMagnitude = new int[verticeCount];
		for (int k = 0; k < verticeCount; k++) {
			normalX[k] = 0;
			normalY[k] = 0;
			normalZ[k] = 0;
			normalMagnitude[k] = 0;
		}

		for (int face = 0; face < faceCount; face++)
			if (faceIntensity[face] == magic) {
				for (int vertex = 0; vertex < faceVerticeCount[face]; vertex++) {
					int k1 = faceVertices[face][vertex];
					normalX[k1] += faceNormalX[face];
					normalY[k1] += faceNormalY[face];
					normalZ[k1] += faceNormalZ[face];
					normalMagnitude[k1]++;
				}

			}

		for (int j1 = 0; j1 < verticeCount; j1++)
			if (normalMagnitude[j1] > 0)
				vertexIntensity[j1] = (normalX[j1] * lightDirectionX
						+ normalY[j1] * lightDirectionY + normalZ[j1]
						* lightDirectionZ)
						/ (divisor * normalMagnitude[j1]);

	}

	public final int makeFace(int i, int ai[], int j, int k) {
		if (faceCount >= maxFaces) {
			return -1;
		} else {
			faceVerticeCount[faceCount] = i;
			faceVertices[faceCount] = ai;
			faceFillFront[faceCount] = j;
			faceFillBack[faceCount] = k;
			transformState = 1;
			return faceCount++;
		}
	}

	public final void merge(GameModel[] pieces, int count, boolean transState) {
		int fCount = 0;
		int vCount = 0;
		for (int l = 0; l < count; l++) {
			fCount += pieces[l].faceCount;
			vCount += pieces[l].verticeCount;
		}

		allocate(vCount, fCount);
		if (transState)
			faces = new int[fCount][];
		for (int i1 = 0; i1 < count; i1++) {
			GameModel source = pieces[i1];
			source.commit();
			lightAmbience = source.lightAmbience;
			lightDiffuse = source.lightDiffuse;
			lightDirectionX = source.lightDirectionX;
			lightDirectionY = source.lightDirectionY;
			lightDirectionZ = source.lightDirectionZ;
			lightDirectionMagnitude = source.lightDirectionMagnitude;
			for (int sourceFace = 0; sourceFace < source.faceCount; sourceFace++) {
				int[] destVertices = new int[source.faceVerticeCount[sourceFace]];
				int[] srcVertices = source.faceVertices[sourceFace];
				for (int k1 = 0; k1 < source.faceVerticeCount[sourceFace]; k1++)
					destVertices[k1] = vertexAt(
							source.vertexX[srcVertices[k1]],
							source.vertexY[srcVertices[k1]],
							source.vertexZ[srcVertices[k1]]);

				int destFace = makeFace(source.faceVerticeCount[sourceFace],
						destVertices, source.faceFillFront[sourceFace],
						source.faceFillBack[sourceFace]);
				faceIntensity[destFace] = source.faceIntensity[sourceFace];
				faceCameraNormalScale[destFace] = source.faceCameraNormalScale[sourceFace];
				faceCameraNormalMagnitude[destFace] = source.faceCameraNormalMagnitude[sourceFace];
				if (transState)
					if (count > 1) {
						faces[destFace] = new int[source.faces[sourceFace].length + 1];
						faces[destFace][0] = i1;
						System.arraycopy(source.faces[sourceFace], 0, faces[destFace], 1, source.faces[sourceFace].length);

					} else {
						faces[destFace] = new int[source.faces[sourceFace].length];
						System.arraycopy(source.faces[sourceFace], 0, faces[destFace], 0, source.faces[sourceFace].length);

					}
			}

		}

		transformState = 1;
	}

	public void orient(int yaw, int pitch, int roll) {
		orientationYaw = yaw & 0xff;
		orientationPitch = pitch & 0xff;
		orientationRoll = roll & 0xff;
		determineTransformKind();
		transformState = 1;
	}

	public void place(int x, int y, int z) {
		baseX = x;
		baseY = y;
		baseZ = z;
		determineTransformKind();
		transformState = 1;
	}

	public void prepareProjection() {
		vertexCameraX = new int[verticeCount];
		vertexCameraY = new int[verticeCount];
		vertexCameraZ = new int[verticeCount];
		vertexViewX = new int[verticeCount];
		vertexViewY = new int[verticeCount];
	}

	public void project(int cameraX, int cameraY, int cameraZ, int cameraPitch,
			int cameraRoll, int cameraYaw, int viewDistance, int nearClip) {
		apply();
		if (z1 > Scene.frustum_far || z2 < Scene.frustum_near
				|| x1 > Scene.frustum_right || x2 < Scene.frustum_left
				|| y1 > Scene.frustum_top || y2 < Scene.frustum_bottom) {
			visible = false;
			return;
		}
		visible = true;

		int yawSin = 0;
		int yawCos = 0;
		int pitchSin = 0;
		int pitchCos = 0;
		int rollSin = 0;
		int rollCos = 0;

		@SuppressWarnings("unused")
		int sin = sine9[cameraPitch];
		@SuppressWarnings("unused")
		int cos = sine9[cameraPitch + 256];

		if (cameraYaw != 0) {
			yawSin = sine11[cameraYaw];
			yawCos = sine11[cameraYaw + 1024];
		}
		if (cameraRoll != 0) {
			rollSin = sine11[cameraRoll];
			rollCos = sine11[cameraRoll + 1024];
		}
		if (cameraPitch != 0) {
			pitchSin = sine11[cameraPitch];
			pitchCos = sine11[cameraPitch + 1024];
		}
		for (int vertex = 0; vertex < verticeCount; vertex++) {
			int x = vertexTransformedX[vertex];
			//System.out.println((220 - 192) * 128);
			int y = vertexTransformedY[vertex];
			int z = vertexTransformedZ[vertex];
			if((220 - 56) * 128 == y) {
				System.err.println("Yup Y");
			}
			//System.out.println(z);
			//x = (220 - 192) * 128;
			//System.out.println(x);
			x -= cameraX;
			y -= cameraY;
			z -= cameraZ;
			if (cameraYaw != 0) {
				int lX = y * yawSin + x * yawCos >> 15;
				y = y * yawCos - x * yawSin >> 15;
				x = lX;
			}
			if (cameraRoll != 0) {
				int lX = z * rollSin + x * rollCos >> 15;
				z = z * rollCos - x * rollSin >> 15;
				x = lX;
			}
			if (cameraPitch != 0) {
				int lY = y * pitchCos - z * pitchSin >> 15;
				z = y * pitchSin + z * pitchCos >> 15;
				y = lY;
			}
			viewDistance = 9;
			if (z >= nearClip)
				vertexViewX[vertex] = (x << viewDistance) / z;
			else
				vertexViewX[vertex] = x << viewDistance;
			if (z >= nearClip)
				vertexViewY[vertex] = (y << viewDistance) / z;
			else
				vertexViewY[vertex] = y << viewDistance;
			vertexCameraX[vertex] = x;
			vertexCameraY[vertex] = y;
			vertexCameraZ[vertex] = z;
		}
	}

	public final int readBase64(byte[] buff) {
		for (; buff[dataOffset] == 10 || buff[dataOffset] == 13; )
			dataOffset++;
		int high = base64Alphabet[buff[dataOffset++] & 0xff];
		int mid = base64Alphabet[buff[dataOffset++] & 0xff];
		int low = base64Alphabet[buff[dataOffset++] & 0xff];
		int val = (high * 4096 + mid * 64 + low) - 0x20000;
		if (val == 0x1e240)
			val = magic;
		return val;
	}

	public void reduce(int i, int j) {
		faceCount -= i;
		if (faceCount < 0)
			faceCount = 0;
		verticeCount -= j;
		if (verticeCount < 0)
			verticeCount = 0;
	}

	public void relight() {
		if (unlit && isolated)
			return;
		for (int face = 0; face < faceCount; face++) {
			int[] vertice = faceVertices[face];
			int aX = vertexTransformedX[vertice[0]];
			int aY = vertexTransformedY[vertice[0]];
			int aZ = vertexTransformedZ[vertice[0]];
			int bX = vertexTransformedX[vertice[1]] - aX;
			int bY = vertexTransformedY[vertice[1]] - aY;
			int bZ = vertexTransformedZ[vertice[1]] - aZ;
			int cX = vertexTransformedX[vertice[2]] - aX;
			int cY = vertexTransformedY[vertice[2]] - aY;
			int cZ = vertexTransformedZ[vertice[2]] - aZ;
			int normX = bY * cZ - cY * bZ;
			int normY = bZ * cX - cZ * bX;
			int normZ;
			for (normZ = bX * cY - cX * bY; normX > 8192 || normY > 8192
					|| normZ > 8192 || normX < -8192 || normY < -8192
					|| normZ < -8192; normZ >>= 1) {
				normX >>= 1;
				normY >>= 1;
			}

			int normMagnitude = (int) (256D * Math.sqrt(normX * normX + normY
					* normY + normZ * normZ));
			if (normMagnitude <= 0)
				normMagnitude = 1;
			faceNormalX[face] = (normX * 0x10000) / normMagnitude;
			faceNormalY[face] = (normY * 0x10000) / normMagnitude;
			faceNormalZ[face] = (normZ * 65535) / normMagnitude;
			faceCameraNormalScale[face] = -1;
		}

		light();
	}

	public void rotate(int yaw, int pitch, int roll) {
		orientationYaw = orientationYaw + yaw & 0xff;
		orientationPitch = orientationPitch + pitch & 0xff;
		orientationRoll = orientationRoll + roll & 0xff;
		determineTransformKind();
		transformState = 1;
	}

	public void setLight(boolean gouraud, int ambience, int diffuse, int x,
			int y, int z) {
		lightAmbience = 256 - ambience * 4;
		lightDiffuse = (64 - diffuse) * 16 + 128;
		if (unlit)
			return;
		for (int face = 0; face < faceCount; face++)
			if (gouraud)
				faceIntensity[face] = magic;
			else
				faceIntensity[face] = 0;

		lightDirectionX = x;
		lightDirectionY = y;
		lightDirectionZ = z;
		lightDirectionMagnitude = (int) Math.sqrt(x * x + y * y + z * z);
		light();
	}

	public void setLight(int x, int y, int z) {
		if (!unlit) {
			lightDirectionX = x;
			lightDirectionY = y;
			lightDirectionZ = z;
			lightDirectionMagnitude = (int) Math.sqrt(x * x + y * y + z * z);
			light();
		}
	}

	public void setLight(int ambience, int diffuse, int x, int y, int z) {
		lightAmbience = 256 - ambience * 4;
		lightDiffuse = (64 - diffuse) * 16 + 128;
		if (!unlit) {
			lightDirectionX = x;
			lightDirectionY = y;
			lightDirectionZ = z;
			lightDirectionMagnitude = (int) Math.sqrt(x * x + y * y + z * z);
			light();
		}
	}

	public void setVertexAmbience(int vertex, int ambience) {
		vertexAmbience[vertex] = (byte) ambience;
	}

	public GameModel[] split(int i, int j, int pieceDX, int pieceDZ, int rows,
			int count, int pieceMaxVertice, boolean pickable) {
		commit();
		int pieceVerticeCount[] = new int[count];
		int pieceFaceCount[] = new int[count];
		for (int l1 = 0; l1 < count; l1++) {
			pieceVerticeCount[l1] = 0;
			pieceFaceCount[l1] = 0;
		}

		for (int face = 0; face < faceCount; face++) {
			int sumX = 0;
			int sumZ = 0;
			int vCount = faceVerticeCount[face];
			int[] vertice = faceVertices[face];
			for (int i4 = 0; i4 < vCount; i4++) {
				sumX += vertexX[vertice[i4]];
				sumZ += vertexZ[vertice[i4]];
			}

			int piece = sumX / (vCount * pieceDX)
					+ (sumZ / (vCount * pieceDZ)) * rows;
			pieceVerticeCount[piece] += vCount;
			pieceFaceCount[piece]++;
		}

		GameModel pieces[] = new GameModel[count];
		for (int l2 = 0; l2 < count; l2++) {
			if (pieceVerticeCount[l2] > pieceMaxVertice)
				pieceVerticeCount[l2] = pieceMaxVertice;
			pieces[l2] = new GameModel(pieceVerticeCount[l2],
					pieceFaceCount[l2], true, true, true, pickable, true);
			pieces[l2].lightDiffuse = lightDiffuse;
			pieces[l2].lightAmbience = lightAmbience;
		}

		for (int face = 0; face < faceCount; face++) {
			int sumX = 0;
			int sumZ = 0;
			int vCount = faceVerticeCount[face];
			int[] vertice = faceVertices[face];
			for (int vertex = 0; vertex < vCount; vertex++) {
				sumX += vertexX[vertice[vertex]];
				sumZ += vertexZ[vertice[vertex]];
			}

			int piece = sumX / (vCount * pieceDX)
					+ (sumZ / (vCount * pieceDZ)) * rows;
			copyLighting(pieces[piece], vertice, vCount, face);
		}

		for (int piece = 0; piece < count; piece++)
			pieces[piece].prepareProjection();

		return pieces;
	}

	public void translate(int x, int y, int z) {
		baseX += x;
		baseY += y;
		baseZ += z;
		determineTransformKind();
		transformState = 1;
	}

	public final int vertexAt(int x, int y, int z) {
		for (int l = 0; l < verticeCount; l++)
			if (vertexX[l] == x && vertexY[l] == y && vertexZ[l] == z)
				return l;

		if (verticeCount >= maxVertices) {
			return -1;
		} else {
			vertexX[verticeCount] = x;
			vertexY[verticeCount] = y;
			vertexZ[verticeCount] = z;
			return verticeCount++;
		}
	}
}
