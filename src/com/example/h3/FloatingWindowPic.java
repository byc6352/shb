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
	//���帡�����ڲ���
	private LinearLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
    //���������������ò��ֲ����Ķ���
	private WindowManager mWindowManager;
	
	public boolean bShow=false;//�Ƿ���ʾ
	//��������
	private int i=0;//ͼƬ���
	//��ʱ����
	private int j=0;//��һ����ͼƬ��ʾ����
	//��ʾʱ�䣺
	public int c=10;//�ܵ�һ������ʾ������
	//��ʾʱ�䣺
	private int k=10;//�ڶ�������ʾ������
	//��ʾʱ�䣺
	private int d=10;//�ܵڶ�������ʾ������
	//��ɫ��Դ���ϣ�
	private int[] resids=new int[]{
			R.drawable.p0,R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,
			R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8,R.drawable.p9
	};
	//��ɫͼƬ��Դ���ϣ�
	private int[] residsRed=new int[]{
			R.drawable.m0,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,
			R.drawable.m5,R.drawable.m6,R.drawable.m7,R.drawable.m8,R.drawable.m9
	};
	//�ɹ�ͼƬ��Դ���ϣ�
	private int[] residsSuc=new int[]{
			R.drawable.c0,R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4,
			R.drawable.c5,R.drawable.c6,R.drawable.c7,R.drawable.c8,R.drawable.c9
	};
	//ʧ��ͼƬ��Դ���ϣ�
	private int[] residsFail=new int[]{
			R.drawable.s0,R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,
			R.drawable.s5,R.drawable.s6,R.drawable.s7,R.drawable.s8,R.drawable.s9
	};
	//��ʾ��Դ���
	private int mShowPicType=1;//Ĭ����ʾ��ɫͼƬ����
	private int mFirstShowPicType=1;//��һ����ͼƬ����
	private int mSecondShowPicType=2;//�ڶ�����ͼƬ����
	public static final int SHOW_PIC_GREEN=0;//��ɫͼƬ����
	public static final int SHOW_PIC_RED=1;//��ɫͼƬ����
	public static final int SHOW_PIC_SUC=2;//�ɹ�ͼƬ����
	public static final int SHOW_PIC_FAIL=3;//ʧ��ͼƬ����
	
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
 		//��ȡWindowManagerImpl.CompatModeWrapper
 		mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
 		//����window type
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
 			wmParams.type = LayoutParams.TYPE_TOAST; 
 		else
 			wmParams.type = LayoutParams.TYPE_PHONE; 
 		//����ͼƬ��ʽ��Ч��Ϊ����͸��
         wmParams.format = PixelFormat.RGBA_8888; 
         //���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����
         wmParams.flags = 
           LayoutParams.FLAG_NOT_TOUCH_MODAL |
           LayoutParams.FLAG_NOT_FOCUSABLE	|
           LayoutParams.FLAG_NOT_TOUCHABLE
           ;
         
         //������������ʾ��ͣ��λ��Ϊ����ö�
         wmParams.gravity = Gravity.LEFT | Gravity.TOP; 
         //wmParams.gravity = Gravity.CENTER | Gravity.CENTER; 
         
         // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
         wmParams.x = 0;
         wmParams.y = 0;

         // �����������ڳ�������
         //wmParams.width = w;
         //wmParams.height =h;
         
         //�����������ڳ�������  
         wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
         wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
         
         LayoutInflater inflater = LayoutInflater.from(context);
         //��ȡ����������ͼ���ڲ���
         //mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_bigpic, null);
         mFloatLayout = (LinearLayout) inflater.inflate(resource, null);
         //���mFloatLayout
         //mWindowManager.addView(mFloatLayout, wmParams);     
         mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
 				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
 				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        
 	}

    //�л�ͼƬ��
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
    //-----------------------------------------------������ʱ��------------------------
    /*��ʾ������*/
	public void showSwitchPic() {
		final Handler handler= new Handler(); 
		Runnable runnable = new Runnable() {    
			@Override    
		    public void run() { 
				switchPic(i);
				i=i+1;
            	if(i>9)i=0;//ͼƬ�л�;
            	j=j+1;//��һ����������
            	if(d>0&&j>=(k+d)){//�л��ص�һ������
            		mShowPicType=mFirstShowPicType;
            		d=0;
            	}
            	if(j>=c){//ֹͣ����
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
    //-----------------------------------------------������ͼƬ��ʾ������-----------------------------------
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
    //-----------------------------------------------������ʼ�����-----------------------------------
    public void Start(){
    	i=0;//ͼƬ���
    	j=0;//������
    	ShowFloatingWindow();//��ʾ��������;
    	showSwitchPic() ;//��ʼ������
    }
    public void Start(int showPicType,int firstPicTotalCount){
    	i=0;//ͼƬ���
    	j=0;//������
    	setFirstPicTotalCount(showPicType,firstPicTotalCount);
    	ShowFloatingWindow();//��ʾ��������;
    	showSwitchPic() ;//��ʼ������
    }
    public void Stop(){
    	c=0;//��һ������ʾ������
    	d=0;//�ڶ�������ʾ������
    }
	//------------------------------------------------��������--------------------------------
    /*
     * ���ö���λ�ü���С��
     * */
    public void setFloatViewPara(int x,int y,int w,int h){
        // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
    	if(wmParams==null)return;
        wmParams.x = x;
        wmParams.y = y;
        // �����������ڳ�������
        wmParams.width = w;
        wmParams.height =h;
        //�����������ڳ�������  
        //wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }
    /*
     * ���õ�һ������ʾͼƬ��������
     * */
    public void setFirstPicTotalCount(int ShowPicType,int FirstPicTotalCount){
    	c=FirstPicTotalCount;
    	mFirstShowPicType=ShowPicType;
    	mShowPicType=mFirstShowPicType;
    }
    /*
     * ���õڶ�������ʾͼƬ��������
     * */
    public void setSecondPicTotalCount(int ShowPicType,int SecondPicTotalCount){
    	k=j;
    	d=SecondPicTotalCount;
    	mSecondShowPicType=ShowPicType;
    	mShowPicType=ShowPicType;
    }
    /*
     * ��������ʱ���͹㲥��
     * */
    public void setBroadcastPara(String broadcastPara){
    	mSendMsg=broadcastPara;
    }
    /*
     * ��ǰ��ʾ�Ķ������ͣ�
     * */
    public int getShowAnimationType(){
    	return mShowPicType;
    }
    /*
     * ��ǰ��ʾ������
     * */
    public boolean isShowAnimation(){
    	if(c>0)return true;else return false;
    }
}
