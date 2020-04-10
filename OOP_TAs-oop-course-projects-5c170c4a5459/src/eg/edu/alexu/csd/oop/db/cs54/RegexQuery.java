package eg.edu.alexu.csd.oop.db.cs54;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RegexQuery {

	private static String regex = "";
	private Pattern p;
	private Matcher m;
	private String tableName = "";
	private String path = null;
	private CreateANDDrop data = new CreateANDDrop();
	private Select select = new Select();
	private Update update = new Update();
	private Delete delete = new Delete();
	private Xml insert = new Xml();
	private ArrayList<String> col = new ArrayList<String>();
	private ArrayList<String> type = new ArrayList<>();
	private DTD dtd = new DTD();

	public String matchQuery(String query) {
		String key = "BadQuery";
		// check select
		regex = "((?i)SELECT(?-i)\\s+[\\w\\*\\)\\(\\,\\s+]+\\s+(?i)FROM(?-i)\\s+[\\s+\\w]+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			key = "select";
		}
		// update
		regex = "((?i)UPDATE(?-i)\\s+[\\w]+\\s+(?i)SET(?-i)\\s+[\\w\\,\\'\\=]+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			key = "update";
		}
		// insert
		regex = "((?i)INSERT(?-i)\\s+(?i)INTO(?-i)\\s+[\\d\\w]+[\\s+\\w\\d\\)\\(\\,]*\\s+(?i)VALUES(?-i)\\s+\\([\\d\\w\\'\\,\\)]+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			key = "insert";
		}
		// delete
		regex = "((?i)DELETE(?-i)\\s+(?i)FROM(?-i)\\s+[\\d\\w\\'\\=]+)";

		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			key = "delete";
		}
		return key;
	}

	public String matchStrQuery(String query) {
		String key = "BadQuery";
		// create database
		regex = "((?i)CREATE(?-i)\\s+(?i)DATABASE(?-i)\\s+[\\s\\w]+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			key = "createData";
		}
		// DROP database
		regex = "((?i)DROP(?-i)\\s+(?i)DATABASE(?-i)\\s+[\\s\\w]+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			key = "dropData";
		}
		// create table
		regex = "((?i)CREATE(?-i)\\s+(?i)TABLE(?-i)\\s+[\\d*\\w\\s*]+[\\(][\\s+\\w\\d*\\,]*[\\)]\\s*)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			key = "createTable";
		}
		// drop table
		regex = "((?i)DROP(?-i)\\s+(?i)TABLE(?-i)\\s+[\\s\\w]+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			key = "dropTable";
		}
		return key;
	}

	public Object[][] select(String query) {
		String pattern1 = "(?i)Select(?-i)\\s*([a-zA-Z0-9_]*)\\s*(?i)From(?-i)\\s*(.*?)\\s*(?i)where(?-i)\\s*([a-zA-Z]\\w*)\\s*(=|<|>)\\s*([a-zA-Z0-9_']*)";
	    String pattern2 = "(?i)Select(?-i)\\s*\\*\\s*(?i)From(?-i)\\s*(.*?)\\s*(?i)where(?-i)\\s*([a-zA-Z]\\w*)\\s*(=|<|>)\\s*([a-zA-Z0-9_']*)";
	    String pattern3 = "(?i)Select(?-i)\\s*\\*\\s*(?i)From(?-i)\\s*([a-zA-Z0-9_]*)";
	    	 p = Pattern.compile(pattern1);
		     m = p.matcher(query);
		    if(m.find()) {
		    String s = m.group(1);
	         s = s.replaceAll("\\s+","");
	         List<String> p = new ArrayList<String>(Arrays.asList(s.split(",")));
	         if(!m.group(2).equals(tableName)) {
	 			tableName = m.group(2);
	 			col = new ArrayList();
	 			type = new ArrayList();
	 			File check = new File(path + System.getProperty("file.separator")+tableName+".xml");
	 			if(check.exists()) {
	 				File dtd = new File(path+System.getProperty("file.separator")+tableName+".dtd");
	 				BufferedReader br = null;
	 				try {
	 					br = new BufferedReader(new FileReader(dtd));
	 				} catch (FileNotFoundException e2) {
	 					// TODO Auto-generated catch block
	 					e2.printStackTrace();
	 				}

	 			    try {
	 			    	String st="";
	 					while ((st=br.readLine()) != null) {
	 					    if(st.contains("Row")&&!st.contains("Row*")) {
	 					    	int index = st.indexOf('(');
	 					    	index++;
	 					    	String get = "";
	 					    	while(st.charAt(index)!=')') {
	 					    		if(st.charAt(index)==',') {
	 					    			col.add(get);
	 					    			get = new String();
	 					    			index++;
	 					    		}else if(st.charAt(index+1)==')') {
	 					    			get+= st.charAt(index);
	 					    			col.add(get);
	 					    			break;
	 					    		}
	 					    		else {
	 					    			get+= st.charAt(index);
	 					    			index++;
	 					    		}
	 					    	}
	 					    	break;
	 					    }
	 					}
	 				} catch (IOException e1) {
	 					// TODO Auto-generated catch block
	 					e1.printStackTrace();
	 				}
	 			    try {
	 					br.close();
	 				} catch (IOException e1) {
	 					// TODO Auto-generated catch block
	 					e1.printStackTrace();
	 				}
	 				File dtdtype = new File(path + System.getProperty("file.separator")+tableName+"dtd.xml");
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
	 					doc = dBuilder.parse(dtdtype);
	 				} catch (SAXException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				} catch (IOException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				}
	 				doc.getDocumentElement().normalize();
	 				NodeList nList = doc.getElementsByTagName(tableName);
	 				for (int i = 0; i < nList.getLength(); i++) {
	 			           Node nNode = nList.item(i);
	 			           
	 			           
	 			           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 			              Element eElement = (Element) nNode;
	 			              
	 			              for (int j = 0 ; j < col.size() ; j++) {
	 			            	  String coll = "";
	 			            	  coll = eElement.getElementsByTagName(col.get(j)).item(i).getTextContent();
	 			            	  type.add(coll);
	 			              }
	 			           }
	 				}
	 			}
	 		}
	         return select.selectWithColumn(path, m.group(2), m.group(3), m.group(4), m.group(5),p , type);
		    }
		    p = Pattern.compile(pattern2);
		     m = p.matcher(query);
	
		    if(m.find()) {
		    	if(!m.group(1).equals(tableName)) {
					tableName = m.group(1);
					col = new ArrayList();
					type = new ArrayList();
					File check = new File(path + System.getProperty("file.separator")+tableName+".xml");
					if(check.exists()) {
						File dtd = new File(path+System.getProperty("file.separator")+tableName+".dtd");
						BufferedReader br = null;
						try {
							br = new BufferedReader(new FileReader(dtd));
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

					    try {
					    	String st="";
							while ((st=br.readLine()) != null) {
							    if(st.contains("Row")&&!st.contains("Row*")) {
							    	int index = st.indexOf('(');
							    	index++;
							    	String get = "";
							    	while(st.charAt(index)!=')') {
							    		if(st.charAt(index)==',') {
							    			col.add(get);
							    			get = new String();
							    			index++;
							    		}else if(st.charAt(index+1)==')') {
							    			get+= st.charAt(index);
							    			col.add(get);
							    			break;
							    		}
							    		else {
							    			get+= st.charAt(index);
							    			index++;
							    		}
							    	}
							    	break;
							    }
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					    try {
							br.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						File dtdtype = new File(path + System.getProperty("file.separator")+tableName+"dtd.xml");
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
							doc = dBuilder.parse(dtdtype);
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						doc.getDocumentElement().normalize();
						NodeList nList = doc.getElementsByTagName(tableName);
						for (int i = 0; i < nList.getLength(); i++) {
					           Node nNode = nList.item(i);
					           
					           
					           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					              Element eElement = (Element) nNode;
					              
					              for (int j = 0 ; j < col.size() ; j++) {
					            	  String coll = "";
					            	  coll = eElement.getElementsByTagName(col.get(j)).item(i).getTextContent();
					            	  type.add(coll);
					              }
					           }
						}
					}
				}
		    	return select.select(path, m.group(1), m.group(2), m.group(3), m.group(4) , type);
		    }
		    p = Pattern.compile(pattern3);
		     m = p.matcher(query);
		    if(m.find()) {
		    	if(!m.group(1).equals(tableName)) {
					tableName = m.group(1);
					col = new ArrayList();
					type = new ArrayList();
					File check = new File(path + System.getProperty("file.separator")+tableName+".xml");
					if(check.exists()) {
						File dtd = new File(path+System.getProperty("file.separator")+tableName+".dtd");
						BufferedReader br = null;
						try {
							br = new BufferedReader(new FileReader(dtd));
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

					    try {
					    	String st="";
							while ((st=br.readLine()) != null) {
							    if(st.contains("Row")&&!st.contains("Row*")) {
							    	int index = st.indexOf('(');
							    	index++;
							    	String get = "";
							    	while(st.charAt(index)!=')') {
							    		if(st.charAt(index)==',') {
							    			col.add(get);
							    			get = new String();
							    			index++;
							    		}else if(st.charAt(index+1)==')') {
							    			get+= st.charAt(index);
							    			col.add(get);
							    			break;
							    		}
							    		else {
							    			get+= st.charAt(index);
							    			index++;
							    		}
							    	}
							    	break;
							    }
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					    try {
							br.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						File dtdtype = new File(path + System.getProperty("file.separator")+tableName+"dtd.xml");
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
							doc = dBuilder.parse(dtdtype);
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						doc.getDocumentElement().normalize();
						NodeList nList = doc.getElementsByTagName(tableName);
						for (int i = 0; i < nList.getLength(); i++) {
					           Node nNode = nList.item(i);
					           
					           
					           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					              Element eElement = (Element) nNode;
					              
					              for (int j = 0 ; j < col.size() ; j++) {
					            	  String coll = "";
					            	  coll = eElement.getElementsByTagName(col.get(j)).item(i).getTextContent();
					            	  type.add(coll);
					              }
					           }
						}
					}
				}
		    	return select.selectAlltable(path, m.group(1),type);
		    }
	    
		return null;
		
	}

	public int insert(String query) {
		int start = 0;
		int done = 0;
		boolean in = false;
		ArrayList<String> col_in = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();
		String sub1 = "";
		String sub2 = "";
		regex = "((?i)INSERT(?-i)\\s+(?i)INTO(?-i)\\s+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
		start = m.end();
		}
		regex = "(\\s*\\(|\\s*(?i)VALUES(?-i))";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
		if(!query.substring(start, m.start()).equals(tableName)) {
			tableName = query.substring(start, m.start());
			col = new ArrayList();
			type = new ArrayList();
			File check = new File(path + System.getProperty("file.separator")+tableName+".xml");
			if(check.exists()) {
				File dtd = new File(path+System.getProperty("file.separator")+tableName+".dtd");
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(dtd));
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			    try {
			    	String st="";
					while ((st=br.readLine()) != null) {
					    if(st.contains("Row")&&!st.contains("Row*")) {
					    	int index = st.indexOf('(');
					    	index++;
					    	String get = "";
					    	while(st.charAt(index)!=')') {
					    		if(st.charAt(index)==',') {
					    			col.add(get);
					    			get = new String();
					    			index++;
					    		}else if(st.charAt(index+1)==')') {
					    			get+= st.charAt(index);
					    			col.add(get);
					    			break;
					    		}
					    		else {
					    			get+= st.charAt(index);
					    			index++;
					    		}
					    	}
					    	break;
					    }
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    try {
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				File dtdtype = new File(path + System.getProperty("file.separator")+tableName+"dtd.xml");
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
					doc = dBuilder.parse(dtdtype);
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName(tableName);
				for (int i = 0; i < nList.getLength(); i++) {
			           Node nNode = nList.item(i);
			           
			           
			           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			              Element eElement = (Element) nNode;
			              
			              for (int j = 0 ; j < col.size() ; j++) {
			            	  String coll = "";
			            	  coll = eElement.getElementsByTagName(col.get(j)).item(i).getTextContent();
			            	  type.add(coll);
			              }
			           }
				}
			}
		}
		tableName = query.substring(start, m.start());
		tableName.trim();//final table name
		}
		regex ="(\\()";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		int count = 0;
		while(m.find()) {
			count++;
		}
		regex = "(\\([\\d\\w\\'\\,\\s]+\\))";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if(count==2) {
		if (m.find()) {
		sub1 = query.substring(m.start(), m.end());
		}
		}
		if (m.find()) {
		sub2 = query.substring(m.start(), m.end());
		}
		regex = "([\\d\\w\\']+)";
		p = Pattern.compile(regex);
		if(count==2) {
		m = p.matcher(sub1);
		while (m.find()) {
			col_in.add(sub1.substring(m.start(), m.end()));//array list of col 
		}
		
		}
		m = p.matcher(sub2);
		while (m.find()) {
			values.add(sub2.substring(m.start(), m.end()));//array list of values
		}

		try {
			boolean found = false;
			if(col.size()!=0) {
				for(int i=0;i<col_in.size();i++) {
				found=false;
				for(int j=0;j<col.size();j++) {
					if(col_in.get(i).toLowerCase().equals(col.get(j).toLowerCase())) {
						found=true;
					}
				}
				if(!found) {
					return 0;
				}
			}
			}
			if (col_in.size() != values.size()) {
				return -2;
			}
			in = insert.Insert(path  + System.getProperty("file.separator") + tableName +".xml",tableName, col_in, values);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (in) {
			done = 1;
		}
		else {
			done = 0;
		}
		return done;

	}

public int update(String query) {
		String str = "";
		String colCond = "";
		String condition = "";
		boolean found = false;
		int start = 0;
		int count =0;
		regex = "((?i)UPDATE(?-i)\\s+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
		start = m.end();
		}
		regex = "(\\s+(?i)SET(?-i)\\s+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
			if(!query.substring(start, m.start()).equals(tableName)) {
				tableName = query.substring(start, m.start());
				col = new ArrayList();
				type = new ArrayList();
				File check = new File(path + System.getProperty("file.separator")+tableName+".xml");
				if(check.exists()) {
					File dtd = new File(path+System.getProperty("file.separator")+tableName+".dtd");
					BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(dtd));
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

				    try {
				    	String st="";
						while ((st=br.readLine()) != null) {
						    if(st.contains("Row")&&!st.contains("Row*")) {
						    	int index = st.indexOf('(');
						    	index++;
						    	String get = "";
						    	while(st.charAt(index)!=')') {
						    		if(st.charAt(index)==',') {
						    			col.add(get);
						    			get = new String();
						    			index++;
						    		}else if(st.charAt(index+1)==')') {
						    			get+= st.charAt(index);
						    			col.add(get);
						    			break;
						    		}
						    		else {
						    			get+= st.charAt(index);
						    			index++;
						    		}
						    	}
						    	break;
						    }
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    try {
						br.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					File dtdtype = new File(path + System.getProperty("file.separator")+tableName+"dtd.xml");
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
						doc = dBuilder.parse(dtdtype);
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName(tableName);
					for (int i = 0; i < nList.getLength(); i++) {
				           Node nNode = nList.item(i);
				           
				           
				           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				              Element eElement = (Element) nNode;
				              
				              for (int j = 0 ; j < col.size() ; j++) {
				            	  String coll = "";
				            	  coll = eElement.getElementsByTagName(col.get(j)).item(i).getTextContent();
				            	  type.add(coll);
				              }
				           }
					}
				}
			}
		tableName = query.substring(start, m.start());
		tableName.trim();//final name
		start = m.end();
		}
		regex = "(\\,)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		ArrayList<String> temp = new ArrayList<>();
		ArrayList<String> col_con = new ArrayList<>();
		ArrayList<String> value = new ArrayList<>();
		while (m.find()) {
			temp.add(query.substring(start, m.start()));
			start = m.end();
		}
		regex = "((?i)WHERE(?-i)\\s*)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (!m.find()) {//there is no condition (valid)
			temp.add(query.substring(start, query.length()));
			
		} else {
			temp.add(query.substring(start, m.start()));
            found = true;
			str = query.substring(m.end(), query.length());
			regex = "(\\=|\\<|\\>)";
			p = Pattern.compile(regex);
			m = p.matcher(str);
			if (m.find()) {
			colCond = str.substring(0, m.start());
			condition = str.substring(m.start(), str.length());
			if(condition.charAt(1)!=' ') {
				String curr = condition.substring(1, condition.length());
				condition = condition.replace(condition.subSequence(1, condition.length()), " ");
				condition+= curr;
				
			}
			
			}

		}

		regex = "(\\=)";
		p = Pattern.compile(regex);

		for (int i = 0; i < temp.size(); i++) {
			str = temp.get(i);
			int index = str.lastIndexOf("=");
			m = p.matcher(str);
			if (m.find()) {
			col_con.add(str.substring(0, m.start()));
			String condition1 = str.substring(index+1, str.length());
			value.add(condition1);
			//col will be updated in all cases( if there is a condition or not)
			}
		}
		if(found) {
				count = update.update( path , tableName , colCond, col, condition, value);
			
			
		}else {
			
				try {
					count = update.updateFullTable( path  , tableName , value, col);
				} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return count;

	}

	public int delete(String query) {

		String colName = "";
		String condition = "";
		int start = 0;
		int count=0;
		regex = "((?i)DELETE(?-i)\\s+(?i)FROM(?-i)\\s+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if(m.find()) {
		start = m.end();
		}
		regex = "((?i)WHERE(?-i))";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (!m.find()) {
			File check = new File(path + System.getProperty("file.separator")+query.substring(start, query.length())+".xml");
			if(!query.substring(start, query.length()).equals(tableName)&&check.exists()) {
				tableName = query.substring(start, query.length());
				col = new ArrayList();
				type = new ArrayList();
				if(check.exists()) {
					File dtd = new File(path+System.getProperty("file.separator")+tableName+".dtd");
					BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(dtd));
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

				    try {
				    	String st="";
						while ((st=br.readLine()) != null) {
						    if(st.contains("Row")&&!st.contains("Row*")) {
						    	int index = st.indexOf('(');
						    	index++;
						    	String get = "";
						    	while(st.charAt(index)!=')') {
						    		if(st.charAt(index)==',') {
						    			col.add(get);
						    			get = new String();
						    			index++;
						    		}else if(st.charAt(index+1)==')') {
						    			get+= st.charAt(index);
						    			col.add(get);
						    			break;
						    		}
						    		else {
						    			get+= st.charAt(index);
						    			index++;
						    		}
						    	}
						    	break;
						    }
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    try {
						br.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					File dtdtype = new File(path + System.getProperty("file.separator")+tableName+"dtd.xml");
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
						doc = dBuilder.parse(dtdtype);
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName(tableName);
					for (int i = 0; i < nList.getLength(); i++) {
				           Node nNode = nList.item(i);
				           
				           
				           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				              Element eElement = (Element) nNode;
				              
				              for (int j = 0 ; j < col.size() ; j++) {
				            	  String coll = "";
				            	  coll = eElement.getElementsByTagName(col.get(j)).item(i).getTextContent();
				            	  type.add(coll);
				              }
				           }
					}
				}
			}
			tableName = query.substring(start, query.length());
			tableName.trim();
			count = delete.deletetable(path, tableName);
			
		} else {
			if(!query.substring(start, query.length()).equals(tableName)) {
				tableName = query.substring(start, m.start());
				tableName = tableName.replaceAll("\\s+","");
				File check = new File(path + System.getProperty("file.separator")+tableName+".xml");
				if(check.exists()) {
				col = new ArrayList();
				type = new ArrayList();
				if(check.exists()) {
					File dtd = new File(path+System.getProperty("file.separator")+tableName+".dtd");
					BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(dtd));
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

				    try {
				    	String st="";
						while ((st=br.readLine()) != null) {
						    if(st.contains("Row")&&!st.contains("Row*")) {
						    	int index = st.indexOf('(');
						    	index++;
						    	String get = "";
						    	while(st.charAt(index)!=')') {
						    		if(st.charAt(index)==',') {
						    			col.add(get);
						    			get = new String();
						    			index++;
						    		}else if(st.charAt(index+1)==')') {
						    			get+= st.charAt(index);
						    			col.add(get);
						    			break;
						    		}
						    		else {
						    			get+= st.charAt(index);
						    			index++;
						    		}
						    	}
						    	break;
						    }
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    try {
						br.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					File dtdtype = new File(path + System.getProperty("file.separator")+tableName+"dtd.xml");
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
						doc = dBuilder.parse(dtdtype);
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName(tableName);
					for (int i = 0; i < nList.getLength(); i++) {
				           Node nNode = nList.item(i);
				           
				           
				           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				              Element eElement = (Element) nNode;
				              
				              for (int j = 0 ; j < col.size() ; j++) {
				            	  String coll = "";
				            	  coll = eElement.getElementsByTagName(col.get(j)).item(i).getTextContent();
				            	  type.add(coll);
				              }
				           }
					}
				}
			}
			}
			tableName = query.substring(start, m.start());
			tableName = tableName.replaceAll("\\s+","");
			tableName.trim();//final name
			start = m.end() + 1;
			regex = "(=|<|>)";
			p = Pattern.compile(regex);
			m = p.matcher(query);
			if(m.find()) {
			colName = query.substring(start, m.start()).toLowerCase();
			colName.trim();
			colName = colName.replaceAll("\\s+","");
			condition = query.substring(m.start(), query.length());
			condition.trim();//as > 8
			if(condition.charAt(1)!=' ') {
				String curr = condition.substring(1, condition.length());
				condition = condition.replace(condition.subSequence(1, condition.length()), " ");
				condition+= curr;
				
			}
			count = delete.delete(path, tableName, colName,condition, col);
			}
			
		}
		return count;
	}

	public String createData(String query) {
		String databaseName = "";
		regex = "((?i)CREATE(?-i)\\s+(?i)DATABASE(?-i)\\s+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
		int start = m.end();
		databaseName = query.substring(start, query.length());
		databaseName.trim();
		path = data.createdatabase(databaseName);
		return path;
		}
		return path;
		

	}

	public Boolean createTable(String query) {
		 col = new ArrayList<String>();
		 type = new ArrayList<>();
		if (!query.contains("(")) {
			try {
				throw new SQLException();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int start = 0;
		String str = null;
		col = new ArrayList<>();
		if (path.equals(null)) {
			return false;
		}
		File table = new File(path);
		regex = "((?i)CREATE(?-i)\\s+(?i)TABLE(?-i)\\s+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if(m.find()) {
		start = m.end();
		}
		regex = "(\\()";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if(m.find()) {
		tableName = query.substring(start, m.start());
		tableName.trim();
		tableName = tableName.replaceAll("\\s+","");
		}
		for (File file: table.listFiles()) {
	        if ( file.getName().equals(tableName+".xml") && file.exists()) {
	            return false;
	        }
	    }
		regex = "(\\([\\s*\\w\\d\\,]+\\))";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if(m.find()) {
		str = query.substring(m.start(), m.end());
		}
		regex = "(\\w+\\d*\\s+\\w)";
		p = Pattern.compile(regex);
		m = p.matcher(str);
		String temp = "";
		while (m.find()) {
			temp = str.substring(m.start(), m.end()-1);
			temp = temp.replaceAll("\\s+","");
			temp.trim();
			temp = temp.replaceAll("\\s+","");
			col.add(temp);
		}
		regex = "(\\w+\\d*\\s*\\,)|(\\w+\\d*\\s*\\))";
		p = Pattern.compile(regex);
		m = p.matcher(str);
		while (m.find()) {
			temp = str.substring(m.start(), m.end() - 1);
			temp = temp.replaceAll("\\s+","");
			temp.trim();
			type.add(temp);
		}
		
		if (col.size() != type.size() ) {
			return false;
			
		}
		if (col.size()==0 || type.size()==0 || !query.contains("(")) {
			try {
				throw new SQLException();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
		try {
			data.createtable(path, tableName);
			insert.createfiletype(path,tableName,col,type);
			insert.creatTable(path, tableName, col, type);
			try {
				dtd.writeDTD(path + System.getProperty("file.separator") + tableName +".dtd", tableName, col);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			insert.get_col_type(col, type);
			return true;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return false;
	}

	public String dropData(String query) {
		String databaseName = "";
		regex = "((?i)DROP(?-i)\\s+(?i)DATABASE(?-i)\\s+)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if (m.find()) {
		int start = m.end();
		databaseName = query.substring(start, query.length());
		databaseName.trim();
		path = data.dropdatabase(databaseName);
		}
		return path;
	}

	public void dropTable(String query) {
		regex = "((?i)DROP(?-i)\\s(?i)TABLE(?-i)\\s)";
		p = Pattern.compile(regex);
		m = p.matcher(query);
		if(m.find()) {
		int start = m.end();
		tableName = query.substring(start, query.length());
		tableName.trim();
		data.droptable(path, tableName);
		}
	}

	public String getPath() {
		return path;
	}
	public String getTableName() {
		return tableName;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Map<String,String> getmaptype(){
		return insert.getmap();
	}

}
