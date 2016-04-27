package com.hope.kqt.android.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.hope.kqt.android.Application;

import android.util.Log;



/**
 * 网络请求工具类
 * @author aaronwang
 *
 */
public class WebUtils 
{
	public static String get(String path) throws Exception
	{
		Log.i("get", path);
		HttpClient httpclient = HttpClientHelper.getHttpClient();

		HttpGet httpget = new HttpGet(Application.SERVER_IP+path);
		
		httpget.setHeader("User-Agent", "Android");
		
		HttpResponse httpresponse = httpclient.execute(httpget);
		int statuscode = httpresponse.getStatusLine().getStatusCode();

		if(statuscode == 200)
			return getRet(httpresponse);

		Log.i("statuscode", statuscode+"");
		
		return null;
	}
	
	public static InputStream getInputStream(String path) throws Exception
	{
		Log.i("get", path);
		HttpClient httpclient = HttpClientHelper.getHttpClient();

		HttpGet httpget = new HttpGet(Application.SERVER_IP+path);
		HttpResponse httpresponse = httpclient.execute(httpget);
		int statuscode = httpresponse.getStatusLine().getStatusCode();

		if(statuscode == 200)
			return httpresponse.getEntity().getContent();
		
		return null;
	}
	
	public static InputStream getInputStreamByUrl(String url) throws Exception
	{
		Log.i("get", url);
		HttpClient httpclient = HttpClientHelper.getHttpClient();

		HttpGet httpget = new HttpGet(url);
		HttpResponse httpresponse = httpclient.execute(httpget);
		int statuscode = httpresponse.getStatusLine().getStatusCode();

		if(statuscode == 200)
			return httpresponse.getEntity().getContent();
		
		return null;
	}
	
	public static String post(String path,Map<String,String> data) throws Exception
	{
		Log.i("post", path);
		HttpClient httpclient = HttpClientHelper.getHttpClient();

		HttpPost httppost = new HttpPost(Application.SERVER_IP+path);
		
		httppost.setHeader("User-Agent", "Android");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		for(String key:data.keySet())
			nvps.add(new BasicNameValuePair(key,data.get(key)));
		
		httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse httpresponse = httpclient.execute(httppost);
		int statuscode = httpresponse.getStatusLine().getStatusCode();
		
		if(statuscode == 200)
			return getRet(httpresponse);
		
		Log.i("statuscode", statuscode+"");
		
		return null;
	}
	
	public static String post(String path,List<String> files,Map<String,String> data) throws Exception
	{
		HttpClient httpclient = HttpClientHelper.getHttpClient();
		HttpPost httppost = new HttpPost(Application.SERVER_IP+path);
		
		httppost.setHeader("User-Agent", "Android");
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		for(String filepath:files)
		{
			FileBody body = new FileBody(new File(filepath));
			builder.addPart("img_file", body);
		}
		
		for(String key:data.keySet())
			builder.addTextBody(key,data.get(key));

		//builder.addTextBody("img_title", KQTUtils.parseByte2HexStr("呵呵".getBytes()));
		
		
		httppost.setEntity(builder.build());
		
		HttpResponse httpresponse = httpclient.execute(httppost);
		int statuscode = httpresponse.getStatusLine().getStatusCode();
		
		if(statuscode == HttpStatus.SC_OK)
		{
			String r = EntityUtils.toString(httpresponse.getEntity(), HTTP.UTF_8);
			Log.i("HopeLogistics", "ret: "+r);
			return r;
		}
		
		return null;
	}
	
	private static String getRet(HttpResponse response) throws IllegalStateException, IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer sb = new StringBuffer();
		
		String output;
		while((output=br.readLine())!=null)
		{
			sb.append(output);
		}
		
		Log.i("getRet", sb.toString());
		
		return sb.toString();
	}
}
