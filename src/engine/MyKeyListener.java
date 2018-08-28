package engine;

import java.awt.event.KeyEvent;

public interface MyKeyListener
{
	void keyPressed(KeyEvent e);
	void keyReleased(KeyEvent e);
	void keyTyped(KeyEvent e);
}