package engine;

public class Camera
{
	private static Camera theCamera;
	public int x = 0, y = 0, width, height;
	
	private Camera()
	{
		width = Engine.getEngine().getWidth();
		height = Engine.getEngine().getHeight();
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public static Camera getCamera()
	{
		if(theCamera == null)
			theCamera = new Camera();
		
		return theCamera;
	}
}
