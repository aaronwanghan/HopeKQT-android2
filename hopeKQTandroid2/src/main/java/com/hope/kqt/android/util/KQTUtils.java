package com.hope.kqt.android.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.hope.kqt.entity.User;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 监控操作工具类
 * @author aaronwang
 *
 */

public class KQTUtils 
{
	//public final static long CLIENT_TIME_LONG = 1000*60*60;			//操作超时时间
	//public final static long LOGIN_TIME_LONG = 1000*5;
	
	/**
	 * AES解密
	 * @param content
	 * @param password
	 * @return
	 */
	public static String decrypt(String content, byte[] password) {
		try {  
			SecretKeySpec key = new SecretKeySpec(password, "AES");        
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化  
			byte[] result = cipher.doFinal(parseHexStr2Byte(content));  
			return new String(result);
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return null;  
	} 
	
	/**
	 * AES加密
	 * @param content
	 * @param password
	 * @return
	 */
	public static String encrypt(String content, byte[] password) {  
		try {             
			SecretKeySpec key = new SecretKeySpec(password, "AES");  
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
			byte[] result = cipher.doFinal(content.getBytes());

			return parseByte2HexStr(result); // 加密  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return null;  
	}
	
/*	public static byte[] getKey()
	{
		KeyGenerator keygen;
		try {
			keygen = KeyGenerator.getInstance("AES");
			keygen.init(256);
			return keygen.generateKey().getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}*/
	/**
	 * byte[]转换成String
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) 
        {  
                String hex = Integer.toHexString(buf[i] & 0xFF);  
                if (hex.length() == 1) 
                        hex = '0' + hex;  

                sb.append(hex.toUpperCase());  
        }
        return sb.toString();  
	}
	
	/**
	 * String 转换成 byte[]
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) 
	{  
        if (hexStr.length() < 1)  
                return null;  
        byte[] result = new byte[hexStr.length()/2];  
        for (int i = 0;i< hexStr.length()/2; i++) {  
                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                result[i] = (byte) (high * 16 + low);  
        }  
        return result;  
	}
	
	/**
	 * 为TextView 填入电量信息
	 * @param v
	 * @param e
	 */
	public static void setElectricity(TextView v,Double e)
	{
		if(e==null)
		{
			v.setText("");
			return;
		}
		
		if(e>=3.84)
			v.setTextColor(Color.parseColor("#006600"));
		else if(e>=3.73)
			v.setTextColor(Color.parseColor("#CCCC00"));
		else
			v.setTextColor(Color.RED);
		
		v.setText(electricityToString(e));
	}
	
	/**
	 * 将电压 转换成电量
	 * @param e
	 * @return
	 */
	public static String electricityToString(Double e)
	{
		if(e==null)
			return "";
		
		if(e>=4.12)
			return "100%";
		else if(e<4.12 && e>=4.08)
			return "90%";
		else if(e<4.08 && e>=3.97)
			return "80%";
		else if(e<3.97 && e>=3.90)
			return "70%";
		else if(e<3.90 && e>=3.84)
			return "60%";
		else if(e<3.84 && e>=3.79)
			return "50%";
		else if(e<3.79 && e>=3.76)
			return "40%";
		else if(e<3.76 && e>=3.73)
			return "30%";
		else if(e<3.73 && e>=3.71)
			return "20%";
		else if(e<3.71 && e>=3.65)
			return "10%";
		else if(e<3.65 && e>=3.63)
			return "5%";
		else if(e<3.63 && e>3)
			return "1%";
		else
			return "0%";
	}
	
	public static void setLinearLayoutHeightBasedOnChildren(LinearLayout lLayout)
	{
		if(lLayout.getOrientation()!=LinearLayout.VERTICAL)
			return;
		
		int totalHeight = 0;
		for(int i=0; i<lLayout.getChildCount();i++)
		{
			View item = lLayout.getChildAt(i);
			totalHeight += getViewHeight(item);
		}
		
		ViewGroup.LayoutParams params = lLayout.getLayoutParams(); 
		params.height = totalHeight;
		lLayout.setLayoutParams(params);
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) 
	{  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();  
            //Log.i("listItem.getMeasuredHeight()",listItem.getMeasuredHeight()+"");            
        }  
  //Log.i("listAdapter.getCount()",listAdapter.getCount()+"");
  //Log.i("totalHeight",totalHeight+"");
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        //Log.i("params.height",params.height+"");      
        listView.setLayoutParams(params);  
    }

 	
 	/**
 	 * 反射调用get方法 获得参数值
 	 * @param obj
 	 * @param att
 	 * @return
 	 */
 	public static Object getter(Object obj, String att) 
    {
        try {
            Method method = obj.getClass().getMethod("get" + att);
            return  method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

 	
 	public static int getViewHeight(View view)
 	{
 		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
 		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
 		view.measure(w, h);
 		return view.getMeasuredHeight();
 	}
 	
 	public static double round(double v,int scale)
 	{
 		if(scale<0)
 		{
 			throw new IllegalArgumentException(
 					"The scale must be a positive integer or zero");
 		}
 		BigDecimal b = new BigDecimal(Double.toString(v));
 		BigDecimal one = new BigDecimal("1");
 		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
 	}
 	
 	public static void setStartAndEnd(Intent i,long taskTime)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(taskTime);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		int h = c.get(Calendar.HOUR_OF_DAY);
		long start = 0l;
		long end = 0l;
		
		if(h<12)
		{
			start = getHourTime(c,0);
			end = getHourTime(c,12);
		}
		else if(h>=12 && h<18)
		{
			start = getHourTime(c,12);
			end = getHourTime(c,18);
		}
		else
		{
			start = getHourTime(c,18);
			end = getHourTime(c,24);
		}
		i.putExtra("start", start);
		i.putExtra("end", end);
	}
 	
 	private static long getHourTime(Calendar c,int hour)
 	{
 		c.set(Calendar.HOUR_OF_DAY, hour);
		return c.getTimeInMillis();
 	}
 	
 	public static long getNowTime()
	{
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		now.set(Calendar.MILLISECOND, 0);
		
		return now.getTimeInMillis();
	}
 	
 	public static String getUserUpdateKey(User user)
	{
		if(user==null)
			return "";
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(user.getId()).append("-");
		sb.append((user.getTeacher()==null? "null":""+user.getTeacher().getId())).append("-");
		sb.append(user.getCreateDate());
		
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = md5.digest(sb.toString().getBytes());
			
			return KQTUtils.parseByte2HexStr(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return "";
	}

	public static String taskTimeToColor(long call,long start,long end){
		long now = System.currentTimeMillis();
		if((call!=start && now<call )|| now<start)
		{
			return "#66CCFF";
		}

		if((end!=0 && now>end) || now>start+30*60*1000)
		{
			return "#999999";
		}

		return "#66CC33";
	}
}
