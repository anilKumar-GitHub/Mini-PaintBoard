package com.designSpace.EntitySet;

/*
 * 	Class					:	Line
 * 	Functionality			:	Line class defines set of method to hold and draw line.
 *	Extends Abstract class	:	Entity.
 */

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import com.designSpace.PaintingBoard;

public class Line extends Entity	{

	Point point1 = new Point();	// holds line starting x,y position
	Point point2 = new Point(); // holds line end x,y position

	/* Constructor	: Line().
	 * Scope		: public
	 * Description 	: This is default constructor used to initialize line points to 0,0 and 0,0.
	 */
	public Line() {
		setPoint1(new Point(0, 0));
		setPoint2(new Point(0, 0));	
	}
	
	/* Constructor	: Line(int x1, int y1, int x2, int y2).
	 * Scope		: public
	 * Description 	: This parameterized constructor assigns all points of line to x1,y1 and x2,y2.
	 */
	public Line(int x1, int y1, int x2, int y2)	{
		setPoint1(new Point(x1, y1));
		setPoint2(new Point(x2, y2));
	}

	/* Constructor	: Line(Polygon poly).
	 * Scope		: public
	 * Description 	: This parameterized constructor assigns all points of line from polygon object. 
	 * 				  This constructor is called when polygon have all points to draw a line.
	 */
	public Line(Polygon poly)	{
		// Read all x and y points from poly
		int x[] = poly.xpoints;
		int y[] = poly.ypoints;
		// Set to points of line 
		setPoint1(new Point(x[0], y[0]));
		setPoint2(new Point(x[1], y[1]));
	}
	
	/* Constructor	: Line(Polygon poly, int x2, int y2).
	 * Scope		: public
	 * Description 	: This parameterized constructor assigns point1.x, point1.y points of line from polygon object and point2.x, point2.y from x2, y2. 
	 * 				  This constructor is called to create imaginary line with x2, y2 line.
	 */
	public Line(Polygon poly, int x2, int y2)	{

		int x[] = poly.xpoints;
		int y[] = poly.ypoints;
		
		setPoint1(new Point(x[0], y[0]));
		setPoint2(new Point(x2, y2));
	}
	
	// getter to get point1
	public Point getPoint1() {
		return point1;
	}
	// setter to set point1
	public void setPoint1(Point point1) {
		this.point1 = point1;
	}
	
	// getter to get point2
	public Point getPoint2() {
		return point2;
	}
	// setter to set point2
	public void setPoint2(Point point2) {
		this.point2 = point2;
	}

	// overrides toString() and creates own string format as follow to store in file,
	@Override
	public String toString() {

		return "\nLINE "+point1.x+" "+point1.y+" "+point2.x+" "+point2.y;
	}

	// Draws the final line on the canvas.
	@Override
	public void drawEntity(Graphics Grp) {		
		Grp.drawLine((int)getPoint1().getX(), (int)getPoint1().getY(), (int)getPoint2().getX(), (int)getPoint2().getY());
	}

	// Draws the imaginary line on the canvas.
	@Override
	public void drawImaginaryEntity(Graphics Grp) {

		Grp.drawLine((int)getPoint1().getX(), (int)getPoint1().getY(), (int)getPoint2().getX(), (int)getPoint2().getY());
	}

	// Construct the line every time with new x2 and y2 position.
	@Override
	public void constructEntity(PaintingBoard pb, MouseEvent ME) {
		
		// If no point is selected then sets the poly with initial points
		if( pb.poly.npoints < 1 )	{
			// Sets type of entity
			Entity.currentEntityType = ENTITY_TYPE.LINE;
			// Adds points to poly object
			pb.poly.addPoint(ME.getX(), ME.getY());
			// Reflects the changes in poly to ensure poly is added with initial points
			pb.paintArea.setPoly(pb.poly);
			pb.statusBar.setText(pb.statusBar.getText()+" Constructing Line");
		}else	{
			// When second time mouse clicked, adds those points to poly and creates line instance
			pb.poly.addPoint(ME.getX(), ME.getY());
			// Adds new line to Entities list.
			pb.paintArea.getEntities().add(new Line(pb.poly));
			// Set poly to initial point to indicate no points are selected
			pb.poly.npoints = 0;
			// Reflects the changes in poly to ensure poly is ready to receive new line points
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


	// Returns the length of line.
	@Override
	public double getLength() {
		return Math.sqrt(Math.pow(point2.x - point1.x , 2) + Math.pow(point2.y - point1.y, 2) );
	}

	// Checks for starting and end points to find whether comes under mouse current points.
	@Override
	public boolean onMouseHover(MouseEvent ME) {
		if( point1.getX() == ME.getX() && point1.getY() == ME.getY() )	{
			return true;
		}
		else if( point2.getX() == ME.getX() && point2.getY() == ME.getY() )	{
			return true;
		}
		else return false;
	}

	// Return minimum distance from current mouse point to starting or ending point
	// Return which point has minimum distance
	@Override
	public double getMinimumDistance(int x, int y) {

		double x1 = x;
		double y1 = y;
		double x2 = point1.x;
		double y2 = point1.y;
		// find distance between start point and x,y
		double d1 = Math.sqrt( (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) ); 

		x2 = point2.x;
		y2 = point2.y;
		// find distance between end point and x,y
		double d2 = Math.sqrt( (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) ); 

		return d1 < d2 ? d1 : d2;	// compares which as minimum distance
	}

	// Moves line to new x, y position 
	@Override
	public void moveBy(int x, int y) {

		// Before moving line insert all line points into polygon, then move polygon to new  x, y position 
		Polygon p = new Polygon();
		// Find selected line by finding distance between current line and line points
		int selectPoint = getClosetEntityToMove(x, y);
		// if that returns 1 means start point of line is selected, otherwise end points of line
		// This is done to preserve the line order
		if(  selectPoint == 1 )	{
			// At this point, inserting start points first
			p.addPoint(point1.x, point1.y);
			p.addPoint(point2.x, point2.y);
			// Build-in polygon method to move object to new x,y position
			p.translate(x-point1.x, y-point1.y);
			point1.x = p.xpoints[0];
			point1.y = p.ypoints[0];
			point2.x = p.xpoints[1];
			point2.y = p.ypoints[1];			
		}else	{
			// At this point, inserting end points first
			p.addPoint(point2.x, point2.y);
			p.addPoint(point1.x, point1.y);			
			p.translate(x-point2.x, y-point2.y);			
			point2.x = p.xpoints[0];
			point2.y = p.ypoints[0];
			point1.x = p.xpoints[1];
			point1.y = p.ypoints[1];
		}
	}

	// getClosetEntityToMove() is similar to getMinimumDistance() but return nearest entity to mouse point
	// 1 indicates starting points and 2 indicates end points.
	private int getClosetEntityToMove(int x, int y) {

		double x1 = x;
		double y1 = y;
		double x2 = point1.x;
		double y2 = point1.y;		
		double d1 = Math.sqrt( (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) ); 

		x2 = point2.x;
		y2 = point2.y;
		double d2 = Math.sqrt( (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) ); 

		return d1 < d2 ? 1 : 2;
	}
}
