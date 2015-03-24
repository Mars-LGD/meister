package com.xiaotian.extend;

import org.xiaotian.json.JSONException;
import org.xiaotian.json.JSONObject;


public class JSONTest {
	
	public static void main(String[] args){
		JSONObject jsonObj=new JSONObject();
		try {
			jsonObj.put("1", "hello");
			String str=jsonObj.toString();
			System.out.println(new JSONObject(str));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
