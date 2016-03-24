package daedalusCodeComponents.generic.expression;

import java.util.Vector;

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
		String vals = "";
		
		for(DaedalusExpression exp:expressions) {
			if(!vals.equals("")) {
				vals+=", ";
			}
			vals+= exp.toString();
		}
		
		return super.toString()+ " List { "+vals+" }";
	}
}
