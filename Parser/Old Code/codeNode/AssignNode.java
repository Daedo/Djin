package codeNode;

public class AssignNode extends CodeNode {

	public AssignNode(VariableNode left, CalculationNode right) {
		super(left, right);
	}
	
	@Override
	public void execute() {
		int val = ((CalculationNode)right()).getValue();
		VariableNode var = ((VariableNode) left());
		DebugMessage.log("Assign -> "+var+" -> "+val);
		var.assign(val);
	}
	
	@Override
	public String toString() {
		return "Assign ["+left().toString()+"] to ("+right().toString()+")";
	}
}
