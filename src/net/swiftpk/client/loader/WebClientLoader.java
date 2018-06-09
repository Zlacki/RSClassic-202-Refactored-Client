package net.swiftpk.client.loader;

import static net.swiftpk.client.util.GameConstants.GAME_SITE;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import net.swiftpk.client.loader.various.AppletUtils;
import net.swiftpk.client.loader.various.ProgressCallback;
import net.swiftpk.client.loader.various.VirtualBrowser;
import net.swiftpk.client.util.ImplementationDelegate;
import net.swiftpk.client.mudclient;

public class WebClientLoader extends Applet implements Runnable,
		ImplementationDelegate, ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Thread applet_thread = null;

	private mudclient<WebClientLoader> instance;

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		Insets insets = super.getInsets();
		mudclient.getInstance().onResize(
				super.getWidth() - insets.left - insets.right,
				super.getHeight() - insets.top - insets.bottom - 11);
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

	public void downloadCache() {
		AppletUtils.DISPLAY_MESSAGE = "Checking dictionarys";
		if (AppletUtils.doDirChecks())
			try {
				AppletUtils.DISPLAY_MESSAGE = "Deleting old cache";
				for(File file : AppletUtils.CACHE.listFiles())
					file.delete();
				AppletUtils.DISPLAY_MESSAGE = "Downloading cache ";
				new VirtualBrowser().getRaw(new URL(
						GAME_SITE + "/client/cache"),
						new ProgressCallback() {
							@Override
							public void onComplete(byte[] bytes) {
								try (FileOutputStream fos = new FileOutputStream(
										AppletUtils.CACHEFILE.getPath())) {
									fos.write(bytes);
								} catch (IOException e) {
									AppletUtils.percentage = 0;
									AppletUtils.DISPLAY_MESSAGE = "Failed to save cache";
								}
								AppletUtils.DISPLAY_MESSAGE = "Cache downloaded...";
								AppletUtils.extractFolder(
										AppletUtils.CACHEFILE.getPath(),
										AppletUtils.CACHE.toString());
							}

							@Override
							public void update(int pos, int length) {
								AppletUtils.percentage = pos * 100 / length;
							}
						});
			} catch (MalformedURLException e) {
				AppletUtils.percentage = 0;
				AppletUtils.DISPLAY_MESSAGE = "Failed to grab cache";
			}
		else
			AppletUtils.DISPLAY_MESSAGE = "Dictionarys can not be created";
	}

	@Override
	public final Container getContainerImpl() {
		return this;
	}

	@Override
	public void init() {
		AppletUtils.isApplet = true;
		this.applet_thread = new Thread(this);
		this.applet_thread.start();
	}

	@Override
	public final void keyPressed(KeyEvent e) {
		this.instance.keyDown(e.isShiftDown(), e.isControlDown(),
				e.isActionKey(), e.getKeyCode(), e.getKeyChar(), e);
	}

	@Override
	public final void keyReleased(KeyEvent e) {
		this.instance.keyUp(e.getKeyCode());
	}

	@Override
	public final void keyTyped(KeyEvent e) {
	}

	@Override
	public final void mouseClicked(MouseEvent e) {
	}

	@Override
	public final void mouseDragged(MouseEvent e) {
		this.instance.mouseDrag(e, e.getX() - super.getInsets().left, e.getY()
				- super.getInsets().top);
	}

	@Override
	public final void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public final void mouseExited(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public final void mouseMoved(MouseEvent e) {
		this.instance.mouseMove(e, e.getX() - super.getInsets().left, e.getY()
				- super.getInsets().top);
	}

	@Override
	public final void mousePressed(MouseEvent e) {
		this.instance.mouseDown(e, e.getX() - super.getInsets().left, e.getY()
				- super.getInsets().top);
	}

	@Override
	public final void mouseReleased(MouseEvent e) {
		this.instance.mouseUp(e, e.getX() - super.getInsets().left, e.getY()
				- super.getInsets().top);
	}

	@Override
	public final void mouseWheelMoved(MouseWheelEvent e) {
		this.instance.mouseWheelMoved(e);
	}

	public void onLogin() {
	}

	public void onLogout() {
	}

	@Override
	public void paint(Graphics g) {
		if (this.instance != null) {
			return;
		}
		AppletUtils.render(g);
		AppletUtils.drawPercentage(g, AppletUtils.percentage,
				AppletUtils.DISPLAY_MESSAGE);
	}

	@Override
	public void run() {
		if (!AppletUtils.CACHEFILE.exists())
			downloadCache();
		AppletUtils.DISPLAY_MESSAGE = "Loading client";
		System.out.println(AppletUtils.GAME_WIDTH + " " + AppletUtils.GAME_HEIGHT);
		this.instance = new mudclient<>(this, AppletUtils.GAME_WIDTH,
				AppletUtils.GAME_HEIGHT);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		this.instance.run();
	}

	@Override
	public final void update(Graphics g) {
		paint(g);
	}

}