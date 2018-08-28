package engine;

public interface Damageable
{
	/**
	 * The purpose of doDamage is to do damage to the object.
	 * A more appropriate name might be receiveDamage.
	 * @param amount
	 */
	public void doDamage(double amount);
	/**
	 * The purpose of handleEnemyCollision is to do precisely
	 * that, handle the collision. More often than not, this 
	 * function will lead to a call of the doDamage function.
	 * @param target
	 */
	public void handleEnemyCollision(DrawableObject target);
}
