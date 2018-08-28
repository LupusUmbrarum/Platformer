package projectiles;

import java.awt.Color;

import engine.Camera;
import engine.Damageable;
import engine.DrawableObject;
import engine.Engine;
import main.PlatformManager;

public class Projectile_Crawler extends Projectile
{
	public Projectile_Crawler()
	{
		this(0, 0, null);
	}
	
	public Projectile_Crawler(DrawableObject owner)
	{
		this(0, 0, owner);
	}
	
	public Projectile_Crawler(int x, int y)
	{
		this(x, y, null);
	}
	
	public Projectile_Crawler(int x, int y, DrawableObject owner)
	{
		super(x, y, owner);
		this.color = new Color(208, 242, 40);
	}
	
	public void move()
	{
		if(!onGround)
		{
			yVelocity++;
			
			if(yVelocity > 15)
				yVelocity = 15;
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
				
				if(temp instanceof Damageable && temp != owner)
				{
					handleEnemyCollision(temp);
					
					((PlatformManager)Engine.getCurrentGame()).removeDrawableObject(this);
					return;
				}
				else if(temp != owner)
				{
					if(this.yLocation < temp.yLocation)
					{
						this.yLocation = temp.yLocation - this.height;
					}
					else if(this.yLocation + this.height > temp.yLocation + temp.height)
					{
						this.yLocation = temp.yLocation + temp.height;
					}
					else if(this.yLocation + (this.height / 2) < temp.yLocation + (temp.height / 2))
					{
						this.yLocation = temp.yLocation - this.height;
					}
					
					yVelocity = 0;
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
			boolean hitOwner = false;
			
			for(int x = 0; x < hits.length; x++)
			{
				temp = hits[x];
				
				if(temp instanceof Damageable && temp != owner)
				{
					handleEnemyCollision(temp);
					
					((PlatformManager)Engine.getCurrentGame()).removeDrawableObject(this);
					break;
				}
				else if(temp == owner)
				{
					hitOwner = true;
				}
				else
				{
					if(this.xLocation < temp.xLocation)
					{
						this.xLocation = temp.xLocation - this.width;
					}
					else if(this.xLocation + this.width > temp.xLocation + temp.width)
					{
						this.xLocation = temp.xLocation + temp.width;
					}
				}
			}
			
			if(!hitOwner)
			{
				((PlatformManager)Engine.getCurrentGame()).removeDrawableObject(this);
			}
		}
		
		if(this.xLocation < Camera.getCamera().x || this.xLocation > Camera.getCamera().x + Camera.getCamera().width)
		{
			((PlatformManager)Engine.getCurrentGame()).removeDrawableObject(this);
		}
	}
}
