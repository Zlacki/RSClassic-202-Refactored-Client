package net.swiftpk.client.gfx;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import net.swiftpk.client.cache.Stream;

public class Sprite {
	/**
	 * Declares which colour to render transparent
	 */
	private static final int TRANSPARENT = Color.BLACK.getRGB();
	/**
	 * Holds all the pixel values for the sprite image
	 */
	private final int[] pixels;
	/**
	 * The sprite's width
	 */
	private final int width;
	/**
	 * The sprite's height
	 */
	private final int height;
	/**
	 * The id of this sprite
	 */
	private final int id = -1;
	/**
	 * Whether or not this sprite requires a coordinate shift when rendering
	 */
	private boolean requiresShift;
	/**
	 * If it does require a shift, this is how many x pixels it shifts by
	 */
	private int xShift = 0;
	/**
	 * If it does require a shift, this is how many y pixels it shifts by
	 */
	private int yShift = 0;
	/**
	 * This is not a fully-understood variable, but it seems to usually
	 * represent the variable 'width's value
	 */
	private int width2 = 0;
	/**
	 * This is not a fully-understood variable, but it seems to usually
	 * represent the variable 'height's value
	 */
	private int height2 = 0;

	/**
	 * Constructs a new sprite with no variable settings
	 */
	public Sprite() {
		pixels = new int[0];
		width = 0;
		height = 0;
	}

	/**
	 * Constructs a new sprite with the given pixel byteBuffer, width and height
	 * @param pixels
	 * @param width
	 * @param height
	 */
	public Sprite(int[] pixels, int width, int height) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}

	/**
	 * Sets the width and height 'clones' to the given values
	 *
	 * @param width2
	 *            the new width2 value
	 * @param height2
	 *            the new height2 value
	 */
	public void setSomething(int width2, int height2) {
		this.width2 = width2;
		this.height2 = height2;
	}

	/**
	 * @return this sprite's width2 var
	 */
	public int getWidth2() {
		return width2;
	}

	/**
	 * @return this sprite's height2 var
	 */
	public int getHeight2() {
		return height2;
	}

	/**
	 * @return this sprite's ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets this sprite's shift vars
	 *
	 * @param xShift
	 *            how far to shift this sprite along x axis
	 * @param yShift
	 *            how far to shift this sprite along y axis
	 */
	public void setShift(int xShift, int yShift) {
		this.xShift = xShift;
		this.yShift = yShift;
	}

	/**
	 * Sets whether or not this sprite needs to shift
	 *
	 * @param requiresShift
	 *            the shift flag
	 */
	public void setRequiresShift(boolean requiresShift) {
		this.requiresShift = requiresShift;
	}

	/**
	 * @return if this sprite needs to be shifted
	 */
	public boolean requiresShift() {
		return requiresShift;
	}

	/**
	 * @return this sprite's x shift value
	 */
	public int getXShift() {
		return xShift;
	}

	/**
	 * @return this sprite's y shift value
	 */
	public int getYShift() {
		return yShift;
	}

	/**
	 * @return all this sprite's pixel byteBuffer
	 */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * @param i
	 *            the array index to get
	 * @return the given pixel from the pixel byteBuffer array
	 */
	public int getPixel(int i) {
		if (i < 0 || i >= pixels.length) {
			System.out.println("getPixel(" + i + ") out of bounds: max = "
					+ pixels.length);
			return -1;
		}

		return pixels[i];
	}

	/**
	 * Sets the given pixel index to the given value
	 *
	 * @param i
	 *            the pixel index
	 * @param val
	 *            the pixel value
	 */
	public void setPixel(int i, int val) {
		if (i < 0 || i >= pixels.length) {
			System.out.println("setPixel(" + i + ", " + val
					+ ") out of bounds: max = " + pixels.length);
			return;
		}

		pixels[i] = val;
	}

	/**
	 * @return this sprite's width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return this sprite's height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return this sprite's byteBuffer into a buffered image
	 */
	public BufferedImage toImage() {
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				img.setRGB(x, y, pixels[x + y * width]);
		}

		return img;
	}

	/**
	 * @param img
	 * @return a sprite object from a buffered image's byteBuffer
	 */
	public static Sprite fromImage(BufferedImage img) {
		int[] pixels = new int[img.getWidth() * img.getHeight()];

		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int rgb = img.getRGB(x, y);

				if (rgb == TRANSPARENT)
					rgb = 0;

				pixels[x + y * img.getWidth()] = rgb;
			}
		}

		return new Sprite(pixels, img.getWidth(), img.getHeight());
	}

	/**
	 * Create a new sprite from raw byteBuffer packed into the given ByteBuffer
	 *
	 * @param stream
	 * @return
     */
	public static Sprite unpack(Stream stream) {

		int width = stream.readInt();
		int height = stream.readInt();
		boolean requiresShift = stream.read() == 1;



		int xShift = stream.readInt();
		int yShift = stream.readInt();

		int width2 = stream.readInt();
		int height2 = stream.readInt();

		int[] pixels = new int[width * height];

		for (int pixel = 0; pixel < pixels.length; pixel++)
			pixels[pixel] = stream.readInt();

		Sprite sprite = new Sprite(pixels, width, height);
		sprite.setRequiresShift(requiresShift);
		sprite.setShift(xShift, yShift);
		sprite.setSomething(width2, height2);

		return sprite;
	}
}
