package daedalusExecution.runner.pointer;

import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusCodeComponents.generic.type.DaedalusTypePattern;

public class DaedalusMethodPointer extends DaedalusVarPointer {
	public DaedalusMethodPointer(DaedalusType type) {
		super(type);
	}

	public DaedalusTypePattern inType;
}
