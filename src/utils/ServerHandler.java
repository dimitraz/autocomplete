package utils;

import java.io.IOException;
import com.google.gson.Gson;

import controllers.QuickAutocomplete;

public class ServerHandler {
	static QuickAutocomplete auto;
	
	public ServerHandler() throws IOException {
	}
	
	/**
	 * 
	 * @param id index of chosen method
	 * @param prefix prefix to search
	 * @param matches number of matches
	 * @return returnValue Json string of matching term(s)
	 * @throws IOException
	 */
	public static String doAutocomplete(int id, String prefix, int matches) throws IOException {
		auto = new QuickAutocomplete(); 
		auto.parseDataFile("data/wiktionary.txt");
		
		// Return json string of option chosen
		String returnValue = "";
		// Create a new Gson object
		Gson obj = new Gson();	
		switch (id) {		
			case 0:			
				returnValue = obj.toJson(auto.matches(prefix)); 
				break;
			case 1:
				returnValue = obj.toJson(auto.matches(prefix, matches));  
				break;
			case 2:
				returnValue = obj.toJson(auto.bestMatch(prefix));  
				break;
			case 3:
				returnValue = obj.toJson(auto.weightOf(prefix));
				break;
			default:
				System.out.println("Invalid option selected.");
				break;
		}
		
		// Return the Json string
		return returnValue;		
	}
}
