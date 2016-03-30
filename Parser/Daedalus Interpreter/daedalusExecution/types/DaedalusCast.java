package daedalusExecution.types;

import java.util.function.Function;

import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusExecution.intermediateObjects.DaedalusTypedValue;

public class DaedalusCast {
	private DaedalusTypePair castDirection;
	private Function<DaedalusTypedValue, DaedalusTypedValue> castFunction;
	
	//Identity
	public DaedalusCast(DaedalusType type) {
		this.castDirection = new DaedalusTypePair(type, type);
		this.castFunction = n->n;
	}
	
	public DaedalusTypePair getCastDirection() {
		return this.castDirection;
	}
	
	public Function<DaedalusTypedValue, DaedalusTypedValue> getCastFunction() {
		return this.castFunction;
	}
}
