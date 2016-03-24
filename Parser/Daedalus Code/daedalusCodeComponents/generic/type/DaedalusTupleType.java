package daedalusCodeComponents.generic.type;

public class DaedalusTupleType extends DaedalusType {
	private DaedalusTypePattern subTypes;
	private boolean hasEndArray;
	
	public DaedalusTupleType(DaedalusTypePattern sub,boolean endArray) {
		this.subTypes = sub;
		this.hasEndArray = endArray;
	}
	
	@Override
	public String toString() {
		return super.toString()+ " tup("+this.subTypes.toString()+")";
	}
}
