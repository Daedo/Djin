package daedalusCodeComponents.statements.stream;

import java.util.Optional;
import java.util.Vector;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusStreamParameterList extends DaedalusSyntaxElement {
	private Vector<Optional<DaedalusExpression>> parameter;
	
	public DaedalusStreamParameterList() {
		this.parameter = new Vector<>();
	}
	
	public boolean addParameter(DaedalusExpression exp) {
		if(exp!=null) {
			this.parameter.add(Optional.of(exp));
		} else {
			this.parameter.add(Optional.empty());
		}
		return true;
	}
	
	@Override
	public String toString() {
		String out = "";
		for(Optional<DaedalusExpression> p:this.parameter) {
			if(!out.equals("")) {
				out+=", ";
			}
			if(p.isPresent()) {
				out+=p.get();
			} else {
				out+="*";
			}
		}
		return "("+out+")";
	}
}
