package daedalusCodeComponents.generic.expression;

import java.util.Vector;

import support.Tools;

public class DaedalusExpressionList extends DaedalusExpression {
	private Vector<DaedalusExpression> expressions;
	
	public DaedalusExpressionList() {
		this.expressions = new Vector<>();
	}
	
	public boolean addExperssion(DaedalusExpression expr) {
		this.expressions.add(expr);
		return true;
	}
	
	@Override
	public String toString() {
		String vals = Tools.list("{","}",this.expressions);
		return vals;
	}
}
