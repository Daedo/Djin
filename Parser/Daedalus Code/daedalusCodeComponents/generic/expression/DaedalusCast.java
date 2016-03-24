package daedalusCodeComponents.generic.expression;

import daedalusCodeComponents.generic.type.DaedalusType;

public class DaedalusCast extends DaedalusExpression{
	private DaedalusType castType;
	private DaedalusExpression expression;
	
	public DaedalusCast(DaedalusType type, DaedalusExpression expr) {
		this.castType = type;
		this.expression = expr;
	}
	
	@Override
	public String toString() {
		return "("+this.castType+") "+this.expression;
	}
}
