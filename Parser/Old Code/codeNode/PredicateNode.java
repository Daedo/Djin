package codeNode;

public class PredicateNode  extends CodeNode {
	private Boolean value;
	private Character operator;
	
	protected PredicateNode(CodeNode left,CodeNode right) {
		super(left,right);
	}
	
	public PredicateNode(boolean val) {
		super(null,null);
		this.value = new Boolean(val);
	}
	
	public PredicateNode(Character op,PredicateNode left, PredicateNode right) {
		super(left,right);
		this.operator = op;
	}
	
	public boolean getValue() {
		if (this.operator == null) return this.value.booleanValue();
		
		PredicateNode left = (PredicateNode)left();
		PredicateNode right = (PredicateNode)right();
		
		switch (this.operator.charValue()) {
		case '&':
			return left.getValue() && right.getValue();
		case '|':
			return left.getValue() || right.getValue();
		case '^':
			boolean l = left.getValue();
			boolean r = right.getValue();
			
			return (l^r);
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public String toString() {
		return (this.operator == null ? "Value " + this.value : "Operator '" + this.operator + '\'') + " | " + getValue();
	}
}
