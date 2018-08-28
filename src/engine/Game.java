package engine;

/**
 * The purpose of this interface is to set apart certain classes
 * as games that can be loaded into the Engine as the current game.
 */
public interface Game
{
	public DrawableObject[] checkCollision(DrawableObject target);
	public void gameTick(long deltaTime);
	public DrawableObject getPlayer();
}
