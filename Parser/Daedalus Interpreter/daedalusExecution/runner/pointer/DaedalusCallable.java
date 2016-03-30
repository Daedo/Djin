package daedalusExecution.runner.pointer;

import daedalusExecution.intermediateObjects.DaedalusIntermediateValue;

public interface DaedalusCallable {
	public DaedalusIntermediateValue resolve(DaedalusIntermediateValue... daedalusIntermediateValues);
}
