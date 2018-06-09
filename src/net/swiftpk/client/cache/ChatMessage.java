package net.swiftpk.client.cache;

public class ChatMessage {

	public static char aCharArray560[] = new char[100];

	private static final char ALLOWED_CHARACTERS[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h',
			'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
			'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')',
			'-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%',
			'"', '[', ']' };

	public static byte messageData[] = new byte[100];

	public static String byteToString(byte abyte0[], int i, int j) {
		try {
			int k = 0;
			int l = -1;
			for (int i1 = 0; i1 < j; i1++) {
				int j1 = abyte0[i++] & 0xff;
				int k1 = j1 >> 4 & 0xf;
				if (l == -1) {
					if (k1 < 13)
						aCharArray560[k++] = ALLOWED_CHARACTERS[k1];
					else
						l = k1;
				} else {
					aCharArray560[k++] = ALLOWED_CHARACTERS[((l << 4) + k1) - 195];
					l = -1;
				}
				k1 = j1 & 0xf;
				if (l == -1) {
					if (k1 < 13)
						aCharArray560[k++] = ALLOWED_CHARACTERS[k1];
					else
						l = k1;
				} else {
					aCharArray560[k++] = ALLOWED_CHARACTERS[((l << 4) + k1) - 195];
					l = -1;
				}
			}

			boolean flag = true;
			for (int l1 = 0; l1 < k; l1++) {
				char c = aCharArray560[l1];
				if (l1 > 4 && c == '@')
					aCharArray560[l1] = ' ';
				if (c == '%')
					aCharArray560[l1] = ' ';
				if (flag && c >= 'a' && c <= 'z') {
					aCharArray560[l1] += '\uFFE0';
					flag = false;
				}
				if (c == '.' || c == '!' || c == ':')
					flag = true;
			}

			return new String(aCharArray560, 0, k);
		} catch (Exception _ex) {
			return ".";
		}
	}

	public static int stringToByteArray(String s) {
		if (s.length() > 80)
			s = s.substring(0, 80);
		s = s.toLowerCase();
		int i = 0;
		int j = -1;
		for (int k = 0; k < s.length(); k++) {
			char c = s.charAt(k);
			int l = 0;
			for (int i1 = 0; i1 < ALLOWED_CHARACTERS.length; i1++) {
				if (c != ALLOWED_CHARACTERS[i1])
					continue;
				l = i1;
				break;
			}

			if (l > 12)
				l += 195;
			if (j == -1) {
				if (l < 13)
					j = l;
				else
					messageData[i++] = (byte) l;
			} else if (l < 13) {
				messageData[i++] = (byte) ((j << 4) + l);
				j = -1;
			} else {
				messageData[i++] = (byte) ((j << 4) + (l >> 4));
				j = l & 0xf;
			}
		}

		if (j != -1)
			messageData[i++] = (byte) (j << 4);
		return i;
	}

}
