package engine;

public class MovableObject extends DrawableObject
{
	public int xVelocity = 0, yVelocity = 0, speed = 3, direction;
	public static final int GO_LEFT = -1, GO_RIGHT = 1, GO_UP = -1, GO_DOWN = 1;
	public long tickCount = 0, delayTime = 5, tempTick, tempTime;
	public static final long MAX_TICK_COUNT = 2147483646;
	public boolean onGround = false;
	
	public MovableObject()
	{
		super();
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
	
	public void move()
	{
		
	}
	
	public void updateHitbox()
	{
		this.hitbox.setLocation(this.xLocation, this.yLocation);
	}
}
