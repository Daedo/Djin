package codeNode;

import org.parboiled.trees.ImmutableBinaryTreeNode;

public class CodeNode extends ImmutableBinaryTreeNode<CodeNode> {

	public CodeNode(CodeNode left, CodeNode right) {
		super(left, right);
	}

	public void execute() {
		if(left()!=null) {
			left().execute();
		}
		
		if(right()!=null) {
			right().execute();
		}
	}
}
