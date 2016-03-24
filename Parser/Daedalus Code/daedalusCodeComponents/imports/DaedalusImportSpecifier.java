package daedalusCodeComponents.imports;

import java.util.Optional;
import java.util.Vector;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.DaedalusName;

public class DaedalusImportSpecifier extends DaedalusSyntaxElement {
	Vector<DaedalusName> components;
	Vector<Optional<DaedalusName>> renames;
	
	public DaedalusImportSpecifier() {
		this.components = new Vector<>();
		this.renames = new Vector<>();
	}
	
	public boolean addComponent(DaedalusName name) {
		if(name==null) {
			return false;
		}
		
		this.components.addElement(name);
		this.renames.addElement(Optional.empty());
		return true;
	}
	
	public boolean addComponent(DaedalusName name,DaedalusName rename) {
		if(rename==null) {
			return addComponent(name);
		}
		
		if(name==null) {
			return false;
		}
		
		this.components.addElement(name);
		this.renames.addElement(Optional.of(rename));
		return true;
	}
	
	@Override
	public String toString() {
		String spec = "{";
		for(int i=0;i<this.components.size();i++) {
			if(i!=0) {
				spec+=", ";
			}
			Optional<DaedalusName> rename = this.renames.get(i);
			
			spec+=this.components.get(i).getValue()+ (rename.isPresent()? " - "+rename.get().getValue():"");
			
		}
		spec+="}";
		
		return "import only "+spec;
	}
}
