package plotgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReadAllFile {
	String fileContent;
	JSONObject obj = new JSONObject();
	public ReadAllFile() {
		try { 
			File f = new File("C:\\Users\\s830s\\eclipse-workspace\\plotgraph\\planData\\All.txt"); 
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
	public ArrayList<String> getAllPubSchoolName() {
		ArrayList<String> allPubSchoolName = new ArrayList<String>();
		JSONArray array = obj.getJSONArray("allPubSchoolName");
		for(int i=0;i<array.length();++i)
			allPubSchoolName.add(array.getString(i));
		return allPubSchoolName;
	}
	public ArrayList<String> getAllPubSchoolNum() {
		ArrayList<String> allPubSchoolNum = new ArrayList<String>();
		JSONArray array = obj.getJSONArray("allPubSchoolNum");
		for(int i=0;i<array.length();++i)
			allPubSchoolNum.add(Integer.toString(array.getInt(i)));
		return allPubSchoolNum;
	}
	public ArrayList<String> getAllPriSchoolName() {
		ArrayList<String> allPriSchoolName = new ArrayList<String>();
		JSONArray array = obj.getJSONArray("allPriSchoolName");
		for(int i=0;i<array.length();++i)
			allPriSchoolName.add(array.getString(i));
		return allPriSchoolName;
	}
	public ArrayList<String> getAllPriSchoolNum() {
		ArrayList<String> allPriSchoolNum = new ArrayList<String>();
		JSONArray array = obj.getJSONArray("allPriSchoolNum");
		for(int i=0;i<array.length();++i)
			allPriSchoolNum.add(Integer.toString(array.getInt(i)));
		return allPriSchoolNum;
	}
	public JSONArray getAllLink() {
		return obj.getJSONArray("allLinkArray");
	}
	public JSONArray getAllCate() {
		return obj.getJSONArray("allCateArray");
	}
	public JSONArray getAllData() {
		return obj.getJSONArray("allDataArray");
	}
}
