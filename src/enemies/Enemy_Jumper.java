package enemies;

import java.awt.Color;

public class Enemy_Jumper extends Enemy
{
	public Enemy_Jumper()
	{
		this(0, 0, GO_LEFT);
	}
	
	public Enemy_Jumper(int x, int y)
	{
		this(x, y, GO_LEFT);
	}
	
	public Enemy_Jumper(int x, int y, int direction)
	{
		super();
		this.color = Color.YELLOW;
		this.direction = direction;
		this.width *= .75;
		this.height *= .75;
		this.hitbox.width = width;
		this.hitbox.height = height;
		this.xLocation = x;
		this.yLocation = y;
		updateHitbox();
		this.speed = 5;
		//xVelocity = speed * this.direction;
		this.health = 50;
	}
	
	public void specialAction()
	{
		if(onGround)
		{
			this.yVelocity = -11;
		}
	}
}
