package daedalusCodeComponents.statements.loop;

import java.util.Optional;

import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.statements.DaedalusStatement;

public abstract class DaedalusLoop extends DaedalusStatement{
	private Optional<DaedalusName> label;
	private DaedalusStatement body;
	
	public DaedalusLoop(DaedalusStatement code) {
		this.label = Optional.empty();
		this.body = code;
	}
	
	public boolean setLabel(DaedalusName lab) {
		if(lab!=null) {
			this.label = Optional.of(lab);
		}
		return true;
	}
	
	@Override
	public String toString() {
		String out="";
		if(this.label.isPresent()) {
			out = " "+this.label.get().toString()+":";
		}
		return super.toString() + out;
	}
}
