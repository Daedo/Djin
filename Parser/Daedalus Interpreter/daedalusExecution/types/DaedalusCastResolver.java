package daedalusExecution.types;

import java.util.HashMap;
import java.util.Map;

import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusExecution.intermediateObjects.DaedalusTypedValue;

public class DaedalusCastResolver {
	private Map<DaedalusTypePair,DaedalusCast> castMap;
	
	public DaedalusCastResolver() {
		this.castMap = new HashMap<>();
	}
	
	public void addCast(DaedalusCast cast) {
		DaedalusTypePair p = cast.getCastDirection();
		this.castMap.put(p, cast);
	}
	
	public DaedalusTypedValue resolveCast(DaedalusTypePair direction, DaedalusTypedValue value) {
		DaedalusCast cast = this.castMap.get(direction);
		return cast.getCastFunction().apply(value);
	}
	
	public DaedalusTypedValue resolveCast(DaedalusType start, DaedalusType end, DaedalusTypedValue value) {
		return resolveCast(new DaedalusTypePair(start, end),value);
	}
}
