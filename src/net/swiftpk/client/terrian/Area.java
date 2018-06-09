package net.swiftpk.client.terrian;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Area {

	private Polygon area;
	private int plane;

	/**
	 * @param swX
	 *            The X axle of the <i>South West</i> <b>Tile</b> of the
	 *            <b>Area</b>
	 * @param swY
	 *            The Y axle of the <i>South West</i> <b>Tile</b> of the
	 *            <b>Area</b>
	 * @param neX
	 *            The X axle of the <i>North East</i> <b>Tile</b> of the
	 *            <b>Area</b>
	 * @param neY
	 *            The Y axle of the <i>North East</i> <b>Tile</b> of the
	 *            <b>Area</b>
	 */
	public Area(int swX, int swY, int neX, int neY) {
		this(new Tile(swX, swY), new Tile(neX, neY), 0);
	}

	/**
	 * @param sw
	 *            The <i>South West</i> <b>Tile</b> of the <b>Area</b>
	 * @param ne
	 *            The <i>North East</i> <b>Tile</b> of the <b>Area</b>
	 */
	public Area(Tile sw, Tile ne) {
		this(sw, ne, 0);
	}

	/**
	 * @param sw
	 *            The <i>South West</i> <b>Tile</b> of the <b>Area</b>
	 * @param ne
	 *            The <i>North East</i> <b>Tile</b> of the <b>Area</b>
	 * @param plane
	 *            The plane of the <b>Area</b>.
	 */
	public Area(Tile sw, Tile ne, int plane) {
		this(new Tile[] { sw, new Tile(ne.getX() + 1, sw.getY()),
				new Tile(ne.getX() + 1, ne.getY() + 1),
				new Tile(sw.getX(), ne.getY() + 1) }, plane);
	}

	/**
	 * @param tiles
	 *            An Array containing of <b>Tiles</b> forming a polygon shape.
	 */
	public Area(Tile[] tiles) {
		this(tiles, 0);
	}

	/**
	 * @param tiles
	 *            An Array containing of <b>Tiles</b> forming a polygon shape.
	 * @param plane
	 *            The plane of the <b>Area</b>.
	 */
	public Area(Tile[] tiles, int plane) {
		this.area = tileArrayToPolygon(tiles);
		this.plane = plane;
	}

	/**
	 * @param x
	 *            The x location of the <b>Tile</b> that will be checked.
	 * @param y
	 *            The y location of the <b>Tile</b> that will be checked.
	 * @return True if the <b>Area</b> contains the given <b>Tile</b>.
	 */
	public boolean contains(int x, int y) {
		return this.contains(new Tile(x, y));
	}

	/**
	 * @param plane
	 *            The plane to check.
	 * @param tiles
	 *            The <b>Tile(s)</b> that will be checked.
	 * @return True if the <b>Area</b> contains the given <b>Tile(s)</b>.
	 */
	public boolean contains(int plane, Tile... tiles) {
		return this.plane == plane && this.contains(tiles);
	}

	/**
	 * @param tiles
	 *            The <b>Tile(s)</b> that will be checked.
	 * @return True if the <b>Area</b> contains the given <b>Tile(s)</b>.
	 */
	public boolean contains(Tile... tiles) {
		Tile[] areaTiles = this.getTileArray();
		for (Tile check : tiles) {
			for (Tile space : areaTiles) {
				if (check.equals(space)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param curr
	 *            first <b>Tile</b>
	 * @param dest
	 *            second <b>Tile</b>
	 * @return the distance between the first and the second Tile
	 */
	private double distanceBetween(Tile curr, Tile dest) {
		return Math.sqrt((curr.getX() - dest.getX())
				* (curr.getX() - dest.getX()) + (curr.getY() - dest.getY())
				* (curr.getY() - dest.getY()));
	}

	/**
	 * @return The bounding box of the <b>Area</b>.
	 */
	public Rectangle getBounds() {
		return new Rectangle(this.area.getBounds().x + 1,
				this.area.getBounds().y + 1, this.getWidth(), this.getHeight());
	}

	/**
	 * @return The central <b>Tile</b> of the <b>Area</b>.
	 */
	public Tile getCentralTile() {
		if (area.npoints < 1)
			return null;
		int totalX = 0, totalY = 0;
		for (int i = 0; i < area.npoints; i++) {
			totalX += area.xpoints[i];
			totalY += area.ypoints[i];
		}
		return new Tile(Math.round(totalX / area.npoints), Math.round(totalY
				/ area.npoints));
	}

	/**
	 * @return The distance between the the <b>Tile</b> that's most <i>South</i>
	 *         and the <b>Tile</b> that's most <i>North</i>.
	 */
	public int getHeight() {
		return this.area.getBounds().height;
	}

	/**
	 * @param base
	 * @return The nearest <b>Tile</b> in the <b>Area</b> to the given
	 *         <b>Tile</b>.
	 */
	public Tile getNearestTile(Tile base) {
		Tile[] tiles = this.getTileArray();
		Tile cur = null;
		double dist = -1;
		for (Tile tile : tiles) {
			double distTmp = distanceBetween(tile, base);
			if (cur == null) {
				dist = distTmp;
				cur = tile;
			} else if (distTmp < dist) {
				cur = tile;
				dist = distTmp;
			}
		}
		return cur;
	}

	/**
	 * @return The plane of the <b>Area</b>.
	 */
	public int getPlane() {
		return plane;
	}

	public Tile getRandomTile() {
		if (getTileArray() != null) {
			return getTileArray()[random(0, getTileArray().length - 1)];
		}
		return null;
	}

	/**
	 * @return The <b>Tiles</b> the <b>Area</b> contains.
	 */
	public Tile[] getTileArray() {
		ArrayList<Tile> list = new ArrayList<>();
		for (int x = this.getX(); x <= (this.getX() + this.getWidth()); x++) {
			for (int y = this.getY(); y <= (this.getY() + this.getHeight()); y++) {
				if (this.area.contains(x, y)) {
					list.add(new Tile(x, y));
				}
			}
		}
		Tile[] tiles = new Tile[list.size()];
		for (int i = 0; i < list.size(); i++)
			tiles[i] = list.get(i);
		return tiles;
	}

	/**
	 * @return The <b>Tiles</b> the <b>Area</b> contains.
	 */
	public Tile[][] getTiles() {
		Tile[][] tiles = new Tile[this.getWidth() + 1][this.getHeight() + 1];
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				if (this.area.contains(this.getX() + i, this.getY() + j)) {
					tiles[i][j] = new Tile(this.getX() + i, this.getY() + j);
				}
			}
		}
		return tiles;
	}

	/**
	 * @return The distance between the the <b>Tile</b> that's most <i>East</i>
	 *         and the <b>Tile</b> that's most <i>West</i>.
	 */
	public int getWidth() {
		return this.area.getBounds().width;
	}

	/**
	 * @return The X axle of the <b>Tile</b> that's most <i>West</i>.
	 */
	public int getX() {
		return this.area.getBounds().x;
	}

	/**
	 * @return The Y axle of the <b>Tile</b> that's most <i>South</i>.
	 */
	public int getY() {
		return this.area.getBounds().y;
	}

	private int random(final int min, final int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}

	/**
	 * Converts an shape made of <b>Tile</b> to a polygon.
	 *
	 * @param tiles
	 *            The <b>Tile</b> of the Polygon.
	 * @return The Polygon of the <b>Tile</b>.
	 */
	private Polygon tileArrayToPolygon(Tile[] tiles) {
		Polygon poly = new Polygon();
		for (Tile t : tiles) {
			poly.addPoint(t.getX(), t.getY());
		}
		return poly;
	}

}
