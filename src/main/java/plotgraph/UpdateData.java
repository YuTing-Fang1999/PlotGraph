package plotgraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

public class UpdateData{
	String fileName;
	public UpdateData(String fileName) {
		this.fileName=fileName;
	}
	public void update() {
		//關掉警告
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	    
    	String category = fileName.replaceAll("[^(a-zA-Z)]","" );
    	int number = Integer.parseInt(fileName.replaceAll("[^(0-9)]", ""));
    	Infos infos;
    	JSONObject obj = new JSONObject();
	    if (category.equals("Pub")) {
			infos = new SearchPlan().crawlPubInfos(number);
			obj = infos.toJson();
			ParseObj pObj = new ParseObj(obj);
			pObj.savePubFile();
		}else {
			infos = new SearchPlan().crawlPriInfos(number);
			obj = infos.toJson();
			ParseObj pObj = new ParseObj(obj);
			pObj.savePriFile();
		}
	}
}
