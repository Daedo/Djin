package daedalusExecution.types;

import daedalusCodeComponents.generic.type.DaedalusType;

public class TypePair {
	public DaedalusType startType;
	public DaedalusType endType;

	public TypePair(DaedalusType start, DaedalusType end) {
		this.startType  = start;
		this.endType = end;
	}
}