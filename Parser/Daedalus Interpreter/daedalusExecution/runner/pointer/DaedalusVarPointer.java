package daedalusExecution.runner.pointer;

import java.util.Optional;

import daedalusCodeComponents.generic.type.DaedalusType;

public class DaedalusVarPointer {
	private DaedalusType outType;
	private Optional<DaedalusCallable> value;
	
	public DaedalusVarPointer(DaedalusType type) {
		this.outType = type;
		this.value = Optional.empty();
	}
	
	public void setValue(DaedalusCallable val) {
		if(val==null) {
			this.value = Optional.empty();
		} else {
			this.value = Optional.of(val);
		}
	}
	
	public DaedalusType getOutType() {
		return this.outType;
	}
}
