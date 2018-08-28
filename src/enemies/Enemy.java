package enemies;

import java.awt.Color;

import engine.Damageable;
import engine.DrawableObject;
import engine.Engine;
import engine.MovableObject;
import main.PlatformManager;
import main.Platformer;
import projectiles.Projectile;

public class Enemy extends MovableObject implements Damageable
{
	public boolean onGround = false;
	public double health;
	
	public Enemy()
	{
		this(0, 0, GO_LEFT);
	}
	
	public Enemy(int x, int y)
	{
		this(x, y, GO_LEFT);
	}
	
	public Enemy(int x, int y, int direction)
	{
		super();
		this.color = Color.RED;
		this.direction = direction;
		this.width *= 1.5;
		this.height *= 1.5;
		this.hitbox.width = width;
		this.hitbox.height = height;
		this.xLocation = x;
		this.yLocation = y;
		updateHitbox();
		xVelocity = speed * this.direction;
		this.health = 100;
		this.tempTick = 0;
	}
	
	public void move()
	{
		if(!onGround)
		{
			yVelocity++;
			if(yVelocity > 15)
			{
				yVelocity = 15;
			}
		}
		//move along the y-axis
		this.yLocation += yVelocity;
		
		updateHitbox();
				
		DrawableObject[] hits = Engine.getCurrentGame().checkCollision(this);
		
		//check vertical collision
		if(hits.length > 0)
		{
			DrawableObject temp;
			
			for(int x = 0; x < hits.length; x++)
			{
				temp = hits[x];
				
				if(temp instanceof Platformer || temp instanceof Projectile)
				{
					handleEnemyCollision(temp);
				}
				else
				{
					if(this.yLocation < temp.yLocation)
					{
						this.yLocation = temp.yLocation - this.height;
						onGround = true;
						yVelocity = 0;
						updateHitbox();
					}
					else if(this.yLocation + this.height > temp.yLocation + temp.height)
					{
						this.yLocation = temp.yLocation + temp.height;
						yVelocity = 0;
					}
				}
			}
		}
		else
		{
			onGround = false;
		}
		
		//move along the x-axis
		this.xLocation += xVelocity;
		
		updateHitbox();
				
		hits = Engine.getCurrentGame().checkCollision(this);
				
		//check horizontal collision
		if(hits.length > 0)
		{
			DrawableObject temp;
			
			for(int x = 0; x < hits.length; x++)
			{
				temp = hits[x];
				
				if(temp instanceof Platformer || temp instanceof Projectile)
				{
					handleEnemyCollision(temp);
				}
				else
				{
					if(this.xLocation < temp.xLocation)
					{
						this.xLocation = temp.xLocation - this.width;
						this.xVelocity *= -1;
					}
					else if(this.xLocation + this.width > temp.xLocation + temp.width)
					{
						this.xLocation = temp.xLocation + temp.width;
						this.xVelocity *= -1;
					}
				}
			}
		}
	}
	
	public void tick(long deltaTime)
	{
		tickCount++;
		
		move();
		
		draw(Engine.getEngine().getRasterGraphics());
		
		if(tickCount == MAX_TICK_COUNT)
		{
			tickCount = 0;
		}
		
		specialAction();
		
		if(tickCount % 2 == 0 && tempTime > 0)
		{
			tempTime--;
			this.isVisible = !this.isVisible;
		}
		else
		{
			this.isVisible = true;
		}
	}
	
	public void specialAction()
	{
		
	}
	
	public void doDamage(double amount)
	{
		if(tempTime == 0)
		{
			tempTick = tickCount;
			health -= amount;
			tempTime = (int) (delayTime * 2);
		}
		
		if(health <= 0)
		{
			((PlatformManager)Engine.getCurrentGame()).removeDrawableObject(this);
		}
	}
	
	public void handleEnemyCollision(DrawableObject target)
	{
		int halfwayPoint = this.yLocation + (this.height / 2);
		
		if(halfwayPoint < target.yLocation + target.height)
		{
			((Damageable)target).doDamage(100);
		}
	}
}