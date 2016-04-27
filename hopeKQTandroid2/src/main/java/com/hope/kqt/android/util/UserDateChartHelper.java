package com.hope.kqt.android.util;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.hope.kqt.entity.UserData;
import com.hope.kqt.entity.UserDatasItem;

public class UserDateChartHelper 
{
	private List<UserDatasItem> datas;
	//private String title;
	private String yTitle;
	
	//private List<String> types;
	
	public UserDateChartHelper(List<UserDatasItem> datas,String title,String yTitle) {
		super();
		this.datas = datas;
		//this.title = title;
		this.yTitle = yTitle;
	}

	private XYMultipleSeriesDataset getChartDataset()
	{
		XYMultipleSeriesDataset xydataset = new XYMultipleSeriesDataset();
		
		//this.types = new ArrayList<String>();
		
		if(this.datas!=null && !this.datas.isEmpty())
		{
			for(UserDatasItem uitem:this.datas)
			{
				TimeSeries series = new TimeSeries(uitem.getName());
				//XYSeries cseries = new XYSeries(uitem.getName());
				
				//this.types.add(BarChart.TYPE);
				//this.types.add(LineChart.TYPE);
				
				List<UserData> logs = uitem.getDatas();
				if(logs!=null && !logs.isEmpty())
				{	
					for(UserData ud:logs)
					{
						//cseries.add(ud.getTime(), ud.getValue());
						series.add(ud.getTime(), ud.getValue());
					}
				}
				
				xydataset.addSeries(series);
				//xydataset.addSeries(cseries);
			}
		}

		
		
		return xydataset;
	}
	
	private XYMultipleSeriesRenderer getChartRenderer()
	{
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		
		for(int i=0;i<this.datas.size();i++)
		{	
			XYSeriesRenderer xyrenderer = new XYSeriesRenderer();
			//XYSeriesRenderer ssrenderer = new XYSeriesRenderer();
			xyrenderer.setLineWidth(5);
			xyrenderer.setColor(Color.parseColor(this.getColor()));
			xyrenderer.setDisplayChartValues(true);
			xyrenderer.setDisplayChartValuesDistance(10);
			//ssrenderer.setColor(Color.parseColor(this.getColor()));
			
			//renderer.addSeriesRenderer(ssrenderer);
			renderer.addSeriesRenderer(xyrenderer);
		}
		renderer.setAxisTitleTextSize(16);				//坐标轴标题文字大小
		renderer.setChartTitleTextSize(25);			//曲线图标题文字大小
	    renderer.setLabelsTextSize(20);				//标签文字大小
	    renderer.setLegendTextSize(20);				//图列文字大小
	    renderer.setPointSize(5f);						//显示点大小
	    renderer.setMargins(new int[] {20, 30, 15, 0}); //边框设置
	    //renderer.setZoomButtonsVisible(true);			//缩放按键
	    renderer.setZoomEnabled(true);					//缩放功能
	    renderer.setShowGrid(true);					//网格
	    
	    renderer.setYTitle(this.yTitle);				//Y轴标题
	    
	    renderer.setLabelsColor(Color.GREEN);			//标签颜色
		
	    renderer.setBarSpacing(5); //2个条形图之间的距离

	    if(this.datas!=null && !this.datas.isEmpty())
		{
	    	for(UserDatasItem uitem:this.datas)
	    	{	
	    		List<UserData> logs = uitem.getDatas();
	    		if(logs!=null && !logs.isEmpty())
	    		{	
	    			for(UserData ud:logs)
	    			{
	    				Date d = Date.valueOf(ud.getTime());
	    				renderer.addXTextLabel(ud.getTime(), d.getMonth()+"-"+d.getDay());
	    				renderer.setXLabels(0);
	    			}
	    		}
	    	}
			
		}
	    
		return renderer;
	}
	
	public View getLineView(Context context)
	{
		
		//return ChartFactory.getCombinedXYChartView(context, this.getChartDataset(), this.getChartRenderer(), types.toArray(new String[0]));
		return ChartFactory.getTimeChartView(context, 
				this.getChartDataset(), this.getChartRenderer(), null);
	}
	
	public View getBarView(Context context)
	{
		return ChartFactory.getBarChartView(context, this.getChartDataset(), this.getChartRenderer(), null);
	}
	
	private List<String> colors = new ArrayList<String>();
	private static final String[] COLOR_ITEMS = {"FF","CC","99","66","33","00"};
	
	private String getColor()
	{
		StringBuffer sb = new StringBuffer("#");
		
		int i = new java.util.Random().nextInt(COLOR_ITEMS.length);
		sb.append(COLOR_ITEMS[i]);
		
		if(i>COLOR_ITEMS.length/2)
			sb.append(COLOR_ITEMS[COLOR_ITEMS.length-1-i]);
		else
			sb.append(COLOR_ITEMS[new java.util.Random().nextInt(COLOR_ITEMS.length)]);
		
		i = new java.util.Random().nextInt(99);
		sb.append((i<10? "0"+i:i));
		
		if(colors.indexOf(sb.toString())>-1)
			return getColor();
		
		colors.add(sb.toString());
		
		Log.i("color",sb.toString());
		return sb.toString();
	}
}
