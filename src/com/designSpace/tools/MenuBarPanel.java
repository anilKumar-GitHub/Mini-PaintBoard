package com.designSpace.tools;
/**
 *		Documentation
 * 		Author		: Anil Kumar B P.
 * 		Programming Language : Core Java.
 * 		Save file as menuBarPanel.java
 * 		
 */
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.designSpace.PaintingBoard;
import com.designSpace.EntitySet.Entity;
import com.designSpace.EntitySet.Entity.ENTITY_TYPE;
import com.designSpace.EntitySet.Polygons;
import com.designSpace.EntitySet.FileOperation;

/*
 * 	Class			:	menuBarPanel.
 * 	Functionality	:	Holds the all menu items and implements their operations.
 *	Extends			:	JMenuBar.
 *	Implements 		:	ActionListener.
 */
public class MenuBarPanel extends JMenuBar implements ActionListener{

	private static final long serialVersionUID = -5394178329018979536L;
	// Declares the all Menu items 
	JMenu file = new JMenu("File");
	JMenu operations = new JMenu("PolyGon Operations");
	public JMenuItem New = new JMenuItem("New");
	JMenuItem save = new JMenuItem("Save");
	JMenuItem open = new JMenuItem("Open");
	JMenuItem exit = new JMenuItem("Exit");
	JMenuItem openXMLFile = new JMenuItem("Open XML File"); 
	JMenuItem findLongestLengthEntity = new JMenuItem("Find Longest Entity");
	JMenuItem findLowestLengthEntity  = new JMenuItem("Find Lowest Entity");
	PaintingBoard pb = null;	// Holds association to paintBoard
	
	/* Constructor	: menuBarPanel(paintBoard paintboard).
	 * Scope		: public
	 * Description 	: This is default constructor used to all menu items and add action listener.
	 * 				  
	 */
	public MenuBarPanel(PaintingBoard paintboard)	{
		
		// Adding File menu item to file file menu.
		file.add(New);
		file.add(save);
		file.add(open);
		file.addSeparator();
		file.add(exit);
		
		// Adding polygon operation option to Polygon Operation menu
		operations.add(openXMLFile);
		operations.add(findLongestLengthEntity);
		operations.add(findLowestLengthEntity);
		
		// Adding object to Event listener
		New.addActionListener(this);
		save.addActionListener(this);
		open.addActionListener(this);
		exit.addActionListener(this);
		openXMLFile.addActionListener(this);
		findLongestLengthEntity.addActionListener(this);
		findLowestLengthEntity.addActionListener(this);

		// Adding menu to menu bar
		add(file);
		add(operations);
		
		this.pb = paintboard;		// reference to paintBoard object
	}

	/* Function		: actionPerformed(ActionEvent AE).
	 * Return   	: void
	 * Scope		: public
	 * Description 	: Listens to mouse click event on menu item, 
	 * 				  calls appropriate method to perform operation.
	 * 				  AE.getSource() holds the reference to currently selected menu item.
	 * 				  
	 */	

	@Override
	public void actionPerformed(ActionEvent AE) {

		pb.paintArea.operationalEntity = null;
		// if new file option is selected
		if( AE.getSource().equals(New)) 	{
			// checks for old file last modification.
			// if not saved last modification, asks to user for saving old opened file if opened
			if( pb.paintArea.LAST_MODIFICATION == FileOperation.LAST_MODIFICATION_NOT_SAVED )	{
				// Asks confirmation to save.
				int option = JOptionPane.showConfirmDialog(this, "Save File NONAME.txt");
				if( option == JOptionPane.YES_OPTION )	{
					FileOperation.saveFile(pb.paintArea);	// saves file if yes,
				}else if( option == JOptionPane.NO_OPTION )	{
					pb.paintArea.LAST_MODIFICATION = FileOperation.LAST_MODIFICATION_SAVED;
				}else if( option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION )	{
					return;
				}
			}
			// Opens file with name NONAME.txt
			pb.statusBar.setText("New File Opened.");
			pb.paintArea.fileName = "NONAME.txt";
			// sets FILEOPENSTATUS to NEWFILEOPENED
			pb.paintArea.FILE_OPEN_STATUS = FileOperation.NEW_FILE_OPENED;
			// Creates new Arraylist of entities to hold new entities,
			pb.paintArea.setEntities(new ArrayList<Entity>());
			pb.frm.setTitle("Mini-Paint Board : "+pb.paintArea.fileName);
			pb.paintArea.repaint();
		}

		if( AE.getSource().equals(save))	{
			boolean saveFlag = true;	// used to display message
			// checks for whether file is opened or not,
			if( pb.paintArea.FILE_OPEN_STATUS != FileOperation.FILE_NOT_OPENED)	{
				pb.statusBar.setText("Saving file. Select File Name");
				// calls save operation
				saveFlag =  FileOperation.saveFile(pb.paintArea);
				pb.frm.setTitle("Mini-Paint Board : "+pb.paintArea.fileName);
			}
			if( saveFlag )	// displays appropriate message.
				pb.statusBar.setText("File saved.");
			else pb.statusBar.setText("File not saved");
		}

		if( AE.getSource().equals(open))	{

			pb.statusBar.setText("Opening file. Select file name...");
			// Checks whether previous opened file last modification is saved or not
			if( pb.paintArea.LAST_MODIFICATION == FileOperation.LAST_MODIFICATION_NOT_SAVED )	{
				// if not saved, asks user to save file,
				int option = JOptionPane.showConfirmDialog(this, "Save File "+ new File(pb.paintArea.fileName).getName());
				if( option == JOptionPane.YES_OPTION )	{
					boolean saveFlag = FileOperation.saveFile(pb.paintArea);
					// if user want to save but not saved, then remains in same previous file not opens new file
					if( ! saveFlag )	return;
				}else if( option == JOptionPane.NO_OPTION )	{
					pb.paintArea.LAST_MODIFICATION = FileOperation.LAST_MODIFICATION_NOT_SAVED;
				}else if( option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION )	{
					pb.paintArea.LAST_MODIFICATION = FileOperation.LAST_MODIFICATION_NOT_SAVED;
				}
			}

			// calls open file operation
			FileOperation.openFile(pb.paintArea);
			pb.frm.setTitle("Mini-Paint Board : "+pb.paintArea.fileName);
			// sets currentEntityType to none,
			Entity.currentEntityType = ENTITY_TYPE.NONE;
		}

		// Asks for confirmed exit option
		if( AE.getSource().equals(exit) )	{
			
			if( JOptionPane.showConfirmDialog(this, "Are You Sure to Exit ?") == JOptionPane.OK_OPTION )	{
				System.exit(0);
			}
			
		}
		

		// This opens XML file for run test-script to test application,
		if( AE.getSource().equals(openXMLFile) )	{

			pb.statusBar.setText("Opening XML File... Select valid XML file.");
			// calls openXMLFile operation to parse and read the xml file,
			boolean openFlag = FileOperation.openXMLFile(pb.paintArea);
			if( openFlag )	{
				pb.frm.setTitle("Mini-Paint Board : "+pb.paintArea.fileName);
				// Before drawing entities on canvas[graph], it places all entities in x axis to 0
				pb.paintArea.setEntities(FileOperation.placeEntitiesInOrder(pb.paintArea.entities));
				// Then calls repaint option to draw entities
				pb.paintArea.repaint();
			}
		}

		// Find the entity that has Longest length among all polygon
		// This method is for polygon only, if it used with other entity may give wrong result,
		if( AE.getSource().equals(findLongestLengthEntity)) 	{

			// check whether file is opened or not 
			if( pb.paintArea.FILE_OPEN_STATUS == FileOperation.FILE_NOT_OPENED )	return;
			// Finds longest entity and returns reference to entity object,
			Entity entity = Entity.findLongestEntity(pb.paintArea.entities);
			pb.paintArea.operationalEntity = entity;	// set this object to operational entity,
			pb.paintArea.repaint();
			// Display information of about longest polygon
			pb.statusBar.setText("Longest Entity is draw in blue color.");
			Polygons polygon = (Polygons) entity;
			JOptionPane.showMessageDialog(this, "Longest Length Entity is \n"+entity.toString());
		}
		
		// Find the entity that has Lowest length among all polygon
		// This method is for polygon only, if it used with other entity may give wrong result,
		if( AE.getSource().equals(findLowestLengthEntity))	{	
			
			// check whether file is opened or not 
			if( pb.paintArea.FILE_OPEN_STATUS == FileOperation.FILE_NOT_OPENED )	return;
			// Finds longest entity and returns reference to entity object,
			Entity entity = Entity.findLowestEntity(pb.paintArea.entities);
			pb.paintArea.operationalEntity = entity;
			System.out.println(pb.paintArea.operationalEntity.toString());
			pb.paintArea.repaint();
			// Display information of about longest polygon
			pb.statusBar.setText("Lowest Entity is draw in blue color.");
			JOptionPane.showMessageDialog(this, "Lowest Length Entity is \n"+entity.toString());
		}
	}
}