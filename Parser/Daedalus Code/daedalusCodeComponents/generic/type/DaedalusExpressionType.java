package daedalusCodeComponents.generic.type;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusExpressionType extends DaedalusType {
	private DaedalusExpression expression;
	public DaedalusExpressionType(DaedalusExpression expr) {
		this.expression = expr;
	}
	
	@Override
	public String toString() {
		return this.expression+".class";
	}
}
