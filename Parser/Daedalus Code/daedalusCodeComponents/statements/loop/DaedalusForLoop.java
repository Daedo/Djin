package daedalusCodeComponents.statements.loop;

import java.util.Optional;

import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.statements.DaedalusStatement;

public class DaedalusForLoop extends DaedalusLoop{
	Optional<DaedalusStatement> init,update;
	Optional<DaedalusExpression> cond;
	
	public DaedalusForLoop(DaedalusStatement code) {
		super(code);
		this.init = Optional.empty();
		this.update = Optional.empty();
		this.cond = Optional.empty();
	}
	
	public boolean setInit(DaedalusStatement st) {
		if(st!=null) {
			this.init = Optional.of(st);
		}
		return true;
	}
	
	public boolean setConditional(DaedalusExpression expr) {
		if(expr!=null) {
			this.cond = Optional.of(expr);
		}
		return true;
	}
	
	public boolean setUpdate(DaedalusStatement st) {
		if(st!=null) {
			this.update = Optional.of(st);
		}
		return true;
	}
	
	@Override
	public String toString() {
		String o1 = "";
		String o2 = "";
		String o3 = "";
		if(this.init.isPresent()) {
			o1 = this.init.get().toString();
		}
		
		if(this.cond.isPresent()) {
			o2 = this.cond.get().toString();
		}
		
		if(this.update.isPresent()) {
			o3 = this.update.get().toString();
		}
		return super.toString()+" FOR ("+o1+";"+o2+";"+o3+")";
	}
}
