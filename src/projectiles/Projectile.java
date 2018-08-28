package projectiles;

import java.awt.Color;

import engine.Camera;
import engine.Damageable;
import engine.DrawableObject;
import engine.Engine;
import engine.MovableObject;
import main.PlatformManager;

public class Projectile extends MovableObject implements Damageable
{
	public DrawableObject owner;
	public double damage = 50;
	
	public Projectile()
	{
		this(0, 0, GO_LEFT, null);
	}
	
	public Projectile(DrawableObject owner)
	{
		this(0, 0, GO_LEFT, owner);
	}
	
	public Projectile(int x, int y, DrawableObject owner)
	{
		this(x, y, GO_LEFT, owner);
	}
	
	public Projectile(int x, int y, int direction, DrawableObject owner)
	{
		super();
		this.color = new Color(242, 40, 208);
		this.xLocation = x;
		this.yLocation = y;
		this.width *= .25;
		this.height *= .25;
		this.hitbox.width = this.width;
		this.hitbox.height = this.height;
		updateHitbox();
		this.direction = direction;
		this.speed = 9;
		this.xVelocity = speed * direction;
		this.owner = owner;
		((PlatformManager)Engine.getCurrentGame()).addDrawableObject(this, PlatformManager.ENEMY);
	}
	
	public void move()
	{
		DrawableObject[] hits;
		
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
					return;
				}
				else if(temp == owner)
				{
					hitOwner = true;
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
	
	public void doDamage(double amount)
	{
		
	}
	
	public void handleEnemyCollision(DrawableObject target)
	{
		((Damageable)target).doDamage(damage);
	}
}