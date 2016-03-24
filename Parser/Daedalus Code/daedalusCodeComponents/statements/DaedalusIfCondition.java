package daedalusCodeComponents.statements;

import java.util.Optional;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusIfCondition extends DaedalusStatement{
	Optional<DaedalusExpression> conditional;
	DaedalusStatement body;
	Optional<DaedalusIfCondition> onFail;

	public DaedalusIfCondition(DaedalusStatement block) {
		this.body = block;
		this.conditional = Optional.empty();
		this.onFail = Optional.empty();
	}

	public DaedalusIfCondition(DaedalusExpression cond,DaedalusStatement block) {
		this(block);
		this.conditional = Optional.of(cond);
	}

	public DaedalusIfCondition(DaedalusExpression cond,DaedalusStatement block,DaedalusIfCondition fail) {
		this(cond,block);

		if(fail!=null) {
			this.onFail = Optional.of(fail);
		}
	}

	public boolean setOnFail(DaedalusIfCondition fail) {
		if(fail!=null) {
			this.onFail = Optional.of(fail);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String e = "";

		if(this.conditional.isPresent()) {
			e = "if ("+this.conditional.get()+ ")";
			
			if(this.onFail.isPresent()) {
				e+="\n\t"+this.onFail.get().toString();
			}
		} else {
			e = "else";
		}
		return super.toString()+" "+e;
	}
}
