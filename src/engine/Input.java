package engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Input
{
	private static Input input;
	private static ArrayList<MyKeyListener> listeners = new ArrayList<MyKeyListener>();
	
	private Input()
	{
		
	}
	
	public static Input getInput()
	{
		if(input == null)
			input = new Input();
		
		return input;
	}
	
	public static void addMyKeyListener(MyKeyListener listener)
	{
		listeners.add(listener);
	}
	
	public static void pressKey(KeyEvent e)
	{
		for(int x = 0; x < listeners.size(); x++)
		{
			listeners.get(x).keyPressed(e);
		}
	}
	
	public static void releaseKey(KeyEvent e)
	{
		for(int x = 0; x < listeners.size(); x++)
		{
			listeners.get(x).keyReleased(e);
		}
	}
	
	public static void typeKey(KeyEvent e)
	{
		for(int x = 0; x < listeners.size(); x++)
		{
			listeners.get(x).keyTyped(e);
		}
	}
}