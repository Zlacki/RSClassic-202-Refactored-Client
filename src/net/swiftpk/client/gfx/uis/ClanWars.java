package net.swiftpk.client.gfx.uis;

import java.awt.Rectangle;
import net.swiftpk.client.gfx.GraphicalComponent;
import net.swiftpk.client.gfx.GraphicalOverlay;
import net.swiftpk.client.gfx.components.Box;
import net.swiftpk.client.gfx.components.Button;
import net.swiftpk.client.gfx.components.CheckBox;
import net.swiftpk.client.gfx.components.DrawString;
import net.swiftpk.client.gfx.components.GameFrame;
import net.swiftpk.client.gfx.components.Line;
import net.swiftpk.client.gfx.components.LineY;
import net.swiftpk.client.mudclient;

public class ClanWars extends GraphicalOverlay {

	public ClanWars(mudclient<?> mc) {
		super(mc);
		GameFrame frame = new GameFrame("Clan Wars", new Rectangle(70, 40, 412,
				170));
		frame.setOpaque(40);
		add(frame);

		Box boxoptions = new Box(new Rectangle(90, 65, 380, 85));
		boxoptions.setText("Active wars", 0xffffff);

		Line line = new Line(90, 80, 380);

		DrawString range = new DrawString("Range", false, new Rectangle(95, 75,
				0, 0));
		range.setColor(range.convertToJag(48, 244, 255));
		DrawString magic = new DrawString("Magic", false, new Rectangle(148,
				75, 0, 0));
		magic.setColor(range.convertToJag(48, 244, 255));
		DrawString prayer = new DrawString("Prayer", false, new Rectangle(190,
				75, 0, 0));
		prayer.setColor(range.convertToJag(48, 244, 255));
		DrawString armour = new DrawString("Armour", false, new Rectangle(240,
				75, 0, 0));
		armour.setColor(range.convertToJag(48, 244, 255));
		DrawString npcs = new DrawString("NPCS", false, new Rectangle(294, 75,
				0, 0));
		npcs.setColor(range.convertToJag(48, 244, 255));
		DrawString respawn = new DrawString("Respawn", false, new Rectangle(
				345, 75, 0, 0));
		respawn.setColor(range.convertToJag(48, 244, 255));

		DrawString timer = new DrawString("Join", false, new Rectangle(412, 75,
				0, 0));
		timer.setColor(range.convertToJag(48, 244, 255));

		add(boxoptions, line, range, magic, prayer, armour, npcs, respawn,
				timer, new LineY(140, 65, 85), new LineY(187, 65, 85),
				new LineY(235, 65, 85), new LineY(290, 65, 85), new LineY(335,
						65, 85), new LineY(408, 65, 85));

		int x = 105;
		int y = 81;
		for (int index = 0; index < 4; index++) {
			final int indexSet = index;
			CheckBox test = new CheckBox(x, y);
			test.setSelected(true);
			add(test);
			test = new CheckBox(x + 50, y);
			test.setSelected(true);
			add(test);

			test = new CheckBox(x + 100, y);
			test.setSelected(true);
			add(test);

			test = new CheckBox(x + 150, y);
			test.setSelected(true);
			add(test);

			test = new CheckBox(x + 200, y);
			test.setSelected(true);
			add(test);
			test = new CheckBox(x + 260, y);
			test.setSelected(true);
			add(test);

			Button button = new Button(new Rectangle(x + 310, y, 40, 15));
			button.setText("Join"); // , range.convertToJag(48, 244, 255)
			button.setAction((int x1, int y1, int button1) -> {
				System.out.println(indexSet);
			});
			add(button);
			if (index >= 1) {
				Line lineY = new Line(x - 17, y, 385);
				add(lineY);
			}
			y += 17;
		}
	}
	
	@Override
	public final boolean add(GraphicalComponent... comp) {
		return super.add(comp);
	}

}
