package main;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.support.StringVar;
import org.parboiled.support.Var;

import codeNode.AssignNode;
import codeNode.CalculationNode;
import codeNode.CodeNode;
import codeNode.CompareNode;
import codeNode.IfElseNode;
import codeNode.WhileNode;
import codeNode.PredicateNode;
import codeNode.VariableNode;



@BuildParseTree
public class CalculatorParser extends BaseParser<CodeNode> {
	Rule Base() {
		return Block();
	}
	
	Rule Block() {
		
		Var<CodeNode> b = new Var<>();
		
		return Sequence(
				FirstOf(While(),IfCondition(),Sequence(Assignment(),String(";"))),
				Optional(Block(),b.set(pop())),
				push(new CodeNode(pop(),b.get()))
				);
		
		
		
		/*return OneOrMore(Sequence(
				FirstOf(While(),IfCondition(),Sequence(Assignment(),String(";"))),
						push(new CodeNode(pop(), pop()))));*/
	}
	
	Rule While() {
		return Sequence(String("while"),'(',Predicate(),')','{',Block(),'}',push(new WhileNode((PredicateNode)pop(1), pop())));
	}
	
	Rule IfCondition() {
		Var<PredicateNode> p = new Var<>();
		Var<CodeNode> i = new Var<>();
		Var<CodeNode> e = new Var<>();
		return Sequence(String("if"),'(',
				Sequence(Predicate(),p.set((PredicateNode) pop()))
						,')','{',
				Sequence(Block(),i.set(pop()))
						,'}',
				Optional(String("else"),'{',Block(),e.set(pop()),'}'),
				push(new IfElseNode(p.get(), i.get(), e.get())));
	}
	
	Rule Predicate() {
		Var<Character> ch = new Var<>();
		return FirstOf(
				Sequence(Comparison(),Optional(FirstOf(String("&&"),String("||"),String("^^")),ch.set(matchedChar()),Predicate(),push(new PredicateNode(ch.get(), (PredicateNode)pop(),(PredicateNode) pop())))),
				Sequence(Boolean(), push(new PredicateNode(Boolean.parseBoolean(match()))))
				
		);
	}
	
	Rule Comparison() {
		Var<Character> op = new Var<>();
		
		return Sequence(Expression(),
				FirstOf(
						Sequence(String("=="),op.set('=')),
						Sequence(String(">="),op.set('g')),
						Sequence(String("<="),op.set('l')),
						Sequence(String("!="),op.set('!')),
						Sequence(String(">"),op.set('>')),
						Sequence(String("<"),op.set('<'))),Expression(),push(new CompareNode(op.get(), (CalculationNode)pop(1), (CalculationNode)pop())));
	}
	
	Rule Boolean() {
		return FirstOf(String("true"),String("false"));
	}
	
	Rule Assignment() {
		return Sequence(Variable(),String("="),Expression(),push(new AssignNode((VariableNode)pop(1), (CalculationNode)pop())));
	}
	
	Rule Expression() {
		Var<Character> op = new Var<>();
        return Sequence(
            Term(),
            ZeroOrMore(AnyOf("+-"),op.set(matchedChar()), Term(),
            		push(new CalculationNode(op.get(), (CalculationNode)pop(1), (CalculationNode)pop())))
        );
    }

    Rule Term() {
    	Var<Character> op = new Var<>();
        return Sequence(
            Factor(),
            ZeroOrMore(AnyOf("* /"),op.set(matchedChar()), Factor(),
            		push(new CalculationNode(op.get(), (CalculationNode)pop(1), (CalculationNode)pop())))
        );
    }

    Rule Factor() {
        return FirstOf(
            Number(),
            Sequence('(', Expression(), ')'),
            Variable()
        );
    }

    Rule Number() {
        return Sequence(OneOrMore(CharRange('0', '9')),push(new CalculationNode(Integer.parseInt(matchOrDefault("0")))));
    }
    
    Rule Variable() {
    	StringVar n = new StringVar();
    	StringVar i = new StringVar();
    	return Sequence(Name(),n.set(match()),Optional(Index(),i.set(match())),
    			push(new VariableNode(n.get(), i.get()))
    			);
    }
    
    Rule Index() {
    	return Sequence(String("["),Expression(),String("]"));
    }
    
    Rule Name() {
    	return OneOrMore(FirstOf(CharRange('a','z'),CharRange('A','Z')));
    }
}
