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

	public static String getSurrindingLinesWithNumbers(String data,int sInd,int eInd) {
		int begin = sInd;
		int end = eInd;
		
		while(begin>0 && data.charAt(begin)=='\n') {
			begin--;
		}
		
		while(end<(data.length()-1) && data.charAt(end)=='\n') {
			end++;
		}
		
		int count = 2;
		while(begin>0 && count>0) {
			
			if(data.charAt(begin)=='\n') {
				count--;
				
				while(begin>0 && data.charAt(begin)=='\n') {
					begin--;
				}
			} else {
				begin--;
			}
			
		}
		begin++;
		
		count = 1;
		while(end<(data.length()-1) && count>0) {
			
			if(data.charAt(end)=='\n') {
				count--;
				while(end<(data.length()-1) && data.charAt(end)=='\n') {
					end++;
				}
			} else {
				end++;
			}
		}
		
		while(data.charAt(begin)=='\n') {
			begin++;
		}
		
		while(data.charAt(end)=='\n') {
			end--;
		}
		
		int lineNumber = 1;
		
		for(int i=0;i<begin;i++) {
			if(data.charAt(i)=='\n') {
				lineNumber++;
			}
		}
		
		String[] lines = data.substring(begin,end).trim().split("\n");
		
		String out = "";
		
		for(int i=0;i<lines.length;i++) {
			out+= (lineNumber+i)+"\t"+lines[i]+"\n";
		}
		
		return out.trim();
	}
}
