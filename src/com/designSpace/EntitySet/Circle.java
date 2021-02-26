package com.designSpace.EntitySet;
/*
 * 	Class					:	Circle
 * 	Functionality			:	Circle class defines set of method to hold and draw Circle.
 *	Extends Abstract class	:	Entity.
 */
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import com.designSpace.PaintingBoard;

public class Circle extends Entity	{

	Point point = new Point(0, 0);	// holds position to draw circle
	int radius = 0;					// holds radius of circle

	/* Constructor	: Circle().
	 * Scope		: public
	 * Description 	: This is default constructor used to initialize circle start point to 0,0 and radius to 0.
	 */
	public Circle() {
		setPoint(new Point(0, 0));
		radius = 0;
	}

	/* Constructor	: Circle(int x, int y, int radius)
	 * Scope		: public
	 * Description 	: This parameterized constructor assigns start point to x,y and radius.
	 */
	public Circle(int x, int y, int radius)	{
		setPoint(new Point(x, y));
		setRadius(radius);
	}
	
	/* Constructor	: Circle(Polygon poly)
	 * Scope		: public
	 * Description 	: This parameterized constructor assigns start point of circle from polygon object. 
	 * 				  This constructor is called when polygon have all points to draw a circle.
	 */
	public Circle(Polygon poly)	{
		int x[] = poly.xpoints;
		int y[] = poly.ypoints;

		int X1 = 0, X2 = 0;
		int Y1 = 0, Y2 = 0;

		// While drawing circle, we have to check greater point position, to set that position as start position
		if( x[1] > x[0] )	{	X2 = x[1];	X1 = x[0]; 	}
		else				{	X2 = x[0];	X1 = x[1];	}
		if( y[1] > y[0] )	{	Y2 = y[1];	Y1 = y[0];	}
		else				{	Y2 = y[0];	Y1 = y[1];	}	 

		// Calculate radius between center point and current mouse point position
		radius = (X2-X1) > (Y2-Y1) ? (X2-X1) : (Y2-Y1);
		// Set start point
		setPoint(new Point(x[0]-radius, y[0]-radius));
		radius *= 2;	// make double of that radius, to ensure whole exact diameter
	}
	
	/* Constructor	: Circle(Polygon poly, int x2 , int y2)
	 * Scope		: public
	 * Description 	: This parameterized constructor assigns start point of circle from polygon object. 
	 * 				  This constructor is same as above but it creates temporary circle to show imaginary circle before drawing actual circle 
	 */
	public Circle(Polygon poly, int x2 , int y2)	{
		int x[] = poly.xpoints;
		int y[] = poly.ypoints;

		int X1 = 0, X2 = 0;
		int Y1 = 0, Y2 = 0;

		// While drawing circle, we have to greater point position, to set that position as start position
		if( x2 > x[0] )	{	X2 = x2;	X1 = x[0]; 	}
		else			{	X2 = x[0];	X1 = x2;	}
		if( y2 > y[0] )	{	Y2 = y2;	Y1 = y[0];	}
		else			{	Y2 = y[0];	Y1 = y2;	}	 
		
		// Calculate radius between center point and current mouse point position
		radius = (X2-X1) > (Y2-Y1) ? (X2-X1) : (Y2-Y1);
		// Set start point
		setPoint(new Point(x[0]-radius, y[0]-radius));
		radius *= 2;		// make double of that radius, to ensure whole exact diameter
	}


	// getter and setter methods
	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	// overrides toString() and creates own string format as follow to store in file,
	@Override
	public String toString() {
		
		return "\nCIRCLE "+point.x+" "+point.y+" "+radius;
	}

	// Draws the final circle on the canvas.
	@Override
	public void drawEntity(Graphics Grp) {
		Grp.drawOval((int)getPoint().getX(), (int)getPoint().getY(), radius, radius);
	}

	// Draws the imaginary circle on the canvas.
	@Override
	public void drawImaginaryEntity(Graphics Grp) {
		Grp.drawOval((int)getPoint().getX(), (int)getPoint().getY(), radius, radius);
	}

	// Construct the circle every time with new x2 and y2 position.
	@Override
	public void constructEntity(PaintingBoard pb, MouseEvent ME) {

		// If no point is selected then sets the poly with initial points
		if( pb.poly.npoints < 1 )	{
			// Sets type of entity
			Entity.currentEntityType = ENTITY_TYPE.CIRCLE;
			// Reflects the changes in poly to ensure poly is added with initial points
			pb.poly.addPoint(ME.getX(), ME.getY());
			pb.paintArea.setPoly(pb.poly);
		}else	{
			// When second time mouse clicked, adds those points to poly and creates circle instance
			pb.poly.addPoint(ME.getX(), ME.getY());
			// Adds new circle to Entities list.
			pb.paintArea.getEntities().add(new Circle(pb.poly));
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

	// Returns the length of circle.
	@Override
	public double getLength() {
		// not implemented by sub class
		// default value is 0
		return 0;
	}

	// Checks for starting and end points to find whether comes under mouse current points.
	@Override
	public boolean onMouseHover(MouseEvent ME) {

		// Mouse position calculated with respect to starting x,y position and total radius 
		if( ME.getX() > point.getX() && ME.getX() < (point.getX() + radius) && ME.getY() > point.getY() && ME.getY() < (point.getY() + radius))
			return true;
		else
			return false;
	}

	// Return minimum distance from current mouse point to starting to total radius covered
	// Return which point has minimum distance
	@Override
	public double getMinimumDistance(int x, int y) {
		double x1 = x;
		double y1 = y;
		double x2 = point.x;
		double y2 = point.y;		
		// find distance between start point and x,y
		double d1 = Math.sqrt( (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) ); 

		// find distance between end point and x,y with radius added 
		x2 = point.x+radius;
		y2 = point.y+radius;
		double d2 = Math.sqrt( (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) ); 

		return d1 < d2 ? d1 : d2;
	}

	// Moves line to new x, y position 
	@Override
	public void moveBy(int x, int y) {

		// Moves the circle with respective point to x,y points
		point.setLocation(x-radius/2, y-radius/2);
	}
}
