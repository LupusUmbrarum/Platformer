package projectiles;

import java.awt.Color;

import engine.Camera;
import engine.Damageable;
import engine.DrawableObject;
import engine.Engine;
import main.PlatformManager;

public class Projectile_Mortar extends Projectile
{
	public Projectile_Mortar()
	{
		this(0, 0, null);
	}
	
	public Projectile_Mortar(DrawableObject owner)
	{
		this(0, 0, owner);
	}
	
	public Projectile_Mortar(int x, int y)
	{
		this(x, y, null);
	}
	
	public Projectile_Mortar(int x, int y, DrawableObject owner)
	{
		super(x, y, owner);
		this.color = new Color(67, 242, 40);
		this.yVelocity = -15;
	}
	
	public void move()
	{
		yVelocity++;
		
		if(yVelocity > 15)
			yVelocity = 15;
		
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
				
				if(temp != owner)
				{
					if(temp instanceof Damageable)
					{
						handleEnemyCollision(temp);
					}
				}
			}
			
			((PlatformManager)Engine.getCurrentGame()).removeDrawableObject(this);
			return;
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
				
				if(temp != owner)
				{
					if(temp instanceof Damageable)
					{
						handleEnemyCollision(temp);
					}
				}
			}
			
			((PlatformManager)Engine.getCurrentGame()).removeDrawableObject(this);
			return;
		}
		
		if(this.xLocation < Camera.getCamera().x || this.xLocation > Camera.getCamera().x + Camera.getCamera().width)
		{
			((PlatformManager)Engine.getCurrentGame()).removeDrawableObject(this);
		}
	}
}
