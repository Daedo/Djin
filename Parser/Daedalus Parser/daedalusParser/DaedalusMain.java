package daedalusParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ErrorLocatingParseRunner;
import org.parboiled.support.ParsingResult;

import daedalusCodeComponents.DaedalusCompilationUnit;
import daedalusCodeComponents.DaedalusSyntaxElement;
import support.Tools;

public class DaedalusMain {

	public static void main(String[] args) {
		String path = "C:\\Users\\Dominik\\Desktop\\Dokumente\\Außerschulisches\\Programmierung\\Daedalus Language\\";
		String name = "testfile";
		String end = ".dae";
		
		
		try {
			DaedalusCompilationUnit unit = parseFile(path+name+end);
			System.out.println(unit);
		} catch (DaedalusParsingException e) {
			System.err.println(e.getMessage());
		}
	}

	public static DaedalusCompilationUnit parseFile(String fileName) throws DaedalusParsingException{

		DaedalusParser parser = Parboiled.createParser(DaedalusParser.class);

		String data = "";
		try {
			data = Files.lines(Paths.get(fileName)).map(s->s+"\n").reduce((String s1,String s2)->s1+s2).get();
		} catch (IOException e) {
			throw new DaedalusParsingException("File Error:\n"+e.getMessage());
		}

		ErrorLocatingParseRunner<DaedalusSyntaxElement> runner = new ErrorLocatingParseRunner<>(parser.CompilationUnit());
		ParsingResult<DaedalusSyntaxElement> result = runner.run(data);
		if(result.hasErrors()) {
			ParseError err = result.parseErrors.get(0);
			throw new DaedalusParsingException(Tools.getSurrindingLinesWithNumbers(data, err.getStartIndex(), err.getEndIndex()));
		}

		if(!result.valueStack.isEmpty()) {
			DaedalusSyntaxElement top = result.valueStack.pop();
			if(top instanceof DaedalusCompilationUnit) {
				return (DaedalusCompilationUnit) top;
			}
		}
		throw new DaedalusParsingException("Intern Parsing Exception");
	}
}
