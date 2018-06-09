package net.swiftpk.client.gfx.uis;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.swiftpk.client.cache.Data;
import net.swiftpk.client.gfx.GraphicalComponent;
import net.swiftpk.client.gfx.GraphicalOverlay;
import net.swiftpk.client.gfx.action.DragListener;
import net.swiftpk.client.gfx.action.ScrollListener;
import net.swiftpk.client.gfx.components.Box;
import net.swiftpk.client.gfx.components.DrawString;
import net.swiftpk.client.gfx.components.GameFrame;
import net.swiftpk.client.gfx.components.ScrollBar;
import net.swiftpk.client.gfx.components.TextBox;
import net.swiftpk.client.mudclient;

public class BankUI extends GraphicalOverlay {
	public class BankItem {
		private int pos;
		private int id;
		private int amount;

		public BankItem(int pos, int id, int amount) {
			this.setPos(pos);
			this.setId(id);
			this.setAmount(amount);
		}

		public int getAmount() {
			return amount;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			if(getId() >= 0)
				return Data.itemName[getId()];
			return null;
		}

		public int getPos() {
			return pos;
		}

		public final void setAmount(int amount) {
			this.amount = amount;
		}

		public final void setId(int id) {
			this.id = id;
		}

		public final void setPos(int pos) {
			this.pos = pos;
		}
	}

	private mudclient<?> client;
//	public BankItem[] items = new BankItem[197];
	public Map<Integer, BankItem> items = new HashMap<>();
	private Box[] bank_slots = new Box[35];
	private GameFrame frame;
	public ScrollBar scroll;
	private int index;
	private TextBox text;

	@Override
	public final void setMenu(boolean b) {
		super.setMenu(b);
	}

	@Override
	public final boolean add(GraphicalComponent... comp) {
		return super.add(comp);
	}

	public BankUI(mudclient<?> mc) {
		super(mc);
		client = mc;
		setMenu(true);
		frame = new GameFrame("Bank", new Rectangle(85, 40, 380, 220));
		add(frame);
		scroll = new ScrollBar(new Rectangle(360, 11, 14, 200));
		scroll.setSize(49);
		generateBoxes();
		sortBankItems();
		frame.add(scroll);
		frame.setDragListener(new DragListener() {
			@Override
			public boolean onDragging(int startX, int startY) {
				return false;
			}

			@Override
			public void stopDragging() {
			}
		});
		scroll.addScrollListener(new ScrollListener() {
			@Override
			public void onScrollUpdate(int index) {
				if(index >= 0 && index <= 24)
					sortBankItems();
				else if(index > 24)
					scroll.setFirstIndex(24);
				else
					scroll.setFirstIndex(0);
			}

			@Override
			public void scrolling(int type) {
			}
		});
		text = new TextBox(new Rectangle(10, 210, 120, 20));
		text.addKeyListener((char c, int key) -> {
			if(client.inputBoxType != 0)
				return false;
			String validCharSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_ ";
			if(key == 8) {
				text.append("DELETE");
				sortBankItems();
				return true;
			}
			if(text.getText().length() < 30)
				for(int k = 0; k < validCharSet.length(); k++)
					if(c == validCharSet.charAt(k)) {
						scroll.setFirstIndex(0);
						text.append(c + "");
						sortBankItems();
						return true;
					}
			return false;
		});
		frame.add(text);
		DrawString close = new DrawString("Close window", false, new Rectangle(
				300, 0, client.surface.stringWidth("Close window", 1), client.surface.stringHeight(1)));
		close.setFill(close.convertToJag(255, 255, 255));
		close.setAction((int x, int y, int button) -> {
			client.streamClass.addNewFrame(215);
			client.streamClass.formatCurrentFrame();
			setVisible(false);
		});
		frame.add(close);
		DrawString depositInventory = new DrawString("Deposit inventory", false, new Rectangle(
				150, 213, client.surface.stringWidth("Deposit inventory", 1), client.surface.stringHeight(1)));
		depositInventory.setFill(depositInventory.convertToJag(255, 255, 255));
		depositInventory.setAction((int x, int y, int button) -> {
			client.streamClass.addNewFrame(153);
			client.streamClass.formatCurrentFrame();
		});
		frame.add(depositInventory);
	}

	public List<BankItem> filterName(String name) {
		List<BankItem> found = new ArrayList<>();
		items.values().stream().filter((item) -> (item.getName() != null
				&& item.getName().toLowerCase()
						.contains(name.toLowerCase()))).forEach((item) -> {
							found.add(item);
		});
		return found;
	}

	public final void generateBoxes() {
		for(int i = 0; i < 197; i++)
			items.put(i, new BankItem(i, -1, -1));
		int x = 10, y = 10;
		index = 0;
		for(int indexY = 0; indexY < 5; indexY++) {
			for(int indexBox = 1; indexBox < 8; indexBox++) {
				bank_slots[index] = new Box(new Rectangle(x, y + 10, 49, 38));
				bank_slots[index].setItem(items.get(index));
				bank_slots[index].setAction((int x1, int y1, int button) -> {
					int newIndex = ((x1 - (frame.getX() + 10)) / 49) + (((y1 - (frame.getY() + 20)) / 38) * 7);
					if(button == 1) {
						if(!client.rightClickOptions) {
							client.getStreamClass()
									.addNewFrame(
											client.inventoryCount(bank_slots[newIndex]
													.getItemId()) > 0 ? 152
												: 224);
							client.getStreamClass().addShort(
									bank_slots[newIndex].getItemId());
							client.getStreamClass()
									.addInt(
											client.inventoryCount(bank_slots[newIndex]
													.getItemId()) > 0 ? client
															.inventoryCount(bank_slots[newIndex]
																	.getItemId())
												: 1);
							client.getStreamClass().formatCurrentFrame();
						} else {
							for(int ix = 0; ix < client.menuLength; ix++) {
								int k = client.tradeWindowX + 2;
								int i1 = client.tradeWindowY + 11
										+ (ix + 1) * 15;
								if(x1 <= k - 2 || y1 <= i1 - 12 || y1 >= i1 + 4 || x1 >= (k - 3) + client.menuWidth) {
									continue;
								}
								client.menuClick(ix);
							}
							client.tradeWindowX = -100;
							client.tradeWindowY = -100;
							client.mouseButtonClick = 0;
							client.rightClickOptions = false;
							client.valueSet = false;
						}
					} else if(button == 2 && !client.rightClickOptions) {
						client.tradeWindowX = x1;
						client.tradeWindowY = y1;
						for(int jx = 0; jx < client.menuLength; jx++) {
							client.menuText1[jx] = null;
							client.menuText2[jx] = null;
							client.menuActionVariable[jx] = -1;
							client.menuActionVariable2[jx] = -1;
							client.menuID[jx] = -1;
						}
						String name = Data.itemName[bank_slots[newIndex]
								.getItemId()];
						client.menuLength = 0;
						client.menuText1[client.menuLength] = "Withdraw 1 @lre@";
						client.menuText2[client.menuLength] = name;
						client.menuID[client.menuLength] = 786;
						client.menuActionVariable[client.menuLength] = bank_slots[newIndex]
								.getItemId();
						client.menuActionVariable2[client.menuLength] = 1;
						client.menuLength++;
						client.menuText1[client.menuLength] = "Withdraw All @lre@";
						client.menuText2[client.menuLength] = name;
						client.menuID[client.menuLength] = 786;
						client.menuActionVariable[client.menuLength] = bank_slots[newIndex]
								.getItemId();
						client.menuActionVariable2[client.menuLength] = bank_slots[newIndex]
								.getItemAmount();
						client.menuLength++;
						client.menuText1[client.menuLength] = "Withdraw X @lre@";
						client.menuText2[client.menuLength] = name;
						client.menuID[client.menuLength] = 787;
						client.menuActionVariable[client.menuLength] = bank_slots[newIndex]
								.getItemId();
						client.menuLength++;
						client.menuText1[client.menuLength] = "Deposit 1 @lre@";
						client.menuText2[client.menuLength] = name;
						client.menuID[client.menuLength] = 788;
						client.menuActionVariable[client.menuLength] = bank_slots[newIndex]
								.getItemId();
						client.menuActionVariable2[client.menuLength] = 1;
						client.menuLength++;
						client.menuText1[client.menuLength] = "Deposit All @lre@";
						client.menuText2[client.menuLength] = name;
						client.menuID[client.menuLength] = 788;
						client.menuActionVariable[client.menuLength] = bank_slots[newIndex]
								.getItemId();
						client.menuActionVariable2[client.menuLength] = client
								.inventoryCount(bank_slots[newIndex]
										.getItemId());
						client.menuLength++;
						client.menuText1[client.menuLength] = "Deposit X @lre@";
						client.menuText2[client.menuLength] = name;
						client.menuID[client.menuLength] = 790;
						client.menuActionVariable[client.menuLength] = bank_slots[newIndex]
								.getItemId();
						client.menuLength++;
						client.rightClickOptions = true;
					}
				});
				frame.add(bank_slots[index]);
				x = 10 + (49 * indexBox);
				index++;
			}
			x = 10;
			y += 38;
		}
	}

	public final void sortBankItems() {
		if(text != null && !text.getText().equals("*")) {
			int start = 0;
			for(Box box : bank_slots) {
				for(GraphicalComponent gc : frame.getComponents()) {
					if(gc.equals(box)) {
						box.setItem(new BankItem(start, -1, -1));
					}
				}
			}
			for(BankItem found : filterName(text.getText().replace("*", ""))) {
				if(start < 35) {
					Box box = bank_slots[start];
					if(box != null) {
						box.setItem(found);
						start++;
					}
				}
			}
		} else {
			int start = scroll.getIndex() * 7;
			for(Box box : bank_slots) {
				for(GraphicalComponent gc : frame.getComponents()) {
					if(gc.equals(box)) {
						if(start < 197) {
							if(items.containsKey(start))
								box.setItem(items.get(start));
							else
								box.setItem(new BankItem(start, -1, -1));
							start++;
						}
					}
				}
			}
		}
	}
}
