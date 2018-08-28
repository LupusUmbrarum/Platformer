package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

import enemies.Enemy;
import enemies.Enemy_Jumper;
import engine.Camera;
import engine.DrawableObject;
import engine.Engine;
import engine.Game;
import engine.MovableObject;

public class PlatformManager implements Game
{
	public DrawableObject player;
	private ArrayList<ArrayList<DrawableObject>> objectList = new ArrayList<>();
	private int width = Engine.getEngine().getWidth(), height = Engine.getEngine().getHeight();
	public static final int PLAYER = 0, BLOCK = 1, ENEMY = 2;
	public static int columnWidth = 100;
	
	public PlatformManager()
	{
		//floor
		for(int x = 0; x < 1000; x++)
		{
			addBlock(x*(width/25), height-100);
		}
		
		//stairs
		for(int x = 0; x < 50; x++)
		{
			addBlock(600 + x*(width/25) + 10*x, height - 250 - 20*x);
		}
		
		//floating platform
		for(int x = 0; x < 25; x++)
		{
			addBlock((x+1)*25 + 1500, height - 200);
		}
		
		addBlock(400, height-100-20);
		addBlock(400, height-100-40);
		addBlock(400, height-100-60);
		addBlock(400, height-100-80);
		
		addDrawableObject(new Platformer(100, height-100-50), PLAYER);
		
		addDrawableObject(new Enemy(1000, height-100-100), ENEMY);
		
		addDrawableObject(new Enemy_Jumper(1700, height - 300, Enemy.GO_LEFT), ENEMY);
	}
	
	public void gameTick(long deltaTime)
	{
		LinkedList<DrawableObject> updatedObjects = new LinkedList<>();
		
		DrawableObject temp;
		
		//tick every object
		for(int x = 0; x < objectList.size(); x++)
		{
			for(int y = 0; y < objectList.get(x).size(); y++)
			{
				objectList.get(x).get(y).tick(deltaTime);
			}
		}
		
		//check to see if the MoveableObjects need to have their column index adjusted
		for(int x = 0; x < objectList.size(); x++)
		{
			for(int y = 0; y < objectList.get(x).size(); y++)
			{
				if(objectList.get(x).get(y) instanceof MovableObject)
				{
					temp = objectList.get(x).get(y);
					
					if(updateColumnIndex(objectList.get(x).get(y)))
					{
						y--;
						updatedObjects.add(temp);
					}
				}
			}
		}
		
		for(int x = 0; x < updatedObjects.size(); x++)
		{
			temp = updatedObjects.get(x);
			
			if(temp instanceof Platformer)
			{
				player = null;
				addDrawableObject(temp, PLAYER);
			}
			else
			{
				addDrawableObject(temp, 999);
			}
		}
		
		//System.out.println("-------------------------------");
		
		if(player != null)
		{
			//control moving the camera
			//check for horizontal movement
			if(player.xLocation <= Camera.getCamera().x + Camera.getCamera().width*3/8)
			{
				Camera.getCamera().setX(player.xLocation - Camera.getCamera().width*3/8);
			}
			else if(player.xLocation + player.width >= Camera.getCamera().x + Camera.getCamera().width*5/8)
			{
				Camera.getCamera().setX(player.xLocation + player.width - Camera.getCamera().width*5/8);
			}
			
			//check for vertical movement
			//I'm sure one day I'll get this to work in a more succinct way
			//but today is not that day
			if(-player.yLocation + height/4 > Camera.getCamera().y)
			{
				Camera.getCamera().setY(-player.yLocation + height/4);
			}
			else if(-(player.yLocation + player.height) < (Camera.getCamera().y - Camera.getCamera().height*3/4))
			{
				Camera.getCamera().setY(-player.yLocation + -player.height + Camera.getCamera().height*3/4);
			}
		}
	}
	
	public DrawableObject getPlayer()
	{
		return player;
	}
	
	public boolean updateColumnIndex(DrawableObject target)
	{		
		int colNum = target.xLocation / columnWidth;
		
		if(colNum - 1 < 0)
		{
			colNum = 1;
		}
		
		boolean result = false;
		
		for(int x = colNum - 1; x <= colNum + 1 && x < objectList.size(); x++)
		{
			if(objectList.get(x).remove(target))
			{
				result = true;
			}
		}
		
		return result;
	}
	
	public DrawableObject[] checkCollision(DrawableObject target)
	{
		LinkedList<DrawableObject> tempArr = new LinkedList<>();
		
		DrawableObject temp;
		
		DrawableObject[] finalArr = {};
		
		int colNum = target.xLocation / columnWidth;
		
		//make sure we don't try to access a negative index in the array
		if(colNum < 0)
		{
			return finalArr;
		}
		
		ArrayList<DrawableObject> searchArr;
		
		//initialize x to the previous colNum or 0, whichever's higher.
		//iterate through the columns before, at, and after the target's column.
		for(int x = (colNum - 1 > -1 ? colNum - 1 : 0) ; x <= colNum + 1 ; x++)
		{
			searchArr = objectList.get(x);
			
			for(int y = 0; y < searchArr.size(); y++)
			{
				temp = searchArr.get(y);
				
				//if the newly selected object is the target, skip this turn
				//do not pass go, do not collect 200 dollars
				if(temp == target)
				{
					continue;
				}
				
				//else, check to see if the target's hitbox is overlapping the object's
				//hitbox. If they are overlapping, add the object to the tempArr
				if(target.hitbox.intersects(temp.hitbox) && target.collisionEnabled && temp.collisionEnabled)
				{
					tempArr.add(temp);
				}
			}
		}
		
		//if we didn't hit anything, return a null array
		if(tempArr.size() == 0)
		{
			return finalArr;
		}
		
		//if we did hit something, make a new, boring array to empty
		//the fancy array into
		finalArr = new DrawableObject[tempArr.size()];
		
		//while the fancy array has objects, empty the fancy array
		//into the old array.
		while(tempArr.size() > 0)
		{
			finalArr[tempArr.size()-1] = tempArr.remove(0);
		}
		
		return finalArr;
	}
	
	public boolean addBlock(int x, int y)
	{
		return addDrawableObject(new DrawableObject(width/25, height/25, x, y, Color.CYAN), 999);
	}
	
	public boolean addDrawableObject(DrawableObject obj, int type)
	{
		int colNum = obj.xLocation / columnWidth;
		
		if(colNum < 0)
		{
			return false;
		}
		
		while(objectList.size() < (colNum + 1 + Math.sqrt((double)colNum)))
		{
			objectList.add(new ArrayList<DrawableObject>());
		}
		
		switch(type)
		{
		case PLAYER:
		{
			if(player == null)
			{
				player = obj;
				return objectList.get(colNum).add(obj);
			}
			else
			{
				return false;
			}
		}
		default:
		{
			return objectList.get(colNum).add(obj);
		}
		}
	}
	
	public void addDrawableObject(DrawableObject obj)
	{
		
	}
	
	public void removeDrawableObject(DrawableObject obj)
	{
		if(obj.xLocation/columnWidth - 1 >= 0)
		{
			objectList.get(obj.xLocation/columnWidth - 1).remove(obj);
		}
		objectList.get(obj.xLocation/columnWidth).remove(obj);
		objectList.get(obj.xLocation/columnWidth + 1).remove(obj);
	}
}
