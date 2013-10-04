package edu.buffalo.cse.ir.wikiindexer;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
/**
 * 
 */







import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;

import edu.buffalo.cse.ir.wikiindexer.IndexerConstants.RequiredConstant;
import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.indexer.IndexReader;
import edu.buffalo.cse.ir.wikiindexer.indexer.IndexerException;
import edu.buffalo.cse.ir.wikiindexer.indexer.PostingNode;
import edu.buffalo.cse.ir.wikiindexer.indexer.SharedDictionary;
import edu.buffalo.cse.ir.wikiindexer.parsers.Parser;
import edu.buffalo.cse.ir.wikiindexer.test.AllTests;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerFactory;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.DocumentTransformer;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.IndexableDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;

/**
 * @author nikhillo
 *
 */
public class SingleRunner {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	static int abc=0;	
	public static void main(String[] args) throws InterruptedException {
		if (args.length != 2) {
			printUsage();
			System.exit(1);
		} else {
			if (args[0] != null && args[0].length() > 0) {
				String filename = args[0];
				Properties properties = loadProperties(filename);
				if (properties == null) {
					System.err.println("Error while loading the Properties file. Please check the messages above and try again");
					System.exit(2);
				} else  {
					if (args[1] != null && args[1].length() == 2) {
						String mode = args[1].substring(1).toLowerCase();
						
						if ("t".equals(mode)) {
							runTests(filename);
						} else if ("i".equals(mode)) {
							//runIndexer(properties);
							readIndexer();
						} else if ("b".equals(mode)) {
							runTests(filename);
							runIndexer(properties);
						} else {
							System.err.println("Invalid mode specified!");
							printUsage();
							System.exit(4);
						}	
					} else {
						System.err.println("Invalid or no mode specified!");
						printUsage();
						System.exit(5);
					}
				}
			} else {
				System.err.println("The provided properties filename is empty or could not be read");
				printUsage();
				System.exit(3);
			}	
		}

	}
	
	private static void readIndexer() {
		IndexReader indexReader = new IndexReader(null, INDEXFIELD.AUTHOR);
		int nAuthor=0;
		try {
			nAuthor= indexReader.getTotalKeyTerms();
			/*Map<String, LinkedList<PostingNode>> map = indexReader.getMap();
//			System.out.println("Map size:" + map.size());
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				LinkedList<PostingNode> list = (LinkedList<PostingNode>) mapEntry.getValue();
				System.out.println("Map Key==="+ mapEntry.getKey());
				for(int i=1;i<list.size();i++){
					PostingNode pn = list.get(i);
					System.out.println(":Author Posting data==="+pn.getValue()+":Posting freq===" + pn.getFrequency());
				}
//				System.out.println("Link posting list: "+ list);
			}*/
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Author count:" + nAuthor);
		
		IndexReader indexReader1 = new IndexReader(null, INDEXFIELD.CATEGORY);
		int nCategory=0;
		try {
			nCategory = indexReader1.getTotalKeyTerms();
			/*Map<String, LinkedList<PostingNode>> map = indexReader1.getMap();
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				LinkedList<PostingNode> list = (LinkedList<PostingNode>) mapEntry.getValue();
				System.out.println("Map Key==="+ mapEntry.getKey());
				for(int i=1;i<list.size();i++){
					PostingNode pn = list.get(i);
					System.out.println("Term Posting data:"+pn.getValue()+" Posting freq:" + pn.getFrequency());
				}
			}*/
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Category count:" + nCategory);
		
		
		IndexReader indexReader2 = new IndexReader(null, INDEXFIELD.TERM);
		int nTerm=0;
		try {
			FileWriter fw = new FileWriter("files/termOut.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			nTerm = indexReader2.getTotalKeyTerms();
			Map<String, LinkedList<PostingNode>> map = indexReader2.getMap();
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				LinkedList<PostingNode> list = (LinkedList<PostingNode>) mapEntry.getValue();
//				System.out.println("Map Key==="+ mapEntry.getKey());
				bw.write(mapEntry.getKey().toString());
				bw.write("\n");
				/*for(int i=1;i<list.size();i++){
					PostingNode pn = list.get(i);
					System.out.println("Term Posting data:"+pn.getValue()+" Posting freq:" + pn.getFrequency());
				}*/
			}
			bw.close();
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Term count:" + nTerm);
		
		IndexReader indexReader3 = new IndexReader(null, INDEXFIELD.LINK);
		int nLink=0;
		try {
			nLink = indexReader3.getTotalKeyTerms();
			/*Map<Integer, LinkedList<PostingNode>> map = indexReader3.getLinkMap();
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				LinkedList<PostingNode> list = (LinkedList<PostingNode>) mapEntry.getValue();
				for(int i=1;i<list.size();i++){
					PostingNode pn = list.get(i);
					System.out.println("Link Posting data:"+pn.getValue()+" Posting freq:" + pn.getFrequency());
				}
			}*/
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Link count:" + nLink);
		
	}

	private static void runIndexer(Properties properties) throws InterruptedException {
		long start;
		System.out.println("Starting .......");
		ArrayList<WikipediaDocument> list = new ArrayList<WikipediaDocument>();
		Parser parser = new Parser(properties);
		start = System.currentTimeMillis();
		parser.parse(FileUtil.getDumpFileName(properties), list);
		
		System.out.println("Finished parsing: " + (System.currentTimeMillis() - start));
		Map<INDEXFIELD, Tokenizer> tknizerMap;
		ExecutorService svc = Executors.newSingleThreadExecutor();
		CompletionService<IndexableDocument> pool = new ExecutorCompletionService<IndexableDocument>(svc);
		int numdocs = list.size();
		
		start = System.currentTimeMillis();
		System.out.println("Starting tokenization");
		for (WikipediaDocument doc : list) {
			tknizerMap = initMap(properties);
			pool.submit(new DocumentTransformer(tknizerMap, doc));
		}
		
		System.out.println("Submitted tokenization: " + (System.currentTimeMillis() - start));
		
		IndexableDocument idoc;
		SharedDictionary docDict = new SharedDictionary(properties, INDEXFIELD.LINK);
		
		int currDocId;
		ThreadedIndexerRunner termRunner = new ThreadedIndexerRunner(properties);
		SingleIndexerRunner authIdxer = new SingleIndexerRunner(properties, INDEXFIELD.AUTHOR, INDEXFIELD.LINK, docDict, false);
		SingleIndexerRunner catIdxer = new SingleIndexerRunner(properties, INDEXFIELD.CATEGORY, INDEXFIELD.LINK, docDict, false);
		SingleIndexerRunner linkIdxer = new SingleIndexerRunner(properties, INDEXFIELD.LINK, INDEXFIELD.LINK, docDict, true);
		Map<String, Integer> tokenmap;
		
		System.out.println("Starting indexing.....");
		start = System.currentTimeMillis();
		double pctComplete = 0;
		
		for (int i = 0; i < numdocs; i++) {
			try {
				idoc = pool.take().get();
				if (idoc != null) {
					currDocId = docDict.lookup(idoc.getDocumentIdentifier());
					TokenStream stream;
					try {
						for (INDEXFIELD fld : INDEXFIELD.values()) {
							stream = idoc.getStream(fld);
							
							if (stream != null) {
								tokenmap = stream.getTokenMap();
								
								if (tokenmap != null) {
									switch (fld) {
									case TERM:
										termRunner.addToIndex(tokenmap,
												currDocId);
										break;
									case AUTHOR:
										authIdxer.processTokenMap(
												currDocId, tokenmap);
										break;
									case CATEGORY:
										
										catIdxer.processTokenMap(currDocId,
												tokenmap);
										break;
									case LINK:          
											
										linkIdxer.processTokenMap(
												currDocId, tokenmap);
										break;
									}
								}
							}
							

						}
					} catch (IndexerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pctComplete = (i * 100.0d) / numdocs;
			
			if (pctComplete % 10 == 0) {
				System.out.println(pctComplete+ "% submission complete");
			}
		}
		
		System.out.println("Submitted all tasks in: " + (System.currentTimeMillis() - start));
		
		try {
			termRunner.cleanup();
			authIdxer.cleanup();
			catIdxer.cleanup();
			linkIdxer.cleanup();
			docDict.writeToDisk();
			docDict.cleanUp();
		} catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Waiting for all tasks to complete");
		while (termRunner.isFinished() && authIdxer.isFinished() && catIdxer.isFinished() && linkIdxer.isFinished()) {
			//do nothing
			Thread.sleep(3000);
		}
		System.out.println("Process complete: " + (System.currentTimeMillis() - start));
		svc.shutdown();
		
	}
	
	private static Map<INDEXFIELD, Tokenizer> initMap(Properties props) {
		HashMap<INDEXFIELD, Tokenizer> map = new HashMap<INDEXFIELD, Tokenizer>(INDEXFIELD.values().length);
		TokenizerFactory fact = TokenizerFactory.getInstance(props);
		for (INDEXFIELD fld : INDEXFIELD.values()) {
			map.put(fld, fact.getTokenizer(fld));
		}
		
		return map;
	}

	/**
	 * Method to print the correct usage to run this class.
	 */
	private static void printUsage() {
		System.err.println("The usage is: ");
		System.err.println("java edu.buffalo.cse.ir.wikiindexer.Runner <filename> <flag>");
		System.err.println("where - ");
		System.err.println("filename: Fully qualified file name from which to load the properties");
		System.err.println("flag: one amongst the following -- ");
		System.err.println("-t: Only execute tests");
		System.err.println("-i: Only run the indexer");
		System.err.println("-b: Run both, tests first then indexer");
		
	}
	
	/**
	 * Method to execute all tests
	 * @param filename: Filename for the properties file
	 */
	private static void runTests(String filename) {
		System.setProperty("PROPSFILENAME", filename);
		JUnitCore core = new JUnitCore();
		core.run(new Computer(), AllTests.class);
		
	}
	
	/**
	 * Method to load the Properties object from the given file name
	 * @param filename: The filename from which to load Properties
	 * @return The loaded object
	 */
	private static Properties loadProperties(String filename) {

		try {
			Properties props = FileUtil.loadProperties(filename);
			
			if (validateProps(props)) {
				return props;
			} else {
				System.err.println("Some properties were either not loaded or recognized. Please refer to the manual for more details");
				return null;
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to open or load the specified file: " + filename);
		} catch (IOException e) {
			System.err.println("Error while reading properties from the specified file: " + filename);
		}
		
		return null;
	}
	
	/**
	 * Method to validate that the properties object has been correctly loaded
	 * @param props: The Properties object to validate
	 * @return true if valid, false otherwise
	 */
	private static boolean validateProps(Properties props) {
		/* Validate size */
		if (props != null && props.entrySet().size() == IndexerConstants.NUM_PROPERTIES) {
			/* Get all required properties and ensure they have been set */
			Field[] flds = IndexerConstants.class.getDeclaredFields();
			boolean valid = true;
			Object key;
			
			for (Field f : flds) {
				if (f.isAnnotationPresent(RequiredConstant.class) ) {
					try {
						key = f.get(null);
						if (!props.containsKey(key) || props.get(key) == null) {
							System.err.println("The required property " + f.getName() + " is not set");
							valid = false;
						}
					} catch (IllegalArgumentException e) {
						
					} catch (IllegalAccessException e) {
						
					}
				}
			}
			
			return valid;
		}
		
		return false;
	}

}
