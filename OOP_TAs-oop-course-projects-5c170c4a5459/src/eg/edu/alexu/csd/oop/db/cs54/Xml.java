package eg.edu.alexu.csd.oop.db.cs54;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.xml.sax.SAXException;

public class Xml {
	private static Map<String, String> col_name_type =new HashMap<String, String>();
	private static Map<String, String> col_name_data =new HashMap<String, String>();
	
	
	public static void creatTable ( String path , String table_name , ArrayList<String> col_name , ArrayList<String> col_type ) 
	throws ParserConfigurationException, TransformerException {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement(table_name);
            doc.appendChild(rootElement);
            
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,table_name +".dtd");
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            //write to file
            StreamResult file = new StreamResult(path  + System.getProperty("file.separator") + table_name +".xml");
            
            transformer.transform(source, file);
            
	}
	
	public void get_col_type (ArrayList<String> col_name , ArrayList<String> col_type){
		
		
		for (int i = 0 ; i < col_type.size() ; i++) {
			col_name_type.put(col_name.get(i).toLowerCase(), col_type.get(i));
		}
		
		}
	
	public static void valid(ArrayList<String> col_name, ArrayList<String> col_data) {
		if (col_name.size() != 0) {
		for (int i = 0 ; i < col_data.size() ; i++) {
			col_name_data.put(col_name.get(i).toLowerCase(), col_data.get(i));
		}
		}else {
			int j = 0;
			 for (Map.Entry<String, String> entry : col_name_data.entrySet())
		        {
				 col_name_data.put(entry.getKey().toLowerCase(), col_data.get(j));
				 j++;
		        }
		}
	
	}
	
	
	public boolean Insert(String file_name  , String table_name, ArrayList<String> col_name, ArrayList<String> col_data)
			throws ParserConfigurationException, SAXException, IOException, TransformerException
		      
		{
		valid(col_name, col_data);
		File f = new File(file_name);
		if(f.exists() && !f.isDirectory()) { 
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file_name);
        Element root = document.getDocumentElement();
        
            Element row = document.createElement("Row");
            
	        for (Map.Entry<String, String> entry : col_name_data.entrySet())
	        {
	        	String name = entry.getKey().toLowerCase();
	        	String data = col_name_data.get(name);
	        	String pattern = "\\'\\w*\\W*\\'";
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(data);
				if (m.find()) {
					//data = data.replaceAll("[\'\']","");
				}
				else {
				}
				
	        	row.appendChild(getRowElements(document, row, name, String.valueOf(data)));
	        } 
            root.appendChild(row);

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,table_name +".dtd");
        StreamResult result = new StreamResult(file_name);
        transformer.transform(source, result);
        return true;
		}
		else {
			return false;
		}
		}
	
    //utility method to create text node
    private static Node getRowElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
    public static void createfiletype(String path, String tablename,ArrayList<String> col, ArrayList<String> types) throws TransformerException, ParserConfigurationException {
    	File type = new File(path+System.getProperty("file.separator") +tablename+"dtd.xml");
    	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root elements
		Document doc = docBuilder.newDocument();
		Element roottable = doc.createElement(tablename);
		Element columnname = null;
        for(int i=0; i<col.size();i++) {
        	columnname = doc.createElement(col.get(i));
        	columnname.appendChild(doc.createTextNode(types.get(i)));
        	roottable.appendChild(columnname);
        }
        doc.appendChild(roottable);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(type);
		transformer.transform(source, result);

}
    public Map<String,String> getmap(){
    	return col_name_type;
    }
}