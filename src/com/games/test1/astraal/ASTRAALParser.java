package com.games.test1.astraal;

import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.games.test1.EventHandler;
import com.games.test1.aal.AALInterpreter;
import com.games.test1.aal.AALStatement;

//import android.util.Log;

/**
 * ASTRAAL: Abstract Syntax Tree for Representing Android Adventure Language.
 * 
 * Ah, backronyms.    
 * 
 * This class parses an ASTRAAL XML file into an ASTRAAL tree (also redundant!).
 */
public class ASTRAALParser {
	public static final String DEFAULT_ID = "main";
	
	private static final String RESOURCES_TAG = "RESOURCES";
	private static final String RES_ANIMATION_TAG = "ANIMATION";
	private static final String RES_SOUND_TAG = "SOUND";
	private static final String SCENE_TAG = "SCENE";
	private static final String OBJECT_TAG = "OBJECT";
	private static final String NAVIGATION_TAG = "NAVIGATION";
	private static final String ITEMS_TAG = "ITEMS";
	private static final String ITEM_TAG = "ITEM";
	private static final String JOURNALS_TAG = "JOURNALS";
	private static final String JOURNAL_TAG = "JOURNAL";
	private static final String PAGE_TAG = "PAGE";
	
	/**
	 * Utility method for cleanly retrieving an attribute.
	 * @param node
	 * @param attrName
	 * @param defaultValue
	 * @return
	 */
	public static String getAttributeValue(Node node, String attrName, String defaultValue) {
		String value = defaultValue;
		try {
			value = node.getAttributes().getNamedItem(attrName).getNodeValue();
		} catch (Exception e) { }
		
		return value;
	}
	
	/**
	 * Utility method to return the contents of a node.
	 * @param node Node to get contents of.
	 * @return
	 */
	public static String getNodeContents(Node node) {
		/** START FROM http://faq.javaranch.com/java/GetNodeValue **/
		StringBuffer buf = new StringBuffer();
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node textChild = children.item(i);
			if (textChild.getNodeType() != Node.TEXT_NODE) {
				System.err.println("Mixed content! Skipping child element " + textChild.getNodeName());
				continue;
			}
			buf.append(textChild.getNodeValue());
		}
		return buf.toString();
		/** END FROM http://faq.javaranch.com/java/GetNodeValue **/
	}
	
	/**
	 * Construct the ASTRAAL tree from an XML document.
	 * @param source - InputSource to read from.
	 */
	public ASTRAALRoot parse(InputSource source) {
		AALInterpreter interpreter = new AALInterpreter();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		ASTRAALRoot astraalRt = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(source);
			Element root = dom.getDocumentElement();
			
			// Build the ASTRAAL Root.
			astraalRt = new ASTRAALRoot(root.getAttribute("gamename"), 
										root.getAttribute("version"));
			
			// Get a list of resource blocks.
			NodeList resourceBlocks = root.getElementsByTagName(RESOURCES_TAG);
			parseResources(astraalRt, resourceBlocks);
			
			// Get a list of inventory items.
			NodeList invNodes = root.getElementsByTagName(ITEMS_TAG);
			parseItems(astraalRt, invNodes);
			
			// Load in journal data.
			NodeList journalsNode = root.getElementsByTagName(JOURNALS_TAG);
			parseJournals(astraalRt, journalsNode);
			
			// Now, get a list of scenes.
			NodeList sceneNodes = root.getElementsByTagName(SCENE_TAG);
			parseScenes(interpreter, astraalRt, sceneNodes);
			
			
		} catch (Exception e) {
			Log.w("ASTRAAL", e.toString());			
			return null;
		}
		
		return astraalRt;
	}

	private void parseJournals(ASTRAALRoot astraalRt, NodeList journalsNode) {
		ASTRAALJournal newJournal;
		for (int i = 0; i < journalsNode.getLength(); i++) {
			NodeList journals = journalsNode.item(i).getChildNodes();
			for (int j = 0; j < journals.getLength(); j++) {
				Node journalNode = journals.item(j);				
				if (journalNode.getNodeName().equals(JOURNAL_TAG)) {
					// Create a journal.
					String 	id = getAttributeValue(journalNode, "id", ""),
							title = getAttributeValue(journalNode, "title", "");
					newJournal = new ASTRAALJournal(id, title);
					
					parseJournalPages(astraalRt, newJournal, journalNode);
					
					// Add the newly-created journal to the system.
					astraalRt.addJournal(newJournal);
				}
			}
		}
		
	}

	private void parseJournalPages(ASTRAALRoot astraalRt, 
			ASTRAALJournal newJournal, Node journalNode) {
		
		// Add each page to the passed-in journal.
		NodeList pages = journalNode.getChildNodes();
		for (int i = 0; i < pages.getLength(); i++) {
			Node page = pages.item(i);
			if (page.getNodeName().equals(PAGE_TAG)) {
				String 	id = getAttributeValue(page, "id", ""),
						title = getAttributeValue(page, "title", ""),
						text = getNodeContents(page);
				
				ASTRAALJournalPage newPage = new ASTRAALJournalPage(id, title, text);
				newJournal.addPage(newPage);
			}
		}
		
	}

	private void parseItems(ASTRAALRoot astraalRt, NodeList invNodes) {		
		ASTRAALInventoryItem newItem;
		for (int i = 0; i < invNodes.getLength(); i++) {
			NodeList items = invNodes.item(i).getChildNodes();
			for (int j = 0; j < items.getLength(); j++) {
				Node itemNode = items.item(j);
				
				if (itemNode.getNodeName().equals(ITEM_TAG)) {
					// Create an item.
					String 	id = getAttributeValue(itemNode, "id", ""),
							name = getAttributeValue(itemNode, "name", ""),
							sprite = getAttributeValue(itemNode, "sprite", ""),
							description = getNodeContents(itemNode);
					newItem = new ASTRAALInventoryItem(id, name, description, sprite);
					
					// Add the newly-created item to the game.
					astraalRt.addItem(newItem);
				}
			}
		}
		
	}

	private void parseScenes(AALInterpreter interpreter, ASTRAALRoot astraalRt,
			NodeList sceneNodes) {
		for (int i = 0; i < sceneNodes.getLength(); i++) {
			Node sceneNode = sceneNodes.item(i);
			// Get basic metadata.
			String 	id  = getAttributeValue(sceneNode, "id", ""),
				  	resourceblock = getAttributeValue(sceneNode, "resourceblock", "main"),
				  	background = getAttributeValue(sceneNode, "background", ""),
				  	sWidth = getAttributeValue(sceneNode, "width", "800"),
				  	sHeight = getAttributeValue(sceneNode, "height", "480");
			int width = Integer.parseInt(sWidth),
				height = Integer.parseInt(sHeight);
			
			ASTRAALScene scene = new ASTRAALScene(id, resourceblock, background, width, height);
			
			// Get objects.
			NodeList children = sceneNode.getChildNodes();			
			parseObjectsInScene(interpreter, scene, children);
			
			// Get navigation cues.			
			parseNavigationInScene(scene, children);
			
			// Error-check:
			if (scene.getObjects().size() == 0) {
				Log.w("ASTRAAL", "No objects in scene " + id);
			}
			
			astraalRt.addScene(scene);

		}
	}

	private void parseNavigationInScene(ASTRAALScene scene, NodeList children) {
		for (int j = 0; j < children.getLength(); j++) {
			Node objectNode = children.item(j);
			if (objectNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (!objectNode.getNodeName().equals(NAVIGATION_TAG))
				continue;
			
			String 	position = getAttributeValue(objectNode, "position", "left"),
					newSceneID = getAttributeValue(objectNode, "to_scene", "");

			// This will be used by GameExecutor later in some way -- the exact implementation
			// is left up to first GameExecutor and then MainGainState.						
			scene.addNavigationCue(new ASTRAALNavigationCue(position, newSceneID));
		}
	}

	private void parseObjectsInScene(AALInterpreter interpreter,
			ASTRAALScene scene, NodeList objects) {
		for (int j = 0; j < objects.getLength(); j++) {
			Node objectNode = objects.item(j);
			if (objectNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (!objectNode.getNodeName().equals(OBJECT_TAG))
				continue;
														
			String 	oid = getAttributeValue(objectNode, "id", ""),
					spriteID = getAttributeValue(objectNode, "sprite", ""),
					sX = getAttributeValue(objectNode, "x", "0"),
					sY = getAttributeValue(objectNode, "y", "0"),
					sW = getAttributeValue(objectNode, "width", "0"),
					sH = getAttributeValue(objectNode, "height", "0");
			
			int	x = Integer.parseInt(sX),  
			 	y = Integer.parseInt(sY), 
				w = Integer.parseInt(sW), 
				h = Integer.parseInt(sH);
			
			ASTRAALObject object = new ASTRAALObject(oid, x, y, w, h, spriteID);
			
			// Attach AALStatements for all events.
			EventHandler handler = new EventHandler();
			NodeList eventNodes = objectNode.getChildNodes();
			for (int k = 0; k < eventNodes.getLength(); k++) {
				Node eventNode = eventNodes.item(k);
				if (eventNode.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
				String eventName = getAttributeValue(eventNode, "event", "");
				String script = getNodeContents(eventNode);
				
				// Parse the script and attach it as an event.
				try {
					AALStatement stmt = interpreter.interpret(script);
					handler.attachEvent(EventHandler.getTypeFromEventName(eventName),
									stmt);
				} catch (Exception e) {
					Log.w("ASTRAAL", "Error parsing AAL for object " + object.getID() +", event " + eventName + ": " + e.getMessage());
				}
				
			}
			
			// Attach the event handler.
			object.attachEventHandler(handler);
			
			// Add the new object to this scene's list.
			scene.addObject(object);				
		}
	}

	private void parseResources(ASTRAALRoot astraalRt, NodeList resourceBlocks) {
		for (int i = 0; i < resourceBlocks.getLength(); i++) {
			Node resBlock = resourceBlocks.item(i);				
							
			// Create a new resource block with what parameters we can get.
			String 	id = getAttributeValue(resBlock, "id", "main"),
					preload = getAttributeValue(resBlock, "preload", "false");
			
			ASTRAALResourceBlock block = new ASTRAALResourceBlock(id, preload);
			
			// Load in resources.
			NodeList resources = resBlock.getChildNodes();
			for (int j = 0; j < resources.getLength(); j++) {
				Node resource = resources.item(j);
				if (resource.getNodeType() != Node.ELEMENT_NODE)
					continue;
		
				ASTRAALResource newRes = new ASTRAALResource(); // default value
				
				String rID = getAttributeValue(resource, "id", "");
				if (resource.getNodeName().equals(RES_ANIMATION_TAG)) {
					newRes = new ASTRAALResAnimation(rID);				
				} else if (resource.getNodeName().equals(RES_SOUND_TAG)) {
					// TODO: Create sound clip resource.
				}
				
				// Have the resource do what it will with the child nodes, to
				// finish setting itself up.
				newRes.readFromNode(resource);
				
				// Add the new resource onto the resource block.
				block.addResource(newRes);				
			}
			
			// Add the resource block to the root.
			astraalRt.addResourceBlock(block);
		}
	}
	
	
	
	

}
