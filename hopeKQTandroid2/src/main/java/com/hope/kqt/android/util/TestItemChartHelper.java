package com.hope.kqt.android.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.hope.kqt.entity.UserData;
import com.hope.kqt.entity.UserDatasItem;
import com.hope.kqt.entity.UserMLog;
import com.hope.kqt.entity.UserMedicineLogItem;
import com.hope.kqt.entity.UserTestItem;
import com.hope.kqt.entity.UserTestLog;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by han on 2016/4/27.
 */
public class TestItemChartHelper
{
    private UserTestItem userTestItem;

    public TestItemChartHelper(UserTestItem userTestItem) {
        this.userTestItem = userTestItem;
    }

    public View getChartView(Context context)
    {
        //String[] types = new String[]{LineChart.TYPE, BarChart.TYPE};
        return ChartFactory.getCombinedXYChartView(context,getChartDataset(),getChartRenderer(),this.getTypes());
    }

    private String[] getTypes()
    {
        String[] types = new String[]{};
        List<String> list = new ArrayList<String>();

        if(userTestItem!=null){
            if(userTestItem.getChildren()!=null){
                for(UserTestItem item:userTestItem.getChildren())
                    list.add(LineChart.TYPE);
            }else{
                list.add(LineChart.TYPE);
            }

            if(userTestItem.getUserMedicineLogItems()!=null){
                for(UserMedicineLogItem item:userTestItem.getUserMedicineLogItems())
                    list.add(BarChart.TYPE);
            }
        }

        return list.toArray(types);
    }

    private XYMultipleSeriesDataset getChartDataset()
    {
        XYMultipleSeriesDataset xydataset = new XYMultipleSeriesDataset();

        if(userTestItem!=null){
            if(userTestItem.getChildren()!=null){
                for(UserTestItem item:userTestItem.getChildren())
                    xydataset.addSeries(this.userTestItemToTimeSerics(item));

            } else {
                xydataset.addSeries(this.userTestItemToTimeSerics(userTestItem));
            }

            if(userTestItem.getUserMedicineLogItems()!=null){
                for(UserMedicineLogItem item:userTestItem.getUserMedicineLogItems())
                    xydataset.addSeries(this.userMedicineLogItemToTimeSeries(item));
            }
        }

        return xydataset;
    }

    private XYMultipleSeriesRenderer getChartRenderer()
    {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        if(userTestItem.getChildren()!=null){
            for(UserTestItem item:userTestItem.getChildren())
            {
                this.addXTextLabel(renderer,item);
                renderer.addSeriesRenderer(this.getXYSeriesRenderer());
            }

        } else {
            this.addXTextLabel(renderer,userTestItem);
            renderer.addSeriesRenderer(this.getXYSeriesRenderer());
        }

        if(userTestItem.getUserMedicineLogItems()!=null){
            for(UserMedicineLogItem item:userTestItem.getUserMedicineLogItems())
            {
                this.addXTextLabel(renderer,item);
                renderer.addSeriesRenderer(this.getXYSeriesRenderer());
            }
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

        renderer.setYTitle(this.userTestItem.getUnit());				//Y轴标题

        renderer.setLabelsColor(Color.GREEN);			//标签颜色

        renderer.setBarSpacing(5); //2个条形图之间的距离

        /*if(this.datas!=null && !this.datas.isEmpty())
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

        }*/

        return renderer;
    }

    private TimeSeries userTestItemToTimeSerics(UserTestItem userTestItem)
    {
        TimeSeries series = new TimeSeries(userTestItem.getName());

        if(userTestItem.getLogs()!=null)
            for(UserTestLog log:userTestItem.getLogs())
                series.add(log.getTaskTime(),Double.valueOf(log.getValue()));

        return series;
    }

    private TimeSeries userMedicineLogItemToTimeSeries(UserMedicineLogItem userMedicineLogItem)
    {
        TimeSeries series = new TimeSeries(userMedicineLogItem.getName());

        if(userMedicineLogItem.getLogs()!=null)
            for(UserMLog log:userMedicineLogItem.getLogs())
                series.add(log.getTaskTime(),log.getNum());

        return series;
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

    private XYSeriesRenderer getXYSeriesRenderer(){
        XYSeriesRenderer xyrenderer = new XYSeriesRenderer();

        xyrenderer.setLineWidth(5);
        xyrenderer.setColor(Color.parseColor(this.getColor()));
        xyrenderer.setDisplayChartValues(true);
        xyrenderer.setDisplayChartValuesDistance(10);
        return xyrenderer;
    }

    private void addXTextLabel(XYMultipleSeriesRenderer renderer,UserTestItem item)
    {
        if(item.getLogs()!=null){
            for(UserTestLog log: item.getLogs()){
                Date d = Date.valueOf(log.getTaskTime());
                renderer.addXTextLabel(log.getTaskTime(), d.getMonth()+"-"+d.getDay());
                renderer.setXLabels(0);
            }
        }
    }

    private void addXTextLabel(XYMultipleSeriesRenderer renderer,UserMedicineLogItem item)
    {
        if(item.getLogs()!=null){
            for(UserMLog log: item.getLogs()){
                Date d = Date.valueOf(log.getTaskTime());
                renderer.addXTextLabel(log.getTaskTime(), d.getMonth()+"-"+d.getDay());
                renderer.setXLabels(0);
            }
        }
    }

    /*private List<UserData> userMLogToUserData(List<UserMLog> logs)
    {
        if(logs==null || logs.isEmpty())
            return null;

        List<UserData> list = new ArrayList<UserData>();

        for(int i=0;i<logs.size();i++)
        {
            UserMLog log = logs.get(i);
            UserMLog on = null;

            if(i>0)
                on = logs.get(i-1);

            UserData data = new UserData();
            data.setTime(log.getTaskTime());
            data.setValue(log.getNum());

            if(on!=null)
                data.setProgress(log.getNum()-on.getNum());

            list.add(data);
        }

        return list;
    }

    private List<UserData> userTestLogToUserData(List<UserTestLog> logs)
    {
        if(logs==null || logs.isEmpty())
            return null;

        List<UserData> list = new ArrayList<UserData>();

        for(int i=0;i<logs.size();i++)
        {
            UserTestLog log = logs.get(i);

            if(log.getValue()==null || "".equals(log.getValue()))
                continue;

            UserTestLog on = this.getOnData(logs, i);

            UserData data = new UserData();
            data.setTime(log.getTaskTime());
            data.setValue(Double.valueOf(log.getValue()));

            if(on!=null)
                data.setProgress(getProress(data.getValue(),Double.valueOf(on.getValue())));

            list.add(data);
        }

        return list;
    }

    private UserTestLog getOnData(List<UserTestLog> logs,int i)
    {
        if(i>0)
        {
            UserTestLog log = logs.get(i-1);
            if(log.getValue()==null || "".equals(log.getValue()))
                return this.getOnData(logs, i-1);

            return log;
        }

        return null;
    }

    private Double getProress(Double d1,Double d2)
    {
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);

        return bd1.subtract(bd2).doubleValue();
    }*/
}
