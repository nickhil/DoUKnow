package com.mrjellybean.douknow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import android.util.Log;

public class HttpCon 
{
	 static JSONArray jArray;
	 static String result = null;
     static InputStream is = null;
	 static StringBuilder sb=null;	
	 int flag=0;	
	 public static final String LOG_TAG="HI..";
	 public static String getConnection(final String url,ArrayList<NameValuePair> nameValuePairs)
	 {	 
  	      //http post
  	      try
  	      {  
  	    	    HttpClient httpclient = new DefaultHttpClient();      	    	        	     	        	    	    	       	    	        	   	    	        	 
  	    	    HttpPost httppost = new HttpPost(url);    	    
  	    	    	    	    
  	    	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  	    	    HttpResponse response = httpclient.execute(httppost);  	    	        
  	    	    HttpEntity entity = response.getEntity();
  	    	    is = entity.getContent();  	    	   
  	       }
  	      catch(Exception e)
  	      { Log.e("log_tag", "Error in http connection"+e.toString()); }
  	      //convert response to string
  	      try
  	      {
  	    	  BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
  	    	  sb = new StringBuilder();
  	    	  sb.append(reader.readLine() + "\n");
  	    	  String line="0";
  	    	  while ((line = reader.readLine()) != null)
  	    	  {   sb.append(line + "\n"); }
  	    	 is.close();
  	    	 result=sb.toString();
  	    	 }catch(Exception e)
  	    	{ Log.e("log_tag", "Error converting result "+e.toString()); } 	   	 
  	      
  	   
  	    	return (result);
     }
}