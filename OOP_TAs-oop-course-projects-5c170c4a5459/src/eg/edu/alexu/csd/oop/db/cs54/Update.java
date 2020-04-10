package eg.edu.alexu.csd.oop.db.cs54;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.ArrayList;



public class Update {
	public int update(String path, String table_name, String col_name_update,
			ArrayList<String> col_name , String condition , ArrayList<String> newupdate) {
		File xmlFile = new File(path + System.getProperty("file.separator") + table_name+".xml");
		int count =0;
		if(xmlFile.exists()) {	
				
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Document doc = null;
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        doc.getDocumentElement().normalize();
      
        NodeList nList = doc.getElementsByTagName("Row");
        
        for (int i = 0; i < nList.getLength(); i++) {
           Node nNode = nList.item(i);
           
           
           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) nNode;
              
              for (int j = 0 ; j < col_name.size() ; j++) {
            	  String col = "";
            	  col = eElement.getElementsByTagName(col_name.get(j)).item(0).getTextContent();
            	  
            	  
            	  if (col_name.get(j).toLowerCase().equals(col_name_update.toLowerCase())) {
            		  String con_value = condition.substring(2, condition.length()).toLowerCase();
            		  
            		  if(condition.contains("=")) {
            			  
            			  if( con_value.toLowerCase().equals(col.toLowerCase())) {
            				  count++;
            				  for (int k = 0 ; k < col_name.size() ; k++) {
			            			   eElement.getElementsByTagName(col_name.get(k)).item(0).setTextContent(newupdate.get(k));
            			        
			                      
			            		   }
      					}
            		  }
            		 
            		  else if(condition.contains(">")) {
            			  if(Integer.parseInt(con_value) > Integer.parseInt(col)) {
            				  count++;
            				  for (int k = 0 ; k < col_name.size() ; k++) {
			            			 
  			            		
			                      eElement.getElementsByTagName(col_name.get(k)).item(0).setTextContent(newupdate.get(k));
			            		   }
        					}
            		  }
            		  else if(condition.contains("<")) {
            			  if(Integer.parseInt(con_value) < Integer.parseInt(col)) {
            				  count++;
            				  for (int k = 0 ; k < col_name.size() ; k++) {
			            			 
  			            		
			                      eElement.getElementsByTagName(col_name.get(k)).item(0).setTextContent(newupdate.get(k));
			            		   }
      					}
            		  }
              }
              }
             
           }
        }
        doc.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, table_name +".dtd");
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path + System.getProperty("file.separator") + table_name+".xml"));
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return count;
     } else {
        return -1;
     }
		
	}
		
	public int updateFullTable( String path , String table_name , ArrayList<String> newupdate ,
			ArrayList<String> col_name) throws TransformerException, ParserConfigurationException, SAXException, IOException {
			int count = 0;
			File xmlFile = new File(path + System.getProperty("file.separator") + table_name+".xml");
		
			if(xmlFile.exists())
			
			  {
					
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(xmlFile);
	        doc.getDocumentElement().normalize();
	       
	        NodeList nList = doc.getElementsByTagName("Row");
	        
	        for (int i = 0; i < nList.getLength(); i++) {
	           Node nNode = nList.item(i);
	           
	           
	           if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
	              Element eElement = (Element) nNode;
	              count++;
	           
	              for (int j = 0 ; j < col_name.size() ; j++) {
	            	  eElement.getElementsByTagName(col_name.get(j)).item(0).setTextContent(newupdate.get(j));
	            	  
	            	   
	               }
	               }
	              
	            }
	        doc.getDocumentElement().normalize();
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, table_name +".dtd");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path + System.getProperty("file.separator") + table_name+".xml"));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
			return count;
			  }
	 
		else {
		    return -1;
		  }
	}	
	}



