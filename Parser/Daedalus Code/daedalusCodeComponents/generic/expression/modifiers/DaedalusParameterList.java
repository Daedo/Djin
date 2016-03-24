package daedalusCodeComponents.generic.expression.modifiers;

import java.util.Vector;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusParameterList extends DaedalusSyntaxElement {
	private Vector<DaedalusExpression> parameter;
	
	public DaedalusParameterList() {
		this.parameter = new Vector<>();
	}
	
	public boolean addParameter(DaedalusExpression exp) {
		this.parameter.add(exp);
		return true;
	}
	
	@Override
	public String toString() {
		String out = "";
		for(DaedalusExpression p:this.parameter) {
			if(!out.equals("")) {
				out+=", ";
			}
			out+=p;
		}
		return "("+out+")";
	}
}
