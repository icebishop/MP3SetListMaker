package org.ice.mp3listsetmaker.setlist;

import java.util.List;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class TracksExtractor {
	
	private TracksExtractor() {
		super();
	}
		
	public static List<String> extractTracks(String data){
		
		DocumentContext jsonContext = JsonPath.parse(data);
		List<String> list = jsonContext.read("$.sets.set[0].song.*.name");
		
		list.addAll(jsonContext.read("$.sets.set[1].song.*.name"));
				
		return list;
	}

}
