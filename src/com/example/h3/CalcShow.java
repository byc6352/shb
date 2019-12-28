/**
 * 
 */
package com.example.h3;

import com.byc.shb.R;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import util.BackgroundMusic;
import util.ConfigCt;
import util.SpeechUtil;

/**
 * @author ASUS
 *
 */
public class CalcShow {
	private static CalcShow current;
	public  Context context;
	private SpeechUtil speaker ;
	private static final int  MSG_RESULT=0x17;//��Ϣ���壺	
	public FloatingWindowPic fwp;//����������ʱ��
	private BackgroundMusic mBackgroundMusic;
	public int mTime=24*60*60;
	public static final String ACTION_CALC_PIC_END = "com.byc.shb.CALC_PIC_END ";//���������Ϣ 
	private int mBase=2;//��������ﵽ��ֵ��2�Ĵη���ʱ�����������
	private int mCount=0;//��ǰ���������
	private int mMax=5;//�ٽ�ֵ��
	 
	 private CalcShow(Context context) {
		 this.context = context;
		 speaker=SpeechUtil.getSpeechUtil(context);
		 mBackgroundMusic=BackgroundMusic.getInstance(context);
		 fwp=FloatingWindowPic.getFloatingWindowPic(context,R.layout.float_click_delay_show);
		 int w=ConfigCt.screenWidth-50;
		 int h=ConfigCt.screenHeight-50;
		 fwp.setFloatViewPara(50, 200,w,h);
		 fwp.setBroadcastPara(ACTION_CALC_PIC_END);
	 }
	public static synchronized CalcShow getInstance(Context context) {
		if(current == null) {
			current = new CalcShow(context);
		}
		return current;
	}
	/*��ʾ������������*/
	public void showAnimation() {
    	mBackgroundMusic.playBackgroundMusic( "dd2.mp3", true);
    	fwp.Start(FloatingWindowPic.SHOW_PIC_RED,10*60*60*24);//24Сʱ;
    }
	/*��ʾ���֣�*/
	public void showTxt() {
		mTime=24*60*60;
		final Handler handler= new Handler(); 
		Runnable runnable = new Runnable() {    
			@Override    
		    public void run() { 
				String aTxt="";
				if(!fwp.isShowAnimation()){//����δ��ʾ��
					aTxt="����ˢȡ������벻Ҫ������";
					Toast.makeText(context, aTxt, Toast.LENGTH_LONG).show();
					speaker.speak(aTxt);
				}else{
					if(fwp.getShowAnimationType()==FloatingWindowPic.SHOW_PIC_RED){//����ˢ�����....
						aTxt="����ˢȡ������벻Ҫ������";
						Toast.makeText(context, aTxt, Toast.LENGTH_LONG).show();
						speaker.speak(aTxt);
						mCount=mCount+1;//���������ʱ
						//int max=getMax(mBase);//��������ٽ��
						
						if(mCount>mMax){//�������.....
							int je=getRadomNum()*getRadomNum();//1.�����Ľ�
							aTxt="��ϲ���ƣ��������"+je+"Ԫ��";
							Toast.makeText(context, aTxt, Toast.LENGTH_LONG).show();
							speaker.speak(aTxt);//2.��ʾ�������
							fwp.setSecondPicTotalCount(FloatingWindowPic.SHOW_PIC_SUC, 10*10);//��ʾ��������
							//mBackgroundMusic.stopBackgroundMusic();//ֹͣ���㴰����ʾ��
							mBase=mBase+1;//3.����Ѷ�(��������ڵ�)
							mCount=0;//4.���������㣻
							Config.Money=Config.Money+je;//5.�����ܽ��;
							Config.getConfig(context).setMoney(Config.Money);
							Intent intent = new Intent(Config.ACTION_ROB_HB);//6.�������������Ϣ
							context.sendBroadcast(intent);
							mMax=getRadomNum()*getRadomNum();;//��������ٽ��
						}
					}
				}
				if(mTime<=0){//��������...
					mBackgroundMusic.stopBackgroundMusic();
					aTxt="�Ƽ�����ͣ����Ҫ������㿪ʼ��ť��";
					Toast.makeText(context,aTxt , Toast.LENGTH_LONG).show();
					speaker.speak(aTxt);
					fwp.Stop();
					return;
				}
				mTime=mTime-10;
				handler.postDelayed(this, 1000*10);    
		    }    
		};
		handler.postDelayed(runnable, 1000*1);  
	}
	/*�Ƽ������ֵ�������ֵʱ��������������¼��㣺*/
	private int getMax(int base) {
		return (int) Math.pow(2,base);
	}
	 //����һ��0~9���������
    private int getRadomNum(){
    	java.util.Random random=new java.util.Random();// ���������
    	int result=random.nextInt(10);// ����[0,10)�����е�������ע�ⲻ����10
    	return result;
    }
	/*ֹͣ������������*/
	public void stopAnimation() {
		fwp.Stop();
	}
	/*ֹͣ������ʾ ��*/
	public void stopTxt() {
		mTime=0;
	}
}
