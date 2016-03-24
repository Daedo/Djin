package main;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;

import codeNode.CodeNode;
import codeNode.Variable;

public class Main {

	public static void main(String[] args) {
		String input = "var=5;if(var<9){while(var<10){var=var+1;}}else{var=var*2;}";
		CalculatorParser parser = Parboiled.createParser(CalculatorParser.class);
		ParsingResult<?> result = new ReportingParseRunner(parser.Base()).run(input);
		if(result.hasErrors()) {
			System.err.println(result.parseErrors.get(0));
		}
		String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
		System.out.println(parseTreePrintOut);
		System.out.println();
		CodeNode code = (CodeNode) result.valueStack.peek();
		code.execute();
		System.out.println("Done");
		Variable.printAll();
	}

}
