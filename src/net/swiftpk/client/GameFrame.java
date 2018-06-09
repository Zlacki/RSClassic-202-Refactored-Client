package net.swiftpk.client;

import static net.swiftpk.client.util.GameConstants.GAME_NAME;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import net.swiftpk.client.util.ImplementationDelegate;

public class GameFrame extends JFrame implements ComponentListener,
		WindowListener, ImplementationDelegate {
	private static final long serialVersionUID = 3384288576278358005L;

	private final GameManager<?> instance;

	public GameFrame(int width, int height) {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setTitle(GAME_NAME);
		toFront();
		setVisible(true);
		this.instance = new mudclient<>(this, width, height);
		Insets insets = super.getInsets();
		super.setSize(width + insets.left + insets.right, height + insets.top
				+ insets.bottom);
		super.setPreferredSize(new Dimension(
				width + insets.left + insets.right, height + insets.top
						+ insets.bottom));
		super.pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		addComponentListener(this);
		addWindowListener(this);
		this.instance.run();
	}
	
	@Override
	public final void addWindowListener(WindowListener mwl) {
		super.addWindowListener(mwl);
	}
	
	@Override
	public final void addComponentListener(ComponentListener mwl) {
		super.addComponentListener(mwl);
	}
	
	@Override
	public final void addKeyListener(KeyListener mwl) {
		super.addKeyListener(mwl);
	}
	
	@Override
	public final void addMouseListener(MouseListener mwl) {
		super.addMouseListener(mwl);
	}
	
	@Override
	public final void addMouseMotionListener(MouseMotionListener mwl) {
		super.addMouseMotionListener(mwl);
	}
	
	@Override
	public final void addMouseWheelListener(MouseWheelListener mwl) {
		super.addMouseWheelListener(mwl);
	}
	
	@Override
	public final void setDefaultCloseOperation(int i) {
		super.setDefaultCloseOperation(i);
	}
	
	@Override
	public final void setTitle(String s) {
		super.setTitle(s);
	}
	
	@Override
	public final void setVisible(boolean b) {
		super.setVisible(b);
	}
	
	@Override
	public final void toFront() {
		super.toFront();
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public final void componentResized(ComponentEvent e) {
		Insets insets = super.getInsets();
		instance.onResize(e.getComponent().getWidth() - insets.left
				- insets.right, e.getComponent().getHeight() - insets.top
				- insets.bottom - 11);
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public final Container getContainerImpl() {
		return this;
	}

	@Override
	public Graphics getGraphics() {
		Insets insets = super.getInsets();
		Graphics gfx = super.getGraphics();
		gfx.translate(insets.left, insets.top);
		return gfx;
	}

	public int getWidthTest() {
		return super.getWidth();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		instance.keyDown(e.isShiftDown(), e.isControlDown(), e.isActionKey(),
				e.getKeyCode(), e.getKeyChar(), e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		instance.keyUp(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Insets insets = super.getInsets();
		instance.mouseDrag(e, e.getX() - insets.left, e.getY()
				- insets.top);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Insets insets = super.getInsets();
		instance.mouseMove(e, e.getX() - insets.left, e.getY()
				- insets.top);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Insets insets = super.getInsets();
		instance.mouseDown(e, e.getX() - insets.left, e.getY()
				- insets.top);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Insets insets = super.getInsets();
		instance.mouseUp(e, e.getX() - insets.left,
				e.getY() - insets.top);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		instance.mouseWheelMoved(e);
	}

	@Override
	public void paint(Graphics g) {
	}

	@Override
	public final void update(Graphics gfx) {
		paint(gfx);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
}
