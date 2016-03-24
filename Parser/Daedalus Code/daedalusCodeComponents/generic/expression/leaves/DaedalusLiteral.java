package daedalusCodeComponents.generic.expression.leaves;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusLiteral extends DaedalusExpression {
	private Object value;
	public DaedalusLiteral(Object val) {
		this.value = val;
		if(this.value instanceof String) {
			this.value = ((String)this.value).substring(1, ((String) this.value).length()-1);
		}
	}
	
	@Override
	public String toString() {
		String out = "null";
		if(this.value!=null) {
			
			if(this.value instanceof String) {
				out = "\""+this.value+"\"";
			} else {
				out = this.value.toString();
			}
		}
		
		
		return super.toString()+" Literal: "+out;
	}
}
