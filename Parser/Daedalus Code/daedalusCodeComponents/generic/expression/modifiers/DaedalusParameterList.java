package daedalusCodeComponents.generic.expression.modifiers;

import java.util.Vector;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.expression.DaedalusExpression;
import support.Tools;

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
		return Tools.list("(", ")", this.parameter);
	}
}
