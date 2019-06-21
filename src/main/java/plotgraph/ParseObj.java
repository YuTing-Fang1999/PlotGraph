package plotgraph;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.json.JSONArray;
import org.json.JSONObject;

import sun.nio.cs.ext.TIS_620;

public class ParseObj {
	String fileContent;
	String SchoolName;
	String SchoolNum;
	String Department;
	JSONObject obj;
	JSONObject dataObject = new JSONObject();
	JSONArray dataArray = new JSONArray();
	JSONArray linkArray = new JSONArray();
	int[] symble = new int[100];
	int n;
	JSONArray member;
	public ParseObj(JSONObject obj) {
		n = obj.getJSONArray("Member").length();
		member = obj.getJSONArray("Member");
		SchoolName = obj.get("SchoolName").toString();
		SchoolNum = obj.get("SchoolNum").toString();
		Department = obj.get("Department").toString();
		//link
		for (int i = 0; i < symble.length; i++) {
			symble[i]=35;
		}
		for (int i = 0; i < n; i++) {
			for (int j = i+1; j < n; j++) {
				JSONObject link = new JSONObject();
				String namei = member.getJSONObject(i).get("name").toString();
				String namej = member.getJSONObject(j).get("name").toString();
				int ni = member.getJSONObject(i).getJSONArray("plan").length();
				int nj = member.getJSONObject(j).getJSONArray("plan").length();
				String cString = new String();
				cString+=namei+"<->"+namej+"<br/>";
				boolean same = false;
				for (int i2 = 0; i2 < ni; i2++) {
					for (int j2 = 0; j2 < nj; j2++) {
						String plani = member.getJSONObject(i).getJSONArray("plan").get(i2).toString();
						String planj = member.getJSONObject(j).getJSONArray("plan").get(j2).toString();
						if (plani.equals(planj)) 
						{
							same = true;
							cString += plani+"<br/>";
						}
					}
				}
				if (same) {
					symble[i]+=2;
					symble[j]+=2;
					link.put("target", namei);
					link.put("source", namej);
					link.put("category", cString);
					linkArray.put(link);
				}
			}
		}
		//data
		String name = new String();
		for(int i=0;i<n;++i) {
			JSONObject node = new JSONObject();//***
			name = (String) member.getJSONObject(i).get("name");
			node.put("name", name);
			node.put("draggable", true);
			node.put("symbolSize", symble[i]);
			node.put("category", SchoolName);
			String planString = SchoolName+":"+name+"<br/>";
			for (int j = 0; j < member.getJSONObject(i).getJSONArray("plan").length(); j++) {
				planString += member.getJSONObject(i).getJSONArray("plan").get(j).toString()+"<br/>";
			}
			node.put("plan", planString);
			dataArray.put(node);
		}
		dataObject.put("dataArray", this.dataArray);
		dataObject.put("linkArray", this.linkArray);
		dataObject.put("SchoolName", this.SchoolName);
		dataObject.put("SchoolNum", this.SchoolNum);
		dataObject.put("Department", this.Department);
	}

	public void savePubFile() {
		JSONArray categories = new JSONArray();
		JSONObject name = new JSONObject();
		name.put("name", SchoolName);
		categories.put(name);
		dataObject.put("categories", categories);
		
		FileWriter fw;
		try {
			fw = new FileWriter("C:\\Users\\s830s\\eclipse-workspace\\plotgraph\\planData\\Pub" + SchoolNum + ".txt");
			fw.write(dataObject.toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void savePriFile() {
		JSONArray categories = new JSONArray();
		JSONObject name = new JSONObject();
		name.put("name", SchoolName);
		categories.put(name);
		dataObject.put("categories", categories);
		
		FileWriter fw;
		try {
			fw = new FileWriter("C:\\Users\\s830s\\eclipse-workspace\\plotgraph\\planData\\Pri" + SchoolNum + ".txt");
			fw.write(dataObject.toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
