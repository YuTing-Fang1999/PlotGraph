package plotgraph;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Infos {
	String schoolName;
	String department;
	int schoolNum;
	ArrayList<Info> infoarray =  new ArrayList<Info>();
	public Infos(String schoolName) {
		this.schoolName = schoolName; 
	}	
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		JSONArray memberArray = new JSONArray();
		
		for (int i = 0; i < this.infoarray.size(); i++) {	
			JSONArray planArray = new JSONArray();
			for(int j=0;j<this.infoarray.get(i).plan.size();++j) {	
				planArray.put(this.infoarray.get(i).plan.get(j));
			}			
			JSONObject memberObj = new JSONObject();
			memberObj.put("plan", planArray);
			memberObj.put("name",this.infoarray.get(i).name);	
			memberObj.put("SchoolName", this.schoolName);
			memberArray.put(memberObj);
		}
		object.put("Member", memberArray);
		object.put("SchoolName", this.schoolName);
		object.put("SchoolNum", this.schoolNum);
		object.put("Department", this.department);
		return object;
	}
	public void printInfos() {
		System.out.println(this.schoolName);
		for (int i = 0; i < this.infoarray.size(); i++) {	
			System.out.println("*"+this.infoarray.get(i).name);
			for(int j=0;j<this.infoarray.get(i).plan.size();++j) {	
				System.out.println(this.infoarray.get(i).plan.get(j));
			}			
		}
	}
}
class Info{
	String name;
	ArrayList<String> plan = new ArrayList<String>();
}
