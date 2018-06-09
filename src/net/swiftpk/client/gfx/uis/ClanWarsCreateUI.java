package net.swiftpk.client.gfx.uis;

import java.awt.Rectangle;
import net.swiftpk.client.gfx.GraphicalComponent;
import net.swiftpk.client.gfx.GraphicalOverlay;
import net.swiftpk.client.gfx.components.Box;
import net.swiftpk.client.gfx.components.Button;
import net.swiftpk.client.gfx.components.CheckBox;
import net.swiftpk.client.gfx.components.DrawString;
import net.swiftpk.client.gfx.components.GameFrame;
import net.swiftpk.client.mudclient;

public class ClanWarsCreateUI extends GraphicalOverlay {
	private int currenttime = 4;
	private int maxtime = 7;
	private int mintime = 2;
	
	@Override
	public final boolean add(GraphicalComponent... comp) {
		return super.add(comp);
	}

	public ClanWarsCreateUI(mudclient<?> mc) {
		super(mc);
		GameFrame frame = new GameFrame("Clan Wars Creation", new Rectangle(
				110, 40, 312, 170));
		frame.setOpaque(40);
		add(frame);

		final CheckBox checkBoxRanged = new CheckBox(125, 90);
		checkBoxRanged.setText("Ranged (OFF)"); // 500 color
		checkBoxRanged.setAction((int x, int y, int button) -> {
			checkBoxRanged.setSelected(!checkBoxRanged.isSelected());
			String isOn = checkBoxRanged.isSelected() ? "ON" : "OFF";
			checkBoxRanged.setText("Ranged (" + isOn + ")"); // 500 color
		});
		final CheckBox checkBoxMagic = new CheckBox(125, 110);
		checkBoxMagic.setText("Magic (OFF)"); // 500 color
		checkBoxMagic.setAction((int x, int y, int button) -> {
			checkBoxMagic.setSelected(!checkBoxMagic.isSelected());
			String isOn = checkBoxMagic.isSelected() ? "ON" : "OFF";
			checkBoxMagic.setText("Magic (" + isOn + ")"); // 500 color
		});
		final CheckBox checkBoxPrayer = new CheckBox(125, 130);
		checkBoxPrayer.setText("Prayer (OFF)"); // 500 color
		checkBoxPrayer.setAction((int x, int y, int button) -> {
			checkBoxPrayer.setSelected(!checkBoxPrayer.isSelected());
			String isOn = checkBoxPrayer.isSelected() ? "ON" : "OFF";
			checkBoxPrayer.setText("Prayer (" + isOn + ")"); // 500 color
		});
		final CheckBox checkBoxArmour = new CheckBox(125, 150);
		checkBoxArmour.setText("Armour (ON)"); // 500 color
		checkBoxArmour.setSelected(true);
		checkBoxArmour.setAction((int x, int y, int button) -> {
			checkBoxArmour.setSelected(!checkBoxArmour.isSelected());
			String isOn = checkBoxArmour.isSelected() ? "ON" : "OFF";
			checkBoxArmour.setText("Armour (" + isOn + ")"); // 500 color
		});
		final DrawString timer = new DrawString("Timer: " + currenttime, false,
				new Rectangle(260, 102, 50, 50));
		timer.setFill(timer.convertToJag(48, 244, 255));

		Button timerup = new Button(new Rectangle(325, 90, 15, 7));
		timerup.setOpaque(190);
		timerup.setAction((int x, int y, int button) -> {
			if (currenttime < maxtime && currenttime >= mintime) {
				currenttime++;
				timer.setText("Timer: " + currenttime);
			}
		});

		Button timerdown = new Button(new Rectangle(325, 98, 15, 7));
		timerdown.setOpaque(190);
		timerdown.setAction((int x, int y, int button) -> {
			if (currenttime <= maxtime && currenttime > mintime) {
				currenttime--;
				timer.setText("Timer: " + currenttime);
			}
		});

		Box boxoptions = new Box(new Rectangle(120, 85, 127, 85));
		boxoptions.setText("Player Options", 0xffffff);

		Box boxsettings = new Box(new Rectangle(255, 85, 140, 85));
		boxsettings.setText("War Settings", 0xffffff);

		final CheckBox checkBoxNPCS = new CheckBox(260, 110);
		checkBoxNPCS.setText("NPCS (ON)"); // , 500 color
		checkBoxNPCS.setSelected(true);
		checkBoxNPCS.setAction((int x, int y, int button) -> {
			checkBoxNPCS.setSelected(!checkBoxNPCS.isSelected());
			String isOn = checkBoxNPCS.isSelected() ? "ON" : "OFF";
			checkBoxNPCS.setText("NPCS (" + isOn + ")"); // , 500 color
		});

		final CheckBox respawn = new CheckBox(260, 130);
		respawn.setText("Respawn (OFF)");// , 500 color
		respawn.setAction((int x, int y, int button) -> {
			respawn.setSelected(!respawn.isSelected());
			String isOn = respawn.isSelected() ? "ON" : "OFF";
			respawn.setText("Respawn (" + isOn + ")"); // , 500 color
		});

		Button create = new Button(new Rectangle(350, 190, 50, 20));
		create.setOpaque(210);
		create.setText("Create"); // color 0
		create.setAction((int x, int y, int button) -> {
			setVisible(false);
		});
		Button cancel = new Button(new Rectangle(300, 190, 50, 20));
		cancel.setOpaque(210);
		cancel.setText("Cancel"); // color 0
		cancel.setAction((int x, int y, int button) -> {
			setVisible(false);
		});

		add(frame, boxoptions, boxsettings, checkBoxRanged, checkBoxMagic,
				checkBoxPrayer, checkBoxArmour, timer, timerup, timerdown,
				checkBoxNPCS, respawn, create, cancel);
	}

}
