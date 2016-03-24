package codeNode;

public class IfElseNode extends CodeNode{

	PredicateNode condition;
	public IfElseNode(PredicateNode cond, CodeNode ifCase, CodeNode elseCase) {
		super(ifCase, elseCase);
		this.condition = cond;
	}

	@Override
	public void execute() {
		if(this.condition.getValue()) {
			DebugMessage.log("If -> True");
			left().execute();
		} else {
			DebugMessage.log("If -> False");
			if(right()!=null) {
				DebugMessage.log("Else");
				right().execute();
			}
		}
	}
}
