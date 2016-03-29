package daedalusExecution.types;

import java.util.function.Function;

import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusExecution.intermediateObjects.DaedalusTypedValue;

public class Cast {
	private TypePair castDirection;
	private Function<DaedalusTypedValue, DaedalusTypedValue> castFunction;
	
	//Identity
	public Cast(DaedalusType type) {
		this.castDirection = new TypePair(type, type);
		this.castFunction = n->n;
	}
	
	public TypePair getCastDirection() {
		return this.castDirection;
	}
	
	public Function<DaedalusTypedValue, DaedalusTypedValue> getCastFunction() {
		return this.castFunction;
	}
}
