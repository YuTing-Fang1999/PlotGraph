package plotgraph;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParseAllFile {
	SearchPlan sPlan  =  new SearchPlan();
	int pubN = sPlan.pubSchooNum;
	int priN = sPlan.priSchooNum;
	JSONArray allLinkArray = new JSONArray();
	JSONArray allDataArray = new JSONArray();
	JSONArray allCateArray = new JSONArray();
	JSONArray allMember = new JSONArray();
	ArrayList<String> allPubSchoolName =  new ArrayList<String>();
	ArrayList<String> allPubSchoolNum =  new ArrayList<String>();
	ArrayList<String> allPriSchoolName =  new ArrayList<String>();
	ArrayList<String> allPriSchoolNum =  new ArrayList<String>();
	public ParseAllFile() {
		for (int i = 1; i < pubN; i++) {
	    	parseFile file = new parseFile("Pub"+i+".txt");//***
	    	if (file.fileContent!=null) {
				allPubSchoolName.add(file.SchoolName);
				allPubSchoolNum.add(file.SchoolNum);
				mergeArray(allCateArray, file.getCategories());
				mergeArray(allMember, file.member);
			}
	    	

		}
		
		for (int i = 0; i < priN; i++) {
			parseFile file = new parseFile("Pri"+i+".txt");//***
			if (file.fileContent!=null) {
				allPriSchoolName.add(file.SchoolName);
				allPriSchoolNum.add(file.SchoolNum);
				mergeArray(allCateArray, file.getCategories());
				mergeArray(allMember, file.member);
			}
		}
//		toLink();
	}
	
//	public JSONArray getAllMember() {
//		return allMember;
//	}
	public ArrayList<String> getAllPubSchoolName() {
		return allPubSchoolName;
	}
	public ArrayList<String> getAllPubSchoolNum() {
		return allPubSchoolNum;
	}
	public ArrayList<String> getAllPriSchoolName() {
		return allPriSchoolName;
	}
	public ArrayList<String> getAllPriSchoolNum() {
		return allPriSchoolNum;
	}
	public JSONArray getAllLink() {
		toLink();
		return allLinkArray;
	}
	public JSONArray getAllCate() {
		return allCateArray;
	}
	public JSONArray getAllData() {
		return allDataArray;
	}
	
	public void mergeArray(JSONArray allArray, JSONArray array) {
		for (int i1 = 0; i1 < array.length(); i1++) {
			allArray.put(array.getJSONObject(i1));
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
//			for (int a = 0; a < Member.getJSONArray("plan").length(); a++) {
//				planString += Member.getJSONArray("plan").get(a).toString()+"<br/>";
//			}
			node.put("plan", planString);
			allDataArray.put(node);
	}
}
