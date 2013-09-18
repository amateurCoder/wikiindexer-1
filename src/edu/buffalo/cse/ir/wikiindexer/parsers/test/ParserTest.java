/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.parsers.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import edu.buffalo.cse.ir.wikiindexer.FileUtil;
import edu.buffalo.cse.ir.wikiindexer.parsers.Parser;
import edu.buffalo.cse.ir.wikiindexer.test.PropertiesBasedTest;
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
		/* TODO: Add structural test here */
		
		
	}

}
