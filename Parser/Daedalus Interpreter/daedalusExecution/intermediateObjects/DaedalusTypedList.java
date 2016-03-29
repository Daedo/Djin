package daedalusExecution.intermediateObjects;

import java.util.Vector;

import daedalusCodeComponents.generic.type.DaedalusTypeLiteral;

public class DaedalusTypedList extends DaedalusTypedValue {
	private DaedalusTypedList(DaedalusTypeLiteral type) {
		super(type,new Vector<DaedalusTypedValue>());
	}
}
