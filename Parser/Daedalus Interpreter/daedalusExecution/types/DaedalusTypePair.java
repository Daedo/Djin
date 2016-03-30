package daedalusExecution.types;

import daedalusCodeComponents.generic.type.DaedalusType;

public class DaedalusTypePair {
	public DaedalusType startType;
	public DaedalusType endType;

	public DaedalusTypePair(DaedalusType start, DaedalusType end) {
		this.startType  = start;
		this.endType = end;
	}
}