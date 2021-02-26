package com.designSpace.EntitySet;
/*
 * 	Class					:	Polygons
 * 	Functionality			:	Polygons class defines set of method to hold and draw Polygons.
 *	Extends Abstract class	:	Entity.
 */
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import com.designSpace.PaintingBoard;

public class Polygons extends Entity {

	// Holds all points information of Polygons 
	Polygon polygonList = null;
	int lastX = 0, lastY = 0;	// lastX, lastY holds the values for temporary mouse position to draw imaginary polygon 
	
	/* Constructor	: Polygons().
	 * Scope		: public
	 * Description 	: This is default constructor used to initialize Polygons with empty set position initially.
	 */
	public Polygons() {
		polygonList = new Polygon();
	}

	/* Constructor	: Polygons(Polygon poly, int x, int y)
	 * Scope		: public
	 * Description 	: This parameterized constructor is called while drawing imaginary polygon, 
	 * 				  then current mouse position x,y is saved in lastX nad lstY.
	 * 				  Already partially constructed polygon is saved in polygonList.
	 */
	public Polygons(Polygon poly, int x, int y) {
		polygonList = poly;		
		lastX = x; 
		lastY = y;
	}

	/* Constructor	: Polygons(Polygon poly)
	 * Scope		: public
	 * Description 	: This parameterized constructor is called when polygon construction is completed.
	 */
	public Polygons(Polygon poly) {

		polygonList = new Polygon();
		// Now retrives all position point from poly to polygonList
		for( int i = 0; i < poly.npoints; i++ )	{
			polygonList.addPoint(poly.xpoints[i], poly.ypoints[i]);
		}
	}

	// overrides toString() and creates own string format as follow to store in file,
	@Override
	public String toString() {
		String points = new String();
		for( int i = 0; i < polygonList.npoints; i++ ){
			points = points + " "+polygonList.xpoints[i]+":"+polygonList.ypoints[i];
		}
		return "\nPOLYGON"+points;
	}

	// Draws the final polygon on the canvas.
	@Override
	public void drawEntity(Graphics Grp) {
		Grp.drawPolygon(polygonList);
	}

	// Draws the imaginary polygon on the canvas.
	@Override
	public void drawImaginaryEntity(Graphics Grp) {

		int i = 0;

		// First draws line for all points
		for( i = 1; i < polygonList.npoints; i++ )	
			Grp.drawLine(this.polygonList.xpoints[i-1], this.polygonList.ypoints[i-1],this.polygonList.xpoints[i], this.polygonList.ypoints[i]);
		// Then to show imaginary polygon it joins stating and end point with intermediate mouse point
		Grp.drawLine(this.polygonList.xpoints[0], this.polygonList.ypoints[0], this.lastX, this.lastY);
		Grp.drawLine(this.polygonList.xpoints[i-1], this.polygonList.ypoints[i-1], this.lastX, this.lastY);

	}

	// Construct the polygon every time with new x2 and y2 position.
	@Override
	public void constructEntity(PaintingBoard pb, MouseEvent ME) {

		// when mouse left button single clicked then adds point to polygon list
		if( ME.getButton() == 1 && ME.getClickCount() == 1 )	{
			// Sets type of entity
			Entity.currentEntityType = ENTITY_TYPE.POLYGON;
			// Reflects the changes in poly to ensure poly is added with initial points
			pb.poly.addPoint(ME.getX(), ME.getY());
			pb.paintArea.setPoly(pb.poly);
		}
		// when mouse left button double clicked then stops collecting point and constructs final polygon
		if( ME.getButton() == 1 && ME.getClickCount() == 2 )	{
			// Adds new polygon to Entities list.
			pb.paintArea.getEntities().add(new Polygons(pb.poly));
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

	/* Function		: getLength().
	 * Return   	: double
	 * Scope		: public
	 * Description 	: Finds length of the polygon and 
	 * 				  return for finding minimum and maximum length polygon in polygon list.
	 * 				  This function is method is highly recommended for polygon.
	 * 				  For other entity given some default value or area of that entity.
	 */	
	@Override
	public double getLength() {

		double len = 0.0;
		int i = 0;
		// finds length one by one line still end of polygon list
		for( i = 1; i < polygonList.npoints; i++ )	{
			len += findLength(polygonList.xpoints[i-1], polygonList.ypoints[i-1], polygonList.xpoints[i], polygonList.ypoints[i]);
		}
		// find length of last line in list
		len += findLength(polygonList.xpoints[i-1], polygonList.ypoints[i-1], polygonList.xpoints[0], polygonList.ypoints[0]);
		return len;
	}
	
	// subroutine to find length
	private double findLength(int x1, int y1, int x2, int y2){

		return Math.sqrt( Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2) );
	}

	// Checks for width and height from starting point to find whether comes under mouse current points.
	@Override
	public boolean onMouseHover(MouseEvent ME) {

		// Returns if mouse points x,y within in polygon
		return polygonList.contains(ME.getX(), ME.getY());
	}

	@Override
	public double getMinimumDistance(int x, int y) {
		return getLength();
	}

	// Moves line to new x, y position 
	@Override
	public void moveBy(int x, int y) {

		polygonList.translate(x-polygonList.xpoints[0], y-polygonList.ypoints[0]);
	}
}
