package daedalusCodeComponents.generic.expression.modifiers;

import java.util.Optional;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusSlice extends DaedalusSyntaxElement {
	private Optional<DaedalusExpression> start,end,step;
	private boolean hasEnd,hasStep;
	
	public DaedalusSlice() {
		this.start = Optional.empty();
		this.end = Optional.empty();
		this.step = Optional.empty();
		
		this.hasEnd = false;
		this.hasStep = false;
	}
	
	public boolean setStart(DaedalusExpression exp) {
		if(exp!=null) {
			this.start = Optional.of(exp);
			return true;
		}
		return false;
	}
	
	public boolean setEnd(DaedalusExpression exp) {
		if(exp!=null) {
			this.end = Optional.of(exp);
			setHasEnd();
			return true;
		}
		return false;
	}
	
	public boolean setHasEnd() {
		this.hasEnd = true;
		return true;
	}
	
	public boolean setHasStep() {
		this.hasStep = true;
		return true;
	}
	
	public boolean setStep(DaedalusExpression exp) {
		if(exp!=null) {
			this.step = Optional.of(exp);
			setHasStep();
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String out = "";
		
		if(this.start!=null) {
			out = this.start.toString();
		}
		
		if(hasEnd) {
			out+=":";
			if(this.end!=null) {
				out+=this.end.toString();
			}
			
			if(hasStep) {
				out+=":";
				if(this.step!=null) {
					out+=this.step.toString();
				}
			}
		}
		return out;
	}
}
