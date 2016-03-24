package daedalusCodeComponents.imports;

import java.util.Optional;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.DaedalusName;

public class DaedalusImport extends DaedalusSyntaxElement {
	private String importPackage;
	private Optional<DaedalusName> rename;
	private Optional<DaedalusImportSpecifier> specifier;
	
	public DaedalusImport(String pack, DaedalusName ren,DaedalusImportSpecifier spec) {
		this.importPackage = pack;
		if(ren==null) {
			this.rename = Optional.empty();
		} else {
			this.rename = Optional.of(ren);
		}
		
		if(spec==null) {
			this.specifier = Optional.empty();
		} else {
			this.specifier = Optional.of(spec);
		}
	}
	
	@Override
	public String toString() {
		String out =  super.toString()+" import \""+this.importPackage+"\"";
		if(this.rename.isPresent()) {
			out+= " as "+this.rename.get().getValue();
		}
		if(this.specifier.isPresent()) {
			out+= " spec: "+this.specifier.get().toString();
		}
		
		
		return out;
	}
	
	
}
