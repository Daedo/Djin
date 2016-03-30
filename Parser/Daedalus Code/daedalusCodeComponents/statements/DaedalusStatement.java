package daedalusCodeComponents.statements;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusExecution.exception.DaedalusException;
import daedalusExecution.intermediateObjects.DaedalusIntermediateValue;

public abstract class DaedalusStatement extends DaedalusSyntaxElement {
		@Override
		public String toString() {
			return "> Statement";
		}
		
		public DaedalusIntermediateValue resolve() throws DaedalusException {
			throw new DaedalusException("Not jet implemented in "+this.getClass().toString());
		}
}
