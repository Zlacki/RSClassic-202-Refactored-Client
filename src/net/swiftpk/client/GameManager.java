package net.swiftpk.client;

import static net.swiftpk.client.util.GameConstants.GAME_SITE;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.swiftpk.client.util.ImplementationDelegate;

public abstract class GameManager<Delegate_T extends ImplementationDelegate>
		implements Runnable {
	protected static boolean isKeyValid(int key) {
		boolean validKeyDown = false;
		for(int j = 0; j < CHARSET.length(); j++) {
			if(key != CHARSET.charAt(j)) {
				continue;
			}
			validKeyDown = true;
			break;
		}
		return validKeyDown;
	}

	public int mouseX;
	public int mouseY;
	private int exitTimeout;
	private final Map<Integer, Boolean> keys = new HashMap<>();
	public int lastActionTimeout;
	public String inputText = "";
	public String enteredText = "";
	public String inputMessage = "";
	public String enteredMessage = "";
	public int mouseDownButton;
	public int lastMouseDownButton;
	public boolean keyF1Toggle;
	public boolean keyDownDown;
	public boolean keyLeftBraceDown;
	public boolean keyLeftDown;
	public boolean keyNMDown;
	public boolean keyRightBraceDown;
	public boolean keyRightDown;
	public boolean keySpaceDown;
	public boolean keyUpDown;
	public boolean ctrl_down;
	private boolean actionDown;
	private boolean shiftDown;
	private boolean controlDown;
	protected final Delegate_T delegate;
	private static final Color LOADING_BAR_COLOR;
	private static final Font TIMES_NEW_ROMAN;
	private static final String CHARSET;
	private static final Font HELVETICA;
	private final long[] currentTimeArray = new long[10];
	private int yOffset;
	protected static BufferedImage LOADING_IMAGE;
	static {
		CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"£$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
		LOADING_BAR_COLOR = new Color(132, 132, 132);
		TIMES_NEW_ROMAN = new Font("TimesRoman", 0, 15);
		HELVETICA = new Font("Helvetica", 1, 13);
	}

	public GameManager(Delegate_T container) {
		this.delegate = container;
	}

	protected final void drawLoadingBarText(int percentLoaded, String loadStage) {
		Graphics gfx = this.delegate.getContainerImpl().getGraphics();
		int x = (this.delegate.getContainerImpl().getWidth() - 281) / 2;
		int y = (this.delegate.getContainerImpl().getHeight() - 148) / 2;
		x += 2;
		y += 90;
		int length = 277 * percentLoaded / 100;
//		drawLoadingScreen();
		gfx.setColor(LOADING_BAR_COLOR);
		gfx.fillRect(x, y, length, 20);
		gfx.setColor(Color.black);
		gfx.fillRect(x + length, y, 277 - length, 20);
		gfx.setColor(new Color(198, 198, 198));
		if((loadStage != null) && (loadStage.length() > 0)) {
			drawString(gfx, loadStage, TIMES_NEW_ROMAN, x + 138, y + 10);
		}
	}

	public void drawLoadingScreen() {
		Graphics gfx = this.delegate.getContainerImpl().getGraphics();
		int x = (this.delegate.getContainerImpl().getWidth() - 281) / 2;
		int y = (this.delegate.getContainerImpl().getHeight() - 148) / 2;
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, this.delegate.getContainerImpl().getWidth(),
				this.delegate.getContainerImpl().getHeight());
		if(LOADING_IMAGE != null)
			gfx.drawImage(LOADING_IMAGE, x - (LOADING_IMAGE.getWidth() / 7), y,
					Color.BLACK, this.delegate.getContainerImpl());
		x += 2;
		y += 90;
		gfx.setColor(new Color(132, 132, 132));
		gfx.drawRect(x - 2, y - 2, 280, 23);
		gfx.setColor(new Color(198, 198, 198));
        drawString(gfx, "Created by SlackNET CompSci - visit " + GAME_SITE.replaceAll("http://", ""), HELVETICA, x + 138, y + 30);
        drawString(gfx, "\2512014-2015 SlackNET, LLC", HELVETICA, x + 138, y + 44);
	}

	protected void drawString(Graphics g, String s, Font font, int x, int y) {
		assert ((g != null) && (s != null) && (s.length() > 0)
				&& (font != null) && (x > 0) && (y > 0));
		FontMetrics fontmetrics = this.delegate.getContainerImpl()
				.getFontMetrics(font);
		fontmetrics.stringWidth(s);
		g.setFont(font);
		g.drawString(s, x - fontmetrics.stringWidth(s) / 2, y
				+ fontmetrics.getHeight() / 4);
	}

	protected void emptyGameWindowMethod() {
	}

	protected final Graphics getGraphics() {
		return this.delegate.getContainerImpl().getGraphics();
	}

	protected void handleMenuKeyDown(int key, char keyChar) {
	}

	protected void handleMouseDown(int button, int x, int y) {
	}

	protected void handleScroll(MouseWheelEvent e) {
	}

	public boolean keyDown(boolean shift, boolean ctrl, boolean action,
			int key, char keyChar, KeyEvent e) {
		this.keys.put(key, true);
		this.actionDown = action;
		this.shiftDown = shift;
		this.controlDown = ctrl;
		handleMenuKeyDown(key, keyChar);
		if((this.controlDown) && (key == 86)) {
			return true;
		}
		if(key == 112)
			this.keyF1Toggle = !this.keyF1Toggle;
		if(key == 37)
			this.keyLeftDown = true;
		if(key == 39)
			this.keyRightDown = true;
		if(this.actionDown)
			return true;
		if((this.actionDown) && (this.shiftDown))
			return true;
		if(this.controlDown)
			return true;
		boolean validKeyDown = isKeyValid(keyChar);
		if((key == 8) && (this.inputText.length() > 0))
			this.inputText = this.inputText.substring(0, this.inputText
					.length() - 1);
		if((key == 8) && (this.inputMessage.length() > 0))
			this.inputMessage = this.inputMessage.substring(0,
					this.inputMessage.length() - 1);
		if((key == 10) || (key == 13)) {
			this.enteredText = this.inputText;
			this.enteredMessage = this.inputMessage;
		}
		if((key == 222) && (!e.isShiftDown()))
			this.inputText += "'";
		if((key == 222) && (e.isShiftDown()))
			this.inputText += "@";
		if((validKeyDown) && (this.inputText.length() < 20))
			this.inputText += keyChar;
		if((validKeyDown) && (this.inputMessage.length() < 80))
			this.inputMessage += keyChar;
		return true;
	}

	public boolean keyUp(int i) {
		if(i == 37)
			keyLeftDown = false;
		if(i == 39)
			keyRightDown = false;
		if(i == 38)
			keyUpDown = false;
		if(i == 40)
			keyDownDown = false;
		if((char) i == ' ')
			keySpaceDown = false;
		if((char) i == 'n' || (char) i == 'm')
			keyNMDown = false;
		if((char) i == 'N' || (char) i == 'M')
			keyNMDown = false;
		if((char) i == '{')
			keyLeftBraceDown = false;
		if((char) i == '}')
			keyRightBraceDown = false;
		return true;
	}

	protected void logoutAndStop() {
	}

	protected synchronized void method2() {
	}

	protected synchronized void method4() {
	}

	public void mouseDown(MouseEvent event, int i, int j) {
		mouseX = i;
		mouseY = j + yOffset;
		if(event.isMetaDown())
			mouseDownButton = 2;
		else
			mouseDownButton = 1;
		lastMouseDownButton = mouseDownButton;
		lastActionTimeout = 0;
		handleMouseDown(mouseDownButton, i, j);
	}

	public boolean mouseDrag(MouseEvent event, int i, int j) {
		mouseX = i;
		mouseY = j + yOffset;
		if(event.isMetaDown())
			mouseDownButton = 2;
		else
			mouseDownButton = 1;
		return true;
	}

	public boolean mouseMove(MouseEvent e, int i, int j) {
		ctrl_down = e.isControlDown();
		mouseX = i;
		mouseY = j + yOffset;
		mouseDownButton = 0;
		lastActionTimeout = 0;
		return true;
	}

	public boolean mouseUp(MouseEvent e, int i, int j) {
		mouseX = i;
		mouseY = j + yOffset;
		mouseDownButton = 0;
		return true;
	}

	public void mouseWheel(int off) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		this.handleScroll(e);
	}

	public void onResize(int i, int j) {
		// delegate.getContainerImpl().resize(i, j);
		delegate.getContainerImpl().setSize(i, j);
	}

	protected final void resetCurrentTimeArray() {
		for(int i = 0; i < 10; i++)
			currentTimeArray[i] = 0L;
	}

	@Override
	public void run() {
		// drawLoadingScreen();
		startGame();
		int anInt10 = 0;
		int index = 0;
		int j = 256;
		int sleepTime = 1;
		int i1 = 0;
		for(int timeIndex = 0; timeIndex < 10; timeIndex++) {
			this.currentTimeArray[timeIndex] = System.currentTimeMillis();
		}
		while(this.exitTimeout >= 0) {
			if(this.exitTimeout > 0) {
				this.exitTimeout -= 1;
				if(this.exitTimeout == 0) {
					break;
				}
			}
			int k1 = j;
			int i2 = sleepTime;
			j = 300;
			sleepTime = 1;
			long now = System.currentTimeMillis();
			if(this.currentTimeArray[index] == 0L) {
				j = k1;
				sleepTime = i2;
			} else if(now > this.currentTimeArray[index]) {
				j = (int) (51200L / (now - this.currentTimeArray[index]));
			}
			if(j < 25)
				j = 25;
			if(j > 256) {
				j = 256;
				sleepTime = (int) (20L - (now - this.currentTimeArray[index]) / 10L);
				if(sleepTime < 10)
					sleepTime = 10;
			}
			try {
				Thread.sleep(sleepTime);
			}
			catch(InterruptedException localInterruptedException) {
			}
			this.currentTimeArray[index] = now;
			index = (index + 1) % 10;
			if(sleepTime > 1) {
				for(int j2 = 0; j2 < 10; j2++) {
					if(this.currentTimeArray[j2] != 0L)
						this.currentTimeArray[j2] += sleepTime;
				}
			}
			int k2 = 0;
			while(i1 < 256) {
				method2();
				i1 += j;
				k2++;
				if(k2 <= 1000)
					continue;
				i1 = 0;
				anInt10 += 6;
				if(anInt10 <= 25)
					break;
				anInt10 = 0;
				this.keyF1Toggle = true;
				break;
			}
			anInt10--;
			i1 &= 255;
			method4();
		}
		logoutAndStop();
	}

	protected void startGame() {
	}
}
