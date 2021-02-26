package com.designSpace.EntitySet;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import com.designSpace.PaintingBoard;

public class DrawingPen{

	private static final long serialVersionUID = -8011579418719206303L;
	java.awt.Polygon polygonList = new java.awt.Polygon();

	public DrawingPen()	{
		
	}

	public DrawingPen(java.awt.Polygon poly)	{

	}
	
	public DrawingPen(java.awt.Polygon poly, int x2, int y2)	{
		
		polygonList = poly;
		polygonList.addPoint(x2, y2);
	}
	
/*	@Override
	public void drawEntity(Graphics Grp) {
		
	}

	@Override
	public String toString() {
		return "drawingPen [polygonList=" + polygonList + "]";
	}

	@Override
	public java.awt.Polygon getPoints() {

		return null;
	}

	@Override
	public void drawImaginaryEntity(Graphics Grp) {

		for( int i = 1; i < polygonList.npoints; i++ )
			Grp.drawLine(polygonList.xpoints[i-1], polygonList.ypoints[i-1],polygonList.xpoints[i], polygonList.ypoints[i]);
		
		//Grp.drawPolygon(polygonList);
		System.out.println("draw maccha");
	}

	@Override
	public void constructEntity(paintBoard pb, MouseEvent ME) {

		System.out.println(ME.getButton()+" == "+ME.getClickCount());
		
//		if( ME.getClickCount() == 2 )

		pb.poly.addPoint(ME.getX(), ME.getY());
		polygonList = pb.poly;
		for( int i = 0; i < polygonList.npoints; i++ )	{
			
			System.out.println(polygonList.xpoints[i]+"==="+polygonList.ypoints[i]);
		}
	}
	
	
	@Override
	public void dispInfo() {
		
		System.out.println("Information of "+this.getClass().getName());
	}

*/}
