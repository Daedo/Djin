package codeNode;

public class CalculationNode extends CodeNode {
	private int value;
	private Character operator;

	public CalculationNode(Character op, CalculationNode left, CalculationNode right) {
		super(left, right);
		this.operator = op;
	}

	public CalculationNode(int val) {
		super(null,null);
		this.value = val;
	}
	
	public int getValue() {
		if (this.operator == null) return this.value;
		
		CalculationNode left = (CalculationNode)left();
		CalculationNode right = (CalculationNode)right();
		
		switch (this.operator.charValue()) {
		case '+':
			return left.getValue() + right.getValue();
		case '-':
			return left.getValue() - right.getValue();
		case '*':
			return left.getValue() * right.getValue();
		case '/':
			return left.getValue() / right.getValue();
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public String toString() {
		return (this.operator == null ? "Value " + this.value : "Operator '" + this.operator + '\'') + " | " + getValue();
	}
}
