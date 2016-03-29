package daedalusExecution.intermediateObjects;

import java.util.Optional;

public class DaedalusReturnCommand extends DaedalusIntermediateValue{
	private Optional<DaedalusTypedValue> value;
	
	public DaedalusReturnCommand() {
		super(DaedalusReturnLiteral.RETURN);
		this.value = Optional.empty();
	}
	
	public DaedalusReturnCommand(DaedalusTypedValue val) {
		this();
		if(val!=null) {
			this.value = Optional.of(val);
		}
	}

}
