package com.designSpace.EntitySet;
/*
 * 	Interface 		:	Entities.
 * 	Functionality	:	Interface provides list methods for all entity to implement.
 * ---------------------------------------------------------------------
 * 	Class			:	Entity.
 * 	Functionality	:	This class holds current entity type and provides entity type list.
 * 						Polygon operation such finding longest and lowest polygon.
 *	Implements		:	Entities.
 */

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;
import com.designSpace.PaintingBoard;

interface Entities{
	public void dispInfo();		// Displays entity type for testing, 

	/*
	 * 	Returns entity details in predefined pattern defined by individual entity.
	 */
	public String toString();		

	
	/*
	 *	Returns the length of of individual entity.
	 *	Note	:	This methods used for finding minimum and maximum length polygon.
	 *  			So best suited for polygon operation, 
	 *  			for other entity i just defined some defined default value and from some entity i considered area of that entity.
	 */
	public double getLength();

	
	
	/*
	 * 	This method translates or moves the entity to new location specified by mouse x,y position.
	 * 	Implementation is different so implementation left to deriving classes.
	 */
	public void moveBy(int x, int y);

	
	
	/*
	 * 	The drawEntity method actuals draws entity on canvas.
	 * 	It takes Graphics object to draw entity.
	 */
	public void 	drawEntity(Graphics Grp);

	
	
	/*
	 * 	onMouseHover method detects mouse move on object position.
	 * 	If mouse if on any entity, that entity is highlighted by return true. 
	 */
	public boolean onMouseHover(MouseEvent ME);

	
	
	/*
	 * drawImaginaryEntity method does same work as drawEntity it will not finalizes the entity design.
	 * It just provides imaginary view of entity to be drawn at each steps of selection.
	 */
	public void drawImaginaryEntity(Graphics Grp);

	
	
	/*
	 * Returns the minimum distance from mouse position and among entity points
	 */
	public double getMinimumDistance(int x, int y);

	
	
	/*
	 * constructEntity method collects the points from mouse click event 
	 * then adds those points to temporary polygon and simultaneously displays partially
	 * constructed entity to draw complete entity.   
	 */
	public void constructEntity(PaintingBoard pb, MouseEvent ME);
}

public abstract class Entity implements Entities{

	/*
	 * 	ENTITY_TYPE defines list of entity type. NONE indicates no entity selected.
	 */
	public static enum ENTITY_TYPE{NONE, PIXEL, LINE, SQUARE, RECTANGLE, CIRCLE, OVEL, TRIANGLE, POLYGON };

	/*
	 * currentEntityType indicates currently drawing entity in application.
	 * default value is NONE.
	 */
	public static ENTITY_TYPE currentEntityType = ENTITY_TYPE.NONE;		
	
	
	
	/* Function		: findLongestEntity(List<Entity> entities).
	 * Return   	: Entity
	 * Scope		: public, static
	 * Description 	: Finds the entity which has lowest length. 
	 */	
	public static Entity findLongestEntity(List<Entity> entities)	{

		// Assuming first one as longest length entity.
		Entity entity = entities.get(0);
		
		for( int i = 0; i < entities.size(); i++ )	{
			// Check any entity have length more than this 
			// If so, then exchange entity,
			if( entities.get(i).getLength() > entity.getLength() )	{
				entity = entities.get(i);
			}
		}
		return entity;
	}

	/* Function		: findLowestEntity(List<Entity> entities).
	 * Return   	: Entity
	 * Scope		: public, static
	 * Description 	: Finds the entity which has lowest length. 
	 */	
	public static Entity findLowestEntity(List<Entity> entities)	{
		
		// Assuming first one as lowest length entity.
		Entity entity = entities.get(0);
		
		for( int i = 1; i < entities.size(); i++ )	{
			// Check any entity have length less than this 
			// If so, then exchange entity,
			if( entities.get(i).getLength() < entity.getLength() )	{
				entity = entities.get(i);
			}
		}
		return entity;
	}

	/* Function		: findClosetEntity(List<Entity> entitiesList, int x, int y).
	 * Return   	: Entity
	 * Scope		: public, static
	 * Description 	: When more than one entity comes under mouse position.
	 * 				  Then this method finds most nearest entity among then to select or highlight that entity.  
	 */	
	public static Entity findClosetEntity(List<Entity> entitiesList, int x, int y) {

		// Assuming first one as lowest length entity.		
		Entity entity = entitiesList.get(0);
		// Find nearest points among all points of that entity.
		double prevDist = entity.getMinimumDistance(x, y);
		for( int i = 0; i < entitiesList.size(); i++ )	{
			// If any entity have minimum distance than previous one entity distance
			// then exchange and  again calculate distance.
			if( entitiesList.get(i).getMinimumDistance(x, y) < prevDist )	{
				entity = entitiesList.get(i);
				prevDist = entity.getMinimumDistance(x, y);
			}
		}
		return entity;
	}

}
