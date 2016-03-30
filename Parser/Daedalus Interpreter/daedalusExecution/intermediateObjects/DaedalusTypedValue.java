package daedalusExecution.intermediateObjects;

import java.math.BigDecimal;
import java.math.BigInteger;

import daedalusCodeComponents.generic.type.DaedalusTypeLiteral;

public class DaedalusTypedValue extends DaedalusIntermediateValue{
	private DaedalusTypeLiteral type;
	private Object value;
	
	protected DaedalusTypedValue(DaedalusTypeLiteral t, Object val) {
		super(DaedalusReturnLiteral.NONE);
		this.type = t;
		this.value = val;
	}
	
	public static DaedalusTypedValue createInt(BigInteger val) {
		return new DaedalusTypedValue(DaedalusTypeLiteral.INT, val);
	}
	
	public static DaedalusTypedValue createDecimal(BigDecimal val) {
		return new DaedalusTypedValue(DaedalusTypeLiteral.DECIMAL, val);
	}
	
	public static DaedalusTypedValue createBoolean(Boolean val) {
		return new DaedalusTypedValue(DaedalusTypeLiteral.BOOLEAN, val);
	}
	
	public static DaedalusIntermediateValue createString(String val) {
		return new DaedalusTypedValue(DaedalusTypeLiteral.STRING, val);
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public DaedalusTypeLiteral getType() {
		return this.type;
	}


}
