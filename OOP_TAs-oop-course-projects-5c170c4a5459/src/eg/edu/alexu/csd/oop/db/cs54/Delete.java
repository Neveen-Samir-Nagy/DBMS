package eg.edu.alexu.csd.oop.db.cs54;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Delete {
	public int delete(String path,String nametable, String namecolumn, String Condition,ArrayList<String> columns) {
		int count = 0;
		File fXmlFile = new File(path + System.getProperty("file.separator") + nametable+".xml");
		if(fXmlFile.exists()) {	
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		NodeList nList = null;
		 nList = doc.getElementsByTagName("Row");
	            	for(int j=0;j<columns.size();j++) {
	            		
	            		if(columns.get(j).toLowerCase().equals(namecolumn.toLowerCase())) {
	            			for (int temp = 0; temp < nList.getLength(); temp++) {
	            	            Node nNode = nList.item(temp);
	            	      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            	        Element eElement = (Element) nNode;
	            	        String col = eElement.getElementsByTagName(columns.get(j)).item(0).getTextContent();
	            	        String value = Condition.substring(2, Condition.length()).toLowerCase();

	            			if(Condition.contains("=")) {
	            			if(col.equals(value)) {
	            				Node parent = eElement.getParentNode();
	                        	parent.removeChild(eElement);
	                        	parent.normalize();
	            				count++;
	            				temp--;
	            			}
	            		}else if(Condition.contains(">")) {
	            			int s1 = (Integer.parseInt(col));
	            			int s2 = (Integer.parseInt(value));
	            			boolean greater = s1 <= s2;
	            			if(greater) {
	            				Node parent = eElement.getParentNode();
	                        	parent.removeChild(eElement);
	                        	parent.normalize();
	            				count++;
	            				temp--;
	            			}
	            		}else if(Condition.contains("<")) {
	            			if((Integer.parseInt(col.toLowerCase())>=(Integer.parseInt(value)))) {
	            				Node parent = eElement.getParentNode();
	                        	parent.removeChild(eElement);
	                        	parent.normalize();
	            				count++;
	            				temp--;
	            			}
	            		}
	            		}
	            	}
	            			break;
	            }
	            		
		 }
	        		try {
	        			doc.getDocumentElement().normalize();
				        TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, nametable +".dtd");
						DOMSource source = new DOMSource(doc);
						StreamResult result = new StreamResult(new File(path + System.getProperty("file.separator") + nametable+".xml"));
						transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			            transformer.transform(source, result);
	        		} catch (TransformerException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
		 if(Condition.contains(">")||Condition.contains("<")) {
			 return columns.size()-count;
		 }
		return count;
		}else {
			return -1;
		}
	}
    public int deletetable(String path, String tablename) {
    	int count =0;
		File fXmlFile = new File(path + System.getProperty("file.separator") + tablename+".xml");
		if(fXmlFile.exists()) {	
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		NodeList nList = null;
		nList = doc.getElementsByTagName("Row");
		for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            	count++;
            	Element eElement = (Element) nNode;
            	Node parent = eElement.getParentNode();
            	parent.removeChild(eElement);
            	temp--;
            	
            }
		}
		try {
			doc.getDocumentElement().normalize();
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, tablename +".dtd");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path + System.getProperty("file.separator") + tablename+".xml"));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
		}else {
			return -1;
		}
		
    }
}

