package net.swiftpk.client.util;

import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

public interface ImplementationDelegate extends MouseListener,
		MouseMotionListener, MouseWheelListener, KeyListener {

	Container getContainerImpl();

}
