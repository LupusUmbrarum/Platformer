package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class DrawableObject 
{
	public boolean drawFill = true;
	public boolean isVisible = true;
	public boolean collisionEnabled = true;
	public int width, height, xLocation, yLocation;
	public Color color;
	public Rectangle hitbox;
	
	public DrawableObject()
	{
		this(25, 25, 0, 0, Color.WHITE);
	}
	
	public DrawableObject(int width, int height, int x, int y)
	{
		this(width, height, x, y, Color.WHITE);
	}
	
	public DrawableObject(int width, int height, int x, int y, Color color)
	{
		this.width = width;
		this.height = height;
		this.xLocation = x;
		this.yLocation = y;
		this.color = color;
		this.hitbox = new Rectangle(this.xLocation, this.yLocation, this.width, this.height);
	}
	
	public void tick(long deltaTime)
	{
		draw(Engine.getEngine().getRasterGraphics());
	}
	
	public void draw(Graphics g)
	{
		if(isVisible)
		{
			g.setColor(this.color);
			
			if(drawFill)
			{
				g.fillRect(this.xLocation - Camera.getCamera().x, Camera.getCamera().y + this.yLocation, this.width, this.height);
			}
			else
			{
				g.drawRect(this.xLocation - Camera.getCamera().x, Camera.getCamera().y + this.yLocation, this.width, this.height);
			}
		}
	}
}
