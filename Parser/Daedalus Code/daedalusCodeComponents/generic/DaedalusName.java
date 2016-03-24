package daedalusCodeComponents.generic;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusName extends DaedalusExpression {
	String value;
	
	public static boolean isValidName(String name) {
		if(name==null || name=="") {
			return false;
		}
		
		return name.matches("[a-zA-Z_][a-zA-Z0-9_]*");
	}
	
	public DaedalusName(String val) {
		if(isValidName(val)) {
			this.value = val;
		} else {
			throw new RuntimeException("invalid name");
		}
	}
	
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return super.toString()+" Name: "+this.value;
	}
	
}
