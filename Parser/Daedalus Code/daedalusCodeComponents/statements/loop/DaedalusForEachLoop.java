package daedalusCodeComponents.statements.loop;

import java.util.Optional;

import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.generic.type.DaedalusTypePattern;
import daedalusCodeComponents.statements.DaedalusStatement;

public class DaedalusForEachLoop extends DaedalusLoop {

	private boolean isForAll;
	private DaedalusExpression container;
	
	private DaedalusTypePattern pattern;
	private DaedalusName subName;
	
	private Optional<DaedalusName> indexName;
	
	public DaedalusForEachLoop(DaedalusStatement code) {
		super(code);
		this.isForAll = false;
	}
	
	public boolean setForAll() {
		this.isForAll = true;
		return true;
	}
	
	public boolean setContainer(DaedalusExpression expr) {
		this.container = expr;
		return true;
	}

	public boolean setSubpattern(DaedalusTypePattern type,DaedalusName name) {
		this.pattern = type;
		this.subName = name;
		return true;
	}
	
	public boolean setIndexName(DaedalusName index) {
		if(index!=null) {
			this.indexName = Optional.of(index);
		}
		return true;
	}
}
