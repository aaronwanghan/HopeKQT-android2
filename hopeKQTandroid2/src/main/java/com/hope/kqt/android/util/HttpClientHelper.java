package com.hope.kqt.android.util;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.hope.kqt.android.Application;


public class HttpClientHelper 
{
    private static HttpClient httpClient;
    
    private HttpClientHelper(){
        
    }
    
    public static synchronized HttpClient getHttpClient(){
        if(null == httpClient){
            //初始化工作
            HttpParams params = new BasicHttpParams();
            
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            
            
            //设置连接管理器的超时
            ConnManagerParams.setTimeout(params, 5000);
            
            //设置连接超时
            HttpConnectionParams.setConnectionTimeout(params, Application.TIME_OUT_CONNECTION);
            //设置Socket超时
            HttpConnectionParams.setSoTimeout(params, 10000);
            
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 80));
            
            ClientConnectionManager conManager = new ThreadSafeClientConnManager(params, schReg);
            
            httpClient = new DefaultHttpClient(conManager, params);
        }
        
        return httpClient;
    }

}
