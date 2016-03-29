package daedalusExecution.types;

import java.util.HashMap;
import java.util.Map;

import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusExecution.intermediateObjects.DaedalusTypedValue;

public class CastResolver {
	private Map<TypePair,Cast> castMap;
	
	public CastResolver() {
		this.castMap = new HashMap<>();
	}
	
	public void addCast(Cast cast) {
		TypePair p = cast.getCastDirection();
		this.castMap.put(p, cast);
	}
	
	public DaedalusTypedValue resolveCast(TypePair direction, DaedalusTypedValue value) {
		Cast cast = this.castMap.get(direction);
		return cast.getCastFunction().apply(value);
	}
	
	public DaedalusTypedValue resolveCast(DaedalusType start, DaedalusType end, DaedalusTypedValue value) {
		return resolveCast(new TypePair(start, end),value);
	}
}
