package support;

import java.util.List;

public class Tools {
	public static String list(String start,String end,String sep,List<?> list) {
		String out = start;
		
		for(int i=0;i<list.size();i++) {
			if(i!=0) {
				out+=sep;
			}
			out+=list.get(i);
		}
		
		out+=end;
		return out;
	}
	
	public static String list(String start,String end,List<?> list) {
		return list(start,end,", ",list);
	}
	
	public static String list(List<?> list) {
		return list("","",", ",list);
	}
}
