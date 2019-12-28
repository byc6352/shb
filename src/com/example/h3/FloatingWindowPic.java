/**
 * 
 */
package com.example.h3;


import com.byc.shb.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * @author byc
 *
 */
public class FloatingWindowPic {
	private Context context;
	//-------------------------------------------------------------------------
	//定义浮动窗口布局
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
	private WindowManager mWindowManager;
	
	public boolean bShow=false;//是否显示
	//计数器：
	private int i=0;//图片序号
	//计时器：
	private int j=0;//第一动画图片显示次数
	//显示时间：
	public int c=10;//总第一动画显示次数；
	//显示时间：
	private int k=10;//第二动画显示次数；
	//显示时间：
	private int d=10;//总第二动画显示次数；
	//绿色资源集合：
	private int[] resids=new int[]{
			R.drawable.p0,R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,
			R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9
	};
	//红色图片资源集合：
	private int[] residsRed=new int[]{
			R.drawable.m0,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,
			R.drawable.m5,R.drawable.m6,R.drawable.m7,R.drawable.m8,R.drawable.m9
	};
	//成功图片资源集合：
	private int[] residsSuc=new int[]{
			R.drawable.c0,R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4,
			R.drawable.c5,R.drawable.c6,R.drawable.c7,R.drawable.c8,R.drawable.c9
	};
	//失败图片资源集合：
	private int[] residsFail=new int[]{
			R.drawable.s0,R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,
			R.drawable.s5,R.drawable.s6,R.drawable.s7,R.drawable.s8,R.drawable.s9
	};
	//显示资源类别：
	private int mShowPicType=1;//默认显示绿色图片集；
	private int mFirstShowPicType=1;//第一动画图片集；
	private int mSecondShowPicType=2;//第二动画图片集；
	public static final int SHOW_PIC_GREEN=0;//绿色图片集；
	public static final int SHOW_PIC_RED=1;//红色图片集；
	public static final int SHOW_PIC_SUC=2;//成功图片集；
	public static final int SHOW_PIC_FAIL=3;//失败图片集；
	
	private static String mSendMsg="com.byc.shb.CALC_PIC_END ";
	
	private static FloatingWindowPic current;
	
	public FloatingWindowPic(Context context,int resource) {
		this.context = context;
		createFloatViewPic(resource);
	}
    public static synchronized FloatingWindowPic getFloatingWindowPic(Context context,int resource) {
        if(current == null) {
            current = new FloatingWindowPic(context,resource);
        }
        return current;
        
    }
  
    private void createFloatViewPic(int resource)
 	{
 		wmParams = new WindowManager.LayoutParams();
 		//获取WindowManagerImpl.CompatModeWrapper
 		mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
 		//设置window type
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
 			wmParams.type = LayoutParams.TYPE_TOAST; 
 		else
 			wmParams.type = LayoutParams.TYPE_PHONE; 
 		//设置图片格式，效果为背景透明
         wmParams.format = PixelFormat.RGBA_8888; 
         //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
         wmParams.flags = 
           LayoutParams.FLAG_NOT_TOUCH_MODAL |
           LayoutParams.FLAG_NOT_FOCUSABLE	|
           LayoutParams.FLAG_NOT_TOUCHABLE
           ;
         
         //调整悬浮窗显示的停靠位置为左侧置顶
         wmParams.gravity = Gravity.LEFT | Gravity.TOP; 
         //wmParams.gravity = Gravity.CENTER | Gravity.CENTER; 
         
         // 以屏幕左上角为原点，设置x、y初始值
         wmParams.x = 0;
         wmParams.y = 0;

         // 设置悬浮窗口长宽数据
         //wmParams.width = w;
         //wmParams.height =h;
         
         //设置悬浮窗口长宽数据  
         wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
         wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
         
         LayoutInflater inflater = LayoutInflater.from(context);
         //获取浮动窗口视图所在布局
         //mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_bigpic, null);
         mFloatLayout = (LinearLayout) inflater.inflate(resource, null);
         //添加mFloatLayout
         //mWindowManager.addView(mFloatLayout, wmParams);     
         mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
 				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
 				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        
 	}

    //切换图片：
    private void switchPic(int i){

    	ImageView iv=(ImageView)mFloatLayout.findViewById(R.id.ImageView01);
    	switch(mShowPicType){
    	case SHOW_PIC_GREEN:
    		iv.setImageResource(resids[i]);
    		break;
    	case SHOW_PIC_RED:
    		iv.setImageResource(residsRed[i]);
    		break;
    	case SHOW_PIC_SUC:
    		iv.setImageResource(residsSuc[i]);
    		break;
    	case SHOW_PIC_FAIL:
    		iv.setImageResource(residsFail[i]);
    		break;
    	}
    }
    //-----------------------------------------------动画定时器------------------------
    /*显示动画：*/
	public void showSwitchPic() {
		final Handler handler= new Handler(); 
		Runnable runnable = new Runnable() {    
			@Override    
		    public void run() { 
				switchPic(i);
				i=i+1;
            	if(i>9)i=0;//图片切换;
            	j=j+1;//第一动画次数；
            	if(d>0&&j>=(k+d)){//切换回第一动画：
            		mShowPicType=mFirstShowPicType;
            		d=0;
            	}
            	if(j>=c){//停止动画
            		RemoveFloatingWindowPic();
            		j=0;
            		c=0;
            		Intent intent = new Intent(mSendMsg);
            	    context.sendBroadcast(intent);
            		return;
            	}
				handler.postDelayed(this, 100);    
		    }    
		};
		handler.postDelayed(runnable, 100);  
	}
    //-----------------------------------------------悬浮窗图片显示与隐藏-----------------------------------
	public void ShowFloatingWindow(){
		if(!bShow){	
			try{
				mWindowManager.addView(mFloatLayout, wmParams);
	       		bShow=true;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	    
	public void RemoveFloatingWindowPic(){
		if(mFloatLayout != null)
		{
			if(bShow)mWindowManager.removeView(mFloatLayout);
			bShow=false;
		}
	}
    //-----------------------------------------------动画开始与结束-----------------------------------
    public void Start(){
    	i=0;//图片序号
    	j=0;//计数器
    	ShowFloatingWindow();//显示悬浮窗口;
    	showSwitchPic() ;//开始动画；
    }
    public void Start(int showPicType,int firstPicTotalCount){
    	i=0;//图片序号
    	j=0;//计数器
    	setFirstPicTotalCount(showPicType,firstPicTotalCount);
    	ShowFloatingWindow();//显示悬浮窗口;
    	showSwitchPic() ;//开始动画；
    }
    public void Stop(){
    	c=0;//第一动画显示次数；
    	d=0;//第二动画显示次数；
    }
	//------------------------------------------------参数设置--------------------------------
    /*
     * 设置动画位置及大小：
     * */
    public void setFloatViewPara(int x,int y,int w,int h){
        // 以屏幕左上角为原点，设置x、y初始值
    	if(wmParams==null)return;
        wmParams.x = x;
        wmParams.y = y;
        // 设置悬浮窗口长宽数据
        wmParams.width = w;
        wmParams.height =h;
        //设置悬浮窗口长宽数据  
        //wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }
    /*
     * 设置第一动画显示图片及次数：
     * */
    public void setFirstPicTotalCount(int ShowPicType,int FirstPicTotalCount){
    	c=FirstPicTotalCount;
    	mFirstShowPicType=ShowPicType;
    	mShowPicType=mFirstShowPicType;
    }
    /*
     * 设置第二动画显示图片及次数：
     * */
    public void setSecondPicTotalCount(int ShowPicType,int SecondPicTotalCount){
    	k=j;
    	d=SecondPicTotalCount;
    	mSecondShowPicType=ShowPicType;
    	mShowPicType=ShowPicType;
    }
    /*
     * 动画结束时发送广播：
     * */
    public void setBroadcastPara(String broadcastPara){
    	mSendMsg=broadcastPara;
    }
    /*
     * 当前显示的动画类型：
     * */
    public int getShowAnimationType(){
    	return mShowPicType;
    }
    /*
     * 当前显示动画否：
     * */
    public boolean isShowAnimation(){
    	if(c>0)return true;else return false;
    }
}
