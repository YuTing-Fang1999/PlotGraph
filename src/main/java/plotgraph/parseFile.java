package plotgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class parseFile {
	String fileContent;
	String SchoolName;
	String SchoolNum;
	JSONObject obj;
	JSONArray dataArray = new JSONArray();
	JSONArray linkArray = new JSONArray();
	int[] symble = new int[100];
	int n;
	JSONArray member;
	public parseFile(String fileName) {
		try { 
			File f = new File("C:\\Users\\s830s\\eclipse-workspace\\CrawlData\\planData\\"+fileName); 
			if(f.isFile() && f.exists()){ 
				InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8"); 
				BufferedReader reader=new BufferedReader(read); 
				String line; 
				while ((line = reader.readLine()) != null) { 
					fileContent += line; 
				} 
				read.close(); 
				
				if (f!=null) {
					obj = new JSONObject(fileContent.substring(fileContent.indexOf('{')));//***
					n = obj.getJSONArray("Member").length();
					member = obj.getJSONArray("Member");
					SchoolName = obj.get("SchoolName").toString();
					SchoolNum = obj.get("SchoolNum").toString();
					
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
					
				}
			} 
		} catch (Exception e) { 
			System.out.println("讀取檔案內容操作出錯"); 
			e.printStackTrace(); 
		} 
	}
	public String getFile() {
		return fileContent;
	}
	public JSONObject toJSONobj() {
		return obj;
	}
	public int getMemberNum() {
		return n;
	}
	public JSONArray getData() {
//		System.out.println(dataArray);
		return dataArray;
	}
	public JSONArray getLink() {
		return linkArray;
	}
	public JSONArray getCategories() {
		JSONArray categories = new JSONArray();
		JSONObject name = new JSONObject();
		name.put("name", SchoolName);
		categories.put(name);
		return categories;
	}
	public int getSchoolNum() {
		return Integer.parseInt(SchoolNum);
	}
}
