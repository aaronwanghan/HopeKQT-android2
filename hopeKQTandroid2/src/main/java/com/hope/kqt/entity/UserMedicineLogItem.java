package com.hope.kqt.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by han on 2016/4/27.
 */
public class UserMedicineLogItem implements Serializable
{
    private String name;		//名称
    private String unit;		//药量

    private List<UserMLog> logs;

    public UserMedicineLogItem(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<UserMLog> getLogs() {
        return logs;
    }

    public void setLogs(List<UserMLog> logs) {
        this.logs = logs;
    }

    public static UserMedicineLogItem valueOf(JSONObject obj) throws JSONException
    {
        if(obj==null)
            return null;

        UserMedicineLogItem umli = new UserMedicineLogItem();
        umli.setName(obj.getString("name"));
        umli.setUnit(obj.getString("unit"));

        if(!obj.isNull("logs"))
        {
            umli.setLogs(new ArrayList<UserMLog>());
            JSONArray array = obj.getJSONArray("logs");
            if(array!=null && array.length()>0)
                for(int i=0;i<array.length();i++)
                    umli.getLogs().add(UserMLog.valueOf(array.getJSONObject(i)));
        }

        return umli;
    }
}
