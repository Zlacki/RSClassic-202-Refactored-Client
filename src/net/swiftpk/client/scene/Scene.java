package net.swiftpk.client.scene;

import net.swiftpk.client.gfx.Surface;

public class Scene {

	private static void method283(int ai[], int ai1[], int i, int j, int k,
			int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2,
			int l2) {
		if (i2 <= 0)
			return;
		int i3 = 0;
		int j3 = 0;
		int i4 = 0;
		if (i1 != 0) {
			i = k / i1 << 7;
			j = l / i1 << 7;
		}
		if (i < 0)
			i = 0;
		else if (i > 16256)
			i = 16256;
		k += j1;
		l += k1;
		i1 += l1;
		if (i1 != 0) {
			i3 = k / i1 << 7;
			j3 = l / i1 << 7;
		}
		if (i3 < 0)
			i3 = 0;
		else if (i3 > 16256)
			i3 = 16256;
		int k3 = i3 - i >> 4;
		int l3 = j3 - j >> 4;
		for (int j4 = i2 >> 4; j4 > 0; j4--) {
			i += k2 & 0x600000;
			i4 = k2 >> 23;
			k2 += l2;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			i = (i & 0x3fff) + (k2 & 0x600000);
			i4 = k2 >> 23;
			k2 += l2;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			i = (i & 0x3fff) + (k2 & 0x600000);
			i4 = k2 >> 23;
			k2 += l2;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			i = (i & 0x3fff) + (k2 & 0x600000);
			i4 = k2 >> 23;
			k2 += l2;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i = i3;
			j = j3;
			k += j1;
			l += k1;
			i1 += l1;
			if (i1 != 0) {
				i3 = k / i1 << 7;
				j3 = l / i1 << 7;
			}
			if (i3 < 0)
				i3 = 0;
			else if (i3 > 16256)
				i3 = 16256;
			k3 = i3 - i >> 4;
			l3 = j3 - j >> 4;
		}

		for (int k4 = 0; k4 < (i2 & 0xf); k4++) {
			if ((k4 & 3) == 0) {
				i = (i & 0x3fff) + (k2 & 0x600000);
				i4 = k2 >> 23;
				k2 += l2;
			}
			ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
			i += k3;
			j += l3;
		}

	}

	private static void method284(int ai[], int ai1[], int i, int j, int k,
			int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2,
			int l2) {
		if (i2 <= 0)
			return;
		int i3 = 0;
		int j3 = 0;
		int i4 = 0;
		if (i1 != 0) {
			i = k / i1 << 7;
			j = l / i1 << 7;
		}
		if (i < 0)
			i = 0;
		else if (i > 16256)
			i = 16256;
		k += j1;
		l += k1;
		i1 += l1;
		if (i1 != 0) {
			i3 = k / i1 << 7;
			j3 = l / i1 << 7;
		}
		if (i3 < 0)
			i3 = 0;
		else if (i3 > 16256)
			i3 = 16256;
		int k3 = i3 - i >> 4;
		int l3 = j3 - j >> 4;
		for (int j4 = i2 >> 4; j4 > 0; j4--) {
			i += k2 & 0x600000;
			i4 = k2 >> 23;
			k2 += l2;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			i = (i & 0x3fff) + (k2 & 0x600000);
			i4 = k2 >> 23;
			k2 += l2;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			i = (i & 0x3fff) + (k2 & 0x600000);
			i4 = k2 >> 23;
			k2 += l2;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			i = (i & 0x3fff) + (k2 & 0x600000);
			i4 = k2 >> 23;
			k2 += l2;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i = i3;
			j = j3;
			k += j1;
			l += k1;
			i1 += l1;
			if (i1 != 0) {
				i3 = k / i1 << 7;
				j3 = l / i1 << 7;
			}
			if (i3 < 0)
				i3 = 0;
			else if (i3 > 16256)
				i3 = 16256;
			k3 = i3 - i >> 4;
			l3 = j3 - j >> 4;
		}

		for (int k4 = 0; k4 < (i2 & 0xf); k4++) {
			if ((k4 & 3) == 0) {
				i = (i & 0x3fff) + (k2 & 0x600000);
				i4 = k2 >> 23;
				k2 += l2;
			}
			ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4)
					+ (ai[j2] >> 1 & 0x7f7f7f);
			i += k3;
			j += l3;
		}

	}

	private static void method285(int ai[], int i, int j, int k, int ai1[],
			int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2,
			int l2, int i3) {
		if (j2 <= 0)
			return;
		int j3 = 0;
		int k3 = 0;
		i3 <<= 2;
		if (j1 != 0) {
			j3 = l / j1 << 7;
			k3 = i1 / j1 << 7;
		}
		if (j3 < 0)
			j3 = 0;
		else if (j3 > 16256)
			j3 = 16256;
		for (int j4 = j2; j4 > 0; j4 -= 16) {
			l += k1;
			i1 += l1;
			j1 += i2;
			j = j3;
			k = k3;
			if (j1 != 0) {
				j3 = l / j1 << 7;
				k3 = i1 / j1 << 7;
			}
			if (j3 < 0)
				j3 = 0;
			else if (j3 > 16256)
				j3 = 16256;
			int l3 = j3 - j >> 4;
			int i4 = k3 - k >> 4;
			int k4 = l2 >> 23;
			j += l2 & 0x600000;
			l2 += i3;
			if (j4 < 16) {
				for (int l4 = 0; l4 < j4; l4++) {
					if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
						ai[k2] = i;
					k2++;
					j += l3;
					k += i4;
					if ((l4 & 3) == 3) {
						j = (j & 0x3fff) + (l2 & 0x600000);
						k4 = l2 >> 23;
						l2 += i3;
					}
				}

			} else {
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				j = (j & 0x3fff) + (l2 & 0x600000);
				k4 = l2 >> 23;
				l2 += i3;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				j = (j & 0x3fff) + (l2 & 0x600000);
				k4 = l2 >> 23;
				l2 += i3;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				j = (j & 0x3fff) + (l2 & 0x600000);
				k4 = l2 >> 23;
				l2 += i3;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
			}
		}

	}

	private static void method286(int ai[], int ai1[], int i, int j, int k,
			int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2,
			int l2) {
		if (i2 <= 0)
			return;
		int i3 = 0;
		int j3 = 0;
		l2 <<= 2;
		if (i1 != 0) {
			i3 = k / i1 << 6;
			j3 = l / i1 << 6;
		}
		if (i3 < 0)
			i3 = 0;
		else if (i3 > 4032)
			i3 = 4032;
		for (int i4 = i2; i4 > 0; i4 -= 16) {
			k += j1;
			l += k1;
			i1 += l1;
			i = i3;
			j = j3;
			if (i1 != 0) {
				i3 = k / i1 << 6;
				j3 = l / i1 << 6;
			}
			if (i3 < 0)
				i3 = 0;
			else if (i3 > 4032)
				i3 = 4032;
			int k3 = i3 - i >> 4;
			int l3 = j3 - j >> 4;
			int j4 = k2 >> 20;
			i += k2 & 0xc0000;
			k2 += l2;
			if (i4 < 16) {
				for (int k4 = 0; k4 < i4; k4++) {
					ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
					i += k3;
					j += l3;
					if ((k4 & 3) == 3) {
						i = (i & 0xfff) + (k2 & 0xc0000);
						j4 = k2 >> 20;
						k2 += l2;
					}
				}

			} else {
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				i = (i & 0xfff) + (k2 & 0xc0000);
				j4 = k2 >> 20;
				k2 += l2;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				i = (i & 0xfff) + (k2 & 0xc0000);
				j4 = k2 >> 20;
				k2 += l2;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				i = (i & 0xfff) + (k2 & 0xc0000);
				j4 = k2 >> 20;
				k2 += l2;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
				i += k3;
				j += l3;
				ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
			}
		}

	}

	private static void method287(int ai[], int ai1[], int i, int j, int k,
			int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2,
			int l2) {
		if (i2 <= 0)
			return;
		int i3 = 0;
		int j3 = 0;
		l2 <<= 2;
		if (i1 != 0) {
			i3 = k / i1 << 6;
			j3 = l / i1 << 6;
		}
		if (i3 < 0)
			i3 = 0;
		else if (i3 > 4032)
			i3 = 4032;
		for (int i4 = i2; i4 > 0; i4 -= 16) {
			k += j1;
			l += k1;
			i1 += l1;
			i = i3;
			j = j3;
			if (i1 != 0) {
				i3 = k / i1 << 6;
				j3 = l / i1 << 6;
			}
			if (i3 < 0)
				i3 = 0;
			else if (i3 > 4032)
				i3 = 4032;
			int k3 = i3 - i >> 4;
			int l3 = j3 - j >> 4;
			int j4 = k2 >> 20;
			i += k2 & 0xc0000;
			k2 += l2;
			if (i4 < 16) {
				for (int k4 = 0; k4 < i4; k4++) {
					ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
							+ (ai[j2] >> 1 & 0x7f7f7f);
					i += k3;
					j += l3;
					if ((k4 & 3) == 3) {
						i = (i & 0xfff) + (k2 & 0xc0000);
						j4 = k2 >> 20;
						k2 += l2;
					}
				}

			} else {
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				i = (i & 0xfff) + (k2 & 0xc0000);
				j4 = k2 >> 20;
				k2 += l2;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				i = (i & 0xfff) + (k2 & 0xc0000);
				j4 = k2 >> 20;
				k2 += l2;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				i = (i & 0xfff) + (k2 & 0xc0000);
				j4 = k2 >> 20;
				k2 += l2;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
				i += k3;
				j += l3;
				ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4)
						+ (ai[j2] >> 1 & 0x7f7f7f);
			}
		}

	}

	private static void method288(int ai[], int i, int j, int k, int ai1[],
			int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2,
			int l2, int i3) {
		if (j2 <= 0)
			return;
		int j3 = 0;
		int k3 = 0;
		i3 <<= 2;
		if (j1 != 0) {
			j3 = l / j1 << 6;
			k3 = i1 / j1 << 6;
		}
		if (j3 < 0)
			j3 = 0;
		else if (j3 > 4032)
			j3 = 4032;
		for (int j4 = j2; j4 > 0; j4 -= 16) {
			l += k1;
			i1 += l1;
			j1 += i2;
			j = j3;
			k = k3;
			if (j1 != 0) {
				j3 = l / j1 << 6;
				k3 = i1 / j1 << 6;
			}
			if (j3 < 0)
				j3 = 0;
			else if (j3 > 4032)
				j3 = 4032;
			int l3 = j3 - j >> 4;
			int i4 = k3 - k >> 4;
			int k4 = l2 >> 20;
			j += l2 & 0xc0000;
			l2 += i3;
			if (j4 < 16) {
				for (int l4 = 0; l4 < j4; l4++) {
					if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
						ai[k2] = i;
					k2++;
					j += l3;
					k += i4;
					if ((l4 & 3) == 3) {
						j = (j & 0xfff) + (l2 & 0xc0000);
						k4 = l2 >> 20;
						l2 += i3;
					}
				}

			} else {
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				j = (j & 0xfff) + (l2 & 0xc0000);
				k4 = l2 >> 20;
				l2 += i3;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				j = (j & 0xfff) + (l2 & 0xc0000);
				k4 = l2 >> 20;
				l2 += i3;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				j = (j & 0xfff) + (l2 & 0xc0000);
				k4 = l2 >> 20;
				l2 += i3;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
				j += l3;
				k += i4;
				if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
					ai[k2] = i;
				k2++;
			}
		}

	}

	private static void method289(int ai[], int i, int j, int k, int ai1[],
			int l, int i1) {
		if (i >= 0)
			return;
		i1 <<= 1;
		k = ai1[l >> 8 & 0xff];
		l += i1;
		int j1 = i / 8;
		for (int k1 = j1; k1 < 0; k1++) {
			ai[j++] = k;
			ai[j++] = k;
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k;
			ai[j++] = k;
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k;
			ai[j++] = k;
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k;
			ai[j++] = k;
			k = ai1[l >> 8 & 0xff];
			l += i1;
		}

		j1 = -(i % 8);
		for (int l1 = 0; l1 < j1; l1++) {
			ai[j++] = k;
			if ((l1 & 1) == 1) {
				k = ai1[l >> 8 & 0xff];
				l += i1;
			}
		}

	}

	private static void method290(int ai[], int i, int j, int k, int ai1[],
			int l, int i1) {
		if (i >= 0)
			return;
		i1 <<= 2;
		k = ai1[l >> 8 & 0xff];
		l += i1;
		int j1 = i / 16;
		for (int k1 = j1; k1 < 0; k1++) {
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			k = ai1[l >> 8 & 0xff];
			l += i1;
		}

		j1 = -(i % 16);
		for (int l1 = 0; l1 < j1; l1++) {
			ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
			if ((l1 & 3) == 3) {
				k = ai1[l >> 8 & 0xff];
				l += i1;
				l += i1;
			}
		}

	}

	private static void method291(int ai[], int i, int j, int k, int ai1[],
			int l, int i1) {
		if (i >= 0)
			return;
		i1 <<= 2;
		k = ai1[l >> 8 & 0xff];
		l += i1;
		int j1 = i / 16;
		for (int k1 = j1; k1 < 0; k1++) {
			ai[j++] = k;
			ai[j++] = k;
			ai[j++] = k;
			ai[j++] = k;
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k;
			ai[j++] = k;
			ai[j++] = k;
			ai[j++] = k;
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k;
			ai[j++] = k;
			ai[j++] = k;
			ai[j++] = k;
			k = ai1[l >> 8 & 0xff];
			l += i1;
			ai[j++] = k;
			ai[j++] = k;
			ai[j++] = k;
			ai[j++] = k;
			k = ai1[l >> 8 & 0xff];
			l += i1;
		}

		j1 = -(i % 16);
		for (int l1 = 0; l1 < j1; l1++) {
			ai[j++] = k;
			if ((l1 & 3) == 3) {
				k = ai1[l >> 8 & 0xff];
				l += i1;
			}
		}

	}

	public static int method305(int i, int j, int k) {
		return -1 - (i / 8) * 1024 - (j / 8) * 32 - k / 8;
	}

	private static byte aByteArray434[];

	private static long aLong431;

	public static int curve[] = new int[2048];

	private static final int SOME_INTS[] = new int[512];

	static int frustum_bottom;

	static int frustum_far;

	static int frustum_left;

	static int frustum_near;

	static int frustum_right;

	static int frustum_top;

	public boolean aBoolean386;

	private boolean aBoolean389;

	boolean aBooleanArray430[];

	byte aByteArrayArray425[][];

	public GameModel activeModel;

	public double aDouble387;

	long aLongArray428[];

	int anInt374;

	public int anInt388;

	private final int anInt402;

	int anInt424;

	int anInt454;

	int anInt455;

	int anIntArray375[];

	int anIntArray377[];

	private final int anIntArray419[];

	private final int anIntArray420[];

	private final int anIntArray421[];

	private final int anIntArray422[];

	int anIntArray427[];

	public int anIntArray437[];

	int anIntArray441[];

	int anIntArray442[];

	int anIntArray443[];

	int anIntArray444[];

	int anIntArray445[];

	int anIntArray446[];

	int anIntArrayArray376[][];

	int anIntArrayArray426[][];
	int anIntArrayArray429[][];
	int anIntArrayArray432[][];
	int anIntArrayArray433[][];
	private int cameraModelCount;
	private int cameraPitch;
	private int cameraRoll;
	CameraVariables cameraVariables[];
	private int cameraX;
	private int cameraY;
	private int cameraYaw;
	private int cameraZ;
	public int clipFar2D;
	public int clipFar3D;
	public int clipNear;
	private int currentVisibleModelCount;
	public int fogZDistance;
	public int fogZFalloff;
	private int halfHeight;
	private int halfHeight2;
	private int halfWidth;
	private int halfWidth2;
	boolean interlace;
	public int lastCameraModelCount;
	public int maxModelCount;
	private final int maxVisibleModelCount;
	public int modelCount;
	int modelLeftY;
	int modelRightY;
	public GameModel models[];
	private int mouseX;
	private int mouseY;
	private final Polygon polygons[];
	private int spriteCount;
	private final int spriteX[];
	private final int spriteY[];
	private final int spriteZ[];
	Surface surface;
	private int viewDistance;
	private final int visibleModelIndeces[];
	private final GameModel visibleModels[];
	private int width;

	public Scene(Surface surface, int maxModels, int maxPolygons,
			int modelFaceCount) {
		anInt374 = 50;
		anIntArray375 = new int[anInt374];
		anIntArrayArray376 = new int[anInt374][256];
		clipNear = 5;
		clipFar3D = 1000;
		clipFar2D = 1000;
		fogZFalloff = 20;
		fogZDistance = 10;
		aBoolean386 = false;
		aDouble387 = 1.1000000000000001D;
		anInt388 = 1;
		aBoolean389 = false;
		maxVisibleModelCount = 100;
		visibleModels = new GameModel[maxVisibleModelCount];
		visibleModelIndeces = new int[maxVisibleModelCount];
		width = 512;
		/*
		 * halfWidth = 256; halfHeight = 192;
		 */
		halfWidth2 = 256;
		halfHeight2 = 256;
		viewDistance = 8;
		anInt402 = 4;
		anIntArray441 = new int[40];
		anIntArray442 = new int[40];
		anIntArray443 = new int[40];
		anIntArray444 = new int[40];
		anIntArray445 = new int[40];
		anIntArray446 = new int[40];
		interlace = false;
		this.surface = surface;
		halfWidth = 500;
		halfHeight = 500;
		anIntArray437 = surface.pixels;
		modelCount = 0;
		maxModelCount = maxModels;
		models = new GameModel[maxModelCount];
		cameraModelCount = 0;
		polygons = new Polygon[maxPolygons];
		for (int polygon = 0; polygon < maxPolygons; polygon++)
			polygons[polygon] = new Polygon();

		spriteCount = 0;
		activeModel = new GameModel(modelFaceCount * 2, modelFaceCount);
		spriteX = new int[modelFaceCount];
		anIntArray420 = new int[modelFaceCount];
		anIntArray421 = new int[modelFaceCount];
		spriteZ = new int[modelFaceCount];
		spriteY = new int[modelFaceCount];
		anIntArray419 = new int[modelFaceCount];
		anIntArray422 = new int[modelFaceCount];
		if (aByteArray434 == null)
			aByteArray434 = new byte[17691];
		cameraX = 0;
		cameraY = 0;
		cameraZ = 0;
		cameraPitch = 0;
		cameraYaw = 0;
		cameraRoll = 0;
		for (int i1 = 0; i1 < 256; i1++) {
			SOME_INTS[i1] = (int) (Math.sin(i1 * 0.02454369D) * 32768D);
			SOME_INTS[i1 + 256] = (int) (Math.cos(i1 * 0.02454369D) * 32768D);
		}

		for (int j1 = 0; j1 < 1024; j1++) {
			curve[j1] = (int) (Math.sin(j1 * 0.00613592315D) * 32768D);
			curve[j1 + 1024] = (int) (Math.cos(j1 * 0.00613592315D) * 32768D);
		}

	}

	public void addModel(GameModel model) {
		if (model == null)
			System.out.println("Warning tried to add null object!");
		if (modelCount < maxModelCount)
			models[modelCount++] = model;
	}

	public void cleanupModels() {
		resetActiveModel();
		for (int i = 0; i < modelCount; i++)
			models[i] = null;

		modelCount = 0;
	}

	public int drawSprite(int x, int z, int y, int l, int i1, int j1, int tag) {
		spriteX[spriteCount] = x;
		spriteZ[spriteCount] = z;
		spriteY[spriteCount] = y;
		anIntArray419[spriteCount] = l;
		anIntArray420[spriteCount] = i1;
		anIntArray421[spriteCount] = j1;
		anIntArray422[spriteCount] = 0;
		int l1 = activeModel.addVertex(z, y, l);
		int i2 = activeModel.addVertex(z, y - j1, l);
		int[] vertice = { l1, i2 };
		activeModel.makeFace(2, vertice, 0, 0);
		activeModel.faceTag[spriteCount] = tag;
		activeModel.faceSpriteType[spriteCount++] = 0;
		return spriteCount - 1;
	}

	public void finishCamera() {
		interlace = surface.interlace;
		int i3 = halfWidth * clipFar3D >> viewDistance;
		int j3 = halfHeight * clipFar3D >> viewDistance;
		frustum_left = 0;
		frustum_right = 0;
		frustum_bottom = 0;
		frustum_top = 0;
		frustum_near = 0;
		frustum_far = 0;
		method279(-i3, -j3, clipFar3D);
		method279(-i3, j3, clipFar3D);
		method279(i3, -j3, clipFar3D);
		method279(i3, j3, clipFar3D);
		method279(-halfWidth, -halfHeight, 0);
		method279(-halfWidth, halfHeight, 0);
		method279(halfWidth, -halfHeight, 0);
		method279(halfWidth, halfHeight, 0);
		frustum_left += cameraX;
		frustum_right += cameraX;
		frustum_bottom += cameraY;
		frustum_top += cameraY;
		frustum_near += cameraZ;
		frustum_far += cameraZ;
		models[modelCount] = activeModel;
		activeModel.transformState = 2;
		for (int i = 0; i < modelCount; i++)
			models[i].project(cameraX, cameraY, cameraZ, cameraPitch,
					cameraYaw, cameraRoll, viewDistance, clipNear);

		models[modelCount].project(cameraX, cameraY, cameraZ, cameraPitch,
				cameraYaw, cameraRoll, viewDistance, clipNear);
		cameraModelCount = 0;
		for (int k3 = 0; k3 < modelCount; k3++) {
			GameModel model = models[k3];
			if (model.visible) {
				for (int j = 0; j < model.faceCount; j++) {
					int l3 = model.faceVerticeCount[j];
					int ai1[] = model.faceVertices[j];
					boolean flag = false;
					for (int k4 = 0; k4 < l3; k4++) {
						int i1 = model.vertexCameraZ[ai1[k4]];
						if (i1 <= clipNear || i1 >= clipFar3D)
							continue;
						flag = true;
						break;
					}

					if (flag) {
						int l1 = 0;
						for (int k5 = 0; k5 < l3; k5++) {
							int j1 = model.vertexViewX[ai1[k5]];
							if (j1 > -halfWidth)
								l1 |= 1;
							if (j1 < halfWidth)
								l1 |= 2;
							if (l1 == 3)
								break;
						}

						if (l1 == 3) {
							int i2 = 0;
							for (int l6 = 0; l6 < l3; l6++) {
								int k1 = model.vertexViewY[ai1[l6]];
								if (k1 > -halfHeight)
									i2 |= 1;
								if (k1 < halfHeight)
									i2 |= 2;
								if (i2 == 3)
									break;
							}

							if (i2 == 3) {
								Polygon cameraModel_1 = polygons[cameraModelCount];
								cameraModel_1.aModel_359 = model;
								cameraModel_1.anInt360 = j;
								method293(cameraModelCount);
								int l8;
								if (cameraModel_1.anInt365 < 0)
									l8 = model.faceFillFront[j];
								else
									l8 = model.faceFillBack[j];
								if (l8 != 0xbc614e) {
									int j2 = 0;
									for (int l9 = 0; l9 < l3; l9++)
										j2 += model.vertexCameraZ[ai1[l9]];

									cameraModel_1.anInt361 = j2 / l3
											+ model.depth;
									cameraModel_1.anInt366 = l8;
									cameraModelCount++;
								}
							}
						}
					}
				}

			}
		}

		GameModel model_1 = activeModel;
		// Draw player
		if (model_1.visible) {
			for (int k = 0; k < model_1.faceCount; k++) {
				int ai[] = model_1.faceVertices[k];
				int j4 = ai[0];
				int l4 = model_1.vertexViewX[j4];
				int l5 = model_1.vertexViewY[j4];
				int i7 = model_1.vertexCameraZ[j4];
				if (i7 > clipNear && i7 < clipFar2D) {
					int i8 = (anIntArray420[k] << viewDistance) / i7;
					int i9 = (anIntArray421[k] << viewDistance) / i7;
					if (l4 - i8 / 2 <= halfWidth && l4 + i8 / 2 >= -halfWidth
							&& l5 - i9 <= halfHeight && l5 >= -halfHeight) {
						Polygon cameraModel_2 = polygons[cameraModelCount];
						cameraModel_2.aModel_359 = model_1;
						cameraModel_2.anInt360 = k;
						method294(cameraModelCount);
						cameraModel_2.anInt361 = (i7 + model_1.vertexCameraZ[ai[1]]) / 2;
						cameraModelCount++;
					}
				}
			}

		}
		if (cameraModelCount == 0)
			return;
		lastCameraModelCount = cameraModelCount;
		method276(polygons, 0, cameraModelCount - 1);
		method277(100, polygons, cameraModelCount);
		for (int i4 = 0; i4 < cameraModelCount; i4++) {
			Polygon cameraModel = polygons[i4];
			GameModel model_2 = cameraModel.aModel_359;
			int l = cameraModel.anInt360;
			if (model_2 == activeModel) {
				int ai2[] = model_2.faceVertices[l];
				int i6 = ai2[0];
				int j7 = model_2.vertexViewX[i6];
				int j8 = model_2.vertexViewY[i6];
				int j9 = model_2.vertexCameraZ[i6];
				int i10 = (anIntArray420[l] << viewDistance) / j9;
				int k10 = (anIntArray421[l] << viewDistance) / j9;
				int i11 = j8 - model_2.vertexViewY[ai2[1]];
				int j11 = model_2.vertexViewX[ai2[1]] - j7;
				int l11 = j7 - i10 / 2;
				int j12 = (halfHeight2 + j8) - k10;
				surface.spriteClipping(l11 + halfWidth2, j12, i10, k10,
						spriteX[l], j11, (256 << viewDistance) / j9);
				if (aBoolean389
						&& currentVisibleModelCount < maxVisibleModelCount) {
					l11 += (anIntArray422[l] << viewDistance) / j9;
					if (mouseY >= j12 && mouseY <= j12 + k10 && mouseX >= l11
							&& mouseX <= l11 + i10 && !model_2.pickable
							&& model_2.faceSpriteType[l] == 0) {
						visibleModels[currentVisibleModelCount] = model_2;
						visibleModelIndeces[currentVisibleModelCount] = l;
						currentVisibleModelCount++;
					}
				}
			} else {
				int k8 = 0;
				int j10 = 0;
				int l10 = model_2.faceVerticeCount[l];
				int ai3[] = model_2.faceVertices[l];
				if (model_2.faceIntensity[l] != 0xbc614e)
					if (cameraModel.anInt365 < 0)
						j10 = model_2.lightAmbience - model_2.faceIntensity[l];
					else
						j10 = model_2.lightAmbience + model_2.faceIntensity[l];
				for (int k11 = 0; k11 < l10; k11++) {
					int k2 = ai3[k11];
					anIntArray444[k11] = model_2.vertexCameraX[k2];
					anIntArray445[k11] = model_2.vertexCameraY[k2];
					anIntArray446[k11] = model_2.vertexCameraZ[k2];
					if (model_2.faceIntensity[l] == 0xbc614e)
						if (cameraModel.anInt365 < 0)
							j10 = (model_2.lightAmbience - model_2.vertexIntensity[k2])
									+ model_2.vertexAmbience[k2];
						else
							j10 = model_2.lightAmbience
									+ model_2.vertexIntensity[k2]
									+ model_2.vertexAmbience[k2];
					if (model_2.vertexCameraZ[k2] >= clipNear) {
						anIntArray441[k8] = model_2.vertexViewX[k2];
						anIntArray442[k8] = model_2.vertexViewY[k2];
						anIntArray443[k8] = j10;
						if (model_2.vertexCameraZ[k2] > fogZDistance)
							anIntArray443[k8] += (model_2.vertexCameraZ[k2] - fogZDistance)
									/ fogZFalloff;
						k8++;
					} else {
						int k9;
						if (k11 == 0)
							k9 = ai3[l10 - 1];
						else
							k9 = ai3[k11 - 1];
						if (model_2.vertexCameraZ[k9] >= clipNear) {
							int k7 = model_2.vertexCameraZ[k2]
									- model_2.vertexCameraZ[k9];
							int i5 = model_2.vertexCameraX[k2]
									- ((model_2.vertexCameraX[k2] - model_2.vertexCameraX[k9]) * (model_2.vertexCameraZ[k2] - clipNear))
									/ k7;
							int j6 = model_2.vertexCameraY[k2]
									- ((model_2.vertexCameraY[k2] - model_2.vertexCameraY[k9]) * (model_2.vertexCameraZ[k2] - clipNear))
									/ k7;
							anIntArray441[k8] = (i5 << viewDistance) / clipNear;
							anIntArray442[k8] = (j6 << viewDistance) / clipNear;
							anIntArray443[k8] = j10;
							k8++;
						}
						if (k11 == l10 - 1)
							k9 = ai3[0];
						else
							k9 = ai3[k11 + 1];
						if (model_2.vertexCameraZ[k9] >= clipNear) {
							int l7 = model_2.vertexCameraZ[k2]
									- model_2.vertexCameraZ[k9];
							int j5 = model_2.vertexCameraX[k2]
									- ((model_2.vertexCameraX[k2] - model_2.vertexCameraX[k9]) * (model_2.vertexCameraZ[k2] - clipNear))
									/ l7;
							int k6 = model_2.vertexCameraY[k2]
									- ((model_2.vertexCameraY[k2] - model_2.vertexCameraY[k9]) * (model_2.vertexCameraZ[k2] - clipNear))
									/ l7;
							anIntArray441[k8] = (j5 << viewDistance) / clipNear;
							anIntArray442[k8] = (k6 << viewDistance) / clipNear;
							anIntArray443[k8] = j10;
							k8++;
						}
					}
				}

				for (int i12 = 0; i12 < l10; i12++) {
					if (anIntArray443[i12] < 0)
						anIntArray443[i12] = 0;
					else if (anIntArray443[i12] > 255)
						anIntArray443[i12] = 255;
					if (cameraModel.anInt366 >= 0)
						if (anIntArray427[cameraModel.anInt366] == 1)
							anIntArray443[i12] <<= 9;
						else
							anIntArray443[i12] <<= 6;
				}

				method281(0, 0, 0, 0, k8, anIntArray441, anIntArray442,
						anIntArray443, model_2, l);
				if (modelRightY > modelLeftY)
					method282(0, 0, l10, anIntArray444, anIntArray445,
							anIntArray446, cameraModel.anInt366, model_2);
			}
		}

		aBoolean389 = false;
	}

	public GameModel[] getVisibleModels() {
		return visibleModels;
	}

	public int method272() {
		return currentVisibleModelCount;
	}

	public int[] method273() {
		return visibleModelIndeces;
	}

	private void method276(Polygon cameraModels[], int i, int j) {
		if (i < j) {
			int k = i - 1;
			int l = j + 1;
			int i1 = (i + j) / 2;
			Polygon cameraModel = cameraModels[i1];
			cameraModels[i1] = cameraModels[i];
			cameraModels[i] = cameraModel;
			int j1 = cameraModel.anInt361;
			while (k < l) {
				do
					l--;
				while (cameraModels[l].anInt361 < j1);
				do
					k++;
				while (cameraModels[k].anInt361 > j1);
				if (k < l) {
					Polygon cameraModel_1 = cameraModels[k];
					cameraModels[k] = cameraModels[l];
					cameraModels[l] = cameraModel_1;
				}
			}
			method276(cameraModels, i, l);
			method276(cameraModels, l + 1, j);
		}
	}

	public void method277(int i, Polygon cameraModels[], int j) {
		for (int k = 0; k <= j; k++) {
			cameraModels[k].aBoolean367 = false;
			cameraModels[k].anInt368 = k;
			cameraModels[k].anInt369 = -1;
		}

		int l = 0;
		do {
			while (cameraModels[l].aBoolean367)
				l++;
			if (l == j)
				return;
			Polygon cameraModel = cameraModels[l];
			cameraModel.aBoolean367 = true;
			int i1 = l;
			int j1 = l + i;
			if (j1 >= j)
				j1 = j - 1;
			for (int k1 = j1; k1 >= i1 + 1; k1--) {
				Polygon cameraModel_1 = cameraModels[k1];
				if (cameraModel.anInt353 < cameraModel_1.anInt355
						&& cameraModel_1.anInt353 < cameraModel.anInt355
						&& cameraModel.anInt354 < cameraModel_1.anInt356
						&& cameraModel_1.anInt354 < cameraModel.anInt356
						&& cameraModel.anInt368 != cameraModel_1.anInt369
						&& !method295(cameraModel, cameraModel_1)
						&& method296(cameraModel_1, cameraModel)) {
					method278(cameraModels, i1, k1);
					if (cameraModels[k1] != cameraModel_1)
						k1++;
					i1 = anInt454;
					cameraModel_1.anInt369 = cameraModel.anInt368;
				}
			}

		} while (true);
	}

	public boolean method278(Polygon cameraModels[], int i, int j) {
		do {
			Polygon cameraModel = cameraModels[i];
			for (int k = i + 1; k <= j; k++) {
				Polygon cameraModel_1 = cameraModels[k];
				if (!method295(cameraModel_1, cameraModel))
					break;
				cameraModels[i] = cameraModel_1;
				cameraModels[k] = cameraModel;
				i = k;
				if (i == j) {
					anInt454 = i;
					anInt455 = i - 1;
					return true;
				}
			}

			Polygon cameraModel_2 = cameraModels[j];
			for (int l = j - 1; l >= i; l--) {
				Polygon cameraModel_3 = cameraModels[l];
				if (!method295(cameraModel_2, cameraModel_3))
					break;
				cameraModels[j] = cameraModel_3;
				cameraModels[l] = cameraModel_2;
				j = l;
				if (i == j) {
					anInt454 = j + 1;
					anInt455 = j;
					return true;
				}
			}

			if (i + 1 >= j) {
				anInt454 = i;
				anInt455 = j;
				return false;
			}
			if (!method278(cameraModels, i + 1, j)) {
				anInt454 = i;
				return false;
			}
			j = anInt455;
		} while (true);
	}

	public void method279(int i, int j, int k) {
		int l = -cameraPitch + 1024 & 0x3ff;
		int i1 = -cameraYaw + 1024 & 0x3ff;
		int j1 = -cameraRoll + 1024 & 0x3ff;
		if (j1 != 0) {
			int k1 = curve[j1];
			int j2 = curve[j1 + 1024];
			int i3 = j * k1 + i * j2 >> 15;
			j = j * j2 - i * k1 >> 15;
			i = i3;
		}
		if (l != 0) {
			int l1 = curve[l];
			int k2 = curve[l + 1024];
			int j3 = j * k2 - k * l1 >> 15;
			k = j * l1 + k * k2 >> 15;
			j = j3;
		}
		if (i1 != 0) {
			int i2 = curve[i1];
			int l2 = curve[i1 + 1024];
			int k3 = k * i2 + i * l2 >> 15;
			k = k * l2 - i * i2 >> 15;
			i = k3;
		}
		if (i < frustum_left)
			frustum_left = i;
		if (i > frustum_right)
			frustum_right = i;
		if (j < frustum_bottom)
			frustum_bottom = j;
		if (j > frustum_top)
			frustum_top = j;
		if (k < frustum_near)
			frustum_near = k;
		if (k > frustum_far)
			frustum_far = k;
	}

	private void method281(int i, int j, int k, int l, int i1, int ai[],
			int ai1[], int ai2[], GameModel model, int j1) {
		switch(i1) {
			case 3:
				int k1 = ai1[0] + halfHeight2;
				int k2 = ai1[1] + halfHeight2;
				int k3 = ai1[2] + halfHeight2;
				int k4 = ai[0];
				int l5 = ai[1];
				int j7 = ai[2];
				int l8 = ai2[0];
				int j10 = ai2[1];
				int j11 = ai2[2];
				int j12 = (halfHeight2 + halfHeight) - 1;
				int l12 = 0;
				int j13 = 0;
				int l13 = 0;
				int j14 = 0;
				int l14 = 0xbc614e;
				int j15 = 0xff439eb2;
				if (k3 != k1) {
					j13 = (j7 - k4 << 8) / (k3 - k1);
					j14 = (j11 - l8 << 8) / (k3 - k1);
					if (k1 < k3) {
						l12 = k4 << 8;
						l13 = l8 << 8;
						l14 = k1;
						j15 = k3;
					} else {
						l12 = j7 << 8;
						l13 = j11 << 8;
						l14 = k3;
						j15 = k1;
					}
					if (l14 < 0) {
						l12 -= j13 * l14;
						l13 -= j14 * l14;
						l14 = 0;
					}
					if (j15 > j12)
						j15 = j12;
				}	int l15 = 0;
				int j16 = 0;
				int l16 = 0;
				int j17 = 0;
				int l17 = 0xbc614e;
				int j18 = 0xff439eb2;
				if (k2 != k1) {
					j16 = (l5 - k4 << 8) / (k2 - k1);
					j17 = (j10 - l8 << 8) / (k2 - k1);
					if (k1 < k2) {
						l15 = k4 << 8;
						l16 = l8 << 8;
						l17 = k1;
						j18 = k2;
					} else {
						l15 = l5 << 8;
						l16 = j10 << 8;
						l17 = k2;
						j18 = k1;
					}
					if (l17 < 0) {
						l15 -= j16 * l17;
						l16 -= j17 * l17;
						l17 = 0;
					}
					if (j18 > j12)
						j18 = j12;
				}	int l18 = 0;
				int j19 = 0;
				int l19 = 0;
				int j20 = 0;
				int l20 = 0xbc614e;
				int j21 = 0xff439eb2;
				if (k3 != k2) {
					j19 = (j7 - l5 << 8) / (k3 - k2);
					j20 = (j11 - j10 << 8) / (k3 - k2);
					if (k2 < k3) {
						l18 = l5 << 8;
						l19 = j10 << 8;
						l20 = k2;
						j21 = k3;
					} else {
						l18 = j7 << 8;
						l19 = j11 << 8;
						l20 = k3;
						j21 = k2;
					}
					if (l20 < 0) {
						l18 -= j19 * l20;
						l19 -= j20 * l20;
						l20 = 0;
					}
					if (j21 > j12)
						j21 = j12;
				}	modelLeftY = l14;
				if (l17 < modelLeftY)
					modelLeftY = l17;
				if (l20 < modelLeftY)
					modelLeftY = l20;
				modelRightY = j15;
				if (j18 > modelRightY)
					modelRightY = j18;
				if (j21 > modelRightY)
					modelRightY = j21;
				int l21 = 0;
				for (k = modelLeftY; k < modelRightY; k++) {
					if (k >= l14 && k < j15) {
						i = j = l12;
						l = l21 = l13;
						l12 += j13;
						l13 += j14;
					} else {
						i = 0xa0000;
						j = 0xfff60000;
					}
					if (k >= l17 && k < j18) {
						if (l15 < i) {
							i = l15;
							l = l16;
						}
						if (l15 > j) {
							j = l15;
							l21 = l16;
						}
						l15 += j16;
						l16 += j17;
					}
					if (k >= l20 && k < j21) {
						if (l18 < i) {
							i = l18;
							l = l19;
						}
						if (l18 > j) {
							j = l18;
							l21 = l19;
						}
						l18 += j19;
						l19 += j20;
					}
					CameraVariables cameraVariables_6 = cameraVariables[k];
					cameraVariables_6.leftX = i;
					cameraVariables_6.rightX = j;
					cameraVariables_6.anInt372 = l;
					cameraVariables_6.anInt373 = l21;
				}	if (modelLeftY < halfHeight2 - halfHeight)
					modelLeftY = halfHeight2 - halfHeight;
				break;
			case 4:
				int l1 = ai1[0] + halfHeight2;
				int l2 = ai1[1] + halfHeight2;
				int l3 = ai1[2] + halfHeight2;
				int l4 = ai1[3] + halfHeight2;
				int i6 = ai[0];
				int k7 = ai[1];
				int i9 = ai[2];
				int k10 = ai[3];
				int k11 = ai2[0];
				int k12 = ai2[1];
				int i13 = ai2[2];
				int k13 = ai2[3];
				int i14 = (halfHeight2 + halfHeight) - 1;
				int k14 = 0;
				int i15 = 0;
				int k15 = 0;
				int i16 = 0;
				int k16 = 0xbc614e;
				int i17 = 0xff439eb2;
				if (l4 != l1) {
					i15 = (k10 - i6 << 8) / (l4 - l1);
					i16 = (k13 - k11 << 8) / (l4 - l1);
					if (l1 < l4) {
						k14 = i6 << 8;
						k15 = k11 << 8;
						k16 = l1;
						i17 = l4;
					} else {
						k14 = k10 << 8;
						k15 = k13 << 8;
						k16 = l4;
						i17 = l1;
					}
					if (k16 < 0) {
						k14 -= i15 * k16;
						k15 -= i16 * k16;
						k16 = 0;
					}
					if (i17 > i14)
						i17 = i14;
				}	int k17 = 0;
				int i18 = 0;
				int k18 = 0;
				int i19 = 0;
				int k19 = 0xbc614e;
				int i20 = 0xff439eb2;
				if (l2 != l1) {
					i18 = (k7 - i6 << 8) / (l2 - l1);
					i19 = (k12 - k11 << 8) / (l2 - l1);
					if (l1 < l2) {
						k17 = i6 << 8;
						k18 = k11 << 8;
						k19 = l1;
						i20 = l2;
					} else {
						k17 = k7 << 8;
						k18 = k12 << 8;
						k19 = l2;
						i20 = l1;
					}
					if (k19 < 0) {
						k17 -= i18 * k19;
						k18 -= i19 * k19;
						k19 = 0;
					}
					if (i20 > i14)
						i20 = i14;
				}	int k20 = 0;
				int i21 = 0;
				int k21 = 0;
				int i22 = 0;
				int j22 = 0xbc614e;
				int k22 = 0xff439eb2;
				if (l3 != l2) {
					i21 = (i9 - k7 << 8) / (l3 - l2);
					i22 = (i13 - k12 << 8) / (l3 - l2);
					if (l2 < l3) {
						k20 = k7 << 8;
						k21 = k12 << 8;
						j22 = l2;
						k22 = l3;
					} else {
						k20 = i9 << 8;
						k21 = i13 << 8;
						j22 = l3;
						k22 = l2;
					}
					if (j22 < 0) {
						k20 -= i21 * j22;
						k21 -= i22 * j22;
						j22 = 0;
					}
					if (k22 > i14)
						k22 = i14;
				}	int l22 = 0;
				int i23 = 0;
				int j23 = 0;
				int k23 = 0;
				int l23 = 0xbc614e;
				int i24 = 0xff439eb2;
				if (l4 != l3) {
					i23 = (k10 - i9 << 8) / (l4 - l3);
					k23 = (k13 - i13 << 8) / (l4 - l3);
					if (l3 < l4) {
						l22 = i9 << 8;
						j23 = i13 << 8;
						l23 = l3;
						i24 = l4;
					} else {
						l22 = k10 << 8;
						j23 = k13 << 8;
						l23 = l4;
						i24 = l3;
					}
					if (l23 < 0) {
						l22 -= i23 * l23;
						j23 -= k23 * l23;
						l23 = 0;
					}
					if (i24 > i14)
						i24 = i14;
				}	modelLeftY = k16;
				if (k19 < modelLeftY)
					modelLeftY = k19;
				if (j22 < modelLeftY)
					modelLeftY = j22;
				if (l23 < modelLeftY)
					modelLeftY = l23;
				modelRightY = i17;
				if (i20 > modelRightY)
					modelRightY = i20;
				if (k22 > modelRightY)
					modelRightY = k22;
				if (i24 > modelRightY)
					modelRightY = i24;
				int j24 = 0;
				for (k = modelLeftY; k < modelRightY; k++) {
					if (k >= k16 && k < i17) {
						i = j = k14;
						l = j24 = k15;
						k14 += i15;
						k15 += i16;
					} else {
						i = 0xa0000;
						j = 0xfff60000;
					}
					if (k >= k19 && k < i20) {
						if (k17 < i) {
							i = k17;
							l = k18;
						}
						if (k17 > j) {
							j = k17;
							j24 = k18;
						}
						k17 += i18;
						k18 += i19;
					}
					if (k >= j22 && k < k22) {
						if (k20 < i) {
							i = k20;
							l = k21;
						}
						if (k20 > j) {
							j = k20;
							j24 = k21;
						}
						k20 += i21;
						k21 += i22;
					}
					if (k >= l23 && k < i24) {
						if (l22 < i) {
							i = l22;
							l = j23;
						}
						if (l22 > j) {
							j = l22;
							j24 = j23;
						}
						l22 += i23;
						j23 += k23;
					}
					CameraVariables cameraVariables_7 = cameraVariables[k];
					cameraVariables_7.leftX = i;
					cameraVariables_7.rightX = j;
					cameraVariables_7.anInt372 = l;
					cameraVariables_7.anInt373 = j24;
				}	if (modelLeftY < halfHeight2 - halfHeight)
					modelLeftY = halfHeight2 - halfHeight;
				break;
			default:
				modelRightY = modelLeftY = ai1[0] += halfHeight2;
				for (k = 1; k < i1; k++) {
					int i2;
					if ((i2 = ai1[k] += halfHeight2) < modelLeftY)
						modelLeftY = i2;
					else if (i2 > modelRightY)
						modelRightY = i2;
				}	if (modelLeftY < halfHeight2 - halfHeight)
					modelLeftY = halfHeight2 - halfHeight;
				if (modelRightY >= halfHeight2 + halfHeight)
					modelRightY = (halfHeight2 + halfHeight) - 1;
				if (modelLeftY >= modelRightY)
					return;
				for (k = modelLeftY; k < modelRightY; k++) {
					CameraVariables camVariables = this.cameraVariables[k];
					camVariables.leftX = 0xa0000;
					camVariables.rightX = 0xfff60000;
				}	int j2 = i1 - 1;
				int i3 = ai1[0];
				int i4 = ai1[j2];
				if (i3 < i4) {
					int i5 = ai[0] << 8;
					int j6 = (ai[j2] - ai[0] << 8) / (i4 - i3);
					int l7 = ai2[0] << 8;
					int j9 = (ai2[j2] - ai2[0] << 8) / (i4 - i3);
					if (i3 < 0) {
						i5 -= j6 * i3;
						l7 -= j9 * i3;
						i3 = 0;
					}
					if (i4 > modelRightY)
						i4 = modelRightY;
					for (k = i3; k <= i4; k++) {
						CameraVariables cameraVariables_2 = cameraVariables[k];
						cameraVariables_2.leftX = cameraVariables_2.rightX = i5;
						cameraVariables_2.anInt372 = cameraVariables_2.anInt373 = l7;
						i5 += j6;
						l7 += j9;
					}
					
				} else if (i3 > i4) {
					int j5 = ai[j2] << 8;
					int k6 = (ai[0] - ai[j2] << 8) / (i3 - i4);
					int i8 = ai2[j2] << 8;
					int k9 = (ai2[0] - ai2[j2] << 8) / (i3 - i4);
					if (i4 < 0) {
						j5 -= k6 * i4;
						i8 -= k9 * i4;
						i4 = 0;
					}
					if (i3 > modelRightY)
						i3 = modelRightY;
					for (k = i4; k <= i3; k++) {
						CameraVariables cameraVariables_3 = cameraVariables[k];
						cameraVariables_3.leftX = cameraVariables_3.rightX = j5;
						cameraVariables_3.anInt372 = cameraVariables_3.anInt373 = i8;
						j5 += k6;
						i8 += k9;
					}
					
				}	for (k = 0; k < j2; k++) {
					int k5 = k + 1;
					int j3 = ai1[k];
					int j4 = ai1[k5];
					if (j3 < j4) {
						int l6 = ai[k] << 8;
						int j8 = (ai[k5] - ai[k] << 8) / (j4 - j3);
						int l9 = ai2[k] << 8;
						int l10 = (ai2[k5] - ai2[k] << 8) / (j4 - j3);
						if (j3 < 0) {
							l6 -= j8 * j3;
							l9 -= l10 * j3;
							j3 = 0;
						}
						if (j4 > modelRightY)
							j4 = modelRightY;
						for (int l11 = j3; l11 <= j4; l11++) {
							CameraVariables cameraVariables_4 = cameraVariables[l11];
							if (l6 < cameraVariables_4.leftX) {
								cameraVariables_4.leftX = l6;
								cameraVariables_4.anInt372 = l9;
							}
							if (l6 > cameraVariables_4.rightX) {
								cameraVariables_4.rightX = l6;
								cameraVariables_4.anInt373 = l9;
							}
							l6 += j8;
							l9 += l10;
						}
						
					} else if (j3 > j4) {
						int i7 = ai[k5] << 8;
						int k8 = (ai[k] - ai[k5] << 8) / (j3 - j4);
						int i10 = ai2[k5] << 8;
						int i11 = (ai2[k] - ai2[k5] << 8) / (j3 - j4);
						if (j4 < 0) {
							i7 -= k8 * j4;
							i10 -= i11 * j4;
							j4 = 0;
						}
						if (j3 > modelRightY)
							j3 = modelRightY;
						for (int i12 = j4; i12 <= j3; i12++) {
							CameraVariables cameraVariables_5 = cameraVariables[i12];
							if (i7 < cameraVariables_5.leftX) {
								cameraVariables_5.leftX = i7;
								cameraVariables_5.anInt372 = i10;
							}
							if (i7 > cameraVariables_5.rightX) {
								cameraVariables_5.rightX = i7;
								cameraVariables_5.anInt373 = i10;
							}
							i7 += k8;
							i10 += i11;
						}
						
					}
				}	if (modelLeftY < halfHeight2 - halfHeight)
					modelLeftY = halfHeight2 - halfHeight;
				break;
		}
		if (aBoolean389 && currentVisibleModelCount < maxVisibleModelCount
				&& mouseY >= modelLeftY && mouseY < modelRightY) {
			CameraVariables cameraVariables_1 = cameraVariables[mouseY];
			if (mouseX >= cameraVariables_1.leftX >> 8
					&& mouseX <= cameraVariables_1.rightX >> 8
					&& cameraVariables_1.leftX <= cameraVariables_1.rightX
					&& !model.pickable && model.faceSpriteType[j1] == 0) {
				visibleModels[currentVisibleModelCount] = model;
				visibleModelIndeces[currentVisibleModelCount] = j1;
				currentVisibleModelCount++;
			}
		}
	}

	private void method282(int i, int j, int k, int ai[], int ai1[], int ai2[],
			int l, GameModel model) {
		if (l == -2)
			return;
		if (l >= 0) {
			if (l >= anInt424)
				l = 0;
			method299(l);
			int i1 = ai[0];
			int k1 = ai1[0];
			int j2 = ai2[0];
			int i3 = i1 - ai[1];
			int k3 = k1 - ai1[1];
			int i4 = j2 - ai2[1];
			k--;
			int i6 = ai[k] - i1;
			int j7 = ai1[k] - k1;
			int k8 = ai2[k] - j2;
			if (anIntArray427[l] == 1) {
				int l9 = i6 * k1 - j7 * i1 << 12;
				int k10 = j7 * j2 - k8 * k1 << (5 - viewDistance) + 7 + 4;
				int i11 = k8 * i1 - i6 * j2 << (5 - viewDistance) + 7;
				int k11 = i3 * k1 - k3 * i1 << 12;
				int i12 = k3 * j2 - i4 * k1 << (5 - viewDistance) + 7 + 4;
				int k12 = i4 * i1 - i3 * j2 << (5 - viewDistance) + 7;
				int i13 = k3 * i6 - i3 * j7 << 5;
				int k13 = i4 * j7 - k3 * k8 << (5 - viewDistance) + 4;
				int i14 = i3 * k8 - i4 * i6 >> viewDistance - 5;
				int k14 = k10 >> 4;
				int i15 = i12 >> 4;
				int k15 = k13 >> 4;
				int i16 = modelLeftY - halfHeight2;
				int k16 = width;
				int i17 = halfWidth2 + modelLeftY * k16;
				byte byte1 = 1;
				l9 += i11 * i16;
				k11 += k12 * i16;
				i13 += i14 * i16;
				if (interlace) {
					if ((modelLeftY & 1) == 1) {
						modelLeftY++;
						l9 += i11;
						k11 += k12;
						i13 += i14;
						i17 += k16;
					}
					i11 <<= 1;
					k12 <<= 1;
					i14 <<= 1;
					k16 <<= 1;
					byte1 = 2;
				}
				if (model.aSceneBoolean) {
					for (i = modelLeftY; i < modelRightY; i += byte1) {
						CameraVariables cameraVariables_3 = cameraVariables[i];
						j = cameraVariables_3.leftX >> 8;
						int k17 = cameraVariables_3.rightX >> 8;
						int k20 = k17 - j;
						if (k20 <= 0) {
							l9 += i11;
							k11 += k12;
							i13 += i14;
							i17 += k16;
						} else {
							int i22 = cameraVariables_3.anInt372;
							int k23 = (cameraVariables_3.anInt373 - i22) / k20;
							if (j < -halfWidth) {
								i22 += (-halfWidth - j) * k23;
								j = -halfWidth;
								k20 = k17 - j;
							}
							if (k17 > halfWidth) {
								int l17 = halfWidth;
								k20 = l17 - j;
							}
							method284(anIntArray437, anIntArrayArray429[l], 0,
									0, l9 + k14 * j, k11 + i15 * j, i13 + k15
											* j, k10, i12, k13, k20, i17 + j,
									i22, k23 << 2);
							l9 += i11;
							k11 += k12;
							i13 += i14;
							i17 += k16;
						}
					}

					return;
				}
				// render walls
				if (!aBooleanArray430[l]) {
					for (i = modelLeftY; i < modelRightY; i += byte1) {
						CameraVariables cameraVariables_4 = cameraVariables[i];
						j = cameraVariables_4.leftX >> 8;
						int i18 = cameraVariables_4.rightX >> 8;
						int l20 = i18 - j;
						if (l20 <= 0) {
							l9 += i11;
							k11 += k12;
							i13 += i14;
							i17 += k16;
						} else {
							int j22 = cameraVariables_4.anInt372;
							int l23 = (cameraVariables_4.anInt373 - j22) / l20;
							if (j < -halfWidth) {
								j22 += (-halfWidth - j) * l23;
								j = -halfWidth;
								l20 = i18 - j;
							}
							if (i18 > halfWidth) {
								int j18 = halfWidth;
								l20 = j18 - j;
							}
							method283(anIntArray437, anIntArrayArray429[l], 0,
									0, l9 + k14 * j, k11 + i15 * j, i13 + k15
											* j, k10, i12, k13, l20, i17 + j,
									j22, l23 << 2);
							l9 += i11;
							k11 += k12;
							i13 += i14;
							i17 += k16;
						}
					}

					return;
				}
				for (i = modelLeftY; i < modelRightY; i += byte1) {
					CameraVariables cameraVariables_5 = cameraVariables[i];
					j = cameraVariables_5.leftX >> 8;
					int k18 = cameraVariables_5.rightX >> 8;
					int i21 = k18 - j;
					if (i21 <= 0) {
						l9 += i11;
						k11 += k12;
						i13 += i14;
						i17 += k16;
					} else {
						int k22 = cameraVariables_5.anInt372;
						int i24 = (cameraVariables_5.anInt373 - k22) / i21;
						if (j < -halfWidth) {
							k22 += (-halfWidth - j) * i24;
							j = -halfWidth;
							i21 = k18 - j;
						}
						if (k18 > halfWidth) {
							int l18 = halfWidth;
							i21 = l18 - j;
						}
						method285(anIntArray437, 0, 0, 0,
								anIntArrayArray429[l], l9 + k14 * j, k11 + i15
										* j, i13 + k15 * j, k10, i12, k13, i21,
								i17 + j, k22, i24);
						l9 += i11;
						k11 += k12;
						i13 += i14;
						i17 += k16;
					}
				}

				return;
			}
			int i10 = i6 * k1 - j7 * i1 << 11;
			int l10 = j7 * j2 - k8 * k1 << (5 - viewDistance) + 6 + 4;
			int j11 = k8 * i1 - i6 * j2 << (5 - viewDistance) + 6;
			int l11 = i3 * k1 - k3 * i1 << 11;
			int j12 = k3 * j2 - i4 * k1 << (5 - viewDistance) + 6 + 4;
			int l12 = i4 * i1 - i3 * j2 << (5 - viewDistance) + 6;
			int j13 = k3 * i6 - i3 * j7 << 5;
			int l13 = i4 * j7 - k3 * k8 << (5 - viewDistance) + 4;
			int j14 = i3 * k8 - i4 * i6 >> viewDistance - 5;
			int l14 = l10 >> 4;
			int j15 = j12 >> 4;
			int l15 = l13 >> 4;
			int j16 = modelLeftY - halfHeight2;
			int l16 = width;
			int j17 = halfWidth2 + modelLeftY * l16;
			byte byte2 = 1;
			i10 += j11 * j16;
			l11 += l12 * j16;
			j13 += j14 * j16;
			if (interlace) {
				if ((modelLeftY & 1) == 1) {
					modelLeftY++;
					i10 += j11;
					l11 += l12;
					j13 += j14;
					j17 += l16;
				}
				j11 <<= 1;
				l12 <<= 1;
				j14 <<= 1;
				l16 <<= 1;
				byte2 = 2;
			}
			if (model.aSceneBoolean) {
				for (i = modelLeftY; i < modelRightY; i += byte2) {
					CameraVariables cameraVariables_6 = cameraVariables[i];
					j = cameraVariables_6.leftX >> 8;
					int i19 = cameraVariables_6.rightX >> 8;
					int j21 = i19 - j;
					if (j21 <= 0) {
						i10 += j11;
						l11 += l12;
						j13 += j14;
						j17 += l16;
					} else {
						int l22 = cameraVariables_6.anInt372;
						int j24 = (cameraVariables_6.anInt373 - l22) / j21;
						if (j < -halfWidth) {
							l22 += (-halfWidth - j) * j24;
							j = -halfWidth;
							j21 = i19 - j;
						}
						if (i19 > halfWidth) {
							int j19 = halfWidth;
							j21 = j19 - j;
						}
						method287(anIntArray437, anIntArrayArray429[l], 0, 0,
								i10 + l14 * j, l11 + j15 * j, j13 + l15 * j,
								l10, j12, l13, j21, j17 + j, l22, j24);
						i10 += j11;
						l11 += l12;
						j13 += j14;
						j17 += l16;
					}
				}

				return;
			}
			if (!aBooleanArray430[l]) {
				for (i = modelLeftY; i < modelRightY; i += byte2) {
					CameraVariables cameraVariables_7 = cameraVariables[i];
					j = cameraVariables_7.leftX >> 8;
					int k19 = cameraVariables_7.rightX >> 8;
					int k21 = k19 - j;
					if (k21 <= 0) {
						i10 += j11;
						l11 += l12;
						j13 += j14;
						j17 += l16;
					} else {
						int i23 = cameraVariables_7.anInt372;
						int k24 = (cameraVariables_7.anInt373 - i23) / k21;
						if (j < -halfWidth) {
							i23 += (-halfWidth - j) * k24;
							j = -halfWidth;
							k21 = k19 - j;
						}
						if (k19 > halfWidth) {
							int l19 = halfWidth;
							k21 = l19 - j;
						}
						method286(anIntArray437, anIntArrayArray429[l], 0, 0,
								i10 + l14 * j, l11 + j15 * j, j13 + l15 * j,
								l10, j12, l13, k21, j17 + j, i23, k24);
						i10 += j11;
						l11 += l12;
						j13 += j14;
						j17 += l16;
					}
				}

				return;
			}
			for (i = modelLeftY; i < modelRightY; i += byte2) {
				CameraVariables cameraVariables_8 = cameraVariables[i];
				j = cameraVariables_8.leftX >> 8;
				int i20 = cameraVariables_8.rightX >> 8;
				int l21 = i20 - j;
				if (l21 <= 0) {
					i10 += j11;
					l11 += l12;
					j13 += j14;
					j17 += l16;
				} else {
					int j23 = cameraVariables_8.anInt372;
					int l24 = (cameraVariables_8.anInt373 - j23) / l21;
					if (j < -halfWidth) {
						j23 += (-halfWidth - j) * l24;
						j = -halfWidth;
						l21 = i20 - j;
					}
					if (i20 > halfWidth) {
						int j20 = halfWidth;
						l21 = j20 - j;
					}
					method288(anIntArray437, 0, 0, 0, anIntArrayArray429[l],
							i10 + l14 * j, l11 + j15 * j, j13 + l15 * j, l10,
							j12, l13, l21, j17 + j, j23, l24);
					i10 += j11;
					l11 += l12;
					j13 += j14;
					j17 += l16;
				}
			}

			return;
		}
		for (int j1 = 0; j1 < anInt374; j1++) {
			if (anIntArray375[j1] == l) {
				anIntArray377 = anIntArrayArray376[j1];
				break;
			}
			if (j1 == anInt374 - 1) {
				int l1 = (int) (Math.random() * anInt374);
				anIntArray375[l1] = l;
				l = -1 - l;
				int k2 = (l >> 10 & 0x1f) * 8;
				int j3 = (l >> 5 & 0x1f) * 8;
				int l3 = (l & 0x1f) * 8;
				for (int j4 = 0; j4 < 256; j4++) {
					int j6 = j4 * j4;
					int k7 = (k2 * j6) / 0x10000;
					int l8 = (j3 * j6) / 0x10000;
					int j10 = (l3 * j6) / 0x10000;
					anIntArrayArray376[l1][255 - j4] = (k7 << 16) + (l8 << 8)
							+ j10;
				}

				anIntArray377 = anIntArrayArray376[l1];
			}
		}

		int i2 = width;
		int l2 = halfWidth2 + modelLeftY * i2;
		byte byte0 = 1;
		if (interlace) {
			if ((modelLeftY & 1) == 1) {
				modelLeftY++;
				l2 += i2;
			}
			i2 <<= 1;
			byte0 = 2;
		}
		if (model.transparent) {
			for (i = modelLeftY; i < modelRightY; i += byte0) {
				CameraVariables camVars = this.cameraVariables[i];
				j = camVars.leftX >> 8;
				int k4 = camVars.rightX >> 8;
				int k6 = k4 - j;
				if (k6 <= 0) {
					l2 += i2;
				} else {
					int l7 = camVars.anInt372;
					int i9 = (camVars.anInt373 - l7) / k6;
					if (j < -halfWidth) {
						l7 += (-halfWidth - j) * i9;
						j = -halfWidth;
						k6 = k4 - j;
					}
					if (k4 > halfWidth) {
						int l4 = halfWidth;
						k6 = l4 - j;
					}
					method290(anIntArray437, -k6, l2 + j, 0, anIntArray377, l7,
							i9);
					l2 += i2;
				}
			}

			return;
		}
		if (aBoolean386) {
			for (i = modelLeftY; i < modelRightY; i += byte0) {
				CameraVariables cameraVariables_1 = cameraVariables[i];
				j = cameraVariables_1.leftX >> 8;
				int i5 = cameraVariables_1.rightX >> 8;
				int l6 = i5 - j;
				if (l6 <= 0) {
					l2 += i2;
				} else {
					int i8 = cameraVariables_1.anInt372;
					int j9 = (cameraVariables_1.anInt373 - i8) / l6;
					if (j < -halfWidth) {
						i8 += (-halfWidth - j) * j9;
						j = -halfWidth;
						l6 = i5 - j;
					}
					if (i5 > halfWidth) {
						int j5 = halfWidth;
						l6 = j5 - j;
					}
					method289(anIntArray437, -l6, l2 + j, 0, anIntArray377, i8,
							j9);
					l2 += i2;
				}
			}

			return;
		}
		// Render color tiles
		for (i = modelLeftY; i < modelRightY; i += byte0) {
			CameraVariables cameraVariables_2 = cameraVariables[i];
			j = cameraVariables_2.leftX >> 8;
			int k5 = cameraVariables_2.rightX >> 8;
			int i7 = k5 - j;
			if (i7 <= 0) {
				l2 += i2;
			} else {
				int j8 = cameraVariables_2.anInt372;
				int k9 = (cameraVariables_2.anInt373 - j8) / i7;
				if (j < -halfWidth) {
					j8 += (-halfWidth - j) * k9;
					j = -halfWidth;
					i7 = k5 - j;
				}
				if (k5 > halfWidth) {
					int l5 = halfWidth;
					i7 = l5 - j;
				}
				method291(anIntArray437, -i7, l2 + j, 0, anIntArray377, j8, k9);
				l2 += i2;
			}
		}

	}

	private void method293(int i) {
		Polygon cameraModel = polygons[i];
		GameModel model = cameraModel.aModel_359;
		int j = cameraModel.anInt360;
		int ai[] = model.faceVertices[j];
		int k = model.faceVerticeCount[j];
		int l = model.faceCameraNormalScale[j];
		int j1 = model.vertexCameraX[ai[0]];
		int k1 = model.vertexCameraY[ai[0]];
		int l1 = model.vertexCameraZ[ai[0]];
		int i2 = model.vertexCameraX[ai[1]] - j1;
		int j2 = model.vertexCameraY[ai[1]] - k1;
		int k2 = model.vertexCameraZ[ai[1]] - l1;
		int l2 = model.vertexCameraX[ai[2]] - j1;
		int i3 = model.vertexCameraY[ai[2]] - k1;
		int j3 = model.vertexCameraZ[ai[2]] - l1;
		int k3 = j2 * j3 - i3 * k2;
		int l3 = k2 * l2 - j3 * i2;
		int i4 = i2 * i3 - l2 * j2;
		if (l == -1) {
			l = 0;
			for (; k3 > 25000 || l3 > 25000 || i4 > 25000 || k3 < -25000
					|| l3 < -25000 || i4 < -25000; i4 >>= 1) {
				l++;
				k3 >>= 1;
				l3 >>= 1;
			}

			model.faceCameraNormalScale[j] = l;
			model.faceCameraNormalMagnitude[j] = (int) (anInt402 * Math.sqrt(k3
					* k3 + l3 * l3 + i4 * i4));
		} else {
			k3 >>= l;
			l3 >>= l;
			i4 >>= l;
		}
		cameraModel.anInt365 = j1 * k3 + k1 * l3 + l1 * i4;
		cameraModel.anInt362 = k3;
		cameraModel.anInt363 = l3;
		cameraModel.anInt364 = i4;
		int j4 = model.vertexCameraZ[ai[0]];
		int k4 = j4;
		int l4 = model.vertexViewX[ai[0]];
		int i5 = l4;
		int j5 = model.vertexViewY[ai[0]];
		int k5 = j5;
		for (int l5 = 1; l5 < k; l5++) {
			int i1 = model.vertexCameraZ[ai[l5]];
			if (i1 > k4)
				k4 = i1;
			else if (i1 < j4)
				j4 = i1;
			i1 = model.vertexViewX[ai[l5]];
			if (i1 > i5)
				i5 = i1;
			else if (i1 < l4)
				l4 = i1;
			i1 = model.vertexViewY[ai[l5]];
			if (i1 > k5)
				k5 = i1;
			else if (i1 < j5)
				j5 = i1;
		}

		cameraModel.anInt357 = j4;
		cameraModel.anInt358 = k4;
		cameraModel.anInt353 = l4;
		cameraModel.anInt355 = i5;
		cameraModel.anInt354 = j5;
		cameraModel.anInt356 = k5;
	}

	private void method294(int i) {
		Polygon cameraModel = polygons[i];
		GameModel model = cameraModel.aModel_359;
		int j = cameraModel.anInt360;
		int ai[] = model.faceVertices[j];
		int l = 0;
		int i1 = 0;
		int j1 = 1;
		int k1 = model.vertexCameraX[ai[0]];
		int l1 = model.vertexCameraY[ai[0]];
		int i2 = model.vertexCameraZ[ai[0]];
		model.faceCameraNormalMagnitude[j] = 1;
		model.faceCameraNormalScale[j] = 0;
		cameraModel.anInt365 = k1 * l + l1 * i1 + i2 * j1;
		cameraModel.anInt362 = l;
		cameraModel.anInt363 = i1;
		cameraModel.anInt364 = j1;
		int j2 = model.vertexCameraZ[ai[0]];
		int k2 = j2;
		int l2 = model.vertexViewX[ai[0]];
		int i3 = l2;
		if (model.vertexViewX[ai[1]] < l2)
			l2 = model.vertexViewX[ai[1]];
		else
			i3 = model.vertexViewX[ai[1]];
		int j3 = model.vertexViewY[ai[1]];
		int k3 = model.vertexViewY[ai[0]];
		int k = model.vertexCameraZ[ai[1]];
		if (k > k2)
			k2 = k;
		else if (k < j2)
			j2 = k;
		k = model.vertexViewX[ai[1]];
		if (k > i3)
			i3 = k;
		else if (k < l2)
			l2 = k;
		k = model.vertexViewY[ai[1]];
		if (k > k3)
			k3 = k;
		else if (k < j3)
			j3 = k;
		cameraModel.anInt357 = j2;
		cameraModel.anInt358 = k2;
		cameraModel.anInt353 = l2 - 20;
		cameraModel.anInt355 = i3 + 20;
		cameraModel.anInt354 = j3;
		cameraModel.anInt356 = k3;
	}

	private boolean method295(Polygon cameraModel, Polygon cameraModel_1) {
		if (cameraModel.anInt353 >= cameraModel_1.anInt355)
			return true;
		if (cameraModel_1.anInt353 >= cameraModel.anInt355)
			return true;
		if (cameraModel.anInt354 >= cameraModel_1.anInt356)
			return true;
		if (cameraModel_1.anInt354 >= cameraModel.anInt356)
			return true;
		if (cameraModel.anInt357 >= cameraModel_1.anInt358)
			return true;
		if (cameraModel_1.anInt357 > cameraModel.anInt358)
			return false;
		GameModel model = cameraModel.aModel_359;
		GameModel model_1 = cameraModel_1.aModel_359;
		int i = cameraModel.anInt360;
		int j = cameraModel_1.anInt360;
		int ai[] = model.faceVertices[i];
		int ai1[] = model_1.faceVertices[j];
		int k = model.faceVerticeCount[i];
		int l = model_1.faceVerticeCount[j];
		int k2 = model_1.vertexCameraX[ai1[0]];
		int l2 = model_1.vertexCameraY[ai1[0]];
		int i3 = model_1.vertexCameraZ[ai1[0]];
		int j3 = cameraModel_1.anInt362;
		int k3 = cameraModel_1.anInt363;
		int l3 = cameraModel_1.anInt364;
		int i4 = model_1.faceCameraNormalMagnitude[j];
		int j4 = cameraModel_1.anInt365;
		boolean flag = false;
		for (int k4 = 0; k4 < k; k4++) {
			int i1 = ai[k4];
			int i2 = (k2 - model.vertexCameraX[i1]) * j3
					+ (l2 - model.vertexCameraY[i1]) * k3
					+ (i3 - model.vertexCameraZ[i1]) * l3;
			if ((i2 >= -i4 || j4 >= 0) && (i2 <= i4 || j4 <= 0))
				continue;
			flag = true;
			break;
		}

		if (!flag)
			return true;
		k2 = model.vertexCameraX[ai[0]];
		l2 = model.vertexCameraY[ai[0]];
		i3 = model.vertexCameraZ[ai[0]];
		j3 = cameraModel.anInt362;
		k3 = cameraModel.anInt363;
		l3 = cameraModel.anInt364;
		i4 = model.faceCameraNormalMagnitude[i];
		j4 = cameraModel.anInt365;
		flag = false;
		for (int l4 = 0; l4 < l; l4++) {
			int j1 = ai1[l4];
			int j2 = (k2 - model_1.vertexCameraX[j1]) * j3
					+ (l2 - model_1.vertexCameraY[j1]) * k3
					+ (i3 - model_1.vertexCameraZ[j1]) * l3;
			if ((j2 >= -i4 || j4 <= 0) && (j2 <= i4 || j4 >= 0))
				continue;
			flag = true;
			break;
		}

		if (!flag)
			return true;
		int ai2[];
		int ai3[];
		if (k == 2) {
			ai2 = new int[4];
			ai3 = new int[4];
			int i5 = ai[0];
			int k1 = ai[1];
			ai2[0] = model.vertexViewX[i5] - 20;
			ai2[1] = model.vertexViewX[k1] - 20;
			ai2[2] = model.vertexViewX[k1] + 20;
			ai2[3] = model.vertexViewX[i5] + 20;
			ai3[0] = ai3[3] = model.vertexViewY[i5];
			ai3[1] = ai3[2] = model.vertexViewY[k1];
		} else {
			ai2 = new int[k];
			ai3 = new int[k];
			for (int j5 = 0; j5 < k; j5++) {
				int i6 = ai[j5];
				ai2[j5] = model.vertexViewX[i6];
				ai3[j5] = model.vertexViewY[i6];
			}

		}
		int ai4[];
		int ai5[];
		if (l == 2) {
			ai4 = new int[4];
			ai5 = new int[4];
			int k5 = ai1[0];
			int l1 = ai1[1];
			ai4[0] = model_1.vertexViewX[k5] - 20;
			ai4[1] = model_1.vertexViewX[l1] - 20;
			ai4[2] = model_1.vertexViewX[l1] + 20;
			ai4[3] = model_1.vertexViewX[k5] + 20;
			ai5[0] = ai5[3] = model_1.vertexViewY[k5];
			ai5[1] = ai5[2] = model_1.vertexViewY[l1];
		} else {
			ai4 = new int[l];
			ai5 = new int[l];
			for (int l5 = 0; l5 < l; l5++) {
				int j6 = ai1[l5];
				ai4[l5] = model_1.vertexViewX[j6];
				ai5[l5] = model_1.vertexViewY[j6];
			}

		}
		return !method309(ai2, ai3, ai4, ai5);
	}

	private boolean method296(Polygon cameraModel, Polygon cameraModel_1) {
		GameModel model = cameraModel.aModel_359;
		GameModel model_1 = cameraModel_1.aModel_359;
		int i = cameraModel.anInt360;
		int j = cameraModel_1.anInt360;
		int ai[] = model.faceVertices[i];
		int ai1[] = model_1.faceVertices[j];
		int k = model.faceVerticeCount[i];
		int l = model_1.faceVerticeCount[j];
		int i2 = model_1.vertexCameraX[ai1[0]];
		int j2 = model_1.vertexCameraY[ai1[0]];
		int k2 = model_1.vertexCameraZ[ai1[0]];
		int l2 = cameraModel_1.anInt362;
		int i3 = cameraModel_1.anInt363;
		int j3 = cameraModel_1.anInt364;
		int k3 = model_1.faceCameraNormalMagnitude[j];
		int l3 = cameraModel_1.anInt365;
		boolean flag = false;
		for (int i4 = 0; i4 < k; i4++) {
			int i1 = ai[i4];
			int k1 = (i2 - model.vertexCameraX[i1]) * l2
					+ (j2 - model.vertexCameraY[i1]) * i3
					+ (k2 - model.vertexCameraZ[i1]) * j3;
			if ((k1 >= -k3 || l3 >= 0) && (k1 <= k3 || l3 <= 0))
				continue;
			flag = true;
			break;
		}

		if (!flag)
			return true;
		i2 = model.vertexCameraX[ai[0]];
		j2 = model.vertexCameraY[ai[0]];
		k2 = model.vertexCameraZ[ai[0]];
		l2 = cameraModel.anInt362;
		i3 = cameraModel.anInt363;
		j3 = cameraModel.anInt364;
		k3 = model.faceCameraNormalMagnitude[i];
		l3 = cameraModel.anInt365;
		flag = false;
		for (int j4 = 0; j4 < l; j4++) {
			int j1 = ai1[j4];
			int l1 = (i2 - model_1.vertexCameraX[j1]) * l2
					+ (j2 - model_1.vertexCameraY[j1]) * i3
					+ (k2 - model_1.vertexCameraZ[j1]) * j3;
			if ((l1 >= -k3 || l3 <= 0) && (l1 <= k3 || l3 >= 0))
				continue;
			flag = true;
			break;
		}

		return !flag;
	}

	public void method297(int i, int j, int k) {
		anInt424 = i;
		aByteArrayArray425 = new byte[i][];
		anIntArrayArray426 = new int[i][];
		anIntArray427 = new int[i];
		aLongArray428 = new long[i];
		aBooleanArray430 = new boolean[i];
		anIntArrayArray429 = new int[i][];
		aLong431 = 0L;
		anIntArrayArray432 = new int[j][];
		anIntArrayArray433 = new int[k][];
	}

	public void method298(int i, byte abyte0[], int ai[], int j) {
		aByteArrayArray425[i] = abyte0;
		anIntArrayArray426[i] = ai;
		anIntArray427[i] = j;
		aLongArray428[i] = 0L;
		aBooleanArray430[i] = false;
		anIntArrayArray429[i] = null;
		method299(i);
	}

	public void method299(int i) {
		if (i < 0)
			return;
		aLongArray428[i] = aLong431++;
		if (anIntArrayArray429[i] != null)
			return;
		if (anIntArray427[i] == 0) {
			for (int j = 0; j < anIntArrayArray432.length; j++)
				if (anIntArrayArray432[j] == null) {
					anIntArrayArray432[j] = new int[16384];
					anIntArrayArray429[i] = anIntArrayArray432[j];
					method300(i);
					return;
				}

			long l = 1L << 30;
			int i1 = 0;
			for (int k1 = 0; k1 < anInt424; k1++)
				if (k1 != i && anIntArray427[k1] == 0
						&& anIntArrayArray429[k1] != null
						&& aLongArray428[k1] < l) {
					l = aLongArray428[k1];
					i1 = k1;
				}

			anIntArrayArray429[i] = anIntArrayArray429[i1];
			anIntArrayArray429[i1] = null;
			method300(i);
			return;
		}
		for (int k = 0; k < anIntArrayArray433.length; k++)
			if (anIntArrayArray433[k] == null) {
				anIntArrayArray433[k] = new int[0x10000];
				anIntArrayArray429[i] = anIntArrayArray433[k];
				method300(i);
				return;
			}

		long l1 = 1L << 30;
		int j1 = 0;
		for (int i2 = 0; i2 < anInt424; i2++)
			if (i2 != i && anIntArray427[i2] == 1
					&& anIntArrayArray429[i2] != null && aLongArray428[i2] < l1) {
				l1 = aLongArray428[i2];
				j1 = i2;
			}

		anIntArrayArray429[i] = anIntArrayArray429[j1];
		anIntArrayArray429[j1] = null;
		method300(i);
	}

	private void method300(int i) {
		char c;
		if (anIntArray427[i] == 0)
			c = '@';
		else
			c = '\200';
		int ai[] = anIntArrayArray429[i];
		int j = 0;
		for (int k = 0; k < c; k++) {
			for (int l = 0; l < c; l++) {
				int j1 = anIntArrayArray426[i][aByteArrayArray425[i][l + k * c] & 0xff];
				j1 &= 0xf8f8ff;
				if (j1 == 0)
					j1 = 1;
				else if (j1 == 0xf800ff) {
					j1 = 0;
					aBooleanArray430[i] = true;
				}
				ai[j++] = j1;
			}

		}

		for (int i1 = 0; i1 < j; i1++) {
			int k1 = ai[i1];
			ai[j + i1] = k1 - (k1 >>> 3) & 0xf8f8ff;
			ai[j * 2 + i1] = k1 - (k1 >>> 2) & 0xf8f8ff;
			ai[j * 3 + i1] = k1 - (k1 >>> 2) - (k1 >>> 3) & 0xf8f8ff;
		}

	}

	public void method301(int i) {
		if (anIntArrayArray429[i] == null)
			return;
		int ai[] = anIntArrayArray429[i];
		for (int j = 0; j < 64; j++) {
			int k = j + 4032;
			int l = ai[k];
			for (int j1 = 0; j1 < 63; j1++) {
				ai[k] = ai[k - 64];
				k -= 64;
			}

			anIntArrayArray429[i][k] = l;
		}

		char c = '\u1000';
		for (int i1 = 0; i1 < c; i1++) {
			int k1 = ai[i1];
			ai[c + i1] = k1 - (k1 >>> 3) & 0xf8f8ff;
			ai[c * 2 + i1] = k1 - (k1 >>> 2) & 0xf8f8ff;
			ai[c * 3 + i1] = k1 - (k1 >>> 2) - (k1 >>> 3) & 0xf8f8ff;
		}

	}

	public int method302(int i) {
		if (i == 0xbc614e)
			return 0;
		method299(i);
		if (i >= 0)
			return anIntArrayArray429[i][0];
		if (i < 0) {
			i = -(i + 1);
			int j = i >> 10 & 0x1f;
			int k = i >> 5 & 0x1f;
			int l = i & 0x1f;
			return (j << 19) + (k << 11) + (l << 3);
		} else {
			return 0;
		}
	}

	public void method303(int i, int j, int k) {
		if (i == 0 && j == 0 && k == 0)
			i = 32;
		for (int l = 0; l < modelCount; l++)
			models[l].setLight(i, j, k);

	}

	public void method304(int i, int j, int k, int l, int i1) {
		if (k == 0 && l == 0 && i1 == 0)
			k = 32;
		for (int j1 = 0; j1 < modelCount; j1++)
			models[j1].setLight(i, j, k, l, i1);

	}

	public int method306(int i, int j, int k, int l, int i1) {
		if (l == j)
			return i;
		else
			return i + ((k - i) * (i1 - j)) / (l - j);
	}

	public boolean method307(int i, int j, int k, int l, boolean flag) {
		if (flag && i <= k || i < k) {
			if (i > l)
				return true;
			if (j > k)
				return true;
			if (j > l)
				return true;
			return !flag;
		}
		if (i < l)
			return true;
		if (j < k)
			return true;
		if (j < l)
			return true;
		else
			return flag;
	}

	public boolean method308(int i, int j, int k, boolean flag) {
		if (flag && i <= k || i < k) {
			if (j > k)
				return true;
			return !flag;
		}
		if (j < k)
			return true;
		else
			return flag;
	}

	public boolean method309(int ai[], int ai1[], int ai2[], int ai3[]) {
		int i = ai.length;
		int j = ai2.length;
		byte byte0 = 0;
		int i20;
		int k20 = i20 = ai1[0];
		int k = 0;
		int j20;
		int l20 = j20 = ai3[0];
		int i1 = 0;
		for (int i21 = 1; i21 < i; i21++)
			if (ai1[i21] < i20) {
				i20 = ai1[i21];
				k = i21;
			} else if (ai1[i21] > k20)
				k20 = ai1[i21];

		for (int j21 = 1; j21 < j; j21++)
			if (ai3[j21] < j20) {
				j20 = ai3[j21];
				i1 = j21;
			} else if (ai3[j21] > l20)
				l20 = ai3[j21];

		if (j20 >= k20)
			return false;
		if (i20 >= l20)
			return false;
		int l;
		int j1;
		boolean flag;
		if (ai1[k] < ai3[i1]) {
			for (l = k; ai1[l] < ai3[i1]; )
				l = (l + 1) % i;
			for (; ai1[k] < ai3[i1]; )
				k = ((k - 1) + i) % i;
			int k1 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k],
					ai1[k], ai3[i1]);
			int k6 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i],
					ai[l], ai1[l], ai3[i1]);
			int l10 = ai2[i1];
			flag = (k1 < l10) | (k6 < l10);
			if (method308(k1, k6, l10, flag))
				return true;
			j1 = (i1 + 1) % j;
			i1 = ((i1 - 1) + j) % j;
			if (k == l)
				byte0 = 1;
		} else {
			for (j1 = i1; ai3[j1] < ai1[k]; )
				j1 = (j1 + 1) % j;
			for (; ai3[i1] < ai1[k]; )
				i1 = ((i1 - 1) + j) % j;
			int l1 = ai[k];
			int i11 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1],
					ai3[i1], ai1[k]);
			int l15 = method306(ai2[((j1 - 1) + j) % j],
					ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
			flag = (l1 < i11) | (l1 < l15);
			if (method308(i11, l15, l1, !flag))
				return true;
			l = (k + 1) % i;
			k = ((k - 1) + i) % i;
			if (i1 == j1)
				byte0 = 2;
		}
		while (byte0 == 0)
			if (ai1[k] < ai1[l]) {
				if (ai1[k] < ai3[i1]) {
					if (ai1[k] < ai3[j1]) {
						int i2 = ai[k];
						int l6 = method306(ai[((l - 1) + i) % i],
								ai1[((l - 1) + i) % i], ai[l], ai1[l], ai1[k]);
						int j11 = method306(ai2[(i1 + 1) % j],
								ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
						int i16 = method306(ai2[((j1 - 1) + j) % j],
								ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1],
								ai1[k]);
						if (method307(i2, l6, j11, i16, flag))
							return true;
						k = ((k - 1) + i) % i;
						if (k == l)
							byte0 = 1;
					} else {
						int j2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i],
								ai[k], ai1[k], ai3[j1]);
						int i7 = method306(ai[((l - 1) + i) % i],
								ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
						int k11 = method306(ai2[(i1 + 1) % j],
								ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
						int j16 = ai2[j1];
						if (method307(j2, i7, k11, j16, flag))
							return true;
						j1 = (j1 + 1) % j;
						if (i1 == j1)
							byte0 = 2;
					}
				} else if (ai3[i1] < ai3[j1]) {
					int k2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i],
							ai[k], ai1[k], ai3[i1]);
					int j7 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i)
							% i], ai[l], ai1[l], ai3[i1]);
					int l11 = ai2[i1];
					int k16 = method306(ai2[((j1 - 1) + j) % j],
							ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai3[i1]);
					if (method307(k2, j7, l11, k16, flag))
						return true;
					i1 = ((i1 - 1) + j) % j;
					if (i1 == j1)
						byte0 = 2;
				} else {
					int l2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i],
							ai[k], ai1[k], ai3[j1]);
					int k7 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i)
							% i], ai[l], ai1[l], ai3[j1]);
					int i12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
							ai2[i1], ai3[i1], ai3[j1]);
					int l16 = ai2[j1];
					if (method307(l2, k7, i12, l16, flag))
						return true;
					j1 = (j1 + 1) % j;
					if (i1 == j1)
						byte0 = 2;
				}
			} else if (ai1[l] < ai3[i1]) {
				if (ai1[l] < ai3[j1]) {
					int i3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i],
							ai[k], ai1[k], ai1[l]);
					int l7 = ai[l];
					int j12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
							ai2[i1], ai3[i1], ai1[l]);
					int i17 = method306(ai2[((j1 - 1) + j) % j],
							ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[l]);
					if (method307(i3, l7, j12, i17, flag))
						return true;
					l = (l + 1) % i;
					if (k == l)
						byte0 = 1;
				} else {
					int j3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i],
							ai[k], ai1[k], ai3[j1]);
					int i8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i)
							% i], ai[l], ai1[l], ai3[j1]);
					int k12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
							ai2[i1], ai3[i1], ai3[j1]);
					int j17 = ai2[j1];
					if (method307(j3, i8, k12, j17, flag))
						return true;
					j1 = (j1 + 1) % j;
					if (i1 == j1)
						byte0 = 2;
				}
			} else if (ai3[i1] < ai3[j1]) {
				int k3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k],
						ai1[k], ai3[i1]);
				int j8 = method306(ai[((l - 1) + i) % i],
						ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
				int l12 = ai2[i1];
				int k17 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j)
						% j], ai2[j1], ai3[j1], ai3[i1]);
				if (method307(k3, j8, l12, k17, flag))
					return true;
				i1 = ((i1 - 1) + j) % j;
				if (i1 == j1)
					byte0 = 2;
			} else {
				int l3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k],
						ai1[k], ai3[j1]);
				int k8 = method306(ai[((l - 1) + i) % i],
						ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
				int i13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
						ai2[i1], ai3[i1], ai3[j1]);
				int l17 = ai2[j1];
				if (method307(l3, k8, i13, l17, flag))
					return true;
				j1 = (j1 + 1) % j;
				if (i1 == j1)
					byte0 = 2;
			}
		while (byte0 == 1)
			if (ai1[k] < ai3[i1]) {
				if (ai1[k] < ai3[j1]) {
					int i4 = ai[k];
					int j13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
							ai2[i1], ai3[i1], ai1[k]);
					int i18 = method306(ai2[((j1 - 1) + j) % j],
							ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
					return method308(j13, i18, i4, !flag);
				}
				int j4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k],
						ai1[k], ai3[j1]);
				int l8 = method306(ai[((l - 1) + i) % i],
						ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
				int k13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
						ai2[i1], ai3[i1], ai3[j1]);
				int j18 = ai2[j1];
				if (method307(j4, l8, k13, j18, flag))
					return true;
				j1 = (j1 + 1) % j;
				if (i1 == j1)
					byte0 = 0;
			} else if (ai3[i1] < ai3[j1]) {
				int k4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k],
						ai1[k], ai3[i1]);
				int i9 = method306(ai[((l - 1) + i) % i],
						ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
				int l13 = ai2[i1];
				int k18 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j)
						% j], ai2[j1], ai3[j1], ai3[i1]);
				if (method307(k4, i9, l13, k18, flag))
					return true;
				i1 = ((i1 - 1) + j) % j;
				if (i1 == j1)
					byte0 = 0;
			} else {
				int l4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k],
						ai1[k], ai3[j1]);
				int j9 = method306(ai[((l - 1) + i) % i],
						ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
				int i14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
						ai2[i1], ai3[i1], ai3[j1]);
				int l18 = ai2[j1];
				if (method307(l4, j9, i14, l18, flag))
					return true;
				j1 = (j1 + 1) % j;
				if (i1 == j1)
					byte0 = 0;
			}
		while (byte0 == 2)
			if (ai3[i1] < ai1[k]) {
				if (ai3[i1] < ai1[l]) {
					int i5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i],
							ai[k], ai1[k], ai3[i1]);
					int k9 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i)
							% i], ai[l], ai1[l], ai3[i1]);
					int j14 = ai2[i1];
					return method308(i5, k9, j14, flag);
				}
				int j5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k],
						ai1[k], ai1[l]);
				int l9 = ai[l];
				int k14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
						ai2[i1], ai3[i1], ai1[l]);
				int i19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j)
						% j], ai2[j1], ai3[j1], ai1[l]);
				if (method307(j5, l9, k14, i19, flag))
					return true;
				l = (l + 1) % i;
				if (k == l)
					byte0 = 0;
			} else if (ai1[k] < ai1[l]) {
				int k5 = ai[k];
				int i10 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i)
						% i], ai[l], ai1[l], ai1[k]);
				int l14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
						ai2[i1], ai3[i1], ai1[k]);
				int j19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j)
						% j], ai2[j1], ai3[j1], ai1[k]);
				if (method307(k5, i10, l14, j19, flag))
					return true;
				k = ((k - 1) + i) % i;
				if (k == l)
					byte0 = 0;
			} else {
				int l5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k],
						ai1[k], ai1[l]);
				int j10 = ai[l];
				int i15 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j],
						ai2[i1], ai3[i1], ai1[l]);
				int k19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j)
						% j], ai2[j1], ai3[j1], ai1[l]);
				if (method307(l5, j10, i15, k19, flag))
					return true;
				l = (l + 1) % i;
				if (k == l)
					byte0 = 0;
			}
		if (ai1[k] < ai3[i1]) {
			int i6 = ai[k];
			int j15 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1],
					ai3[i1], ai1[k]);
			int l19 = method306(ai2[((j1 - 1) + j) % j],
					ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
			return method308(j15, l19, i6, !flag);
		}
		int j6 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k],
				ai3[i1]);
		int k10 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i],
				ai[l], ai1[l], ai3[i1]);
		int k15 = ai2[i1];
		return method308(j6, k10, k15, flag);
	}

	public void reduceSprites(int i) {
		spriteCount -= i;
		activeModel.reduce(i, i * 2);
		if (spriteCount < 0)
			spriteCount = 0;
	}

	public void removeModel(GameModel model) {
		for (int i = 0; i < modelCount; i++)
			if (models[i] == model) {
				modelCount--;
				for (int j = i; j < modelCount; j++)
					models[j] = models[j + 1];
			}
	}

	public void resetActiveModel() {
		spriteCount = 0;
		activeModel.clear();
	}

	public void setCamera(int i, int j, int k, int l, int i1, int j1, int k1) {
		l &= 0x3ff;
		i1 &= 0x3ff;
		j1 &= 0x3ff;
		cameraPitch = 1024 - l & 0x3ff;
		cameraYaw = 1024 - i1 & 0x3ff;
		cameraRoll = 1024 - j1 & 0x3ff;
		int l1 = 0;
		int i2 = 0;
		int j2 = k1;
		if (l != 0) {
			int k2 = curve[l];
			int j3 = curve[l + 1024];
			int i4 = i2 * j3 - j2 * k2 >> 15;
			j2 = i2 * k2 + j2 * j3 >> 15;
			i2 = i4;
		}
		if (i1 != 0) {
			int l2 = curve[i1];
			int k3 = curve[i1 + 1024];
			int j4 = j2 * l2 + l1 * k3 >> 15;
			j2 = j2 * k3 - l1 * l2 >> 15;
			l1 = j4;
		}
		if (j1 != 0) {
			int i3 = curve[j1];
			int l3 = curve[j1 + 1024];
			int k4 = i2 * i3 + l1 * l3 >> 15;
			i2 = i2 * l3 - l1 * i3 >> 15;
			l1 = k4;
		}
		cameraX = i - l1;
		cameraY = j - i2;
		cameraZ = k - j2;
	}

	public void setCameraSize(int halfWindowWidth, int halfWindowHeight,
			int halfWindowWidth2, int halfWindowHeight2, int windowWidth,
			int camSizeInt) {
		halfWidth = halfWindowWidth2;
		halfHeight = halfWindowHeight2;
		halfWidth2 = halfWindowWidth;
		halfHeight2 = halfWindowHeight;
		width = windowWidth;
		viewDistance = camSizeInt;

		anIntArray437 = surface.pixels;

		cameraVariables = new CameraVariables[halfWindowHeight2
				+ halfWindowHeight];
		for (int k1 = 0; k1 < halfWindowHeight2 + halfWindowHeight; k1++)
			cameraVariables[k1] = new CameraVariables();

	}

	public void setModelOffset(int handle, int offset) {
		anIntArray422[handle] = offset;
	}

	public void setModelUnclickable(int handle) {
		activeModel.faceSpriteType[handle] = 1;
	}

	public void updateMouseCoords(int x, int y) {
		mouseX = x - halfWidth2;
		mouseY = y;
		currentVisibleModelCount = 0;
		aBoolean389 = true;
	}

}
