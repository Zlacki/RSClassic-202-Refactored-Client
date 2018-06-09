package net.swiftpk.client.gfx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;

import net.swiftpk.client.util.ImplementationDelegate;
import net.swiftpk.client.util.Utility;

public class Surface implements ImageProducer, ImageObserver {

	public static int convertRGBToLong(int red, int green, int blue) {
		return (red << 16) + (green << 8) + blue;
	}

	public static void drawLetter(Font font, FontMetrics fontmetrics,
			char letter, int charSetOffset, ImplementationDelegate gameWindow,
			int fontNumber, boolean addCharWidth) {
		int charWidth = fontmetrics.charWidth(letter);
		int oldCharWidth = charWidth;
		if (addCharWidth)
			try {
				if (letter == '/')
					addCharWidth = false;
				if (letter == 'f' || letter == 't' || letter == 'w'
						|| letter == 'v' || letter == 'k' || letter == 'x'
						|| letter == 'y' || letter == 'A' || letter == 'V'
						|| letter == 'W')
					charWidth++;
			} catch (Exception _ex) {
			}
		int i1 = fontmetrics.getMaxAscent();
		int j1 = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent();
		int k1 = fontmetrics.getHeight();
		Image image = gameWindow.getContainerImpl().createImage(charWidth, j1);
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, charWidth, j1);
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(String.valueOf(letter), 0, i1);
		if (addCharWidth)
			g.drawString(String.valueOf(letter), 1, i1);
		int ai[] = new int[charWidth * j1];
		PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, charWidth,
				j1, ai, 0, charWidth);
		try {
			pixelgrabber.grabPixels();
		} catch (InterruptedException _ex) {
			return;
		}
		image.flush();
		int l1 = 0;
		int i2 = 0;
		int j2 = charWidth;
		int k2 = j1;
		label0: for (int l2 = 0; l2 < j1; l2++) {
			for (int i3 = 0; i3 < charWidth; i3++) {
				int k3 = ai[i3 + l2 * charWidth];
				if ((k3 & 0xffffff) == 0)
					continue;
				i2 = l2;
				break label0;
			}

		}

		label1: for (int j3 = 0; j3 < charWidth; j3++) {
			for (int l3 = 0; l3 < j1; l3++) {
				int j4 = ai[j3 + l3 * charWidth];
				if ((j4 & 0xffffff) == 0)
					continue;
				l1 = j3;
				break label1;
			}

		}

		label2: for (int i4 = j1 - 1; i4 >= 0; i4--) {
			for (int k4 = 0; k4 < charWidth; k4++) {
				int i5 = ai[k4 + i4 * charWidth];
				if ((i5 & 0xffffff) == 0)
					continue;
				k2 = i4 + 1;
				break label2;
			}

		}

		label3: for (int l4 = charWidth - 1; l4 >= 0; l4--) {
			for (int j5 = 0; j5 < j1; j5++) {
				int l5 = ai[l4 + j5 * charWidth];
				if ((l5 & 0xffffff) == 0)
					continue;
				j2 = l4 + 1;
				break label3;
			}

		}

		SOME_BYTES[charSetOffset * 9] = (byte) (currentFont / 16384);
		SOME_BYTES[charSetOffset * 9 + 1] = (byte) (currentFont / 128 & 0x7f);
		SOME_BYTES[charSetOffset * 9 + 2] = (byte) (currentFont & 0x7f);
		SOME_BYTES[charSetOffset * 9 + 3] = (byte) (j2 - l1);
		SOME_BYTES[charSetOffset * 9 + 4] = (byte) (k2 - i2);
		SOME_BYTES[charSetOffset * 9 + 5] = (byte) l1;
		SOME_BYTES[charSetOffset * 9 + 6] = (byte) (i1 - i2);
		SOME_BYTES[charSetOffset * 9 + 7] = (byte) oldCharWidth;
		SOME_BYTES[charSetOffset * 9 + 8] = (byte) k1;
		for (int k5 = i2; k5 < k2; k5++) {
			for (int i6 = l1; i6 < j2; i6++) {
				int j6 = ai[i6 + k5 * charWidth] & 0xff;
				if (j6 > 30 && j6 < 230)
					SOME_BOOLEANS[fontNumber] = true;
				SOME_BYTES[currentFont++] = (byte) j6;
			}

		}

	}

	public static void loadFont(String smallName, int fontNumber,
			ImplementationDelegate gameWindow) {
		boolean flag = false;
		boolean addCharWidth = false;
		smallName = smallName.toLowerCase();
		if (smallName.startsWith("helvetica"))
			smallName = smallName.substring(9);
		if (smallName.startsWith("h"))
			smallName = smallName.substring(1);
		if (smallName.startsWith("f")) {
			smallName = smallName.substring(1);
			flag = true;
		}
		if (smallName.startsWith("d")) {
			smallName = smallName.substring(1);
			addCharWidth = true;
		}
		if (smallName.endsWith(".jf"))
			smallName = smallName.substring(0, smallName.length() - 3);
		int style = 0;
		if (smallName.endsWith("b")) {
			style = 1;
			smallName = smallName.substring(0, smallName.length() - 1);
		}
		if (smallName.endsWith("p"))
			smallName = smallName.substring(0, smallName.length() - 1);
		int size = Integer.parseInt(smallName);
		Font font = new Font("Helvetica", style, size);
		FontMetrics fontmetrics = gameWindow.getContainerImpl().getFontMetrics(
				font);
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"£$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
		currentFont = 855;
		for (int charSetOffset = 0; charSetOffset < 95; charSetOffset++) {
			drawLetter(font, fontmetrics, charSet.charAt(charSetOffset),
					charSetOffset, gameWindow, fontNumber, addCharWidth);
		}
		fontArray[fontNumber] = new byte[currentFont];
		System.arraycopy(SOME_BYTES, 0, fontArray[fontNumber], 0, currentFont);
		if ((style == 1) && (SOME_BOOLEANS[fontNumber])) {
			SOME_BOOLEANS[fontNumber] = false;
			loadFont("f" + size + "p", fontNumber, gameWindow);
		}
		if ((flag) && (SOME_BOOLEANS[fontNumber])) {
			SOME_BOOLEANS[fontNumber] = false;
			loadFont("d" + size + "p", fontNumber, gameWindow);
		}
	}

	public static int loadFont_(byte[] font_arr) {
		if (font_arr == null) {
			return -1;
		}
		fontArray[currentFont] = font_arr;
		return currentFont++;
	}

	private static final boolean SOME_BOOLEANS[] = new boolean[12];

	private static final byte SOME_BYTES[] = new byte[0x186a0];

	public static int anInt346;

	public static int anInt347;

	public static int anInt348;

	private static int currentFont;

	public static int anInt352;

	static int charIndexes[];

	public static int font_count;

	static byte[][] fontArray = new byte[50][];

	static {
		String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
		charIndexes = new int[256];
		for (int i = 0; i < 256; i++) {
			int j = s.indexOf(i);
			if (j == -1)
				j = 74;
			charIndexes[i] = j * 9;
		}

	}

	public byte aByteArrayArray322[][];

	int anIntArray339[];

	int anIntArray340[];

	int anIntArray341[];

	int anIntArray342[];

	int anIntArray343[];

	int anIntArray344[];

	int anIntArray345[];

	public int anIntArrayArray323[][];

	private int viewport_right;

	private int viewport_bottom;

	private int viewport_left;

	private int viewport_top;

	ColorModel colorModel;

	public Image image;

	public int imageArea;

	ImageConsumer imageConsumer;

	public int imageFullHeight[];

	public int imageFullWidth[];

	public int imageHeight[];

	public int imageHeightUnused;

	public boolean imageTranslate[];

	public int imageWidth[];

	public int imageWidthUnused;

	public boolean interlace;

	public boolean loggedIn;

	public int menuMaxHeight;

	public int menuMaxWidth;

	public int offsetX[];

	public int offsetY[];

	public int pixels[];

	public int surfacePixels[][];

	public Surface(int width, int height, int limit, Component component) {
		interlace = false;
		loggedIn = false;
		viewport_bottom = height;
		viewport_right = width;
		imageWidthUnused = menuMaxWidth = width;
		imageHeightUnused = menuMaxHeight = height;
		imageArea = width * height;
		pixels = new int[imageArea];
		surfacePixels = new int[limit][];
		imageTranslate = new boolean[limit];
		aByteArrayArray322 = new byte[limit][];
		anIntArrayArray323 = new int[limit][];
		imageWidth = new int[limit];
		imageHeight = new int[limit];
		imageFullWidth = new int[limit];
		imageFullHeight = new int[limit];
		offsetX = new int[limit];
		offsetY = new int[limit];
		if (width > 1 && height > 1 && component != null) {
			colorModel = new DirectColorModel(32, 0xff0000, 65280, 255);
			int pixelArea = menuMaxWidth * menuMaxHeight;
			for (int i1 = 0; i1 < pixelArea; i1++)
				pixels[i1] = 0;

			image = component.createImage(this);
			complete();
			component.prepareImage(image, component);
			complete();
			component.prepareImage(image, component);
			complete();
			component.prepareImage(image, component);
		}
	}

	@Override
	public synchronized void addConsumer(ImageConsumer imageconsumer) {
		imageConsumer = imageconsumer;
		imageconsumer.setDimensions(menuMaxWidth, menuMaxHeight);
		imageconsumer.setProperties(null);
		imageconsumer.setColorModel(colorModel);
		imageconsumer.setHints(14);
	}

	public void blackScreen() {
		int area = menuMaxWidth * menuMaxHeight;
		if (!interlace) {
			for (int i = 0; i < area; i++)
				pixels[i] = 0;

			return;
		}
		int i = 0;
		for (int y = -menuMaxHeight; y < 0; y += 2) {
			for (int x = -menuMaxWidth; x < 0; x++)
				pixels[i++] = 0;

			i += menuMaxWidth;
		}

	}

	public void clear() {
		for (int i = 0; i < surfacePixels.length; i++) {
			surfacePixels[i] = null;
			imageWidth[i] = 0;
			imageHeight[i] = 0;
			aByteArrayArray322[i] = null;
			anIntArrayArray323[i] = null;
		}

	}

	public final synchronized void complete() {
		try {
			if (this.imageConsumer != null) {
				this.imageConsumer.setPixels(0, 0, this.menuMaxWidth,
						this.menuMaxHeight, colorModel, this.pixels, 0,
						this.menuMaxWidth);
				this.imageConsumer.imageComplete(2);
			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
	}

	public void draw(Graphics g, int x, int y) {
		complete();
		g.drawImage(image, x, y, this);
	}

	public void draw_ellipse(int x, int y, int a, int b, int color) {
		int wx, wy;
		int thresh;
		int asq = a * a;
		int bsq = b * b;
		int xa, ya;

		drawPixel(x, y + b, color);
		drawPixel(x, y - b, color);

		wx = 0;
		wy = b;
		xa = 0;
		ya = asq * 2 * b;
		thresh = asq / 4 - asq * b;

		for (;;) {
			thresh += xa + bsq;

			if (thresh >= 0) {
				ya -= asq * 2;
				thresh -= ya;
				wy--;
			}

			xa += bsq * 2;
			wx++;

			if (xa >= ya)
				break;

			drawPixel(x + wx, y - wy, color);
			drawPixel(x - wx, y - wy, color);
			drawPixel(x + wx, y + wy, color);
			drawPixel(x - wx, y + wy, color);
		}

		drawPixel(x + a, y, color);
		drawPixel(x - a, y, color);

		wx = a;
		wy = 0;
		xa = bsq * 2 * a;

		ya = 0;
		thresh = bsq / 4 - bsq * a;

		for (;;) {
			thresh += ya + asq;

			if (thresh >= 0) {
				xa -= bsq * 2;
				thresh = thresh - xa;
				wx--;
			}

			ya += asq * 2;
			wy++;

			if (ya > xa)
				break;

			drawPixel(x + wx, y - wy, color);
			drawPixel(x - wx, y - wy, color);
			drawPixel(x + wx, y + wy, color);
			drawPixel(x - wx, y + wy, color);
		}
	}

	public void drawArcX(int x1, int y1, int x2, int size, int color) {
		if (y1 < viewport_top || y1 >= viewport_bottom)
			return;
		if (x1 < viewport_left) {
			x2 -= viewport_left - x1;
			x1 = viewport_left;
		}
		if (x1 + x2 > viewport_right)
			x2 = viewport_right - x1;
		int xPixel = x1 + y1 * menuMaxWidth;
		int sizeCount = 1;
		for (int yPixel = 0; yPixel < x2; yPixel++) {
			if (yPixel < 50) {
				pixels[(xPixel + (menuMaxWidth * sizeCount) + 1) + yPixel] = 100000;
				sizeCount++;
			} else
				pixels[xPixel + yPixel] = color;
		}

	}

	public void drawBlurOut(int i, int j, int k, int l, int i1, int j1) {
		for (int k1 = k; k1 < k + i1; k1++) {
			for (int l1 = l; l1 < l + j1; l1++) {
				int i2 = 0;
				int j2 = 0;
				int k2 = 0;
				int l2 = 0;
				for (int i3 = k1 - i; i3 <= k1 + i; i3++)
					if (i3 >= 0 && i3 < menuMaxWidth) {
						for (int j3 = l1 - j; j3 <= l1 + j; j3++)
							if (j3 >= 0 && j3 < menuMaxHeight) {
								int k3 = pixels[i3 + menuMaxWidth * j3];
								i2 += k3 >> 16 & 0xff;
								j2 += k3 >> 8 & 0xff;
								k2 += k3 & 0xff;
								l2++;
							}

					}

				pixels[k1 + menuMaxWidth * l1] = (i2 / l2 << 16)
						+ (j2 / l2 << 8) + k2 / l2;
			}

		}

	}

	public void drawBox(int x, int y, int width, int height, int color) {
		if (x < viewport_left) {
			width -= viewport_left - x;
			x = viewport_left;
		}
		if (y < viewport_top) {
			height -= viewport_top - y;
			y = viewport_top;
		}
		if (x + width > viewport_right)
			width = viewport_right - x;
		if (y + height > viewport_bottom)
			height = viewport_bottom - y;
		int j1 = menuMaxWidth - width;
		byte byte0 = 1;
		if (interlace) {
			byte0 = 2;
			j1 += menuMaxWidth;
			if ((y & 1) != 0) {
				y++;
				height--;
			}
		}
		int k1 = x + y * menuMaxWidth;
		for (int l1 = -height; l1 < 0; l1 += byte0) {
			for (int i2 = -width; i2 < 0; i2++)
				pixels[k1++] = color;

			k1 += j1;
		}

	}

	public void drawBoxAlpha(int x, int y, int width, int height, int color,
			int alpha) {
		if (x < viewport_left) {
			width -= viewport_left - x;
			x = viewport_left;
		}
		if (y < viewport_top) {
			height -= viewport_top - y;
			y = viewport_top;
		}
		if (x + width > viewport_right)
			width = viewport_right - x;
		if (y + height > viewport_bottom)
			height = viewport_bottom - y;
		int bgAlpha = 256 - alpha;
		int red = (color >> 16 & 0xff) * alpha;
		int green = (color >> 8 & 0xff) * alpha;
		int blue = (color & 0xff) * alpha;
		int j3 = menuMaxWidth - width;
		byte verticalIncrease = 1;
		if (interlace) {
			verticalIncrease = 2;
			j3 += menuMaxWidth;
			if ((y & 1) != 0) {
				y++;
				height--;
			}
		}
		int pixel = x + y * menuMaxWidth;
		for (int verticalPos = 0; verticalPos < height; verticalPos += verticalIncrease) {
			for (int horizontalPos = -width; horizontalPos < 0; horizontalPos++) {
				int bgRed = (pixels[pixel] >> 16 & 0xff) * bgAlpha;
				int bgGreen = (pixels[pixel] >> 8 & 0xff) * bgAlpha;
				int bgBlue = (pixels[pixel] & 0xff) * bgAlpha;
				int newColor = ((red + bgRed >> 8) << 16)
						+ ((green + bgGreen >> 8) << 8) + (blue + bgBlue >> 8);
				pixels[pixel++] = newColor;
			}

			pixel += j3;
		}

	}

	public void drawBoxEdge(int x1, int y1, int x2, int y2, int color) {
		drawLineX(x1, y1, x2, color);
		drawLineX(x1, (y1 + y2) - 1, x2, color);
		drawLineY(x1, y1, y2, color);
		drawLineY((x1 + x2) - 1, y1, y2, color);
	}

	public void drawBoxTextColor(String s, int i, int j, int k, int l, int i1) {
		try {
			int j1 = 0;
			byte abyte0[] = fontArray[k];
			int k1 = 0;
			int l1 = 0;
			for (int i2 = 0; i2 < s.length(); i2++) {
				if (s.charAt(i2) == '@' && i2 + 4 < s.length()
						&& s.charAt(i2 + 4) == '@')
					i2 += 4;
				else if (s.charAt(i2) == '~' && i2 + 4 < s.length()
						&& s.charAt(i2 + 4) == '~')
					i2 += 4;
				else
					j1 += abyte0[charIndexes[s.charAt(i2)] + 7];
				if (s.charAt(i2) == ' ')
					l1 = i2;
				if (s.charAt(i2) == '%') {
					l1 = i2;
					j1 = 1000;
				}
				if (j1 > i1) {
					if (l1 <= k1)
						l1 = i2;
					drawStringCentered(s.substring(k1, l1), i, j, k, l);
					j1 = 0;
					k1 = i2 = l1 + 1;
					j += stringHeight(k);
				}
			}

			if (j1 > 0) {
				drawStringCentered(s.substring(k1), i, j, k, l);
			}
		} catch (Exception exception) {
			System.out.println("centrepara: " + exception);
			exception.printStackTrace();
		}
	}

	public void drawBoxTextRight(String s, int i, int j, int k, int l) {
		drawString(s, i - stringWidth(s, k), j, k, l);
	}

	public void drawCircle(int x, int y, int size, int color, int alpha) {
		int bgAlpha = 256 - alpha;
		int red = (color >> 16 & 0xff) * alpha;
		int green = (color >> 8 & 0xff) * alpha;
		int blue = (color & 0xff) * alpha;
		int boundsBottomY = y - size;
		if (boundsBottomY < 0)
			boundsBottomY = 0;
		int boundsTopY = y + size;
		if (boundsTopY >= menuMaxHeight)
			boundsTopY = menuMaxHeight - 1;
		byte verticalIncrement = 1;
		if (interlace) {
			verticalIncrement = 2;
			if ((boundsBottomY & 1) != 0)
				boundsBottomY++;
		}
		for (int verticalPos = boundsBottomY; verticalPos <= boundsTopY; verticalPos += verticalIncrement) {
			int remainingVerticalSize = verticalPos - y;
			int horizontalSize = (int) Math.sqrt(size * size
					- remainingVerticalSize * remainingVerticalSize);
			int boundsBottomX = x - horizontalSize;
			if (boundsBottomX < 0)
				boundsBottomX = 0;
			int boundsTopX = x + horizontalSize;
			if (boundsTopX >= menuMaxWidth)
				boundsTopX = menuMaxWidth - 1;
			int pixel = boundsBottomX + verticalPos * menuMaxWidth;
			for (int horizontalPos = boundsBottomX; horizontalPos <= boundsTopX; horizontalPos++) {
				int bgRed = (pixels[pixel] >> 16 & 0xff) * bgAlpha;
				int bgGreen = (pixels[pixel] >> 8 & 0xff) * bgAlpha;
				int bgBlue = (pixels[pixel] & 0xff) * bgAlpha;
				pixels[pixel++] = ((red + bgRed >> 8) << 16)
						+ ((green + bgGreen >> 8) << 8) + (blue + bgBlue >> 8);
			}

		}

	}

	public void drawDiagonalLine(int x, int y, int width, int height, int colour) {
		width -= x;
		height -= y;
		if (height == 0) {
			if (width >= 0)
				drawLineX(x, y, width + 1, colour);
			else
				drawLineX(x + width, y, 1 - width, colour);
		} else if (width == 0) {
			if (height >= 0)
				drawLineY(x, y, height + 1, colour);
			else
				drawLineY(x, y + height, 1 - height, colour);
		} else {
			if (width + height < 0) {
				x += width;
				width = -width;
				y += height;
				height = -height;
			}
			if (width > height) {
				y <<= 16;
				y += 32768;
				height <<= 16;
				int y_step = (int) Math.floor((double) height / (double) width
						+ 0.5);
				width += x;
				if (x < viewport_left) {
					y += y_step * (viewport_left - x);
					x = viewport_left;
				}
				if (width >= viewport_right)
					width = viewport_right - 1;
				for (/**/; x <= width; x++) {
					int _y = y >> 16;
					if (_y >= viewport_top && _y < viewport_bottom)
						pixels[x + _y * this.menuMaxWidth] = colour;
					y += y_step;
				}
			} else {
				x <<= 16;
				x += 32768;
				width <<= 16;
				int x_step = (int) Math.floor((double) width / (double) height
						+ 0.5);
				height += y;
				if (y < viewport_top) {
					x += x_step * (viewport_top - y);
					y = viewport_top;
				}
				if (height >= viewport_bottom)
					height = viewport_bottom - 1;
				for (; y <= height; y++) {
					int _x = x >> 16;
					if (_x >= viewport_left && _x < viewport_right)
						pixels[_x + y * this.menuMaxWidth] = colour;
					x += x_step;
				}
			}
		}
	}

	public void drawGradient(int x, int y, int width, int height, int topColor,
			int bottomColor) {
		if (x < viewport_left) {
			width -= viewport_left - x;
			x = viewport_left;
		}
		if (x + width > viewport_right)
			width = viewport_right - x;
		int bottomRed = bottomColor >> 16 & 0xff;
		int bottomGreen = bottomColor >> 8 & 0xff;
		int bottomBlue = bottomColor & 0xff;
		int topRed = topColor >> 16 & 0xff;
		int topGreen = topColor >> 8 & 0xff;
		int topBlue = topColor & 0xff;
		int i3 = menuMaxWidth - width;
		byte verticalIncrease = 1;
		if (interlace) {
			verticalIncrease = 2;
			i3 += menuMaxWidth;
			if ((y & 1) != 0) {
				y++;
				height--;
			}
		}
		int pixel = x + y * menuMaxWidth;
		for (int verticalPos = 0; verticalPos < height; verticalPos += verticalIncrease)
			if (verticalPos + y >= viewport_top
					&& verticalPos + y < viewport_bottom) {
				int newColor = ((bottomRed * verticalPos + topRed
						* (height - verticalPos))
						/ height << 16)
						+ ((bottomGreen * verticalPos + topGreen
								* (height - verticalPos))
								/ height << 8)
						+ (bottomBlue * verticalPos + topBlue
								* (height - verticalPos)) / height;
				for (int horizontalPos = -width; horizontalPos < 0; horizontalPos++)
					pixels[pixel++] = newColor;

				pixel += i3;
			} else {
				pixel += menuMaxWidth;
			}

	}

	public void drawLineX(int x1, int y1, int x2, int color) {
		if (y1 < viewport_top || y1 >= viewport_bottom)
			return;
		if (x1 < viewport_left) {
			x2 -= viewport_left - x1;
			x1 = viewport_left;
		}
		if (x1 + x2 > viewport_right)
			x2 = viewport_right - x1;
		int xPixel = x1 + y1 * menuMaxWidth;
		for (int yPixel = 0; yPixel < x2; yPixel++)
			pixels[xPixel + yPixel] = color;

	}

	public void drawLineY(int x1, int y1, int y2, int color) {
		if (x1 < viewport_left || x1 >= viewport_right)
			return;
		if (y1 < viewport_top) {
			y2 -= viewport_top - y1;
			y1 = viewport_top;
		}
		if (y1 + y2 > viewport_right)
			y2 = viewport_bottom - y1;
		int xPixel = x1 + y1 * menuMaxWidth;
		for (int yPixel = 0; yPixel < y2; yPixel++)
			pixels[xPixel + yPixel * menuMaxWidth] = color;

	}

	public void drawMinimapFlooring(int i, int j, int k, int l, int i1) {
		int j1 = menuMaxWidth;
		int k1 = menuMaxHeight;
		if (anIntArray339 == null) {
			anIntArray339 = new int[512];
			for (int l1 = 0; l1 < 256; l1++) {
				anIntArray339[l1] = (int) (Math.sin(l1 * 0.02454369D) * 32768D);
				anIntArray339[l1 + 256] = (int) (Math.cos(l1 * 0.02454369D) * 32768D);
			}

		}
		int i2 = -imageFullWidth[k] / 2;
		int j2 = -imageFullHeight[k] / 2;
		if (imageTranslate[k]) {
			i2 += offsetX[k];
			j2 += offsetY[k];
		}
		int k2 = i2 + imageWidth[k];
		int l2 = j2 + imageHeight[k];
		int i3 = k2;
		int j3 = j2;
		int k3 = i2;
		int l3 = l2;
		l &= 0xff;
		int i4 = anIntArray339[l] * i1;
		int j4 = anIntArray339[l + 256] * i1;
		int k4 = i + (j2 * i4 + i2 * j4 >> 22);
		int l4 = j + (j2 * j4 - i2 * i4 >> 22);
		int i5 = i + (j3 * i4 + i3 * j4 >> 22);
		int j5 = j + (j3 * j4 - i3 * i4 >> 22);
		int k5 = i + (l2 * i4 + k2 * j4 >> 22);
		int l5 = j + (l2 * j4 - k2 * i4 >> 22);
		int i6 = i + (l3 * i4 + k3 * j4 >> 22);
		int j6 = j + (l3 * j4 - k3 * i4 >> 22);
		if (i1 == 192 && (l & 0x3f) == (anInt348 & 0x3f))
			anInt346++;
		else if (i1 == 128)
			anInt348 = l;
		else
			anInt347++;
		int k6 = l4;
		int l6 = l4;
		if (j5 < k6)
			k6 = j5;
		else if (j5 > l6)
			l6 = j5;
		if (l5 < k6)
			k6 = l5;
		else if (l5 > l6)
			l6 = l5;
		if (j6 < k6)
			k6 = j6;
		else if (j6 > l6)
			l6 = j6;
		if (k6 < viewport_top)
			k6 = viewport_top;
		if (l6 > viewport_bottom)
			l6 = viewport_bottom;
		if (anIntArray340 == null || anIntArray340.length != k1 + 1) {
			anIntArray340 = new int[k1 + 1];
			anIntArray341 = new int[k1 + 1];
			anIntArray342 = new int[k1 + 1];
			anIntArray343 = new int[k1 + 1];
			anIntArray344 = new int[k1 + 1];
			anIntArray345 = new int[k1 + 1];
		}
		for (int i7 = k6; i7 <= l6; i7++) {
			anIntArray340[i7] = 0x5f5e0ff;
			anIntArray341[i7] = 0xfa0a1f01;
		}

		int i8 = 0;
		int k8 = 0;
		int i9 = 0;
		int j9 = imageWidth[k];
		int k9 = imageHeight[k];
		i2 = 0;
		j2 = 0;
		i3 = j9 - 1;
		j3 = 0;
		k2 = j9 - 1;
		l2 = k9 - 1;
		k3 = 0;
		l3 = k9 - 1;
		if (j6 != l4) {
			i8 = (i6 - k4 << 8) / (j6 - l4);
			i9 = (l3 - j2 << 8) / (j6 - l4);
		}
		int j7;
		int k7;
		int l7;
		int l8;
		if (l4 > j6) {
			l7 = i6 << 8;
			l8 = l3 << 8;
			j7 = j6;
			k7 = l4;
		} else {
			l7 = k4 << 8;
			l8 = j2 << 8;
			j7 = l4;
			k7 = j6;
		}
		if (j7 < 0) {
			l7 -= i8 * j7;
			l8 -= i9 * j7;
			j7 = 0;
		}
		if (k7 > k1 - 1)
			k7 = k1 - 1;
		for (int l9 = j7; l9 <= k7; l9++) {
			anIntArray340[l9] = anIntArray341[l9] = l7;
			l7 += i8;
			anIntArray342[l9] = anIntArray343[l9] = 0;
			anIntArray344[l9] = anIntArray345[l9] = l8;
			l8 += i9;
		}

		if (j5 != l4) {
			i8 = (i5 - k4 << 8) / (j5 - l4);
			k8 = (i3 - i2 << 8) / (j5 - l4);
		}
		int j8;
		if (l4 > j5) {
			l7 = i5 << 8;
			j8 = i3 << 8;
			j7 = j5;
			k7 = l4;
		} else {
			l7 = k4 << 8;
			j8 = i2 << 8;
			j7 = l4;
			k7 = j5;
		}
		if (j7 < 0) {
			l7 -= i8 * j7;
			j8 -= k8 * j7;
			j7 = 0;
		}
		if (k7 > k1 - 1)
			k7 = k1 - 1;
		for (int i10 = j7; i10 <= k7; i10++) {
			if (l7 < anIntArray340[i10]) {
				anIntArray340[i10] = l7;
				anIntArray342[i10] = j8;
				anIntArray344[i10] = 0;
			}
			if (l7 > anIntArray341[i10]) {
				anIntArray341[i10] = l7;
				anIntArray343[i10] = j8;
				anIntArray345[i10] = 0;
			}
			l7 += i8;
			j8 += k8;
		}

		if (l5 != j5) {
			i8 = (k5 - i5 << 8) / (l5 - j5);
			i9 = (l2 - j3 << 8) / (l5 - j5);
		}
		if (j5 > l5) {
			l7 = k5 << 8;
			j8 = k2 << 8;
			l8 = l2 << 8;
			j7 = l5;
			k7 = j5;
		} else {
			l7 = i5 << 8;
			j8 = i3 << 8;
			l8 = j3 << 8;
			j7 = j5;
			k7 = l5;
		}
		if (j7 < 0) {
			l7 -= i8 * j7;
			l8 -= i9 * j7;
			j7 = 0;
		}
		if (k7 > k1 - 1)
			k7 = k1 - 1;
		for (int j10 = j7; j10 <= k7; j10++) {
			if (l7 < anIntArray340[j10]) {
				anIntArray340[j10] = l7;
				anIntArray342[j10] = j8;
				anIntArray344[j10] = l8;
			}
			if (l7 > anIntArray341[j10]) {
				anIntArray341[j10] = l7;
				anIntArray343[j10] = j8;
				anIntArray345[j10] = l8;
			}
			l7 += i8;
			l8 += i9;
		}

		if (j6 != l5) {
			i8 = (i6 - k5 << 8) / (j6 - l5);
			k8 = (k3 - k2 << 8) / (j6 - l5);
		}
		if (l5 > j6) {
			l7 = i6 << 8;
			j8 = k3 << 8;
			l8 = l3 << 8;
			j7 = j6;
			k7 = l5;
		} else {
			l7 = k5 << 8;
			j8 = k2 << 8;
			l8 = l2 << 8;
			j7 = l5;
			k7 = j6;
		}
		if (j7 < 0) {
			l7 -= i8 * j7;
			j8 -= k8 * j7;
			j7 = 0;
		}
		if (k7 > k1 - 1)
			k7 = k1 - 1;
		for (int k10 = j7; k10 <= k7; k10++) {
			if (l7 < anIntArray340[k10]) {
				anIntArray340[k10] = l7;
				anIntArray342[k10] = j8;
				anIntArray344[k10] = l8;
			}
			if (l7 > anIntArray341[k10]) {
				anIntArray341[k10] = l7;
				anIntArray343[k10] = j8;
				anIntArray345[k10] = l8;
			}
			l7 += i8;
			j8 += k8;
		}

		int l10 = k6 * j1;
		int ai[] = surfacePixels[k];
		for (int i11 = k6; i11 < l6; i11++) {
			int j11 = anIntArray340[i11] >> 8;
			int k11 = anIntArray341[i11] >> 8;
			if (k11 - j11 <= 0) {
				l10 += j1;
			} else {
				int l11 = anIntArray342[i11] << 9;
				int i12 = ((anIntArray343[i11] << 9) - l11) / (k11 - j11);
				int j12 = anIntArray344[i11] << 9;
				int k12 = ((anIntArray345[i11] << 9) - j12) / (k11 - j11);
				if (j11 < viewport_left) {
					l11 += (viewport_left - j11) * i12;
					j12 += (viewport_left - j11) * k12;
					j11 = viewport_left;
				}
				if (k11 > viewport_right)
					k11 = viewport_right;
				if (!interlace || (i11 & 1) == 0)
					if (!imageTranslate[k])
						method243(pixels, ai, 0, l10 + j11, l11, j12, i12, k12,
								j11 - k11, j9);
					else
						method244(pixels, ai, 0, l10 + j11, l11, j12, i12, k12,
								j11 - k11, j9);
				l10 += j1;
			}
		}

	}

	public void drawPixel(int x, int y, int color) {
		pixels[x + y * menuMaxWidth] = color;
	}

    public void drawPixels(int pixels[][], int drawx, int drawy, int width, int height) {
        for(int x = drawx; x < drawx + width; x++)
            for(int y = drawy; y < drawy + height; y++)
                this.pixels[x + y * menuMaxWidth] = pixels[x - drawx][y - drawy];
    }

	public void drawSleepingTextPicture(int i, byte abyte0[]) {
		int ai[] = surfacePixels[i] = new int[10200];
		imageWidth[i] = 255;
		imageHeight[i] = 40;
		offsetX[i] = 0;
		offsetY[i] = 0;
		imageFullWidth[i] = 255;
		imageFullHeight[i] = 40;
		imageTranslate[i] = false;
		int j = 0;
		int k = 1;
		int l;
		for (l = 0; l < 255;) {
			int i1 = abyte0[k++] & 0xff;
			for (int k1 = 0; k1 < i1; k1++)
				ai[l++] = j;

			j = 0xffffff - j;
		}

		for (int j1 = 1; j1 < 40; j1++) {
			for (int l1 = 0; l1 < 255;) {
				int i2 = abyte0[k++] & 0xff;
				for (int j2 = 0; j2 < i2; j2++) {
					ai[l] = ai[l - 255];
					l++;
					l1++;
				}

				if (l1 < 255) {
					ai[l] = 0xffffff - ai[l - 255];
					l++;
					l1++;
				}
			}

		}

	}

	public void drawSprite(int x, int y, int picture) {
		if (imageTranslate[picture]) {
			x += offsetX[picture];
			y += offsetY[picture];
		}
		int l = x + y * menuMaxWidth;
		int i1 = 0;
		int j1 = imageHeight[picture];
		int k1 = imageWidth[picture];
		int l1 = menuMaxWidth - k1;
		int i2 = 0;
		if (y < viewport_top) {
			int j2 = viewport_top - y;
			j1 -= j2;
			y = viewport_top;
			i1 += j2 * k1;
			l += j2 * menuMaxWidth;
		}
		if (y + j1 >= viewport_bottom)
			j1 -= ((y + j1) - viewport_bottom) + 1;
		if (x < viewport_left) {
			int k2 = viewport_left - x;
			k1 -= k2;
			x = viewport_left;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (x + k1 >= viewport_right) {
			int l2 = ((x + k1) - viewport_right) + 1;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (k1 <= 0 || j1 <= 0)
			return;
		byte byte0 = 1;
		if (interlace) {
			byte0 = 2;
			l1 += menuMaxWidth;
			i2 += imageWidth[picture];
			if ((y & 1) != 0) {
				l += menuMaxWidth;
				j1--;
			}
		}
		if (surfacePixels[picture] == null) {
			method236(pixels, aByteArrayArray322[picture],
					anIntArrayArray323[picture], i1, l, k1, j1, l1, i2, byte0);
		} else {
			method235(pixels, surfacePixels[picture], 0, i1, l, k1, j1, l1, i2,
					byte0);
		}
	}

	public void drawString(String string, int x, int y, int k, int color) {
		try {
			byte abyte0[] = fontArray[k];
			for (int offset = 0; offset < string.length(); offset++)
				if (string.charAt(offset) == '@'
						&& offset + 4 < string.length()
						&& string.charAt(offset + 4) == '@') {
					if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("red"))
						color = 0xff0000;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("lre"))
						color = 0xff9040;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("yel"))
						color = 0xffff00;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("gre"))
						color = 65280;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("blu"))
						color = 255;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("cya"))
						color = 65535;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("mag"))
						color = 0xff00ff;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("whi"))
						color = 0xffffff;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("bla"))
						color = 0;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("dre"))
						color = 0xc00000;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("ora"))
						color = 0xff9040;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("ran"))
						color = (int) (Math.random() * 16777215D);
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("or1"))
						color = 0xffb000;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("or2"))
						color = 0xff7000;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("or3"))
						color = 0xff3000;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("gr1"))
						color = 0xc0ff00;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("gr2"))
						color = 0x80ff00;
					else if (string.substring(offset + 1, offset + 4)
							.equalsIgnoreCase("gr3"))
						color = 0x40ff00;
					offset += 4;
				} else if (string.charAt(offset) == '~'
						&& offset + 4 < string.length()
						&& string.charAt(offset + 4) == '~') {
					char c = string.charAt(offset + 1);
					char c1 = string.charAt(offset + 2);
					char c2 = string.charAt(offset + 3);
					if (c >= '0' && c <= '9' && c1 >= '0' && c1 <= '9'
							&& c2 >= '0' && c2 <= '9')
						x = Integer.parseInt(string.substring(offset + 1,
								offset + 4));
					offset += 4;
				} else if (string.charAt(offset) == '#'
						&& offset + 4 < string.length()
						&& string.charAt(offset + 4) == '#'
						&& string.substring(offset + 1, offset + 4)
								.equalsIgnoreCase("mag")) {
					spriteClip4(x - 5, y - 16, 20, 20, 3161, 0, 0, 0, false);
					x += 14;
					offset += 4;
				} else if (string.charAt(offset) == '#'
						&& offset + 4 < string.length()
						&& string.charAt(offset + 4) == '#'
						&& string.substring(offset + 1, offset + 4)
								.equalsIgnoreCase("rng")) {
					spriteClip4(x - 5, y - 16, 20, 20, 3162, 0, 0, 0, false);
					x += 14;
					offset += 4;
				} else if (string.charAt(offset) == '#'
						&& offset + 4 < string.length()
						&& string.charAt(offset + 4) == '#'
						&& string.substring(offset + 1, offset + 4)
								.equalsIgnoreCase("fig")) {
					spriteClip4(x - 5, y - 16, 20, 20, 2423, 0, 0, 0, false);
					x += 14;
					offset += 4;
				} else {
					int charIndex = charIndexes[string.charAt(offset)];
					if (loggedIn && !SOME_BOOLEANS[k] && color != 0)
						method257(charIndex, x + 1, y, 0, abyte0,
								SOME_BOOLEANS[k]);
					if (loggedIn && !SOME_BOOLEANS[k] && color != 0)
						method257(charIndex, x, y + 1, 0, abyte0,
								SOME_BOOLEANS[k]);
					method257(charIndex, x, y, color, abyte0,
							SOME_BOOLEANS[k]);
					x += abyte0[charIndex + 7];
				}

		} catch (Exception exception) {
			System.out.println("drawstring: " + exception);
			exception.printStackTrace();
		}
	}

	public void drawStringCentered(String s, int i, int j, int k, int l) {
		drawString(s, i - stringWidth(s, k) / 2, j, k, l);
	}

	public void drawWorld(int i) {
		int j = imageWidth[i] * imageHeight[i];
		int ai[] = surfacePixels[i];
		int ai1[] = new int[32768];
		for (int k = 0; k < j; k++) {
			int l = ai[k];
			ai1[((l & 0xf80000) >> 9) + ((l & 0xf800) >> 6) + ((l & 0xf8) >> 3)]++;
		}

		int ai2[] = new int[256];
		ai2[0] = 0xff00ff;
		int ai3[] = new int[256];
		for (int i1 = 0; i1 < 32768; i1++) {
			int j1 = ai1[i1];
			if (j1 > ai3[255]) {
				for (int k1 = 1; k1 < 256; k1++) {
					if (j1 <= ai3[k1])
						continue;
					for (int i2 = 255; i2 > k1; i2--) {
						ai2[i2] = ai2[i2 - 1];
						ai3[i2] = ai3[i2 - 1];
					}

					ai2[k1] = ((i1 & 0x7c00) << 9) + ((i1 & 0x3e0) << 6)
							+ ((i1 & 0x1f) << 3) + 0x40404;
					ai3[k1] = j1;
					break;
				}

			}
			ai1[i1] = -1;
		}

		byte abyte0[] = new byte[j];
		for (int l1 = 0; l1 < j; l1++) {
			int j2 = ai[l1];
			int k2 = ((j2 & 0xf80000) >> 9) + ((j2 & 0xf800) >> 6)
					+ ((j2 & 0xf8) >> 3);
			int l2 = ai1[k2];
			if (l2 == -1) {
				int i3 = 0x3b9ac9ff;
				int j3 = j2 >> 16 & 0xff;
				int k3 = j2 >> 8 & 0xff;
				int l3 = j2 & 0xff;
				for (int i4 = 0; i4 < 256; i4++) {
					int j4 = ai2[i4];
					int k4 = j4 >> 16 & 0xff;
					int l4 = j4 >> 8 & 0xff;
					int i5 = j4 & 0xff;
					int j5 = (j3 - k4) * (j3 - k4) + (k3 - l4) * (k3 - l4)
							+ (l3 - i5) * (l3 - i5);
					if (j5 < i3) {
						i3 = j5;
						l2 = i4;
					}
				}

				ai1[k2] = l2;
			}
			abyte0[l1] = (byte) l2;
		}

		aByteArrayArray322[i] = abyte0;
		anIntArrayArray323[i] = ai2;
		surfacePixels[i] = null;
	}

	public void fade(int i, int j, int k, int l) {
		if (imageTranslate[k]) {
			i += offsetX[k];
			j += offsetY[k];
		}
		int i1 = i + j * menuMaxWidth;
		int j1 = 0;
		int k1 = imageHeight[k];
		int l1 = imageWidth[k];
		int i2 = menuMaxWidth - l1;
		int j2 = 0;
		if (j < viewport_top) {
			int k2 = viewport_top - j;
			k1 -= k2;
			j = viewport_top;
			j1 += k2 * l1;
			i1 += k2 * menuMaxWidth;
		}
		if (j + k1 >= viewport_bottom)
			k1 -= ((j + k1) - viewport_bottom) + 1;
		if (i < viewport_left) {
			int l2 = viewport_left - i;
			l1 -= l2;
			i = viewport_left;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (i + l1 >= viewport_right) {
			int i3 = ((i + l1) - viewport_right) + 1;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (l1 <= 0 || k1 <= 0)
			return;
		byte byte0 = 1;
		if (interlace) {
			byte0 = 2;
			i2 += menuMaxWidth;
			j2 += imageWidth[k];
			if ((j & 1) != 0) {
				i1 += menuMaxWidth;
				k1--;
			}
		}
		if (surfacePixels[k] == null) {
			method239(pixels, aByteArrayArray322[k], anIntArrayArray323[k], j1,
					i1, l1, k1, i2, j2, byte0, l);
		} else {
			method238(pixels, surfacePixels[k], 0, j1, i1, l1, k1, i2, j2,
					byte0, l);
		}
	}

	public void fadePixels() {
		int k = menuMaxWidth * menuMaxHeight;
		for (int j = 0; j < k; j++) {
			int i = pixels[j] & 0xffffff;
			pixels[j] = (i >>> 1 & 0x7f7f7f) + (i >>> 2 & 0x3f3f3f)
					+ (i >>> 3 & 0x1f1f1f) + (i >>> 4 & 0xf0f0f);
		}

	}

	@Override
	public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1) {
		return true;
	}

	@Override
	public synchronized boolean isConsumer(ImageConsumer imageconsumer) {
		return imageConsumer == imageconsumer;
	}

	public void registerSprite(int i, Sprite sprite) {
		offsetX[i] = sprite.getXShift();
		offsetY[i] = sprite.getYShift();
		imageWidth[i] = sprite.getWidth();
		imageHeight[i] = sprite.getHeight();
		imageFullWidth[i] = sprite.getWidth2();
		imageFullHeight[i] = sprite.getHeight2();
		surfacePixels[i] = new int[sprite.getWidth() * sprite.getHeight()];
        imageTranslate[i] = offsetX[i] != 0 || offsetY[i] != 0;
		for(int l2 = 0; l2 < sprite.getPixels().length; l2++)
			surfacePixels[i][l2] = sprite.getPixel(l2);
	}

	public void loadAnimation(int i, byte abyte0[], byte abyte1[],
			int frameCount) {
		int k = Utility.getUnsignedShort(abyte0, 0);
		int l = Utility.getUnsignedShort(abyte1, k);
		k += 2;
		int i1 = Utility.getUnsignedShort(abyte1, k);
		k += 2;
		int j1 = abyte1[k++] & 0xff;
		int ai[] = new int[j1];
		ai[0] = 0xff00ff;
		for (int k1 = 0; k1 < j1 - 1; k1++) {
			ai[k1 + 1] = ((abyte1[k] & 0xff) << 16)
					+ ((abyte1[k + 1] & 0xff) << 8) + (abyte1[k + 2] & 0xff);
			k += 3;
		}

		int l1 = 2;
		for (int i2 = i; i2 < i + frameCount; i2++) {
			offsetX[i2] = abyte1[k++] & 0xff;
			offsetY[i2] = abyte1[k++] & 0xff;
			imageWidth[i2] = Utility.getUnsignedShort(abyte1, k);
			k += 2;
			imageHeight[i2] = Utility.getUnsignedShort(abyte1, k);
			k += 2;
			int j2 = abyte1[k++] & 0xff;
			int k2 = imageWidth[i2] * imageHeight[i2];
			aByteArrayArray322[i2] = new byte[k2];
			anIntArrayArray323[i2] = ai;
			imageFullWidth[i2] = l;
			imageFullHeight[i2] = i1;
			surfacePixels[i2] = null;
            imageTranslate[i2] = offsetX[i2] != 0 || offsetY[i2] != 0;
			if (j2 == 0) {
				for (int l2 = 0; l2 < k2; l2++) {
					aByteArrayArray322[i2][l2] = abyte0[l1++];
					if (aByteArrayArray322[i2][l2] == 0)
						imageTranslate[i2] = true;
				}

			} else if (j2 == 1) {
				for (int i3 = 0; i3 < imageWidth[i2]; i3++) {
					for (int j3 = 0; j3 < imageHeight[i2]; j3++) {
						aByteArrayArray322[i2][i3 + j3 * imageWidth[i2]] = abyte0[l1++];
						if (aByteArrayArray322[i2][i3 + j3 * imageWidth[i2]] == 0)
							imageTranslate[i2] = true;
					}

				}

			}
		}

	}

	public int messageFontHeight(int messageType) {
		if (messageType == 0)
			return 12;
		if (messageType == 1)
			return 14;
		if (messageType == 2)
			return 14;
		if (messageType == 3)
			return 15;
		if (messageType == 4)
			return 15;
		if (messageType == 5)
			return 19;
		if (messageType == 6)
			return 24;
		if (messageType == 7) {
			return 29;
		}
		return method261(messageType);
	}

	public void method227(int i) {
		if (aByteArrayArray322[i] == null)
			return;
		int j = imageWidth[i] * imageHeight[i];
		byte abyte0[] = aByteArrayArray322[i];
		int ai[] = anIntArrayArray323[i];
		int ai1[] = new int[j];
		for (int k = 0; k < j; k++) {
			int l = ai[abyte0[k] & 0xff];
			if (l == 0)
				l = 1;
			else if (l == 0xff00ff)
				l = 0;
			ai1[k] = l;
		}

		surfacePixels[i] = ai1;
		aByteArrayArray322[i] = null;
		anIntArrayArray323[i] = null;
	}

	public void method228(int i, int j, int k, int l, int i1) {
		imageWidth[i] = l;
		imageHeight[i] = i1;
		imageTranslate[i] = false;
		offsetX[i] = 0;
		offsetY[i] = 0;
		imageFullWidth[i] = l;
		imageFullHeight[i] = i1;
		int j1 = l * i1;
		int k1 = 0;
		surfacePixels[i] = new int[j1];
		for (int l1 = j; l1 < j + l; l1++) {
			for (int i2 = k; i2 < k + i1; i2++)
				surfacePixels[i][k1++] = pixels[l1 + i2 * menuMaxWidth];

		}

	}

	public void method229(int i, int j, int k, int l, int i1) {
		imageWidth[i] = l;
		imageHeight[i] = i1;
		imageTranslate[i] = false;
		offsetX[i] = 0;
		offsetY[i] = 0;
		imageFullWidth[i] = l;
		imageFullHeight[i] = i1;
		int j1 = l * i1;
		int k1 = 0;
		surfacePixels[i] = new int[j1];
		for (int l1 = k; l1 < k + i1; l1++) {
			for (int i2 = j; i2 < j + l; i2++)
				surfacePixels[i][k1++] = pixels[i2 + l1 * menuMaxWidth];

		}

	}

	private void method235(int ai[], int ai1[], int i, int j, int k, int l,
			int i1, int j1, int k1, int l1) {
		int i2 = -(l >> 2);
		l = -(l & 3);
		for (int j2 = -i1; j2 < 0; j2 += l1) {
			for (int k2 = i2; k2 < 0; k2++) {
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
			}

			for (int l2 = l; l2 < 0; l2++) {
				i = ai1[j++];
				if (i != 0)
					ai[k++] = i;
				else
					k++;
			}

			k += j1;
			j += k1;
		}

	}

	private void method236(int ai[], byte abyte0[], int ai1[], int i, int j,
			int k, int l, int i1, int j1, int k1) {
		int l1 = -(k >> 2);
		k = -(k & 3);
		for (int i2 = -l; i2 < 0; i2 += k1) {
			for (int j2 = l1; j2 < 0; j2++) {
				byte byte0 = abyte0[i++];
				if (byte0 != 0)
					ai[j++] = ai1[byte0 & 0xff];
				else
					j++;
				byte0 = abyte0[i++];
				if (byte0 != 0)
					ai[j++] = ai1[byte0 & 0xff];
				else
					j++;
				byte0 = abyte0[i++];
				if (byte0 != 0)
					ai[j++] = ai1[byte0 & 0xff];
				else
					j++;
				byte0 = abyte0[i++];
				if (byte0 != 0)
					ai[j++] = ai1[byte0 & 0xff];
				else
					j++;
			}

			for (int k2 = k; k2 < 0; k2++) {
				byte byte1 = abyte0[i++];
				if (byte1 != 0)
					ai[j++] = ai1[byte1 & 0xff];
				else
					j++;
			}

			j += i1;
			i += j1;
		}

	}

	private void method238(int ai[], int ai1[], int i, int j, int k, int l,
			int i1, int j1, int k1, int l1, int i2) {
		int j2 = 256 - i2;
		for (int k2 = -i1; k2 < 0; k2 += l1) {
			for (int l2 = -l; l2 < 0; l2++) {
				i = ai1[j++];
				if (i != 0) {
					int i3 = ai[k];
					ai[k++] = ((i & 0xff00ff) * i2 + (i3 & 0xff00ff) * j2 & 0xff00ff00)
							+ ((i & 0xff00) * i2 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
				} else {
					k++;
				}
			}

			k += j1;
			j += k1;
		}

	}

	private void method239(int ai[], byte abyte0[], int ai1[], int i, int j,
			int k, int l, int i1, int j1, int k1, int l1) {
		int i2 = 256 - l1;
		for (int j2 = -l; j2 < 0; j2 += k1) {
			for (int k2 = -k; k2 < 0; k2++) {
				int l2 = abyte0[i++];
				if (l2 != 0) {
					l2 = ai1[l2 & 0xff];
					int i3 = ai[j];
					ai[j++] = ((l2 & 0xff00ff) * l1 + (i3 & 0xff00ff) * i2 & 0xff00ff00)
							+ ((l2 & 0xff00) * l1 + (i3 & 0xff00) * i2 & 0xff0000) >> 8;
				} else {
					j++;
				}
			}

			j += i1;
			i += j1;
		}

	}

	private void method243(int ai[], int ai1[], int i, int j, int k, int l,
			int i1, int j1, int k1, int l1) {
		for (i = k1; i < 0; i++) {
			pixels[j++] = ai1[(k >> 17) + (l >> 17) * l1];
			k += i1;
			l += j1;
		}

	}

	private void method244(int ai[], int ai1[], int i, int j, int k, int l,
			int i1, int j1, int k1, int l1) {
		for (int i2 = k1; i2 < 0; i2++) {
			i = ai1[(k >> 17) + (l >> 17) * l1];
			if (i != 0)
				pixels[j++] = i;
			else
				j++;
			k += i1;
			l += j1;
		}

	}

	private void method257(int i, int j, int k, int l, byte abyte0[],
			boolean flag) {
		int i1 = j + abyte0[i + 5];
		int j1 = k - abyte0[i + 6];
		int k1 = abyte0[i + 3];
		int l1 = abyte0[i + 4];
		int i2 = abyte0[i] * 16384 + abyte0[i + 1] * 128 + abyte0[i + 2];
		int j2 = i1 + j1 * menuMaxWidth;
		int k2 = menuMaxWidth - k1;
		int l2 = 0;
		if (j1 < viewport_top) {
			int i3 = viewport_top - j1;
			l1 -= i3;
			j1 = viewport_top;
			i2 += i3 * k1;
			j2 += i3 * menuMaxWidth;
		}
		if (j1 + l1 >= viewport_bottom)
			l1 -= ((j1 + l1) - viewport_bottom) + 1;
		if (i1 < viewport_left) {
			int j3 = viewport_left - i1;
			k1 -= j3;
			i1 = viewport_left;
			i2 += j3;
			j2 += j3;
			l2 += j3;
			k2 += j3;
		}
		if (i1 + k1 >= viewport_right) {
			int k3 = ((i1 + k1) - viewport_right) + 1;
			k1 -= k3;
			l2 += k3;
			k2 += k3;
		}
		if (k1 > 0 && l1 > 0) {
			if (flag) {
				method259(pixels, abyte0, l, i2, j2, k1, l1, k2, l2);
				return;
			}
			plotLetter(pixels, abyte0, l, i2, j2, k1, l1, k2, l2);
		}
	}

	private void method259(int ai[], byte abyte0[], int i, int j, int k, int l,
			int i1, int j1, int k1) {
		for (int l1 = -i1; l1 < 0; l1++) {
			for (int i2 = -l; i2 < 0; i2++) {
				int j2 = abyte0[j++] & 0xff;
				if (j2 > 30) {
					if (j2 >= 230) {
						ai[k++] = i;
					} else {
						int k2 = ai[k];
						ai[k++] = ((i & 0xff00ff) * j2 + (k2 & 0xff00ff)
								* (256 - j2) & 0xff00ff00)
								+ ((i & 0xff00) * j2 + (k2 & 0xff00)
										* (256 - j2) & 0xff0000) >> 8;
					}
				} else {
					k++;
				}
			}

			k += j1;
			j += k1;
		}

	}

	public int method261(int i) {
		if (i == 0)
			return fontArray[i][8] - 2;
		else
			return fontArray[i][8] - 1;
	}

	private void plotLetter(int ai[], byte abyte0[], int i, int j, int k,
			int l, int i1, int j1, int k1) {
		try {
			int l1 = -(l >> 2);
			l = -(l & 3);
			for (int i2 = -i1; i2 < 0; i2++) {
				for (int j2 = l1; j2 < 0; j2++) {
					if (abyte0[j++] != 0)
						ai[k++] = i;
					else
						k++;
					if (abyte0[j++] != 0)
						ai[k++] = i;
					else
						k++;
					if (abyte0[j++] != 0)
						ai[k++] = i;
					else
						k++;
					if (abyte0[j++] != 0)
						ai[k++] = i;
					else
						k++;
				}

				for (int k2 = l; k2 < 0; k2++)
					if (abyte0[j++] != 0)
						ai[k++] = i;
					else
						k++;

				k += j1;
				j += k1;
			}

		} catch (Exception exception) {
			System.out.println("plotletter: " + exception);
			exception.printStackTrace();
		}
	}

	private void plotSale1(int ai[], int ai1[], int i, int j, int k, int l,
			int i1, int j1, int k1, int l1, int i2, int j2, int k2) {
		try {
			int l2 = j;
			for (int i3 = -k1; i3 < 0; i3 += k2) {
				int j3 = (k >> 16) * j2;
				for (int k3 = -j1; k3 < 0; k3++) {
					i = ai1[(j >> 16) + j3];
					if (i != 0)
						ai[l++] = i;
					else
						l++;
					j += l1;
				}

				k += i2;
				j = l2;
				l += i1;
			}

		} catch (Exception _ex) {
			System.out.println("error in plot_scale");
		}
	}

	private void plotScale2(int ai[], int ai1[], int i, int j, int k, int l,
			int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2) {
		int i3 = l2 >> 16 & 0xff;
		int j3 = l2 >> 8 & 0xff;
		int k3 = l2 & 0xff;
		try {
			int l3 = j;
			for (int i4 = -k1; i4 < 0; i4 += k2) {
				int j4 = (k >> 16) * j2;
				for (int k4 = -j1; k4 < 0; k4++) {
					i = ai1[(j >> 16) + j4];
					if (i != 0) {
						int l4 = i >> 16 & 0xff;
						int i5 = i >> 8 & 0xff;
						int j5 = i & 0xff;
						if (l4 == i5 && i5 == j5)
							ai[l++] = ((l4 * i3 >> 8) << 16)
									+ ((i5 * j3 >> 8) << 8) + (j5 * k3 >> 8);
						else
							ai[l++] = i;
					} else {
						l++;
					}
					j += l1;
				}

				k += i2;
				j = l3;
				l += i1;
			}

		} catch (Exception _ex) {
			System.out.println("error in plot_scale");
		}
	}

	@Override
	public synchronized void removeConsumer(ImageConsumer imageconsumer) {
		if (imageConsumer == imageconsumer)
			imageConsumer = null;
	}

	@Override
	public void requestTopDownLeftRightResend(ImageConsumer imageconsumer) {
		System.out.println("TDLR");
	}

	public void resetDimensions() {
		viewport_left = 0;
		viewport_top = 0;
		viewport_right = menuMaxWidth;
		viewport_bottom = menuMaxHeight;
	}

	public void resize(int width, int height, int limit, Component component) {
		interlace = false;
		loggedIn = false;

		this.viewport_right = (this.menuMaxWidth = width);
		this.viewport_bottom = (this.menuMaxHeight = height);
		this.pixels = new int[menuMaxWidth * menuMaxHeight];

		imageArea = width * height;

		image = component.createImage(this);
		complete();
		component.prepareImage(image, component);
		complete();
		component.prepareImage(image, component);
		complete();
		component.prepareImage(image, component);
	}

	public void rounded_rectangle(int x, int y, int width, int height,
			int lineColor, int cornerRadius) {
		/*
		 * corners: p1 - p2 | | p4 - p3
		 */
		int p1 = x;
		int p2 = x + width;
		int p3 = p2 + height;
		int p4 = x + height;

		// draw straight lines

		this.drawLineX(x + cornerRadius, y - cornerRadius, p2, lineColor);
		this.drawLineX(x + cornerRadius, p4 - cornerRadius, p2, lineColor);
		this.drawLineY(p1 + cornerRadius, height - cornerRadius, p1, lineColor);
		this.drawLineY(p3 + cornerRadius, height - cornerRadius, p1, lineColor);
		draw_ellipse(x + cornerRadius, 10, 100, 100, 100000);
		/*
		 * line(src, Point (p1.x+cornerRadius,p1.y), Point
		 * (p2.x-cornerRadius,p2.y), lineColor, thickness, lineType); line(src,
		 * Point (p2.x,p2.y+cornerRadius), Point (p3.x,p3.y-cornerRadius),
		 * lineColor, thickness, lineType); line(src, Point
		 * (p4.x+cornerRadius,p4.y), Point (p3.x-cornerRadius,p3.y), lineColor,
		 * thickness, lineType); line(src, Point (p1.x,p1.y+cornerRadius), Point
		 * (p4.x,p4.y-cornerRadius), lineColor, thickness, lineType);
		 *
		 * // draw arcs ellipse( src, p1+Point(cornerRadius, cornerRadius),
		 * Size( cornerRadius, cornerRadius ), 180.0, 0, 90, lineColor,
		 * thickness, lineType ); ellipse( src, p2+Point(-cornerRadius,
		 * cornerRadius), Size( cornerRadius, cornerRadius ), 270.0, 0, 90,
		 * lineColor, thickness, lineType ); ellipse( src,
		 * p3+Point(-cornerRadius, -cornerRadius), Size( cornerRadius,
		 * cornerRadius ), 0.0, 0, 90, lineColor, thickness, lineType );
		 * ellipse( src, p4+Point(cornerRadius, -cornerRadius), Size(
		 * cornerRadius, cornerRadius ), 90.0, 0, 90, lineColor, thickness,
		 * lineType );
		 */
	}

	public void setBounds(int x, int y, int width, int height) {
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (width > menuMaxWidth)
			width = menuMaxWidth;
		if (height > menuMaxHeight)
			height = menuMaxHeight;
		viewport_left = x;
		viewport_top = y;
		viewport_right = width;
		viewport_bottom = height;
	}

	public void setClip(int left, int top, int right, int bottom) {
		if (left < 0) {
			left = 0;
		}
		if (top < 0) {
			top = 0;
		}
		if (right > this.menuMaxWidth) {
			right = this.menuMaxWidth;
		}
		if (bottom > this.menuMaxHeight) {
			bottom = this.menuMaxHeight;
		}
		viewport_left = left;
		viewport_top = top;
		viewport_right = right;
		viewport_bottom = bottom;
	}

	public void setPixelColor(int x, int y, int color) {
		if (x < viewport_left || y < viewport_top || x >= viewport_right
				|| y >= viewport_bottom) {
		} else {
			pixels[x + y * menuMaxWidth] = color;
		}
	}

	public void spriteClip1(int i, int j, int k, int l, int i1) {
		try {
			int j1 = imageWidth[i1];
			int k1 = imageHeight[i1];
			int l1 = 0;
			int i2 = 0;
			int j2 = (j1 << 16) / k;
			int k2 = (k1 << 16) / l;
			if (imageTranslate[i1]) {
				int l2 = imageFullWidth[i1];
				int j3 = imageFullHeight[i1];
				j2 = (l2 << 16) / k;
				k2 = (j3 << 16) / l;
				i += ((offsetX[i1] * k + l2) - 1) / l2;
				j += ((offsetY[i1] * l + j3) - 1) / j3;
				if ((offsetX[i1] * k) % l2 != 0)
					l1 = (l2 - (offsetX[i1] * k) % l2 << 16) / k;
				if ((offsetY[i1] * l) % j3 != 0)
					i2 = (j3 - (offsetY[i1] * l) % j3 << 16) / l;
				k = (k * (imageWidth[i1] - (l1 >> 16))) / l2;
				l = (l * (imageHeight[i1] - (i2 >> 16))) / j3;
			}
			int i3 = i + j * menuMaxWidth;
			int k3 = menuMaxWidth - k;
			if (j < viewport_top) {
				int l3 = viewport_top - j;
				l -= l3;
				j = 0;
				i3 += l3 * menuMaxWidth;
				i2 += k2 * l3;
			}
			if (j + l >= viewport_bottom)
				l -= ((j + l) - viewport_bottom) + 1;
			if (i < viewport_left) {
				int i4 = viewport_left - i;
				k -= i4;
				i = 0;
				i3 += i4;
				l1 += j2 * i4;
				k3 += i4;
			}
			if (i + k >= viewport_right) {
				int j4 = ((i + k) - viewport_right) + 1;
				k -= j4;
				k3 += j4;
			}
			byte byte0 = 1;
			if (interlace) {
				byte0 = 2;
				k3 += menuMaxWidth;
				k2 += k2;
				if ((j & 1) != 0) {
					i3 += menuMaxWidth;
					l--;
				}
			}
			plotSale1(pixels, surfacePixels[i1], 0, l1, i2, i3, k3, k, l, j2,
					k2, j1, byte0);
		} catch (Exception _ex) {
			System.out.println("error in sprite clipping routine");
		}
	}

	public void spriteClip2(int i, int j, int k, int l, int i1, int j1) {
		try {
			int k1 = imageWidth[i1];
			int l1 = imageHeight[i1];
			int i2 = 0;
			int j2 = 0;
			int k2 = (k1 << 16) / k;
			int l2 = (l1 << 16) / l;
			if (imageTranslate[i1]) {
				int i3 = imageFullWidth[i1];
				int k3 = imageFullHeight[i1];
				k2 = (i3 << 16) / k;
				l2 = (k3 << 16) / l;
				i += ((offsetX[i1] * k + i3) - 1) / i3;
				j += ((offsetY[i1] * l + k3) - 1) / k3;
				if ((offsetX[i1] * k) % i3 != 0)
					i2 = (i3 - (offsetX[i1] * k) % i3 << 16) / k;
				if ((offsetY[i1] * l) % k3 != 0)
					j2 = (k3 - (offsetY[i1] * l) % k3 << 16) / l;
				k = (k * (imageWidth[i1] - (i2 >> 16))) / i3;
				l = (l * (imageHeight[i1] - (j2 >> 16))) / k3;
			}
			int j3 = i + j * menuMaxWidth;
			int l3 = menuMaxWidth - k;
			if (j < viewport_top) {
				int i4 = viewport_top - j;
				l -= i4;
				j = 0;
				j3 += i4 * menuMaxWidth;
				j2 += l2 * i4;
			}
			if (j + l >= viewport_bottom)
				l -= ((j + l) - viewport_bottom) + 1;
			if (i < viewport_left) {
				int j4 = viewport_left - i;
				k -= j4;
				i = 0;
				j3 += j4;
				i2 += k2 * j4;
				l3 += j4;
			}
			if (i + k >= viewport_right) {
				int k4 = ((i + k) - viewport_right) + 1;
				k -= k4;
				l3 += k4;
			}
			byte byte0 = 1;
			if (interlace) {
				byte0 = 2;
				l3 += menuMaxWidth;
				l2 += l2;
				if ((j & 1) != 0) {
					j3 += menuMaxWidth;
					l--;
				}
			}
			tranScale(pixels, surfacePixels[i1], 0, i2, j2, j3, l3, k, l, k2,
					l2, k1, byte0, j1);
		} catch (Exception _ex) {
			System.out.println("error in sprite clipping routine");
		}
	}

	public void spriteClip3(int i, int j, int k, int l, int i1, int j1) {
		try {
			int k1 = imageWidth[i1];
			int l1 = imageHeight[i1];
			int i2 = 0;
			int j2 = 0;
			int k2 = (k1 << 16) / k;
			int l2 = (l1 << 16) / l;
			if (imageTranslate[i1]) {
				int i3 = imageFullWidth[i1];
				int k3 = imageFullHeight[i1];
				k2 = (i3 << 16) / k;
				l2 = (k3 << 16) / l;
				i += ((offsetX[i1] * k + i3) - 1) / i3;
				j += ((offsetY[i1] * l + k3) - 1) / k3;
				if ((offsetX[i1] * k) % i3 != 0)
					i2 = (i3 - (offsetX[i1] * k) % i3 << 16) / k;
				if ((offsetY[i1] * l) % k3 != 0)
					j2 = (k3 - (offsetY[i1] * l) % k3 << 16) / l;
				k = (k * (imageWidth[i1] - (i2 >> 16))) / i3;
				l = (l * (imageHeight[i1] - (j2 >> 16))) / k3;
			}
			int j3 = i + j * menuMaxWidth;
			int l3 = menuMaxWidth - k;
			if (j < viewport_top) {
				int i4 = viewport_top - j;
				l -= i4;
				j = 0;
				j3 += i4 * menuMaxWidth;
				j2 += l2 * i4;
			}
			if (j + l >= viewport_bottom)
				l -= ((j + l) - viewport_bottom) + 1;
			if (i < viewport_left) {
				int j4 = viewport_left - i;
				k -= j4;
				i = 0;
				j3 += j4;
				i2 += k2 * j4;
				l3 += j4;
			}
			if (i + k >= viewport_right) {
				int k4 = ((i + k) - viewport_right) + 1;
				k -= k4;
				l3 += k4;
			}
			byte byte0 = 1;
			if (interlace) {
				byte0 = 2;
				l3 += menuMaxWidth;
				l2 += l2;
				if ((j & 1) != 0) {
					j3 += menuMaxWidth;
					l--;
				}
			}
			plotScale2(pixels, surfacePixels[i1], 0, i2, j2, j3, l3, k, l, k2,
					l2, k1, byte0, j1);
		} catch (Exception _ex) {
			System.out.println("error in sprite clipping routine");
		}
	}

	public void spriteClip4(int i, int j, int k, int l, int i1, int j1, int k1,
			int l1, boolean flag) {
		try {
			if (j1 == 0)
				j1 = 0xffffff;
			if (k1 == 0)
				k1 = 0xffffff;
			int i2 = imageWidth[i1];
			int j2 = imageHeight[i1];
			int k2 = 0;
			int l2 = 0;
			int i3 = l1 << 16;
			int j3 = (i2 << 16) / k;
			int k3 = (j2 << 16) / l;
			int l3 = -(l1 << 16) / l;
			if (imageTranslate[i1]) {
				int i4 = imageFullWidth[i1];
				int k4 = imageFullHeight[i1];
				j3 = (i4 << 16) / k;
				k3 = (k4 << 16) / l;
				int j5 = offsetX[i1];
				int k5 = offsetY[i1];
				if (flag)
					j5 = i4 - imageWidth[i1] - j5;
				i += ((j5 * k + i4) - 1) / i4;
				int l5 = ((k5 * l + k4) - 1) / k4;
				j += l5;
				i3 += l5 * l3;
				if ((j5 * k) % i4 != 0)
					k2 = (i4 - (j5 * k) % i4 << 16) / k;
				if ((k5 * l) % k4 != 0)
					l2 = (k4 - (k5 * l) % k4 << 16) / l;
				k = ((((imageWidth[i1] << 16) - k2) + j3) - 1) / j3;
				l = ((((imageHeight[i1] << 16) - l2) + k3) - 1) / k3;
			}
			int j4 = j * menuMaxWidth;
			i3 += i << 16;
			if (j < viewport_top) {
				int l4 = viewport_top - j;
				l -= l4;
				j = viewport_top;
				j4 += l4 * menuMaxWidth;
				l2 += k3 * l4;
				i3 += l3 * l4;
			}
			if (j + l >= viewport_bottom)
				l -= ((j + l) - viewport_bottom) + 1;
			int i5 = j4 / menuMaxWidth & 1;
			if (!interlace)
				i5 = 2;
			if (k1 == 0xffffff) {
				if (surfacePixels[i1] != null)
					if (!flag) {
						spritePlotTransparent(pixels, surfacePixels[i1], 0, k2,
								l2, j4, k, l, j3, k3, i2, j1, i3, l3, i5);
						return;
					} else {
						spritePlotTransparent(pixels, surfacePixels[i1], 0,
								(imageWidth[i1] << 16) - k2 - 1, l2, j4, k, l,
								-j3, k3, i2, j1, i3, l3, i5);
						return;
					}
				if (!flag) {
					spritePlotTransparent(pixels, aByteArrayArray322[i1],
							anIntArrayArray323[i1], 0, k2, l2, j4, k, l, j3,
							k3, i2, j1, i3, l3, i5);
					return;
				} else {
					spritePlotTransparent(pixels, aByteArrayArray322[i1],
							anIntArrayArray323[i1], 0, (imageWidth[i1] << 16)
									- k2 - 1, l2, j4, k, l, -j3, k3, i2, j1,
							i3, l3, i5);
					return;
				}
			}
			if (surfacePixels[i1] != null)
				if (!flag) {
					spritePlotTransparent(pixels, surfacePixels[i1], 0, k2, l2,
							j4, k, l, j3, k3, i2, j1, k1, i3, l3, i5);
					return;
				} else {
					spritePlotTransparent(pixels, surfacePixels[i1], 0,
							(imageWidth[i1] << 16) - k2 - 1, l2, j4, k, l, -j3,
							k3, i2, j1, k1, i3, l3, i5);
					return;
				}
			if (!flag) {
				spritePlotTransparent(pixels, aByteArrayArray322[i1],
						anIntArrayArray323[i1], 0, k2, l2, j4, k, l, j3, k3,
						i2, j1, k1, i3, l3, i5);
			} else {
				spritePlotTransparent(pixels, aByteArrayArray322[i1],
						anIntArrayArray323[i1], 0, (imageWidth[i1] << 16) - k2
								- 1, l2, j4, k, l, -j3, k3, i2, j1, k1, i3, l3,
						i5);
			}
		} catch (Exception _ex) {
			System.out.println("error in sprite clipping routine");
		}
	}

	public void spriteClipping(int i, int j, int k, int l, int i1, int j1,
			int k1) {
		spriteClip1(i, j, k, l, i1);
	}

	private void spritePlotTransparent(int ai[], byte abyte0[], int ai1[],
			int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2,
			int j2, int k2, int l2, int i3) {
		int i4 = j2 >> 16 & 0xff;
		int j4 = j2 >> 8 & 0xff;
		int k4 = j2 & 0xff;
		try {
			int l4 = j;
			for (int i5 = -j1; i5 < 0; i5++) {
				int j5 = (k >> 16) * i2;
				int k5 = k2 >> 16;
				int l5 = i1;
				if (k5 < viewport_left) {
					int i6 = viewport_left - k5;
					l5 -= i6;
					k5 = viewport_left;
					j += k1 * i6;
				}
				if (k5 + l5 >= viewport_right) {
					int j6 = (k5 + l5) - viewport_right;
					l5 -= j6;
				}
				i3 = 1 - i3;
				if (i3 != 0) {
					for (int k6 = k5; k6 < k5 + l5; k6++) {
						i = abyte0[(j >> 16) + j5] & 0xff;
						if (i != 0) {
							i = ai1[i];
							int j3 = i >> 16 & 0xff;
							int k3 = i >> 8 & 0xff;
							int l3 = i & 0xff;
							if (j3 == k3 && k3 == l3)
								ai[k6 + l] = ((j3 * i4 >> 8) << 16)
										+ ((k3 * j4 >> 8) << 8)
										+ (l3 * k4 >> 8);
							else
								ai[k6 + l] = i;
						}
						j += k1;
					}

				}
				k += l1;
				j = l4;
				l += menuMaxWidth;
				k2 += l2;
			}
		} catch (Exception _ex) {
			System.out.println("error in transparent sprite plot routine");
		}
	}

	private void spritePlotTransparent(int ai[], byte abyte0[], int ai1[],
			int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2,
			int j2, int k2, int l2, int i3, int j3) {
		int j4 = j2 >> 16 & 0xff;
		int k4 = j2 >> 8 & 0xff;
		int l4 = j2 & 0xff;
		int i5 = k2 >> 16 & 0xff;
		int j5 = k2 >> 8 & 0xff;
		int k5 = k2 & 0xff;
		try {
			int l5 = j;
			for (int i6 = -j1; i6 < 0; i6++) {
				int j6 = (k >> 16) * i2;
				int k6 = l2 >> 16;
				int l6 = i1;
				if (k6 < viewport_left) {
					int i7 = viewport_left - k6;
					l6 -= i7;
					k6 = viewport_left;
					j += k1 * i7;
				}
				if (k6 + l6 >= viewport_right) {
					int j7 = (k6 + l6) - viewport_right;
					l6 -= j7;
				}
				j3 = 1 - j3;
				if (j3 != 0) {
					for (int k7 = k6; k7 < k6 + l6; k7++) {
						i = abyte0[(j >> 16) + j6] & 0xff;
						if (i != 0) {
							i = ai1[i];
							int k3 = i >> 16 & 0xff;
							int l3 = i >> 8 & 0xff;
							int i4 = i & 0xff;
							if (k3 == l3 && l3 == i4)
								ai[k7 + l] = ((k3 * j4 >> 8) << 16)
										+ ((l3 * k4 >> 8) << 8)
										+ (i4 * l4 >> 8);
							else if (k3 == 255 && l3 == i4)
								ai[k7 + l] = ((k3 * i5 >> 8) << 16)
										+ ((l3 * j5 >> 8) << 8)
										+ (i4 * k5 >> 8);
							else
								ai[k7 + l] = i;
						}
						j += k1;
					}

				}
				k += l1;
				j = l5;
				l += menuMaxWidth;
				l2 += i3;
			}

		} catch (Exception _ex) {
			System.out.println("error in transparent sprite plot routine");
		}
	}

	private void spritePlotTransparent(int ai[], int ai1[], int i, int j,
			int k, int l, int i1, int j1, int k1, int l1, int i2, int j2,
			int k2, int l2, int i3) {
		int i4 = j2 >> 16 & 0xff;
		int j4 = j2 >> 8 & 0xff;
		int k4 = j2 & 0xff;
		try {
			int l4 = j;
			for (int i5 = -j1; i5 < 0; i5++) {
				int j5 = (k >> 16) * i2;
				int k5 = k2 >> 16;
				int l5 = i1;
				if (k5 < viewport_left) {
					int i6 = viewport_left - k5;
					l5 -= i6;
					k5 = viewport_left;
					j += k1 * i6;
				}
				if (k5 + l5 >= viewport_right) {
					int j6 = (k5 + l5) - viewport_right;
					l5 -= j6;
				}
				i3 = 1 - i3;
				if (i3 != 0) {
					for (int k6 = k5; k6 < k5 + l5; k6++) {
						i = ai1[(j >> 16) + j5];
						if (i != 0) {
							int j3 = i >> 16 & 0xff;
							int k3 = i >> 8 & 0xff;
							int l3 = i & 0xff;
							if (j3 == k3 && k3 == l3)
								ai[k6 + l] = ((j3 * i4 >> 8) << 16)
										+ ((k3 * j4 >> 8) << 8)
										+ (l3 * k4 >> 8);
							else
								ai[k6 + l] = i;
						}
						j += k1;
					}

				}
				k += l1;
				j = l4;
				l += menuMaxWidth;
				k2 += l2;
			}
		} catch (Exception _ex) {
			System.out.println("error in transparent sprite plot routine");
		}
	}

	private void spritePlotTransparent(int ai[], int ai1[], int i, int j,
			int k, int l, int i1, int j1, int k1, int l1, int i2, int j2,
			int k2, int l2, int i3, int j3) {
		int j4 = j2 >> 16 & 0xff;
		int k4 = j2 >> 8 & 0xff;
		int l4 = j2 & 0xff;
		int i5 = k2 >> 16 & 0xff;
		int j5 = k2 >> 8 & 0xff;
		int k5 = k2 & 0xff;
		try {
			int l5 = j;
			for (int i6 = -j1; i6 < 0; i6++) {
				int j6 = (k >> 16) * i2;
				int k6 = l2 >> 16;
				int l6 = i1;
				if (k6 < viewport_left) {
					int i7 = viewport_left - k6;
					l6 -= i7;
					k6 = viewport_left;
					j += k1 * i7;
				}
				if (k6 + l6 >= viewport_right) {
					int j7 = (k6 + l6) - viewport_right;
					l6 -= j7;
				}
				j3 = 1 - j3;
				if (j3 != 0) {
					for (int k7 = k6; k7 < k6 + l6; k7++) {
						i = ai1[(j >> 16) + j6];
						if (i != 0) {
							int k3 = i >> 16 & 0xff;
							int l3 = i >> 8 & 0xff;
							int i4 = i & 0xff;
							if (k3 == l3 && l3 == i4)
								ai[k7 + l] = ((k3 * j4 >> 8) << 16)
										+ ((l3 * k4 >> 8) << 8)
										+ (i4 * l4 >> 8);
							else if (k3 == 255 && l3 == i4)
								ai[k7 + l] = ((k3 * i5 >> 8) << 16)
										+ ((l3 * j5 >> 8) << 8)
										+ (i4 * k5 >> 8);
							else
								ai[k7 + l] = i;
						}
						j += k1;
					}

				}
				k += l1;
				j = l5;
				l += menuMaxWidth;
				l2 += i3;
			}
		} catch (Exception _ex) {
			System.out.println("error in transparent sprite plot routine");
		}
	}

	@Override
	public void startProduction(ImageConsumer imageconsumer) {
		addConsumer(imageconsumer);
	}

	public int stringHeight(int messageType) {
		if (messageType == 0)
			return 12;
		if (messageType == 1)
			return 14;
		if (messageType == 2)
			return 14;
		if (messageType == 3)
			return 15;
		if (messageType == 4)
			return 15;
		if (messageType == 5)
			return 19;
		if (messageType == 6)
			return 24;
		if (messageType == 7)
			return 29;
		else
			return method261(messageType);
	}

	public int stringWidth(String s, int i) {
		int j = 0;
		byte[] abyte0 = fontArray[i];
		for (int k = 0; k < s.length(); k++) {
			if ((s.charAt(k) == '@') && (k + 4 < s.length())
					&& (s.charAt(k + 4) == '@'))
				k += 4;
			else if ((s.charAt(k) == '~') && (k + 4 < s.length())
					&& (s.charAt(k + 4) == '~')) {
				k += 4;
			} else if ((s.charAt(k) == '~') && (k + 5 < s.length())
					&& (s.charAt(k + 5) == '~')) {
				k += 5;
			} else {
				j += abyte0[(charIndexes[s.charAt(k)] + 7)];
			}
		}
		return j;
	}

	private void tranScale(int ai[], int ai1[], int i, int j, int k, int l,
			int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2) {
		int i3 = 256 - l2;
		try {
			int j3 = j;
			for (int k3 = -k1; k3 < 0; k3 += k2) {
				int l3 = (k >> 16) * j2;
				for (int i4 = -j1; i4 < 0; i4++) {
					i = ai1[(j >> 16) + l3];
					if (i != 0) {
						int j4 = ai[l];
						ai[l++] = ((i & 0xff00ff) * l2 + (j4 & 0xff00ff) * i3 & 0xff00ff00)
								+ ((i & 0xff00) * l2 + (j4 & 0xff00) * i3 & 0xff0000) >> 8;
					} else {
						l++;
					}
					j += l1;
				}

				k += i2;
				j = j3;
				l += i1;
			}
		} catch (Exception _ex) {
			System.out.println("error in tran_scale");
		}
	}
}
