package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Engine extends JFrame implements KeyListener
{
	private static final long serialVersionUID = 1L;
	private static Engine theEngine;
	private Image raster;
	private Graphics rasterGraphics;
	private Color backgroundColor = Color.BLACK;;
	public static final int FULLSCREEN = -1;
	private static Game currentGame;
	private int framerate = 16;
	
	private Engine()
	{
		this.setSize(800, 600);
		
		addKeyListener(this);
		setDefaultCloseOperation(3);
		setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width/2-this.getWidth()/2, screenSize.height/2-this.getHeight()/2);
		
		setVisible(true);
		init();
	}
	
	public static Engine getEngine()
	{
		if(theEngine == null)
		{
			theEngine = new Engine();
		}
		
		return theEngine;
	}
	
	public void start()
	{
		if(currentGame == null)
		{
			return;
		}
		
		long lastTick = System.currentTimeMillis();
		long current, elapsed;
		
		while(true)
		{
			current = System.currentTimeMillis();
			elapsed = current - lastTick;
			drawBackground();
			
			currentGame.gameTick(elapsed);
			//System.out.println(elapsed);
			
			getGraphics().drawImage(raster, 0, 0, getWidth(), getHeight(), null);
			
			lastTick = current;
			
			try
			{
				Thread.sleep(framerate);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void setFrameRate(int x)
	{
		this.framerate = x;
	}
	
	public void setWidth(int x)
	{
		this.setSize(x, this.getSize().height);
	}
	
	public void setHeight(int y)
	{
		this.setSize(this.getSize().width, y);
	}
	
	public static void setCurrentGame(Game newGame)
	{
		currentGame = newGame;
	}
	
	public static Game getCurrentGame()
	{
		return currentGame;
	}
	
	public Graphics getRasterGraphics()
	{
		return rasterGraphics;
	}
	
	private void init()
	{
		raster = createImage(getWidth(), getHeight());
		rasterGraphics = raster.getGraphics();
	}
	
	private void drawBackground()
	{
		rasterGraphics.setColor(backgroundColor);
		
		rasterGraphics.fillRect(0,  0, getWidth(), getHeight());
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			this.framerate = 1000;
		}
		
		Input.pressKey(e);
	}
	
	public void keyReleased(KeyEvent e)
	{
		Input.releaseKey(e);
	}
	
	public void keyTyped(KeyEvent e)
	{
		Input.typeKey(e);
	}	
}