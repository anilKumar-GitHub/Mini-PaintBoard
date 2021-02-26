package com.designSpace.tools;
/*
 * 	Class			:	toolBarPanel
 * 	Functionality	:	This class holds tool bar.
 * 						Allow user to select different option of entity.
 *	Implements		:	ActionListener.
 *	Extends			:	JToolBar.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.designSpace.PaintingBoard;
import com.designSpace.EntitySet.Entity;
import com.designSpace.EntitySet.Entity.ENTITY_TYPE;
import com.designSpace.EntitySet.FileOperation;

public class ToolBarPanel extends JToolBar implements ActionListener{

	private static final long serialVersionUID = 1L;
	// Declares all tool bar items,
	JButton btnLineSelector = null;			// for Line Entity
	JButton btnCircleSelector = null;		// for Circle Entity
	JButton btnRectangleSelector = null;	// for Rectangle Entity
	JButton btnSquareSelector = null;		// for Square Entity
	JButton btnPolygonSelector	= null;		// for Polygon Entity
	PaintingBoard pb = null;		// creates association with paintBoard object

	public ToolBarPanel(PaintingBoard paintBoard) {
		pb = paintBoard;	// holds reference to the paintBoard object to reflect changes
		initToolBar();
	}
	

	private void initToolBar()	{
		// instantiate the object,
		btnLineSelector = new JButton(new ImageIcon("images/line.jpg"));
		btnCircleSelector = new JButton(new ImageIcon("images/circle.jpg"));
		btnRectangleSelector = new JButton(new ImageIcon("images/rectangle.jpg"));
		btnSquareSelector = new JButton(new ImageIcon("images/square.jpg"));
		btnPolygonSelector = new JButton(new ImageIcon("images/polygon.jpg"));
		//Adding to tool bar
		add(btnLineSelector);
		add(btnCircleSelector);
		add(btnRectangleSelector);
		add(btnSquareSelector);
		add(btnPolygonSelector);
		
		//Adding action listener
		btnLineSelector.addActionListener(this);
		btnCircleSelector.addActionListener(this);
		btnRectangleSelector.addActionListener(this);
		btnSquareSelector.addActionListener(this);
		btnPolygonSelector.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent AE) {

		// if no any file is opened, then simply returns the control without selecting anything
		if( pb.paintArea.FILE_OPEN_STATUS == FileOperation.FILE_NOT_OPENED )
			return;

		// currentEntityType to any one of the entity type depending on the tool item pressed
		// AE.getSource() holds reference to currently selected entity type,
		if( AE.getSource().equals(btnLineSelector) )	
			Entity.currentEntityType = ENTITY_TYPE.LINE;
		else	
			if( AE.getSource().equals(btnCircleSelector) )
				Entity.currentEntityType = ENTITY_TYPE.CIRCLE;
		else
			if( AE.getSource().equals(btnRectangleSelector) )
				Entity.currentEntityType = ENTITY_TYPE.RECTANGLE;
		else
			if( AE.getSource().equals(btnSquareSelector) )
				Entity.currentEntityType = ENTITY_TYPE.SQUARE;
		else
			if( AE.getSource().equals(btnPolygonSelector) )
				Entity.currentEntityType = ENTITY_TYPE.POLYGON;		
		// After selecting new entity type, poly object set to 0, 
		// indicating that no any point is selected still for new entity, 
		pb.paintArea.poly.npoints = 0;
		// operational entity sets to null to remove old entity reference if any,
		pb.paintArea.operationalEntity = null;
	}
}