/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.parsers.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import edu.buffalo.cse.ir.wikiindexer.parsers.Parser;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;

/**
 * @author nikhillo
 *
 */
//@RunWith(Parameterized.class)
public class ParserTest {//extends PropertiesBasedTest {
	
	/*public ParserTest(Properties props) {
		super(props);
	}
	*/
	
	/**
	 * Test method for {@link edu.buffalo.cse.ir.wikiindexer.parsers.Parser#parse(java.lang.String, java.util.Collection)}.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	@Test
	public final void testParse() throws ParserConfigurationException, SAXException, IOException {
//		Parser testClass = new Parser(idxProps);
		Parser testClass = new Parser(null);
		ArrayList<WikipediaDocument> list = new ArrayList<WikipediaDocument>();
		
		//null file
		/*testClass.parse(null, list);
		assertTrue(list.isEmpty());
		
		//empty filename
		testClass.parse("", list);
		assertTrue(list.isEmpty());
		
		//invalid filename
		testClass.parse("abc.xml", list);
		assertTrue(list.isEmpty());*/
		
		//five documents
//		testClass.parse(FileUtil.getRootFilesFolder(idxProps) + "five_entries.xml", list);
		testClass.parse("C:/Sandbox/wikiindexer/files/five_entries.xml", list);
		assertEquals(5, list.size());
		
		assertEquals(8, list.get(0).getSections().size());
		assertEquals("Default", list.get(0).getSections().get(0).getTitle());

		assertEquals(4, list.get(1).getSections().size());
		assertEquals(3, list.get(2).getSections().size());
		assertEquals(1, list.get(3).getSections().size());
		assertEquals(6, list.get(4).getSections().size());
		/* TODO: Add structural test here */
		Iterator<WikipediaDocument> iterator = list.iterator();
		
		/*for (WikipediaDocument temp : list) {
			for(String c:temp.getCategories()){
				System.out.println("C=" + c);
			}
			for(String c:temp.getLinks()){
				System.out.println("L=" + c);
			}
		}*/
		assertEquals(8, list.get(0).getCategories().size());
		
		assertEquals(33, list.get(0).getLinks().size());
		
		
		
		
		
	}

}
