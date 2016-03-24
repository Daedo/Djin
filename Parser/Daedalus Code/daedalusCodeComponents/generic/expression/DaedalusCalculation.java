package daedalusCodeComponents.generic.expression;

import java.util.Vector;

public class DaedalusCalculation extends DaedalusExpression {
	Vector<DaedalusExpression> values;
	Vector<DaedalusOperator> op;
	
	public DaedalusCalculation(DaedalusExpression exp1,DaedalusOperator operator,DaedalusExpression exp2) {
		this.values = new Vector<>();
		this.op = new Vector<>();
		this.values.add(exp1);
		this.values.add(exp2);
		this.op.add(operator);
	}
	
	public void addOpeation(DaedalusOperator operator, DaedalusExpression expression) {
		this.op.add(operator);
		this.values.add(expression);
	}
}
