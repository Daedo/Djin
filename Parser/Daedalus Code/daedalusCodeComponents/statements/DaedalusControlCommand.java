package daedalusCodeComponents.statements;

import java.util.Optional;

import daedalusCodeComponents.generic.DaedalusName;

public class DaedalusControlCommand extends DaedalusStatement{
	private DaedalusControlLiteral type;
	private Optional<DaedalusName> label;
	
	public DaedalusControlCommand(DaedalusControlLiteral lit) {
		this.type = lit;
		this.label = Optional.empty();
	}
	
	public boolean setLabel(DaedalusName lab) {
		if(lab!=null) {
			this.label = Optional.of(lab);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String out = "";
		if(this.label.isPresent()) {
			out = this.label.get().toString();
		}
		
		return super.toString() + this.type+" "+out;
	}
}
