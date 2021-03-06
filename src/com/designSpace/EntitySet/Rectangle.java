package com.designSpace.EntitySet;
/*
 * 	Class					:	Rectangle
 * 	Functionality			:	Rectangle class defines set of method to hold and draw Rectangle.
 *	Extends Abstract class	:	Entity.
 */
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import com.designSpace.PaintingBoard;

public class Rectangle extends Entity	{

	
	// Holds all information of rectangle such as position and dimension
	java.awt.Rectangle rectPoints = new java.awt.Rectangle();	

	/* Constructor	: Rectangle().
	 * Scope		: public
	 * Description 	: This is default constructor used to initialize Rectangle location to 0,0 and dimension to 0,0.
	 */
	public Rectangle()	{
		rectPoints.setLocation(0, 0);
		rectPoints.setSize(0, 0);
	}

	/* Constructor	: Rectangle(Polygon poly)
	 * Scope		: public
	 * Description 	: This parameterized constructor assigns location and dimension of rectangle from polygon object. 
	 * 				  This constructor is called when polygon have all points to draw a rectangle.
	 */
	public Rectangle(Polygon poly)	{
		int x[] = poly.xpoints;
		int y[] = poly.ypoints;
		
		int X1 = 0, X2 = 0, Y1 = 0, Y2 = 0; 
		
		// While drawing circle, we have to check greater point position, to set that position as start position
		if( x[1] > x[0] )	{	X1 = x[0];	X2 = x[1];	}
		else				{	X1 = x[1];	X2 = x[0];	}
		if( y[1] > y[0] )	{	Y1 = y[0];	Y2 = y[1];	}
		else				{	Y1 = y[1];	Y2 = y[0];	}
		
		// Calculate width and height of rectangle from start location to current mouse point position
		int width = (int)Math.sqrt( Math.pow((X2-X1), 2) + Math.pow((Y2-Y2), 2));
		int height = (int)Math.sqrt( Math.pow((X2-X2), 2) + Math.pow((Y2-Y1), 2));
		// Set values
		rectPoints.setLocation(X1, Y1);
		rectPoints.setSize(width, height);
	}
	

	/* Constructor	: Rectangle(Polygon poly, int x2, int y2)
	 * Scope		: public
	 * Description 	: This parameterized constructor assigns all point of rectangle from polygon object. 
	 * 				  This constructor is same as above but it creates temporary rectangle to show imaginary rectangle before drawing actual rectangle
	 */
	public Rectangle(Polygon poly, int x2, int y2)	{
		
		int x[] = poly.xpoints;
		int y[] = poly.ypoints;
		int X1 = 0, X2 = 0, Y1 = 0, Y2 = 0; 
		
		if( x2 > x[0] )	{	X1 = x[0];	X2 = x2;	}
		else			{	X1 = x2;	X2 = x[0];	}
		if( y2 > y[0] )	{	Y1 = y[0];	Y2 = y2;	}
		else			{	Y1 = y2;	Y2 = y[0];	}
		
		int width = (int)Math.sqrt( Math.pow((X2-X1), 2) + Math.pow((Y2-Y2), 2));
		int height = (int)Math.sqrt( Math.pow((X2-X2), 2) + Math.pow((Y2-Y1), 2));
		rectPoints.setLocation(X1, Y1);
		rectPoints.setSize(width, height);
	}
	
	// Explicit to set rectangle properties, 
	public Rectangle(int x, int y, int width, int height) {
		rectPoints.setLocation(x, y);
		rectPoints.setSize(width, height);
	}

	// overrides toString() and creates own string format as follow to store in file,
	@Override
	public String toString() {
		return "\nRECTANGLE "+rectPoints.x+" "+rectPoints.y+" "+rectPoints.width+" "+rectPoints.height;
	}

	// Draws the final rectangle on the canvas.
	@Override
	public void drawEntity(Graphics Grp) {
		Grp.drawRect((int)rectPoints.getX(), (int)rectPoints.getY(), (int)rectPoints.getWidth(), (int)rectPoints.getHeight());
	}

	// Draws the imaginary circle on the canvas.
	@Override
	public void drawImaginaryEntity(Graphics Grp) {
		
		Grp.drawRect((int)rectPoints.getX(), (int)rectPoints.getY(), (int)rectPoints.getWidth(), (int)rectPoints.getHeight());
	}

	// Construct the rectangle every time with new x2 and y2 position.
	@Override
	public void constructEntity(PaintingBoard pb, MouseEvent ME) {

		// If no point is selected then sets the poly with initial points
		if( pb.poly.npoints < 1 )	{
			// Sets type of entity
			Entity.currentEntityType = ENTITY_TYPE.RECTANGLE;
			// Reflects the changes in poly to ensure poly is added with initial points
			pb.poly.addPoint(ME.getX(), ME.getY());
			pb.paintArea.setPoly(pb.poly);
		}else	{
			// When second time mouse clicked, adds those points to poly and creates rectangle instance
			pb.poly.addPoint(ME.getX(), ME.getY());
			// Adds new rectangle to Entities list.
			pb.paintArea.getEntities().add(new Rectangle(pb.poly));
			// Set poly to initial point to indicate no points are selected
			pb.poly.npoints = 0;
			// Reflects the changes in poly to ensure poly is ready to receive new circle points
			pb.paintArea.setPoly(pb.poly);
			// Whenever new entity drawn set LASTMODIFICATION to not saved.
			pb.paintArea.LAST_MODIFICATION = FileOperation.LAST_MODIFICATION_NOT_SAVED;
		}
	}

	// Used to display testing purpose to know type of entity
	@Override
	public void dispInfo() {
		
		System.out.println("Information of "+this.getClass().getName());
	}

	// Returns the length of rectangle.
	@Override
	public double getLength() {

		// Here we considered as area for testing purpose
		return rectPoints.width * rectPoints.height;
	}

	// Checks for width and height from starting point to find whether comes under mouse current points.
	@Override
	public boolean onMouseHover(MouseEvent ME) {

		if( ME.getX() > rectPoints.getX() && ME.getX() < (rectPoints.getX()+rectPoints.getWidth()) && ME.getY() > rectPoints.getY() && ME.getY() < (rectPoints.getY()+rectPoints.getHeight())) 
			return true;
		else
			return false;		
	}

	// Return minimum distance from current mouse point to its end points
	// Return which point has minimum distance
	@Override
	public double getMinimumDistance(int x, int y) {
		double x1 = x;
		double y1 = y;
		double x2 = rectPoints.x;
		double y2 = rectPoints.y;		
		double d1 = Math.sqrt( (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) ); 

		x2 = rectPoints.x + rectPoints.width;
		y2 = rectPoints.y + rectPoints.height;
		double d2 = Math.sqrt( (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) ); 

		return d1 < d2 ? d1 : d2;
	}

	// Moves line to new x, y position 
	@Override
	public void moveBy(int x, int y) {
		rectPoints.setLocation(x-rectPoints.width/2, y-rectPoints.height/2);
	}
}