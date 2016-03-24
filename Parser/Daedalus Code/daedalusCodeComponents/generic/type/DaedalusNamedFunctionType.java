package daedalusCodeComponents.generic.type;

import java.util.Vector;

import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusNamedFunctionType extends DaedalusUnnamedFunctionType {

	private Vector<DaedalusName> inputNames;
	private Vector<DaedalusExpression> konsPrad;
	
	public DaedalusNamedFunctionType(DaedalusType ret, DaedalusTypePattern inp) {
		super(ret, inp);
		this.inputNames = new Vector<>();
		this.konsPrad = new Vector<>();
	}

}
