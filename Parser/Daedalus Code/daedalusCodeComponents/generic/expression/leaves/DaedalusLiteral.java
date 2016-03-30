package daedalusCodeComponents.generic.expression.leaves;

import java.math.BigDecimal;
import java.math.BigInteger;

import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusExecution.exception.DaedalusException;
import daedalusExecution.intermediateObjects.DaedalusIntermediateValue;
import daedalusExecution.intermediateObjects.DaedalusTypedValue;

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
		
		
		return "Literal: "+out;
	}
	
	@Override
	public DaedalusIntermediateValue resolve() throws DaedalusException {
		if(this.value instanceof BigInteger) {
			return DaedalusTypedValue.createInt((BigInteger) this.value);
		}
		
		if(this.value instanceof BigDecimal) {
			return DaedalusTypedValue.createDecimal((BigDecimal) this.value);
		}
		
		if(this.value instanceof Boolean) {
			return DaedalusTypedValue.createBoolean((Boolean) this.value);
		}
		
		if(this.value instanceof String) {
			return DaedalusTypedValue.createString((String) this.value);
		}
		
		throw new DaedalusException("Unknown Literal \""+this.value+"\"");
	}
}
