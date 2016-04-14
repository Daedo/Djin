package daedalusCodeComponents.statements;

import java.util.Vector;

import daedalusExecution.exception.DaedalusException;
import daedalusExecution.intermediateObjects.DaedalusIntermediateValue;
import daedalusExecution.runner.DaedalusExecutionLayer;
import daedalusExecution.runner.DaedalusRunner;

public class DaedalusBlock extends DaedalusStatement {
	private Vector<DaedalusStatement> lines;
	
	public DaedalusBlock() {
		this.lines = new Vector<>();
	}
	
	public boolean addStatement(DaedalusStatement statement){
		this.lines.add(statement);
		return true;
	}
	
	@Override
	public DaedalusIntermediateValue resolve() throws DaedalusException {
		DaedalusRunner.executionContext.pushLayer(new DaedalusExecutionLayer());
		for(DaedalusStatement s:this.lines) {
			s.resolve();
			//TODO Pass on Return
		}
		DaedalusRunner.executionContext.popLayer();
		return super.resolve();
	}
}
