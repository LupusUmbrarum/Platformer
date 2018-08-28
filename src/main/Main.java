package main;

import engine.Engine;

public class Main
{
	public static void main(String[] args)
	{
		Engine.setCurrentGame(new PlatformManager());
		Engine.getEngine().start();
	}
}
