package net.swiftpk.client.terrian;

public class Tile {
	public int x;
	public int y;

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Tile copy() {
		return this;
	}

	public int distanceTo(int x1, int y1, int x2, int y2) {
		double tx = Math.abs(x1 - x2);
		double ty = Math.abs(y1 - y2);

		tx = Math.pow(tx, 2);
		ty = Math.pow(ty, 2);
		return (int) Math.sqrt(tx + ty);
	}

	public int distanceTo(Tile tile) {
		return (int) Math.hypot(tile.getX() - this.getX(),
				tile.getY() - this.getY());
	}

	boolean equals(Tile t) {
		return t.x == x && t.y == y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
