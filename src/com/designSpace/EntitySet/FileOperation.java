package com.designSpace.EntitySet;
/*
 * 	Class			:	fileOperation.
 * 	Functionality	:	This class provides io operation to save, open and creating new file.
 * 						Also Interface is provided for reading XML file reading.
 * 						To test test-script's and run those programs. 
 * 		
 * 						Also provides some file status.
 */
import java.awt.Polygon;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.designSpace.tools.PaintingDashBoard;


public class FileOperation {


	// Constants to indicate current file status
	final public static int FILE_NOT_OPENED = -1;
	final public static int NEW_FILE_OPENED = 0;
	final public static int SAVED_FILE_OPENED = 1;
	final public static boolean LAST_MODIFICATION_SAVED = true;
	final public static boolean LAST_MODIFICATION_NOT_SAVED = false;

	/* Function		: saveFile(paintingBoard paintArea)
	 * Return   	: true/false
	 * Scope		: public, static
	 * Description 	: saveFile method save the file content to specified file.
	 * 				  If file opened newly then opens save dialog box for selecting file name.
	 */	
	public static boolean saveFile(PaintingDashBoard paintArea)	{
		String fileName = null;
		// Checks for whether file is opened or not
		if( paintArea.FILE_OPEN_STATUS == FileOperation.NEW_FILE_OPENED )	{
			// if new file opened
			fileName = saveFileDialogBox();	// opens save dialog box
			// if file name not selected then returns false.
			if( fileName == null )	return false;	
		}
		// if file is already saved
		else if( paintArea.FILE_OPEN_STATUS == FileOperation.SAVED_FILE_OPENED )	{
			// Gives the opened file name to save entities.
			fileName = paintArea.fileName;
		}
		// if not at all file is opened
		else if( paintArea.FILE_OPEN_STATUS == FileOperation.FILE_NOT_OPENED )
			return false;

		// first gets the all entities from paintArea
		List<Entity> entities = paintArea.getEntities();
		
		try	{
			// opens file
			File file = new File(fileName);
			FileOutputStream OSF = new FileOutputStream(file);

			for( int i = 0; i < entities.size(); i++ )
				// Now write one by one entity to file
				// toString contains Description of entity, that is saved to file
				OSF.write(entities.get(i).toString().getBytes());			
		}catch (Exception e) {
			
			System.out.println("Error while Writing "+e.getLocalizedMessage());
		}
		// changes the status value to indicate changes done
		paintArea.fileName = fileName;
		paintArea.FILE_OPEN_STATUS = FileOperation.SAVED_FILE_OPENED;
		paintArea.LAST_MODIFICATION = FileOperation.LAST_MODIFICATION_SAVED;
		return true;
	}
	

	/* Function		: openFile(paintingBoard paintArea)
	 * Return   	: true/false
	 * Scope		: public, static
	 * Description 	: openFile method opens new file content and display on canvas.
	 */	
	public static boolean openFile(PaintingDashBoard paintArea) {
		String fileName = null;
		File file = null;
		// opens file open dialog and gets file name,
		fileName = openFileDialogBox();
		if( fileName == null )	{	
			// if null, no file will not be open
			System.out.println("Not Opened");
			return false;
		}

		paintArea.fileName = fileName;
		paintArea.FILE_OPEN_STATUS = 1;
		System.out.println("opened file name : "+fileName);
		// Creates new entity list to read all entities from file. 
		List<Entity> entities = new ArrayList<Entity>();
		try 	{
			// opens file
			file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			// Line read to read one entity at a time.
			LineNumberReader line = new LineNumberReader(fileReader);
			String str = null;
			int count=0;
			while( ( str = line.readLine() ) != null )	{
				count++;
				// skip first line, because it contains nothing,
				if( count == 1 )	continue;

				// After reading that line just pass that string to converter
				// This convert string to entity, depending on type of entity.
				entities.add(convertStringToEntity(str));
			}
			// close all file connection.
			line.close();
			fileReader.close();
		}catch (Exception e) {
			System.out.println("Error While Reading : "+e.getLocalizedMessage());
		}
		// Now set paintArea entity list to new entity list.
		paintArea.setEntities(entities);
		// call repaint to open file.
		paintArea.repaint();
		return true;
	}
	
	public static String saveFileDialogBox()	{
		// file name chooser
		JFileChooser saveFile = new JFileChooser();
		// read the file name
		int action = saveFile.showSaveDialog(null);
		// if file selected
		if(	action == JFileChooser.APPROVE_OPTION )	{
			return saveFile.getSelectedFile().getAbsolutePath();
		}
		// if other option selected
		if( action == JFileChooser.CANCEL_OPTION ){
			System.out.println("CANCLED");
			return null;
		}
		return null;
	}
	
	
	public static String openFileDialogBox()	{
		// file name chooser
		JFileChooser openFile = new JFileChooser();
		// read file name
		int openStatus = openFile.showOpenDialog(null);

		// if selected
		if(  openStatus == JFileChooser.APPROVE_OPTION )	{
			return openFile.getSelectedFile().getAbsolutePath();
		}else if( openStatus == JFileChooser.CANCEL_OPTION )	
			return null;
		return null;
	}
	

	/**
	 * @param str
	 * @return Entity
	 *		
	 */
	public static Entity convertStringToEntity(String str)	{

		// first split the String into array of string to retrieve all points
		String[] splitedString = str.split(" ");

		// Always first string indicates type of Entity in splitedString 
		// Depending on type of entity, read all points.
		// Convert them to Integer using wrapper class from string.
		// Finally call respective entity type to create instance for that.
		// Immediately return that object to add to entity list.
		if( splitedString[0].equals("CIRCLE") )	{
			return new Circle(Integer.parseInt(splitedString[1]), Integer.parseInt(splitedString[2]), Integer.parseInt(splitedString[3]));
		}
		else
		if( splitedString[0].equals("LINE"))	{			
			return new Line(Integer.parseInt(splitedString[1]), Integer.parseInt(splitedString[2]), Integer.parseInt(splitedString[3]), Integer.parseInt(splitedString[4]));
		}else
		if( splitedString[0].equals("RECTANGLE"))	{
			
			return new Rectangle(Integer.parseInt(splitedString[1]), Integer.parseInt(splitedString[2]), Integer.parseInt(splitedString[3]), Integer.parseInt(splitedString[4]));
		}else
		if( splitedString[0].equals("SQUARE") )	{
			return new Square(Integer.parseInt(splitedString[1]), Integer.parseInt(splitedString[2]), Integer.parseInt(splitedString[3]));
		}
		else 
		if( splitedString[0].equals("POLYGON") )	{
			// First create polygon to hold all points
			Polygon polyList = new Polygon();

			for( int i = 1; i < splitedString.length; i++ )	{
				// Again split to get x:y value individually, and add to poly
				String[] points = splitedString[i].split(":");
				polyList.addPoint(Integer.parseInt(points[0]), Integer.parseInt(points[1]));
			}
			// Create instance of Polygons by passing polyList to constructor. 
			return new Polygons(polyList);
		}
		return null;
	}
	
	

	/* Function		: openXMLFile(paintingBoard paintArea)
	 * Return   	: true/false
	 * Scope		: public, static
	 * Description 	: openXMLFile method opens new XML file content and display on canvas.
	 * 				  Only XML open file interface is given, to test script.
	 */	
	public static boolean openXMLFile(PaintingDashBoard paintArea) {
		String fileName = null;

		// open dialog box
		fileName = openFileDialogBox();
		if( fileName == null )	{
			// if not selected any file
			System.out.println("Not Opened");
			return false;
		}

		// Create new entity list 
		List<Entity> entities = new ArrayList<Entity>();
		try {
			// open XML file,
			File xmlFile = new File(fileName);
			// create new instance for build factory
			DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
			// get document builder
			DocumentBuilder DB = DBF.newDocumentBuilder();
			// then open XML file and parse the XML tag.
			Document doc = DB.parse(xmlFile);

			// Normalize the operation mode.
			doc.getDocumentElement().normalize();
			
			System.out.println("Root Element : "+doc.getDocumentElement().getNodeName());

			// Specifying main starting tag name to read content, 
			NodeList NL = doc.getElementsByTagName("Points");
			for( int i = 0; i < NL.getLength(); i++ )	{

				// Read read child by child element and assign to node,
				Node node = NL.item(i);
				// Determine node element or not
				if( node.getNodeType() == Node.ELEMENT_NODE )	{
					// convert to element to access
					Element e = (Element) node;
					// Create polyList to store  all points of polygon
					Polygon polyList = new Polygon();
					// Determine number of points in polygon
					for( int k = 0; k < e.getElementsByTagName("X").getLength(); k++)	{

						// read each x and y value for each point
						int x = Integer.parseInt(e.getElementsByTagName("X").item(k).getTextContent());
						int y = Integer.parseInt(e.getElementsByTagName("Y").item(k).getTextContent());
						// Add to list
						polyList.addPoint((paintArea.canWidth/2)+x, (paintArea.canHieght/2)+y);
					}
					// add to entity list by creating instance of Polygons class 
					entities.add(new Polygons(polyList));
				}
			}
		} catch (ParserConfigurationException e) {
			System.out.println("Parse Error : "+e.getLocalizedMessage());
			JOptionPane.showMessageDialog(null, "File Correpted or Content alterd");
			return false;
		} catch (SAXException e) {
			System.out.println("SAX Error : "+e.getLocalizedMessage());
			JOptionPane.showMessageDialog(null, "File Correpted or Content alterd");
			return false;
		} catch (IOException e) {
			System.out.println("IO Error : "+e.getLocalizedMessage());
			return false;
		}

		// Finally Add new red Entity List to paintArea entiylist.
		paintArea.setEntities(entities);
		// set file name
		paintArea.fileName = fileName;
		// indicate file is opened
		paintArea.FILE_OPEN_STATUS = 1;
		return true;
	}


	
	/* Function		: placeEntitiesInOrder(List<Entity> entities)
	 * Return   	: List<Entity>
	 * Scope		: public, static
	 * Description 	: Before displaying file on canvas, this method arranges entity in 
	 * 				  order no entity should overlap each other. 
	 */	
	public static List<Entity> placeEntitiesInOrder(List<Entity> entities) {

		// Creates a new Entity List 
		List<Entity> temp = new ArrayList<Entity>();
		// Holds previous x and y position.
		int x_axis = 0, y_axis = 0;

		// iterate through the all entity.
		for( int i = 0; i < entities.size(); i++ )	{
			
			// read one element by element
			Polygons p = (Polygons) entities.get(i);
			int x = 0;
			int y = 0;	// y-axis is not used...
			for( int k = 0; k < p.polygonList.npoints; k++ )	{
				//System.out.println(p.polygonList.xpoints[k]+" : "+p.polygonList.ypoints[k]);
				// find which x-axis have highest value or range and assign that to x
				if( x < p.polygonList.xpoints[k] ) 	x = p.polygonList.xpoints[k];
			}
			// check that comes under precious axis, if not assign that to x_axis
			if(x > x_axis )	{ x_axis = x;	}
			else {
				// check current entity starts or not from 0th position on x-axis
				if( p.polygonList.xpoints[0] > 0 )	{
					// if not comes subtract that amount of position.
					x_axis -= p.polygonList.xpoints[0];
				}
				// then move that entity by x_axis amount of position
				p.polygonList.translate(x_axis, y_axis);
				// assign last position to a_axis to avoid overlap
				x_axis += x;
			}
			// Then add that entity to temp list
			temp.add(p);
/*			System.out.println("last points : x : "+x+"  y : "+y);
			System.out.println(x_axis+":"+y_axis);
*/		}
		return temp; // return new list which avoid overlapping of entities
	}
}