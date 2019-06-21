package plotgraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class UpdateAllData {
	JSONArray allLinkArray = new JSONArray();
	JSONArray allDataArray = new JSONArray();
	JSONArray allCateArray = new JSONArray();
	JSONArray allMember = new JSONArray();
	ArrayList<String> allPubSchoolName =  new ArrayList<String>();
	ArrayList<Integer> allPubSchoolNum =  new ArrayList<Integer>();
	ArrayList<String> allPriSchoolName =  new ArrayList<String>();
	ArrayList<Integer> allPriSchoolNum =  new ArrayList<Integer>();
	public void updateAll() {
		//關掉警告
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
			    
		SearchPlan searchPlan = new SearchPlan();
		for(int s=1;s<searchPlan.pubSchooNum;++s) {
	    	Infos infos = new SearchPlan().crawlPubInfos(s);
	    	if(infos!=null) {
	    		JSONObject obj = new JSONObject();
		    	obj = infos.toJson();
				ParseObj pObj = new ParseObj(obj);
				pObj.savePubFile();
				
				allPubSchoolName.add(obj.getString("SchoolName"));
				allPubSchoolNum.add(obj.getInt("SchoolNum"));
				mergeArray(allCateArray, new JSONArray().put(new JSONObject().put("name", obj.getString("SchoolName"))));
				mergeArray(allMember, obj.getJSONArray("Member"));
	    	}
	    }
	    
	    for(int s=1;s<searchPlan.priSchooNum;++s) {
	    	Infos infos = new SearchPlan().crawlPriInfos(s);
	    	if(infos!=null) {
	    		JSONObject obj = new JSONObject();
		    	obj = infos.toJson();
				ParseObj pObj = new ParseObj(obj);
				pObj.savePriFile();
				
				allPriSchoolName.add(obj.getString("SchoolName"));
				allPriSchoolNum.add(obj.getInt("SchoolNum"));
				mergeArray(allCateArray, new JSONArray().put(new JSONObject().put("name", obj.getString("SchoolName"))));
				mergeArray(allMember, obj.getJSONArray("Member"));
	    	}
	    }
	}
	public void parseAndSavaAll() {
		toLink();
		JSONObject allObject = new JSONObject();
		allObject.put("allPubSchoolName", allPubSchoolName);
		allObject.put("allPubSchoolNum", allPubSchoolNum);
		allObject.put("allPriSchoolName", allPriSchoolName);
		allObject.put("allPriSchoolNum", allPriSchoolNum);
		allObject.put("allLinkArray", allLinkArray);
		allObject.put("allCateArray", allCateArray);
		allObject.put("allDataArray", allDataArray);
		
		//save
		FileWriter fw;
		try {
			fw = new FileWriter("C:\\Users\\s830s\\eclipse-workspace\\plotgraph\\planData\\All.txt");
			fw.write(allObject.toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void toLink() {
		//link
		for (int i = 0; i < allMember.length(); i++) {
			boolean addNode = false;
			boolean sameSchoolName = true;
			String coworkString = new String();
			for (int j = 0; j < allMember.length(); j++) {
				if(i==j) continue;
				boolean samePlan = false;
				JSONObject link = new JSONObject();
				String namei = allMember.getJSONObject(i).get("name").toString();
				String namej = allMember.getJSONObject(j).get("name").toString();
				String SchoolNamei = allMember.getJSONObject(i).get("SchoolName").toString();
				String SchoolNamej = allMember.getJSONObject(j).get("SchoolName").toString();
				int ni = allMember.getJSONObject(i).getJSONArray("plan").length();
				int nj = allMember.getJSONObject(j).getJSONArray("plan").length();
				String cString = new String();
				for (int i2 = 0; i2 < ni; i2++) {
					for (int j2 = 0; j2 < nj; j2++) {
						String plani = allMember.getJSONObject(i).getJSONArray("plan").get(i2).toString();
						String planj = allMember.getJSONObject(j).getJSONArray("plan").get(j2).toString();
						
						if (plani.equals(planj)) 
						{
							samePlan = true;
							addNode = true;
							cString += "---"+plani+"<br/>";
							if (!SchoolNamei.equals(SchoolNamej)) {
								sameSchoolName = false;
							}
						}
					}
				}
				if (samePlan) {
					link.put("target", SchoolNamei+namei);
					link.put("source", SchoolNamej+namej);
					link.put("category", SchoolNamei+"-"+namei+"<->"+SchoolNamej+"-"+namej+
					"<br/>"+cString);
					coworkString+=SchoolNamej+"-"+namej+"<br/>";
					if(i<j) {
						allLinkArray.put(link);
					}
				}
			}
			if (addNode) {
				if (!sameSchoolName){
					NewData(coworkString,allMember.getJSONObject(i));
				}
				
			}
		}
	}
	
	public void NewData(String coworkString, JSONObject Member) {
		String name = new String();
			JSONObject node = new JSONObject();//***
			name = (String) Member.get("name");
			node.put("name", name);
			node.put("id", Member.get("SchoolName")+name);
			node.put("draggable", true);
			node.put("symbolSize", 30);
			node.put("category", Member.get("SchoolName"));
			String planString;
			planString = Member.get("SchoolName")+":"+name+"<br/>";
			planString+="連結成員有:"+"<br/>";
			planString+=coworkString;

			node.put("plan", planString);
			allDataArray.put(node);
	}
	
	public void mergeArray(JSONArray allArray, JSONArray array) {
		for (int i1 = 0; i1 < array.length(); i1++) {
			allArray.put(array.getJSONObject(i1));
		}
	}
}
