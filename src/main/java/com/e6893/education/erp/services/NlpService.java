package com.e6893.education.erp.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;


public class NlpService {
	
	static Set<String> nounPhrases = new HashSet<>();
	
	public static Set<String> getTopics(String searchSentence) {
		InputStream modelInParse = null;
		try {
			//load chunking model
			modelInParse = new FileInputStream("en-parser-chunking.bin"); //from http://opennlp.sourceforge.net/models-1.5/
			ParserModel model = new ParserModel(modelInParse);
			
			//create parse tree
			Parser parser = ParserFactory.create(model);
			Parse topParses[] = ParserTool.parseLine(searchSentence, parser, 1);
			
			//call subroutine to extract noun phrases
			for (Parse p : topParses)
				getNounPhrases(p);
			return nounPhrases;
			
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
		  if (modelInParse != null) {
		    try {
		    	modelInParse.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		
		
		return null;
	}
	
	//recursively loop through tree, extracting noun phrases
	public static void getNounPhrases(Parse p) {
			
	    if (p.getType().equals("NP")) { //NP=noun phrase
	         nounPhrases.add(p.getCoveredText());
	    }
	    for (Parse child : p.getChildren())
	         getNounPhrases(child);
	}
}
