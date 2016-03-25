package daedalusCodeComponents.statements;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusSwitch extends DaedalusStatement {
	private DaedalusExpression controlExpression;
	private DaedalusCase cases;
	
	public DaedalusSwitch(DaedalusExpression expr, DaedalusCase ca) {
		this.controlExpression = expr;
		this.cases = ca;
	}
}
