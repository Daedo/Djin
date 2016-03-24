package codeNode;

public class WhileNode extends CodeNode{

	PredicateNode condition;
	public WhileNode(PredicateNode cond, CodeNode whileBlock) {
		super(whileBlock, null);
		this.condition = cond;
	}

	@Override
	public void execute() {
		while(this.condition.getValue()) {
			DebugMessage.log("While -> True");
			left().execute();
		}
		DebugMessage.log("While -> False");
	}
}


