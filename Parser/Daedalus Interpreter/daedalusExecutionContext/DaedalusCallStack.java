package daedalusExecutionContext;

import java.util.Vector;

public class DaedalusCallStack {
	private Vector<DaedalusCallLayer> callLayers;
	
	public DaedalusCallStack() {
		this.callLayers = new Vector<>();
	}
	
	public void addCallLayer(DaedalusCallLayer layer) {
		this.callLayers.add(layer);
	}
	
	public void popCallLayer() {
		this.callLayers.remove(this.callLayers.size()-1);
	}
	
	public void serach() {
		for(int i=0;i<this.callLayers.size();i++) {
			int searchIndex = this.callLayers.size()-1-i;
			DaedalusCallLayer currentLayer = this.callLayers.get(searchIndex);
			// Search Layer
		}
	}
}
