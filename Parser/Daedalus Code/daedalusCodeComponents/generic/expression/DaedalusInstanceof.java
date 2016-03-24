package daedalusCodeComponents.generic.expression;

import daedalusCodeComponents.generic.type.DaedalusType;

public class DaedalusInstanceof extends DaedalusExpression{
	private DaedalusExpression expession;
	private DaedalusType type;
	
	public DaedalusInstanceof(DaedalusExpression expr,DaedalusType t) {
		this.expession = expr;
		this.type = t;
	}
	
	@Override
	public String toString() {
		return this.expession+" instanceof "+this.type;
	}
}
