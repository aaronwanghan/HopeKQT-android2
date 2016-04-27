package com.hope.kqt.android.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class OnTouchLayout extends RelativeLayout 
{
	private float startMotionX;
	private float startMotionY;
	
	private Handler handler;
	
	private OnTouchListener onLeftListener;
	private OnTouchListener onRightListener;
	
	public OnTouchLayout(Context context) {
		super(context);
		
	}

	public OnTouchLayout(Context context,AttributeSet attrs)
	{
		super(context,attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();

		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				this.startMotionX = x;
				this.startMotionY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				
				break;
			case MotionEvent.ACTION_UP:
				float offsetx = x-this.startMotionX;
				float offsety = y-this.startMotionY;
				
				if(offsetx<-100 && offsety>-100 && offsety<100)
				{
					if(this.onLeftListener!=null)
						this.onLeftListener.action();
					return true;
				}
				else if(offsetx>100 && offsety>-100 && offsety<100)
				{
					if(this.onRightListener!=null)
						this.onRightListener.action();
					return true;
				}
				break;
		}
		
		return false;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private void sendEmptyMessage(int key)
	{
		if(this.handler!=null)
			this.handler.sendEmptyMessage(key);
	}

	public interface OnTouchListener{
		void action();
	}

	public void setOnLeftListener(OnTouchListener onLeftListener) {
		this.onLeftListener = onLeftListener;
	}

	public void setOnRightListener(OnTouchListener onRightListener) {
		this.onRightListener = onRightListener;
	}
	
	
}
