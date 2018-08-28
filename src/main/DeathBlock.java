package main;

import java.awt.Color;

import engine.DrawableObject;

public class DeathBlock extends DrawableObject
{
	public DeathBlock()
	{
		this(0, 0, 0, 0, Color.RED);
	}
	
	public DeathBlock(int width, int height, int x, int y)
	{
		this(width, height, x, y, Color.RED);
	}
	
	public DeathBlock(int width, int height, int x, int y, Color color)
	{
		super(width, height, x, y, color);
		//isVisible = false;
	}
}
