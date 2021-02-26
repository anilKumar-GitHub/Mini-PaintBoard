package com.designSpace.tools;
/**
 *		Documentation
 * 		Author		: Anil Kumar B P.
 * 		Programming Language : Core Java.
 * 		Save file as paintingBoard.java
 * 		
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import com.designSpace.EntitySet.Entity;
import com.designSpace.EntitySet.FileOperation;

/*
 * 	Class			:	paintingBoard
 * 	Functionality	:	This class holds the canvas to draw the entities.
 * 						Displays all entities on canvas graph.
 * 						Also sets the width and height of canvas.
 * 						Also holds name current file name which is drawing currently. 
 *	Extends			:	Canvas.
 */
public class PaintingDashBoard	extends Canvas {
	
	private static final long serialVersionUID = 1L;
	public int canWidth = 1200;		// Specifies width.
	public int canHieght = 600;		// Specifies Height.
	public String fileName = null;	// Specifies name of the file.	
	public int FILE_OPEN_STATUS = FileOperation.FILE_NOT_OPENED;	// indicates file open status.
	public boolean LAST_MODIFICATION = FileOperation.LAST_MODIFICATION_SAVED;	// Indicates file saved or not.

	List<Entity> entities = new ArrayList<Entity>();	// Holds all entities to be drawn.
	Polygon poly = new Polygon();						// Holds imaginary entity point list. 
	// All below three entity object holds, temporary reference to manipulate entity like selection, deletion and moving.
	Entity entity;										
	public Entity operationalEntity = null;
	public Entity selectedEntity = null;
	// indicates the currently mouse over object status. 
	public boolean showOpearationResult = false;
	
	// Set entity  
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	// Get All entities
	public List<Entity> getEntities() {
		return entities;
	}

	// Set new entities list. 
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public Polygon getPoly() {
		return poly;
	}

	public void setPoly(Polygon poly) {
		this.poly = poly;
	}

	//int x = 0, y = 0;
	
	//initial setting of painting board. 
	public PaintingDashBoard() {
		canWidth = 1200;
		canHieght = 600;
		// Temporary file name.
		fileName = new String("NONAME.txt");
		FILE_OPEN_STATUS = FileOperation.FILE_NOT_OPENED;	// set to not opened.
	}
	
/*	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
*/
	public int getCanWidth() {
		return canWidth;
	}

	public void setCanWidth(int canWidth) {
		this.canWidth = canWidth;
	}

	public int getCanHieght() {
		return canHieght;
	}

	public void setCanHieght(int canHieght) {
		this.canHieght = canHieght;
	}

	@Override
	public void paint(Graphics g) {
		
		g.setColor(Color.BLACK);

		g.drawRect(0, 0, canWidth, canHieght);

		// if file not opened set background to black color.
		if( FILE_OPEN_STATUS == FileOperation.FILE_NOT_OPENED )	{
			setBackground(Color.BLACK);
		}else	{
			// if file is open set to gray color.
			g.setColor(Color.GRAY);
			setBackground(Color.WHITE);
			// draw the horizontal[x-axis] and vertical[y-axis] line.
			g.drawLine(canWidth/2, 0, canWidth/2, canHieght);
			g.drawLine(0, canHieght/2, canWidth, canHieght/2);
		}
		
		/*
		 * 	Draw the all entities on the canvas.
		 */
		g.setColor(Color.BLACK);
		for( Entity e : entities )	{
			// calls the respective class method at run time to draw entity.
			e.drawEntity(g);
		}

		// if any object is under mouse pointer then displays that entity.
		if( operationalEntity != null )	{
			g.setColor(Color.BLUE);
			operationalEntity.drawEntity(g);
		}
		
		// if any entity is double clicked then it is displayed with red color to indicate selection.
		if( selectedEntity != null )	{
			g.setColor(Color.RED);
			selectedEntity.drawEntity(g);
		}

		// entity object holds the imaginary entity reference to be drawn temporarily 
		g.setColor(Color.BLACK);
		if( Entity.currentEntityType != Entity.ENTITY_TYPE.NONE  )
			if( poly.npoints != 0 )	// Checks initial point is selected or not.
				entity.drawImaginaryEntity(g);	// calls imaginary entity drawing method.
	}

	// Sets the dimension of canvas.
	@Override
	public Dimension preferredSize() {

		return new Dimension( canWidth, canHieght );
	}

}