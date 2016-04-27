package com.hope.kqt.android.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import com.hope.kqt.android.Application;
import com.hope.kqt.android.LauncherActivity;
import com.hope.kqt.android.MainActivity;
import com.hope.kqt.android.R;
import com.hope.kqt.android.dao.SystemValueDao;
import com.hope.kqt.android.dao.impl.SystemValueFileDaoImpl;
import com.hope.kqt.android.service.TasksService;
import com.hope.kqt.android.util.Date;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.android.util.UserTaskMenusDatasHelper;
import com.hope.kqt.entity.User;
import com.hope.kqt.entity.UserTask;
import com.hope.kqt.entity.UserTaskMenus;

import org.json.JSONObject;

/**
 * Implementation of App Widget functionality.
 */
public class TasksWidget extends AppWidgetProvider
{
    private UserTaskMenusDatasHelper dataHelper;
    private SystemValueDao systemValueDao;
    private User user;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        dataHelperInit(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.tasks_widget);
        ComponentName componentName = new ComponentName(context,TasksWidget.class);

        UserTask ut = this.getNowTask();
        if(ut!=null){
            remoteViews.setImageViewResource(R.id.tw_img, TasksService.icons[ut.getType()]);
            remoteViews.setTextViewText(R.id.tw_text,ut.getTitle());
            remoteViews.setTextViewText(R.id.tw_context_text,(ut.getContent() == null? "":ut.getContent()));
            remoteViews.setTextViewText(R.id.tw_time_text, Date.valueOf(ut.getStartTime()).toTime());

            int color = Color.parseColor(KQTUtils.taskTimeToColor(ut.getCallTime(),ut.getStartTime(),ut.getEndTime()));

            remoteViews.setInt(R.id.tw_text_layout,"setBackgroundColor",color);
        }
        Intent intent = new Intent(context, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.tw_layout,pendingIntent);

        appWidgetManager.updateAppWidget(componentName,remoteViews);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void dataHelperInit(Context context){
        if(this.systemValueDao==null)
            this.systemValueDao = new SystemValueFileDaoImpl(context);

        if(this.user==null)
        {
            String s = systemValueDao.load(Application.USER_SYSTEM_KEY);

            if(s == null || "".equals(s))
            {
                return;
            }

            Log.i("initUser s",(s==null? "null":s));
            try {
                Log.i("systemValueDao user", new String(KQTUtils.parseHexStr2Byte(s)));

                this.user = User.valueOf(new JSONObject(new String(KQTUtils.parseHexStr2Byte(s))));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        if(this.dataHelper==null && this.user!=null)
            this.dataHelper = new UserTaskMenusDatasHelper(context,user);

    }

    private UserTask getNowTask()
    {
        long now = System.currentTimeMillis();
        if(this.dataHelper!=null){
            UserTaskMenus userTaskMenus = this.dataHelper.getNow();
            if(userTaskMenus!=null && !userTaskMenus.getTasks().isEmpty()){
                for(UserTask ut:userTaskMenus.getTasks()){
                    if(ut.getCallTime()>now)
                        return ut;
                }

                return userTaskMenus.getTasks().get(userTaskMenus.getTasks().size()-1);
            }
        }

        return null;
    }
}

