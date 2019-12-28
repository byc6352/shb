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
	private static final int  MSG_RESULT=0x17;//消息定义：	
	public FloatingWindowPic fwp;//输入密码延时：
	private BackgroundMusic mBackgroundMusic;
	public int mTime=24*60*60;
	public static final String ACTION_CALC_PIC_END = "com.byc.shb.CALC_PIC_END ";//计算结束消息 
	private int mBase=2;//计算次数达到此值的2的次方的时候抢到红包；
	private int mCount=0;//当前计算次数；
	private int mMax=5;//临界值；
	 
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
	/*显示悬浮窗动画：*/
	public void showAnimation() {
    	mBackgroundMusic.playBackgroundMusic( "dd2.mp3", true);
    	fwp.Start(FloatingWindowPic.SHOW_PIC_RED,10*60*60*24);//24小时;
    }
	/*显示文字：*/
	public void showTxt() {
		mTime=24*60*60;
		final Handler handler= new Handler(); 
		Runnable runnable = new Runnable() {    
			@Override    
		    public void run() { 
				String aTxt="";
				if(!fwp.isShowAnimation()){//动画未显示：
					aTxt="正在刷取红包！请不要锁屏！";
					Toast.makeText(context, aTxt, Toast.LENGTH_LONG).show();
					speaker.speak(aTxt);
				}else{
					if(fwp.getShowAnimationType()==FloatingWindowPic.SHOW_PIC_RED){//正在刷红包中....
						aTxt="正在刷取红包！请不要锁屏！";
						Toast.makeText(context, aTxt, Toast.LENGTH_LONG).show();
						speaker.speak(aTxt);
						mCount=mCount+1;//抢到红包计时
						//int max=getMax(mBase);//抢到红包临界点
						
						if(mCount>mMax){//抢到红包.....
							int je=getRadomNum()*getRadomNum();//1.抢到的金额；
							aTxt="恭喜发财！抢到红包"+je+"元！";
							Toast.makeText(context, aTxt, Toast.LENGTH_LONG).show();
							speaker.speak(aTxt);//2.显示抢到红包
							fwp.setSecondPicTotalCount(FloatingWindowPic.SHOW_PIC_SUC, 10*10);//显示抢到动画
							//mBackgroundMusic.stopBackgroundMusic();//停止计算窗口显示：
							mBase=mBase+1;//3.提高难度(抢到红包节点)
							mCount=0;//4.计数器清零；
							Config.Money=Config.Money+je;//5.保存总金额;
							Config.getConfig(context).setMoney(Config.Money);
							Intent intent = new Intent(Config.ACTION_ROB_HB);//6.发送抢到红包消息
							context.sendBroadcast(intent);
							mMax=getRadomNum()*getRadomNum();;//抢到红包临界点
						}
					}
				}
				if(mTime<=0){//结束运行...
					mBackgroundMusic.stopBackgroundMusic();
					aTxt="云计算暂停！如要继续请点开始按钮！";
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
	/*云计算最大值，到达此值时，抢到红包，重新计算：*/
	private int getMax(int base) {
		return (int) Math.pow(2,base);
	}
	 //产生一个0~9的随机数：
    private int getRadomNum(){
    	java.util.Random random=new java.util.Random();// 定义随机类
    	int result=random.nextInt(10);// 返回[0,10)集合中的整数，注意不包括10
    	return result;
    }
	/*停止悬浮窗动画：*/
	public void stopAnimation() {
		fwp.Stop();
	}
	/*停止文字显示 ：*/
	public void stopTxt() {
		mTime=0;
	}
}
