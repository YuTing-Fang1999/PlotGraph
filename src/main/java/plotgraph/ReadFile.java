package plotgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReadFile {
	String fileContent;
	JSONObject obj = new JSONObject();
//	int n;
	public ReadFile(String fileName) {
		try { 
			File f = new File("C:\\Users\\s830s\\eclipse-workspace\\plotgraph\\planData\\"+fileName); 
			if(f.isFile() && f.exists()){ 
				InputStreamReader read = new InputStreamReader(new FileInputStream(f)); 
				BufferedReader reader=new BufferedReader(read); 
				String line; 
				while ((line = reader.readLine()) != null) { 
					fileContent += line; 
				} 
				read.close(); 
				
				if (f!=null) {
					obj = new JSONObject(fileContent.substring(fileContent.indexOf('{')));//***
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
//	public int getMemberNum() {
//		return n;
//	}
	public JSONArray getData() {
		return obj.getJSONArray("dataArray");
	}
	public JSONArray getLink() {
		return obj.getJSONArray("linkArray");
	}
	public JSONArray getCategories() {
		return obj.getJSONArray("categories");
	}
	public int getSchoolNum() {
		return Integer.parseInt(obj.getString("SchoolNum"));
	}
	public String getSchoolName() {
		return obj.getString("SchoolName");
	}
	public String getDepartment() {
		return obj.getString("Department");
	}
}
