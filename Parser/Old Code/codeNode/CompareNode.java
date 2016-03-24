package codeNode;

public class CompareNode extends PredicateNode{
	
	private Character operator;
	public CompareNode(Character op,CalculationNode left, CalculationNode right) {
		super(left, right);
		this.operator = op;
	}
	
	@Override
	public boolean getValue() {
		int valLeft = ((CalculationNode) left()).getValue();
		int valRight = ((CalculationNode) right()).getValue();
		
		switch(this.operator.charValue()) {
		case '<': return valLeft < valRight;
		case '>': return valLeft > valRight;
		case '=': return valLeft == valRight;
		case '!': return valLeft != valRight;
		case 'g': return valLeft <= valRight;
		case 'l': return valLeft >= valRight;
		default:
			throw new IllegalStateException();
		}
	}
}
