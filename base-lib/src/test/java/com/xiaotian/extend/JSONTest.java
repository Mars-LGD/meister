package com.xiaotian.extend;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


public class JSONTest {
	
	public static void main(String[] args){
		JSONObject jsonObj=new JSONObject();
		try {
			jsonObj.put("1", "hello");
			String str=jsonObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
