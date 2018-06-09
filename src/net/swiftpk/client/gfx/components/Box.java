package net.swiftpk.client.gfx.components;

import java.awt.Rectangle;
import net.swiftpk.client.cache.Data;
import net.swiftpk.client.gfx.GraphicalComponent;
import net.swiftpk.client.gfx.uis.BankUI.BankItem;
import net.swiftpk.client.mudclient;

public class Box extends GraphicalComponent {

	private String text;

	private int textColor;

	private int fontSize = 3;

	public boolean selected = false;
	
	private BankItem item;

	public BankItem getItem() {
		return item;
	}

	public void setItem(BankItem item) {
		this.item = item;
	}

	public Box(Rectangle bounds) {
		setBoundarys(bounds);
	}
	
	@Override
	public final void setBoundarys(Rectangle bounds) {
		super.setBoundarys(bounds);
	}

	public int getFontSize() {
		return fontSize;
	}

	public int getItemAmount() {
		return item.getAmount();
	}

	public int getItemId() {
		return item.getId();
	}

	public int getTextColor() {
		return textColor;
	}

	@Override
	public void render() {
		if (!visible)
			return;

		mc.surface
				.drawBoxAlpha(getX(), getY(), getWidth(), getHeight(),
						hovering ? this.getFillHovering() : getFill(),
						this.getOpaque());
		mc.surface.drawBoxEdge(getX() - 1, getY() - 1, getWidth() + 1,
				getHeight() + 1, this.getBoarder());
		if (text != null) {
			mc.surface.drawString(text, getX(), getY(), getFontSize(),
					this.getTextColor());
		}
		if (getItemId() != -1) {
			mc.surface.spriteClip4(getX(), getY(), 48, 32,
					mudclient.SPRITE_ITEM
							+ Data.itemInventoryPicture[getItemId()],
					Data.itemPictureMask[getItemId()], 0, 0, false);
			mc.surface.drawString(
					mc.insertCommas(String.valueOf(getItemAmount())),
					getX() + 1, getY() + 10, 1, 65280);
			mc.surface.drawBoxTextRight(mc.insertCommas(String.valueOf(mc
					.inventoryCount(getItemId()))), getX() + 46, getY() + 33,
					1, 65535);
		}
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setItemAmount(int itemamount) {
		item.setAmount(itemamount);
	}

	public void setItemId(int itemid) {
		item.setId(itemid);
	}

	public void setText(String text, int color) {
		this.text = text;
		this.setTextColor(color);
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
}