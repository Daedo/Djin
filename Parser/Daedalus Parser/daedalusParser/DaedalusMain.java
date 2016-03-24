package daedalusParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;

public class DaedalusMain {
	public static void main(String[] args) {
		String path = "C:\\Users\\Dominik\\Desktop\\Dokumente\\Außerschulisches\\Programmierung\\Daedalus Language\\";
		String name = "testfile";
		String end = ".dae";

		DaedalusParser parser = Parboiled.createParser(DaedalusParser.class);
		try {
			String data = Files.lines(Paths.get(path+name+end)).map(s->s+"\n").reduce((String s1,String s2)->s1+s2).get();

			ReportingParseRunner<?> runner = new ReportingParseRunner<>(parser.CompilationUnit());
			ParsingResult<?> result = runner.run(data);
			if(result.hasErrors()) {
				System.err.println("Errors:");
				for(ParseError err:result.parseErrors) {
					System.err.println(err.getErrorMessage());
				}
			} else {
				String tree = ParseTreeUtils.printNodeTree(result);
				//System.out.println(tree);

				while(!result.valueStack.isEmpty()) {
					System.out.println(result.valueStack.pop());
				}
				
				
				try{
					// Create file 
					FileWriter fstream = new FileWriter(path+name+"_out.txt");
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(tree);
					//Close the output stream
					out.close();
				}catch (Exception e){//Catch exception if any
					System.err.println("Error: " + e.getMessage());
				}

			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
