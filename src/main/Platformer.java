package main;

import java.awt.event.KeyEvent;

import engine.Damageable;
import engine.DrawableObject;
import engine.Engine;
import engine.Input;
import engine.MovableObject;
import engine.MyKeyListener;
import projectiles.Projectile;
import projectiles.Projectile_Bouncer;
import projectiles.Projectile_Crawler;
import projectiles.Projectile_Mortar;
import projectiles.Projectile_Type;

public class Platformer extends MovableObject implements MyKeyListener, Damageable
{
	public boolean LEFT = false, RIGHT = false, UP = false, DOWN = false;
	public double health;
	private long timeSinceLastProjectile = 0;
	private long rightTime = 0, leftTime = 0;
	public Projectile_Type projectileType = Projectile_Type.REGULAR;
	
	public Platformer(int x, int y)
	{
		this();
		this.xLocation = x;
		this.yLocation = y;
	}
	
	public Platformer()
	{
		super();
		Input.addMyKeyListener(this);
		this.health = 300;
		this.delayTime = 12;
	}
	
	public void move()
	{
		if(LEFT && !RIGHT)
		{
			xVelocity = -7;
		}
		else if(RIGHT && !LEFT)
		{
			xVelocity = 7;
		}
		else
		{
			xVelocity = 0;
		}
		
		if(!LEFT && !RIGHT)
		{
			if(xVelocity == 1 || xVelocity == -1)
			{
				xVelocity = 0;
			}
			else if(xVelocity < 0)
			{
				xVelocity += 2;
			}
			else if(xVelocity > 0)
			{
				xVelocity -= 2;
			}
		}
		
		if(UP && onGround)
		{
			yVelocity = -10;
		}
		
		if(!onGround && tickCount % 2 == 0)
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
				
				if(temp instanceof Damageable)
				{
					handleEnemyCollision(temp);
				}
				else if(temp instanceof DeathBlock)
				{
					this.doDamage(1000);
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
				
				if(temp instanceof Damageable)
				{
					//handleEnemyCollision(temp);
				}
				else if(temp instanceof DeathBlock)
				{
					this.doDamage(1000);
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
		}
	}
	
	public void handleEnemyCollision(DrawableObject target)
	{
		int halfwayPoint = this.yLocation + (this.height / 2);
		
		if(halfwayPoint < target.yLocation && yVelocity > 0)
		{
			onGround = false;

			yVelocity = -5;
			
			((Damageable)target).doDamage(100);
		}
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
	
	public void setProjectile(Projectile_Type newType)
	{
		this.projectileType = newType;
	}
	
	private void fireProjectile()
	{
		long currentTime = System.currentTimeMillis();
		
		if(currentTime - timeSinceLastProjectile > 250)
		{
			timeSinceLastProjectile = System.currentTimeMillis();
			Projectile p = null;
			
			switch(projectileType)
			{
			case REGULAR:
			{
				p = new Projectile(this.xLocation, this.yLocation, this);
				break;
			}
			case BOUNCING:
			{
				p = new Projectile_Bouncer(this.xLocation, this.yLocation, this);
				break;
			}
			case CRAWLING:
			{
				p = new Projectile_Crawler(this.xLocation, this.yLocation, this);
				break;
			}
			case MORTAR:
			{
				p = new Projectile_Mortar(this.xLocation, this.yLocation, this);
				break;
			}
			default:
			{
				p = new Projectile(this);
			}
			}
			
			if(rightTime > leftTime)
			{
				p.xLocation = this.xLocation + this.width + 1;
				p.xVelocity *= -1;
			}
			else
			{
				p.xLocation = this.xLocation - 1;
			}
		}
	}
	
	public void updateHitbox()
	{
		this.hitbox.setLocation(this.xLocation, this.yLocation);
	}
	
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_A:
		{
			leftTime = System.currentTimeMillis();
			LEFT = true;
			break;
		}
		case KeyEvent.VK_D:
		{
			rightTime = System.currentTimeMillis();
			RIGHT = true;
			break;
		}
		case KeyEvent.VK_W:
		{
			UP = true;
			break;
		}
		case KeyEvent.VK_S:
		{
			
		}
		case KeyEvent.VK_SPACE:
		{
			fireProjectile();
		}
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_A:
		{
			LEFT = false;
			break;
		}
		case KeyEvent.VK_D:
		{
			RIGHT = false;
			break;
		}
		case KeyEvent.VK_W:
		{
			UP = false;
			break;
		}
		case KeyEvent.VK_S:
		{
			
		}
		case KeyEvent.VK_SPACE:
		{
			
			break;
		}
		case KeyEvent.VK_1:
		{
			this.setProjectile(Projectile_Type.REGULAR);
			break;
		}
		case KeyEvent.VK_2:
		{
			this.setProjectile(Projectile_Type.BOUNCING);
			break;
		}
		case KeyEvent.VK_3:
		{
			this.setProjectile(Projectile_Type.CRAWLING);
			break;
		}
		case KeyEvent.VK_4:
		{
			this.setProjectile(Projectile_Type.MORTAR);
			break;
		}
		}
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
}
