package daedalusCodeComponents.statements.stream;

import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.statements.DaedalusStatement;

public class DaedalusStream extends DaedalusStatement {
	private DaedalusExpression head;
	private DaedalusStreamElement next;
	
	public DaedalusStream(DaedalusExpression expr,DaedalusStreamElement nextElement) {
		this.head = expr;
		this.next = nextElement;
	}
	
	@Override
	public String toString() {
		return super.toString()+" Stream "+this.head+" :> "+this.next;
	}
}
