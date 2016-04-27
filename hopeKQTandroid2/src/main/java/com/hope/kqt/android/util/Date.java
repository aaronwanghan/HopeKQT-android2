package com.hope.kqt.android.util;

import java.util.Calendar;

/**
 * 自定义日期类
 * @author aaronwang
 *
 */
public class Date 
{
	private Calendar data;
	
	public Date() {
		this.data = Calendar.getInstance();
	}
	
	public Date(long data)
	{
		this.data = Calendar.getInstance();
		this.data.setTimeInMillis(data);
	}

	public Date(Long data)
	{
		this.data = Calendar.getInstance();
		if(data!=null)
			this.data.setTimeInMillis(data);
	}
	
	public int getYear() {
		return this.data.get(Calendar.YEAR);
	}

	public void setYear(int year) {
		this.data.set(Calendar.YEAR, year);
	}

	public int getMonth() {
		return this.data.get(Calendar.MONTH)+1;
	}

	public void setMonth(int month) {
		this.data.set(Calendar.MONTH, month-1);
	}

	public int getDay() {
		return this.data.get(Calendar.DAY_OF_MONTH);
	}

	public void setDay(int day) {
		this.data.set(Calendar.DAY_OF_MONTH, day);
	}

	public int getHour() {
		return this.data.get(Calendar.HOUR_OF_DAY);
	}

	public void setHour(int hour) {
		this.data.set(Calendar.HOUR_OF_DAY, hour);
	}

	public int getMinute() {
		return this.data.get(Calendar.MINUTE);
	}

	public void setMinute(int minute) {
		this.data.set(Calendar.MINUTE, minute);
	}
	
	public int getSecond()
	{
		return this.data.get(Calendar.SECOND);
	}
	
	public void setSecond(int second) {
		this.data.set(Calendar.SECOND, second);
	}
	/**
	 * 退回日期和时间
	 * @return
	 */
	public String toDateAndTime()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.toDate()).append(" ");
		
		if(this.getHour()<10)
			sb.append(0);
		sb.append(this.getHour()).append(":");
		if(this.getMinute()<10)
			sb.append(0);
		sb.append(this.getMinute()).append(":");
		if(this.getSecond()<10)
			sb.append(0);
		sb.append(this.getSecond());
		
		return sb.toString();
	}
	
	/**
	 * 退回日期
	 * @return
	 */
	public String toDate()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.getYear()).append("年");
		if(this.getMonth()+1<10)
			sb.append(0);
		sb.append(this.getMonth()).append("月");
		if(this.getDay()<10)
			sb.append(0);
		sb.append(this.getDay()).append("日");

		return sb.toString();
	}
	
	/**
	 * 退回时间
	 * @return
	 */
	public String toTime()
	{
		StringBuffer sb = new StringBuffer();
		if(this.getHour()<10)
			sb.append(0);
		sb.append(this.getHour()).append(":");
		if(this.getMinute()<10)
			sb.append(0);
		sb.append(this.getMinute());
		
		//sb.append(" ").append(this.data.get(Calendar.MILLISECOND));
		return sb.toString();
	}
	
	public long getTimeInMillis()
	{
		return this.data.getTimeInMillis();
	}
	
	public static Date valueOf(long timeInMillis)
	{
		Date date = new Date(timeInMillis);
		return date;
	}
	
	public static void main(String[] args)
	{
		System.out.println(Date.valueOf(1385004570001l).toDateAndTime());
	}
}
