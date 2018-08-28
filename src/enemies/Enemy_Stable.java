package enemies;

import java.awt.Color;
import java.awt.Rectangle;

import engine.DrawableObject;
import engine.Engine;
import engine.MovableObject;

public class Enemy_Stable extends Enemy
{
	public Rectangle leftBox, rightBox;
	private MovableObject leftBuffer, rightBuffer;
	
	public Enemy_Stable()
	{
		this(0, 0, GO_LEFT);
	}
	
	public Enemy_Stable(int x, int y)
	{
		this(x, y, GO_LEFT);
	}
	
	public Enemy_Stable(int x, int y, int direction)
	{
		super();
		this.color = Color.GREEN;
		this.direction = direction;
		this.width *= 2;
		this.height *= 2;
		this.hitbox.width = this.width;
		this.hitbox.height = this.height;
		this.xLocation = x;
		this.yLocation = y;
		updateHitbox();
		
		leftBuffer = new MovableObject();
		leftBuffer.width = 5;
		leftBuffer.height = 5;
		leftBuffer.hitbox.width = 5;
		leftBuffer.hitbox.height = 5;
		leftBuffer.updateHitbox();
		
		rightBuffer = new MovableObject();
		rightBuffer.width = 5;
		rightBuffer.height = 5;
		rightBuffer.hitbox.width = 5;
		rightBuffer.hitbox.height = 5;
		rightBuffer.updateHitbox();
		
		this.speed = 2;
		//xVelocity = direction * speed;
		this.health = 200;
	}
	
	public void specialAction()
	{
		DrawableObject[] hits;
		
		leftBuffer.xLocation = this.xLocation - leftBuffer.width;
		leftBuffer.yLocation = this.yLocation + this.height;
		leftBuffer.updateHitbox();

		rightBuffer.xLocation = this.xLocation + this.width;
		rightBuffer.yLocation = this.yLocation + this.height;
		rightBuffer.updateHitbox();
		
		hits = Engine.getCurrentGame().checkCollision(leftBuffer);
		
		if(hits.length == 0)
		{
			this.xVelocity *= -1;
		}
		
		hits = Engine.getCurrentGame().checkCollision(rightBuffer);
		
		if(hits.length == 0)
		{
			this.xVelocity *= -1;
		}
	}
}
