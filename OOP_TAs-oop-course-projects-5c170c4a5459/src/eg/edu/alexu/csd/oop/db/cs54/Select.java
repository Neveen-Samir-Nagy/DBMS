package eg.edu.alexu.csd.oop.db.cs54;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Select {
	private int count = 0;
	private List<String> columns = new ArrayList<String>();
	public Object[][] select(String dataname,String xmlname, String nameColumn,String expression, String valueToCheck , ArrayList<String> type){
		Object[][] allElements = readFile(dataname,xmlname,type);
		int column_number = 0;
		for(int i = 0; i < columns.size(); i++) {
			if(columns.get(i).toLowerCase().equals(nameColumn.toLowerCase())) {
				column_number = i;
			}
		}
		List<Integer> selected_row = new ArrayList<Integer>();
		if(checkstring(allElements[0][column_number].toString())) {
			int check_value = Integer.parseInt(valueToCheck);
		  for(int j = 0; j < allElements.length;j++) {
			int value = Integer.parseInt(String.valueOf(allElements[j][column_number]));
			switch(expression) {
			case"<":
				if(value < check_value) {
					selected_row.add(j);
				}
				break;
			
		    case">":
			    if(value > check_value) {
				   selected_row.add(j);
			    }
			    break;
		
		    case"=":
			    if(value == check_value) {
				   selected_row.add(j);
			    }
			    break;
		    default:
			    	
			}
		}
		} else {
			for(int j = 0; j < allElements.length;j++) {
				if(allElements[j][column_number].toString().equals(valueToCheck)){
					selected_row.add(j);
				}
			}
		}
		Object[][] Elements = new Object[selected_row.size()][columns.size()];
		int count = 0;
		for(int j = 0; j < selected_row.size();j++) {
				for(int i = 0; i < columns.size(); i++) {
					if (type.get(i).contains("int")) {
						Elements[j][i] = Integer.parseInt(allElements[selected_row.get(j)][i].toString());
					}
					else {
						Elements[j][i] = allElements[selected_row.get(j)][i].toString();
					}
					
					
				}
		}
		return Elements;
		
	}
	public Object[][] selectWithColumn(String dataname,String xmlname, String nameColumn, String expression,String valueToCheck,List<String> columnSelected ,  ArrayList<String> type){
		Object[][] allElements = readFile(dataname,xmlname,type);
		if (allElements.equals(null)) {
			return null;
		}
		int column_number = 0;
		for(int i = 0; i < columns.size(); i++) {
			if(columns.get(i).toLowerCase().equals(nameColumn.toLowerCase())) {
				column_number = i;
			}
		}
		List<Integer> columns_selected = new ArrayList<Integer>();
		for(int i = 0; i < columns.size(); i++) {
			for(int j = 0; j < columnSelected.size(); j++) 	
			if(columnSelected.get(j).toLowerCase().equals(columns.get(i).toLowerCase())) {
				columns_selected.add(i);
			}
		}
		List<Integer> selected_row = new ArrayList<Integer>();
		if(checkstring(allElements[0][column_number].toString())) {
			int check_value = Integer.parseInt(valueToCheck);
		  for(int j = 0; j < allElements.length;j++) {
			int value = Integer.parseInt(allElements[j][column_number].toString());
			switch(expression) {
			case"<":
				if(value < check_value) {
					selected_row.add(j);
				}
				break;
			
		    case">":
			    if(value > check_value) {
				   selected_row.add(j);
			    }
			    break;
		
		    case"=":
			    if(value == check_value) {
				   selected_row.add(j);
			    }
			    break;
		    default:
			    	
			}
		}
		} else {
			for(int j = 0; j < allElements.length;j++) {
				if(allElements[j][column_number].toString().equals(valueToCheck)){
					selected_row.add(j);
				}
			}
		
		}
		Object[][] Elements = new Object[selected_row.size()][columnSelected.size()];
		int count = 0;
		for(int j = 0; j < selected_row.size();j++) {
				for(int i = 0; i < columns_selected.size(); i++) {
					if (type.get(i).contains("int")) {
						Elements[j][i] = (allElements[selected_row.get(j)][columns_selected.get(i)]);
					}
					else {
						Elements[j][i] = allElements[selected_row.get(j)][columns_selected.get(i)].toString();
					}
				}
		}
		return Elements;
	}
	 public Object[][] selectAlltable(String dataname,String nametable , ArrayList<String> type) {
			return readFile(dataname,nametable,type);
     }
    private  boolean checkstring(String get) {
    	for(int i=0; i<get.length();i++) {
    		if(Character.isDigit(get.charAt(i))) {
    			continue;
    		}else {
    			return false;
    		}
    	}
		return true;
    	
    }
   
    private   Object[][] readFile(String dataname,String xmlname , ArrayList<String> type) {
    	columns = new ArrayList<String>();
    	File data = new File (dataname);
		if(!data.exists()) {
			return null;
		}
		List<List> element = new ArrayList<List>();
		List<String> element_value = new ArrayList<String>();
		
		int flag = 0;
		File xml = new File (dataname+"\\"+xmlname+".XML");
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				Document doc = builder.parse(xml);
				NodeList rowList = doc.getElementsByTagName("Row");
				for(int i = 0;i < rowList.getLength();i++) {
					Node p = rowList.item(i);
					if(p.getNodeType() == Node.ELEMENT_NODE) {
						Element row = (Element) p;
						NodeList valueList = row.getChildNodes();
						element_value = new ArrayList<String>();
						for(int j = 0; j < valueList.getLength();j++) {
							Node n = valueList.item(j);
							if(n.getNodeType() == Node.ELEMENT_NODE) {
							Element value = (Element) n;
							if(flag == 0) {
							columns.add(value.getTagName());
							}
							element_value.add(value.getTextContent());
							}
						}
						flag =1;
						element.add(element_value);
					}
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object[][] elements = new Object[element.size()][element_value.size()];
			for(int i = 0 ;i<element.size();i++) {
	    		for(int j = 0;j<element_value.size();j++) {
	    			if (type.get(j).contains("int")) {
	    				elements[i][j] = (element.get(i).get(j));
					}
					else {
						elements[i][j] = element.get(i).get(j).toString();
						
					}
	    			elements[i][j] = element.get(i).get(j);
	    		}
	    	}
			return elements;
    }
}


