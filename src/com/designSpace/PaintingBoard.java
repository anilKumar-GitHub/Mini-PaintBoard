package com.designSpace;
/**
 *		Documentation
 *		Start Date 	: 22-12-2015.			Finished On : 04-01-2016.
 * 		Author		: Anil Kumar B P.
 * 		VTU Reg		: 3AE12CS401.
 * 		Programming Language : Core Java.
 * 		Save file as paintBoard.java
 * 		Start-up file to be loaded.
 */
import java.awt.BorderLayout;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

// All Entities classes
import com.designSpace.EntitySet.Circle;
import com.designSpace.EntitySet.Entity;
import com.designSpace.EntitySet.Line;
import com.designSpace.EntitySet.Polygons;
import com.designSpace.EntitySet.Square;
import com.designSpace.EntitySet.Entity.ENTITY_TYPE;
import com.designSpace.EntitySet.FileOperation;

//All Graphic supporting classes
import com.designSpace.tools.PaintingDashBoard;
import com.designSpace.tools.MenuBarPanel;
import com.designSpace.tools.ToolBarPanel;;

/*
 * 	Class			:	paintBoard
 * 	Functionality	:	This class holds menu bar, tool bar and canvas.
 * 						Provides the mouse and key event listener.
 *	Implements		:	MouseListener, MouseMotionListener, KeyListener
 */
public class PaintingBoard implements MouseListener, MouseMotionListener, KeyListener{
	
	public JFrame frm = null;		// Main window frame which holds all objects.
	JScrollPane scrollPanel = null;	// To scroll canvas, in future to enhance the application.
	// Creating association to hold reference.
	public PaintingDashBoard paintArea = null;	// painting area where entities are drawn 
	MenuBarPanel menu = null;				// Provides menu option
	public ToolBarPanel tools = null;		// Provides option for entities selection.

	// Status Bar to show current activity and guide user throughout application
	public JLabel statusBar = new JLabel("File Not opened");	
	Entity entity = null;	// Holds temporary entity to hold status of operation.
	
	int x = 0, y = 0;

	public Polygon poly = new Polygon();
	
	public Polygon getPoly() {
		return poly;
	}

	public void setPoly(Polygon poly) {
		this.poly = poly;
	}

	/* Constructor	: paintBoard().
	 * Scope		: public
	 * Description 	: This is default constructor used to initialize the paint application and start it.
	 * 				  
	 */
	public PaintingBoard() {

		super();
		frm = new JFrame("Mini-Paint Board.");
		paintArea = new PaintingDashBoard();
		// Adding paintArea to scrollable pane
		scrollPanel = new JScrollPane(paintArea);
		
		// initiating menu, tool bar and passing paintBorad object to each. 
		tools = new ToolBarPanel(this);
		menu = new MenuBarPanel(this);
		// Adding events
		paintArea.addMouseListener(this);
		paintArea.addMouseMotionListener(this);
		paintArea.addKeyListener(this);

		// Adding all object to frame
		frm.setJMenuBar(menu);
		frm.add(tools, BorderLayout.NORTH);		
		frm.add(scrollPanel,BorderLayout.CENTER);
		frm.add(statusBar, BorderLayout.SOUTH);

	    frm.setSize(1212, 705);	 // window size.
	    frm.setResizable(false); // resizing is disabled.
	    frm.setVisible(true);	 
	}

	
	/* Function		: main(String[] args).
	 * Return   	: void
	 * Scope		: public, static
	 * Description 	: initiates paintBoard object and running application.
	 * 
	 */
	public static void main(String[] args) {

		new PaintingBoard();
	}


	/* Function		: mouseClicked(MouseEvent ME).
	 * Return   	: void
	 * Scope		: public
	 * Description 	: Initiate the creation of different entities depending on current entity type.
	 * 				  
	 */	
	@Override
	public void mouseClicked(MouseEvent ME) {

		// if file is not opened then it will not allow to draw any entity.
		if( paintArea.FILE_OPEN_STATUS == FileOperation.FILE_NOT_OPENED )	return;
		
		// Whenever Right Mouse button pressed 
		// then current entity type sets to none, indicating no entity is selected to draw. 
		if( ME.getButton() == MouseEvent.BUTTON3)	{
			Entity.currentEntityType = ENTITY_TYPE.NONE;
			poly.npoints = 0;
			paintArea.repaint();
			return;
		}
		
		// if Left-Mouse button is not selected then simply returns.
		if( ME.getButton() != MouseEvent.BUTTON1 )	return;
		
		// Depending on type entity, it creates object of that entity and 
		// calls constructor method of respective class to construct object. 
		if( Entity.currentEntityType == ENTITY_TYPE.LINE)	{
			entity = new Line();
			entity.constructEntity(this, ME);
		}
		
		if( Entity.currentEntityType == ENTITY_TYPE.CIRCLE)	{
			entity = new Circle();
			entity.constructEntity(this, ME);
		}		
		
		if( Entity.currentEntityType == ENTITY_TYPE.RECTANGLE )	{
			entity = new com.designSpace.EntitySet.Rectangle(); 
			entity.constructEntity(this, ME);
		}
		if( Entity.currentEntityType == ENTITY_TYPE.SQUARE )	{
			entity = new Square();
			entity.constructEntity(this, ME);
		}

		if( Entity.currentEntityType == ENTITY_TYPE.POLYGON )	{
			entity = new Polygons();
			entity.constructEntity(this, ME);
		}		
		
		paintArea.selectedEntity = null;
		// Checks for double click and select object in red color to delete object
		// paintArea.selectedEntity object to be deleted
		if( ME.getClickCount() == 2 &&  ME.getButton() == MouseEvent.BUTTON1 )	{
			if( paintArea.operationalEntity != null )					
				paintArea.selectedEntity = paintArea.operationalEntity;
				paintArea.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent ME) {

	}

	@Override
	public void mouseExited(MouseEvent ME) {

	}

	@Override
	public void mousePressed(MouseEvent ME) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	/* Function		: mouseDragged(MouseEvent ME).
	 * Return   	: void
	 * Scope		: public
	 * Description 	: Relocates the object to specified position.
	 * 				  
	 */	
	@Override
	public void mouseDragged(MouseEvent ME) {
		statusBar.setText("\t[ X : "+ME.getX()+", Y : "+ME.getY()+" ]" + " Moving entity... release mouse button after placing entity.");
		if( paintArea.operationalEntity != null )	{
			// Calls respective class to move entity by x, y amount of position. 
			paintArea.operationalEntity.moveBy(ME.getX(), ME.getY());
			paintArea.repaint();
		}
	}

	
	/* Function		: mouseMoved(MouseEvent ME).
	 * Return   	: void
	 * Scope		: public
	 * Description 	: Displays current position of mouse along with current status of drawing panel.
	 * 				  Also allows the creation of imaginary entity before drawing entity.
	 * 				  Provides dummy shape of entity by creating temporary entity.
	 */	
	@Override
	public void mouseMoved(MouseEvent ME) {

		// Stores the mouse position to display.
		String mousePosition = "\t[ X : "+(ME.getX() - paintArea.getCanWidth() / 2)+", Y : "+(paintArea.getCanHieght() / 2 - ME.getY())+" ]";
		statusBar.setText(mousePosition);
		// checks for file status and displays appropriate message.
		if( paintArea.FILE_OPEN_STATUS == FileOperation.FILE_NOT_OPENED )	{
			statusBar.setText(" File Not Opened");
			return;
		}

/*		x = ME.getX();
		y = ME.getY();
		paintArea.setXY(x, y);
		paintArea.operationalEntity = null;
*/

		/*
		 * 	Depending currentEntityType temporary objects are created and 
		 * 	imaginary entity is drawn on canvas before deciding entity to be drawn.
		 * 	At this time temporary object is created with imaginary current x, y position.
		 * 	If polygon[poly] points are equal are less than 0 means still initial point for object is not selected
		 * 	No imaginary entity is drawn.
		 */
		if( Entity.currentEntityType == ENTITY_TYPE.LINE )	{
			statusBar.setText(mousePosition+" Line entity is selected. Press Right-Mouse button deselect entity.");
			if( poly.npoints != 0 )	{
				statusBar.setText(mousePosition+" Constructing Line... Select next position to draw line. Click at specific position to drwa.");
				paintArea.setEntity( new Line(poly,ME.getX(),ME.getY()));
			}
		}

		if( Entity.currentEntityType == ENTITY_TYPE.CIRCLE )	{
			statusBar.setText(mousePosition+" Circle entity is selected. Press Right-Mouse button deselect entity.");
			if( poly.npoints != 0 )	{
				statusBar.setText(mousePosition+" Constructing Circle... Select radius to draw circle. Click at specific position to drwa.");
				paintArea.setEntity(new Circle(poly,ME.getX(),ME.getY()));
			}
		}

		if( Entity.currentEntityType == ENTITY_TYPE.RECTANGLE ){
			statusBar.setText(mousePosition+" Rectangle entity is selected. Press Right-Mouse button deselect entity.");
			if( poly.npoints != 0 )	{
				statusBar.setText(mousePosition+" Constructing Rectangle... Select the width and height of rectangle. Click at specific position to drwa.");
				paintArea.setEntity(new com.designSpace.EntitySet.Rectangle(poly, ME.getX(), ME.getY()));
			}
		}

		if( Entity.currentEntityType == ENTITY_TYPE.SQUARE )	{
			statusBar.setText(mousePosition+" Square entity is selected. Press Right-Mouse button deselect entity.");
			if( poly.npoints != 0 )	{
				statusBar.setText(mousePosition+" Constructing Square... Select side size of the square. Click at specific position to drwa.");
				paintArea.setEntity(new Square(poly, ME.getX(), ME.getY()));
			}
		}

		if( Entity.currentEntityType == ENTITY_TYPE.POLYGON )	{
			statusBar.setText(mousePosition+" Polygon entity is selected. Press Right-Mouse button deselect entity.");			
			if( poly.npoints != 0 )	{
				statusBar.setText(mousePosition+" Constructing Polygon... Click Select next position to draw polygon. Double Click at specific position to drwa.");
				paintArea.setEntity(new Polygons(poly, ME.getX(), ME.getY()));
			}
		}

		/*
		 * 	Stores all entities in List<Entity> comes under mouse pointer.  
		 */
		List<Entity> entitiesList = new ArrayList<Entity>();
		for( int i = 0; i < paintArea.getEntities().size(); i++ )	{
			// indicate by true/false of mouse hover property for current entity.
			boolean flag = paintArea.getEntities().get(i).onMouseHover(ME);
			if(flag)	{
				// Add to all entity to temporary entities
				entitiesList.add(paintArea.getEntities().get(i));
			}
		}
		
		paintArea.selectedEntity = null;
		if( Entity.currentEntityType == ENTITY_TYPE.NONE && paintArea.getEntities().size() > 0)
			if( paintArea.selectedEntity == null )  statusBar.setText(mousePosition+" Double Click to select the entity.  Select and Drag entity to move the entity.");
			else 									statusBar.setText(mousePosition+" Press DEL to delete selected entity.");

		// Now find the closest entity to current mouse pointer.
		paintArea.operationalEntity = null;
		if( entitiesList.size() > 0 )
		paintArea.operationalEntity = Entity.findClosetEntity(entitiesList, ME.getX(), ME.getY());		
		paintArea.repaint();
	}

	@Override
	public void keyPressed(KeyEvent KE) {
	}

	/* Function		: keyReleased(KeyEvent KE).
	 * Return   	: void
	 * Scope		: public
	 * Description 	: Delete the selected entity when DEL is pressed.
	 */	
	@Override
	public void keyReleased(KeyEvent KE) {
		if( KE.getKeyCode() == KeyEvent.VK_DELETE )	{
			// Allows only when object is selected.
			if( paintArea.selectedEntity != null )		{
				// Removes from list of entity and repaint the canvas
				paintArea.getEntities().remove(paintArea.selectedEntity);
				// points to null because even removing also these two object hold reference. 
				paintArea.selectedEntity = null;
				paintArea.operationalEntity = null;
				paintArea.repaint();
				statusBar.setText("Entity deleted..");
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent KE) {
	}
}