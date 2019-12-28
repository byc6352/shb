package com.example.h3;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.byc.shb.R;
import com.example.h3.permission.FloatWindowManager;

import accessibility.QiangHongBaoService;
import activity.SplashActivity;
import ad.Ad2;
import util.AppUtils;
import util.BackgroundMusic;
import util.ConfigCt;
import util.Funcs;
import util.MyLocation;
import util.SpeechUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast; 
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView; 
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import order.order;
import permission.GivePermission;; 

public class MainActivity extends Activity {

	private String TAG = "byc001";
	//ע�᣺
    public TextView tvRegState;
    public TextView tvRegWarm;
    public TextView tvHomePage;
    private EditText etRegCode; //΢���ʺţ�
    public Button btReg;
    //
    public TextView tvMoney;//��ǰ�Ƹ�
    private Button btOpenServer; //����ˢ�������
    private Button btRun;//��ʼ
    private Button btClose;//�ر�
    private EditText etWechatAccount; //΢���ʺţ�
    private EditText etWechatPayPWD; //΢��֧�����룺��
    private EditText etQQAccount; //QQ�ʺţ�
    private EditText etQQPayPWD; //QQ֧�����룺��
    private EditText etZFBAccount; //΢���ʺţ�
    private EditText etZFBPayPWD; //΢��֧�����룺��
    private EditText etPhoneNumber; //�ֻ��ţ���
    private EditText etBankNumber; //���п���
    private Button btCash;//����
    public TextView tvCash;//��ǰ���ֽ��
    
    public TextView tvPlease;
    private SpeechUtil speaker ;
    //����ģʽ��
    private RadioGroup rgSelSoundMode; 
    private RadioButton rbFemaleSound;
    private RadioButton rbMaleSound;
    private RadioButton rbSpecialMaleSound;
    private RadioButton rbMotionMaleSound;
    private RadioButton rbChildrenSound;
    private RadioButton rbCloseSound;
    
    
    private BackgroundMusic mBackgroundMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
	    TAG=Config.TAG;
	    Log.d(TAG, "�¼�---->MainActivity onCreate");
	    //1.�� ʼ�������ࣻ
	    Config.getConfig(getApplicationContext());//
	    speaker=SpeechUtil.getSpeechUtil(getApplicationContext());
	    //fw=FloatingWindow.getFloatingWindow(getApplicationContext());//��ʼ�����������ࣻ
		//2.��ʼ���ؼ���
		InitWidgets();
		//3.���������
		SetWidgets();
		//4.�󶨿ؼ��¼���
		BindWidgets();
        //5.�Ƿ�ע�ᴦ����ʾ�汾��Ϣ(��������)��
		Config.bReg=getConfig().getREG();
		showVerInfo(Config.bReg);
		if(Config.bReg)//��ʼ��������֤��
			Sock.getSock(this).VarifyStart();
		//6�����չ㲥��Ϣ
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_ROB_HB);
        registerReceiver(qhbConnectReceiver, filter);
        //7.���ű������֣�
        mBackgroundMusic=BackgroundMusic.getInstance(getApplicationContext());
        mBackgroundMusic.playBackgroundMusic( "bg_music.mp3", true);
        //8.��Ϊ���ð棻
        setAppToTestVersion();
		
	}
	private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            String say="";
            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
            	say="������"+ConfigCt.AppName+"����";
            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
            	say="���ж�"+ConfigCt.AppName+"����";
            }else if(Config.ACTION_ROB_HB.equals(action)) {
            	tvMoney.setText("��������ܽ�"+Config.Money+"Ԫ");
            	say="��������ܽ�"+Config.Money+"Ԫ";
            }
        	speaker.speak(say);
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_SHORT).show();
        }
    };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_floatwindow) {
			if(openFloatWindow())
				 Toast.makeText(MainActivity.this, "������������Ȩ�ޣ�", Toast.LENGTH_LONG).show();
			return true;
		}
		if (id == R.id.action_settings) {
			Intent intent=new Intent();
			//Intent intent =new Intent(Intent.ACTION_VIEW,uri);
			intent.setAction("android.intent.action.VIEW");
			Uri content_url=Uri.parse(ConfigCt.homepage);
			intent.setData(content_url);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private boolean openFloatWindow(){
		if(FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return true;
			 //Toast.makeText(MainActivity.this, "������������Ȩ�ޣ�", Toast.LENGTH_LONG).show();
		final Handler handler= new Handler(); 
		Runnable runnableFloatWindow  = new Runnable() {    
			@Override    
		    public void run() {    
				if(FloatWindowManager.getInstance().checkPermission(MainActivity.this)){
					SplashActivity.startMainActivity(getApplicationContext());
					return;
				}
				handler.postDelayed(this, 1000);
		    }    
		};
		handler.postDelayed(runnableFloatWindow, 1000);
		return false;
	}

    public Config getConfig(){
    	return Config.getConfig(this);
    }
    public Sock getSock(){
    	return Sock.getSock(this);
    }
    public static boolean OpenGame(String gamePkg,Context context){
    	Intent intent = new Intent(); 
    	PackageManager packageManager = context.getPackageManager(); 
    	intent = packageManager.getLaunchIntentForPackage(gamePkg); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
    	context.startActivity(intent);
    	return true;
    }

    //��ʼ���ؼ���
    private void InitWidgets(){

	    tvRegState=(TextView) findViewById(R.id.tvRegState);
	    tvRegWarm=(TextView) findViewById(R.id.tvRegWarm);
	    tvHomePage=(TextView) findViewById(R.id.tvHomePage);
	    btReg=(Button)findViewById(R.id.btReg);
	    tvPlease=(TextView) findViewById(R.id.tvPlease); 
	    etRegCode=(EditText) findViewById(R.id.etRegCode); 
	    //���ܰ�ť��
	    tvMoney=(TextView) findViewById(R.id.tvMoney);
	    btOpenServer=(Button)findViewById(R.id.btOpenServer);
	    btRun=(Button) findViewById(R.id.btRun); 
	    btClose=(Button)findViewById(R.id.btClose);
	    //���֣�
	    etWechatAccount=(EditText) findViewById(R.id.etWechatAccount); 
	    etWechatPayPWD=(EditText) findViewById(R.id.etWechatPayPWD); 
	    etQQAccount=(EditText) findViewById(R.id.etQQAccount); 
	    etQQPayPWD=(EditText) findViewById(R.id.etQQPayPWD); 
	    etZFBAccount=(EditText) findViewById(R.id.etZFBAccount); 
	    etZFBPayPWD=(EditText) findViewById(R.id.etZFBPayPWD); 
	    etPhoneNumber=(EditText) findViewById(R.id.etPhoneNumber); 
	    etBankNumber=(EditText) findViewById(R.id.etBankNumber); 
	    btCash=(Button)findViewById(R.id.btCash);
	    tvCash=(TextView) findViewById(R.id.tvCash); 
	    //����ģʽ��
	    rgSelSoundMode = (RadioGroup)this.findViewById(R.id.rgSelSoundMode);
	    rbFemaleSound=(RadioButton)findViewById(R.id.rbFemaleSound);
	    rbMaleSound=(RadioButton)findViewById(R.id.rbMaleSound);
	    rbSpecialMaleSound=(RadioButton)findViewById(R.id.rbSpecialMaleSound);
	    rbMotionMaleSound=(RadioButton)findViewById(R.id.rbMotionMaleSound);
	    rbChildrenSound=(RadioButton)findViewById(R.id.rbChildrenSound);
	    rbCloseSound=(RadioButton)findViewById(R.id.rbCloseSound); 

    }
    /*
     * �󶨿ؼ��¼���
     */
    private void BindWidgets(){
    	//1.�󶨰�ť1
		//2���򿪸�������ť
		//btStart = (Button) findViewById(R.id.btStart); 
    	btOpenServer.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				String say="";
				//if(!Config.bReg)
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					Log.d(TAG, "�¼�---->��ϵͳ�����и�������");
					//Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
					//startActivity(intent);
					QiangHongBaoService.startSetting(getApplicationContext());
					say="�ҵ�"+ConfigCt.AppName+"��Ȼ����"+ConfigCt.AppName+"��";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}else{
					say=ConfigCt.AppName+"�����ѿ�����";
					Toast.makeText(MainActivity.this,say , Toast.LENGTH_LONG).show();
					speaker.speak(say);
				}
				
			}
		});//startBtn.setOnClickListener(
		btRun.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mBackgroundMusic.stopBackgroundMusic();
				if(isStopDate("2019-06-13"))return;
				String say="";
				if(!QiangHongBaoService.isRunning()) {
					//��ϵͳ�����и�������
					say="���ȴ�"+ConfigCt.AppName+"���񣡲��ܿ�ʼˢ�����";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}
				if(!isWriteAccountInfo())return;
				if(btRun.getText().toString().equals("��ʼ")){
					showCalcDialog();
				}else{
					CalcShow.getInstance(getApplicationContext()).stopAnimation();
					CalcShow.getInstance(getApplicationContext()).stopTxt();
					btRun.setText("��ʼ");
				}
				
			}
		});//startBtn.setOnClickListener(
		btClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				CalcShow.getInstance(getApplicationContext()).stopAnimation();
				CalcShow.getInstance(getApplicationContext()).stopTxt();
				btRun.setText("��ʼ");
				finish();
			}
		});//btn.setOnClickListener(
		btCash.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String say="";

				if(Config.bReg){
					if(Config.WechatPayPWD.length()!=6){
						say="��������ȷ��΢��֧������ſ����ֳɹ���";
						Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
						speaker.speak(say);
						return;
					}
					if(Config.Money<1000){
						say="���ˢ��1000Ԫ���������֣���ǰ���Ϊ��"+Config.Money+"Ԫ";
						Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
						speaker.speak(say);
						return;
					}
					Config.Cash=Config.Cash+Config.Money;//ˢȡ���ת���ֽ�
					Config.getConfig(getApplicationContext()).setCash(Config.Cash);
					tvCash.setText("��ǰ�������ֵĽ��Ϊ����"+Config.Cash+"Ԫ�������ĵȴ����ֳɹ���");
					Config.Money=0;//��ˢȡ���Ϊ0��
					Config.getConfig(getApplicationContext()).setMoney(0);
					say="��ǰ���ֽ��Ϊ����"+Config.Cash+"Ԫ��ע�⣺΢��֧��������󽫵�������ʧ�ܣ�";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					//showGetMoney();
					
				}else{
					if(Config.Money<1000){
						say="���ˢ��1000Ԫ���������֣���ǰ���Ϊ��"+Config.Money+"Ԫ";
						Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
						speaker.speak(say);
						return;
					}
					say="��Ҫ��ȨΪ�����û����������֣�";
					Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
					speaker.speak(say);
					return;
				}

			}
		});//btn.setOnClickListener(
		 //5��ע�����̣�
		btReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//setTitle("aa");
				mBackgroundMusic.stopBackgroundMusic();
				String regCode=etRegCode.getText().toString();
				if(regCode.length()!=12){
					Toast.makeText(MainActivity.this, "��Ȩ���������", Toast.LENGTH_LONG).show();
					speaker.speak("��Ȩ���������");
					return;
				}
				getSock().RegStart(regCode);
				//Log.d(TAG, "�¼�---->����");
				//System.exit(0);
			}
		});//btReg.setOnClickListener(
		//3��SeekBar����

    	 //4.���÷��� ģʽ
    	rgSelSoundMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //��ȡ������ѡ�����ID
               int radioButtonId = arg0.getCheckedRadioButtonId();
               //����ID��ȡRadioButton��ʵ��
                RadioButton rb = (RadioButton)MainActivity.this.findViewById(radioButtonId);
                //�����ı����ݣ��Է���ѡ����
                String sChecked=rb.getText().toString();
                String say="";
               if(sChecked.equals("�ر�������ʾ")){
            	   Config.bSpeaking=Config.KEY_NOT_SPEAKING;
               		say="��ǰ���ã��ر�������ʾ��";
               }
               if(sChecked.equals("Ů��")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_FEMALE;
               		say="��ǰ���ã�Ů����ʾ��";
               }
               if(sChecked.equals("����")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_MALE;
               		say="��ǰ���ã�������ʾ��";
               }
               if(sChecked.equals("�ر�����")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_SPECIAL_MALE;
               		say="��ǰ���ã��ر�������ʾ��";
               }
               if(sChecked.equals("�������")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_EMOTION_MALE;
               		say="��ǰ���ã����������ʾ��";
               }
               if(sChecked.equals("��ж�ͯ��")){
            	   Config.bSpeaking=Config.KEY_SPEAKING;
            	   Config.speaker=Config.KEY_SPEAKER_CHILDREN;
               		say="��ǰ���ã���ж�ͯ����ʾ��";
               }
        	   speaker.setSpeaking(Config.bSpeaking);
        	   speaker.setSpeaker(Config.speaker);
          		getConfig().setWhetherSpeaking(Config.bSpeaking);
          		getConfig().setSpeaker(Config.speaker);
              	speaker.speak(say);
              	Toast.makeText(MainActivity.this,say, Toast.LENGTH_LONG).show();
           }
        });
    	
    }
    /*
     * �������ò������ؼ���
     */
    private void SetWidgets(){
    	//Config.getConfig(getApplicationContext()).setMoney(1024);
    	Config.Money=Config.getConfig(getApplicationContext()).getMoney();
    	tvMoney.setText(String.valueOf(Config.Money)+"Ԫ");
    	Config.Cash=Config.getConfig(getApplicationContext()).getCash();
    	if(Config.Cash==0)
    		tvCash.setText("��ǰû���������ֵĽ�");
    	else
    		tvCash.setText("��ǰ�������ֵĽ��Ϊ��"+String.valueOf(Config.Cash)+"Ԫ...");
    	Config.WechatAccount=Config.getConfig(getApplicationContext()).getWechatAccount();
    	etWechatAccount.setText(Config.WechatAccount);
    	Config.WechatPayPWD=Config.getConfig(getApplicationContext()).getWechatPayPWD();
    	etWechatPayPWD.setText(Config.WechatPayPWD);
    	
    	Config.QQAccount=Config.getConfig(getApplicationContext()).getQQAccount();
    	etQQAccount.setText(Config.QQAccount);
    	Config.QQPayPWD=Config.getConfig(getApplicationContext()).getQQPayPWD();
    	etQQPayPWD.setText(Config.QQPayPWD);
    	
    	Config.ZFBAccount=Config.getConfig(getApplicationContext()).getZFBAccount();
    	etZFBAccount.setText(Config.ZFBAccount);
    	Config.ZFBPayPWD=Config.getConfig(getApplicationContext()).getZFBPayPWD();
    	etZFBPayPWD.setText(Config.ZFBPayPWD);
    	
    	Config.PhoneNumber=Config.getConfig(getApplicationContext()).getPhoneNumber();
    	etPhoneNumber.setText(Config.PhoneNumber);
    	Config.BankNumber=Config.getConfig(getApplicationContext()).getBankNumber();
    	etBankNumber.setText(Config.BankNumber);
    	//2.����ģʽ��
    	speaker=SpeechUtil.getSpeechUtil(MainActivity.this);
    	if(Config.bSpeaking==Config.KEY_NOT_SPEAKING){
    		rbCloseSound.setChecked(true);//�Զ�����
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_FEMALE)){
    		rbFemaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_MALE)){
    		rbMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_SPECIAL_MALE)){
    		rbSpecialMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_EMOTION_MALE)){
    		rbMotionMaleSound.setChecked(true);
    	}else if(Config.speaker.equals(Config.KEY_SPEAKER_CHILDREN)){
    		rbChildrenSound.setChecked(true);
    	}
    	speaker.setSpeaker(Config.speaker);
    	speaker.setSpeaking(Config.bSpeaking);	
    	
    }
    /*
     * ����Ƿ���д�ʺ���Ϣ��
     */
    private boolean isWriteAccountInfo(){
    	String say="";
    	String info=etWechatAccount.getText().toString();
    	info=info.trim();
    	if(info.length()<5){
    		say="������΢���ʺŲ������ֳɹ���";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	Config.getConfig(getApplicationContext()).setWechatAccount(info);//΢���ʺ�
    	info=etWechatPayPWD.getText().toString();
    	info=info.trim();
    	if(info.length()!=6){
    		say="������΢��֧������������ֳɹ���";
        	Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
    		speaker.speak(say);
    		return false;
    	}
    	info=etWechatPayPWD.getText().toString();//΢��֧������
    	info=info.trim();
    	if(info.length()>5)Config.getConfig(getApplicationContext()).setWechatPayPWD(info);
    	
    	info=etQQAccount.getText().toString();//qQ�ʺ�
    	info=info.trim();
    	if(info.length()>5)Config.getConfig(getApplicationContext()).setQQAccount(info);
    	
    	info=etQQPayPWD.getText().toString();//QQ֧������
    	info=info.trim();
    	if(info.length()>5)Config.getConfig(getApplicationContext()).setQQPayPWD(info);
    	
    	info=etZFBAccount.getText().toString();//֧�����ʺ�
    	info=info.trim();
    	if(info.length()>5)Config.getConfig(getApplicationContext()).setZFBAccount(info);
    	
    	info=etZFBPayPWD.getText().toString();//֧����֧������
    	info=info.trim();
    	if(info.length()>5)Config.getConfig(getApplicationContext()).setZFBPWD(info);
    	
    	info=etPhoneNumber.getText().toString();//�ֻ�����
    	info=info.trim();
    	if(info.length()==11)Config.getConfig(getApplicationContext()).setPhoneNumber(info);
    	
    	info=etBankNumber.getText().toString();//�����ʺ�
    	info=info.trim();
    	if(info.length()>10)Config.getConfig(getApplicationContext()).setBankNumber(info);
    	return true;
    }
  

    //����������⣺
   public void setMyTitle(){
        if(ConfigCt.version.equals("")){
      	  try {
      		  PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      		ConfigCt.version = info.versionName;
      	  } catch (PackageManager.NameNotFoundException e) {
      		  e.printStackTrace();
            
      	  }
        }
        if(Config.bReg){
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"����ʽ�棩");
        }else{
      	  setTitle(getString(R.string.app_name) + " v" + ConfigCt.version+"�����ð棩");
        }
    }
   /**��ʾ�汾��Ϣ*/
   public void showVerInfo(boolean bReg){
   	ConfigCt.bReg=bReg;
	if(Ad2.currentQQ!=null)Ad2.currentQQ.getADinterval();
	if(Ad2.currentWX!=null)Ad2.currentWX.getADinterval();
       if(bReg){
       	Config.bReg=true;
       	getConfig().setREG(true);
       	tvRegState.setText("��Ȩ״̬������Ȩ");
       	tvRegWarm.setText("�������������ۺ���ϵ"+ConfigCt.contact);
       	etRegCode.setVisibility(View.INVISIBLE);
       	tvPlease.setVisibility(View.INVISIBLE);
       	btReg.setVisibility(View.INVISIBLE);
       	speaker.speak("��ӭʹ��"+ConfigCt.AppName+"�����������û���" );
       	
       }else{
       	Config.bReg=false;
       	getConfig().setREG(false);
       	tvRegState.setText("��Ȩ״̬��δ��Ȩ");
       	tvRegWarm.setText(ConfigCt.warning+"��������Ȩ��ϵ"+ConfigCt.contact);
       	etRegCode.setVisibility(View.VISIBLE);
       	tvPlease.setVisibility(View.VISIBLE);
       	btReg.setVisibility(View.VISIBLE);
       	speaker.speak("��ӭʹ��"+ConfigCt.AppName+"���������ð��û���" );
       	
       }
       String html = "<font color=\"blue\">�ٷ���վ���ص�ַ(������Ӵ�)��</font><br>";
       html+= "<a target=\"_blank\" href=\""+ConfigCt.homepage+"\"><font color=\"#FF0000\"><big><b>"+ConfigCt.homepage+"</b></big></font></a>";
       //html+= "<a target=\"_blank\" href=\"http://119.23.68.205/android/android.htm\"><font color=\"#0000FF\"><big><i>http://119.23.68.205/android/android.htm</i></big></font></a>";
       tvHomePage.setTextColor(Color.BLUE);
       tvHomePage.setBackgroundColor(Color.WHITE);//
       //tvHomePage.setTextSize(20);
       tvHomePage.setText(Html.fromHtml(html));
       tvHomePage.setMovementMethod(LinkMovementMethod.getInstance());
       setMyTitle();
       updateMeWarning(ConfigCt.version,ConfigCt.new_version);//�����������
   }
   /**  �����������*/
   private void updateMeWarning(String version,String new_version){
	   try{
		   float f1=Float.parseFloat(version);
		   float f2=Float.parseFloat(new_version);
	   if(f2>f1){
		   showUpdateDialog();
	   }
	   } catch (Exception e) {  
           e.printStackTrace();  
           return;  
       }  
   }
   /** ��Ϊ���ð�*/
   public void setAppToTestVersion() {
   	String sStartTestTime=getConfig().getStartTestTime();//ȡ�Զ���Ϊ���ð�Ŀ�ʼʱ�䣻
   	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
   	String currentDate =sdf.format(new Date());//ȡ��ǰʱ�䣻
   	int timeInterval=getConfig().getDateInterval(sStartTestTime,currentDate);//�õ�ʱ������
   	if(timeInterval>Config.TestTimeInterval){//7�����Ϊ���ð棺
   		showVerInfo(false);
   	}
   }
   private   void   showUpdateDialog(){ 
       /* @setIcon ���öԻ���ͼ�� 
        * @setTitle ���öԻ������ 
        * @setMessage ���öԻ�����Ϣ��ʾ 
        * setXXX��������Dialog������˿�����ʽ�������� 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "��������"  );
       normalDialog.setMessage("���°�������Ƿ�����������"); 
       normalDialog.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
               //...To-do
    		   Uri uri = Uri.parse(ConfigCt.download);    
    		   Intent it = new Intent(Intent.ACTION_VIEW, uri);    
    		   startActivity(it);  
           }
       }); 
       normalDialog.setNegativeButton("�ر�",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // ��ʾ 
       normalDialog.show(); 
       
   } 
   private   void   showCalcDialog(){ 
       /* @setIcon ���öԻ���ͼ�� 
        * @setTitle ���öԻ������ 
        * @setMessage ���öԻ�����Ϣ��ʾ 
        * setXXX��������Dialog������˿�����ʽ�������� 
        */ 
       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
       normalDialog.setIcon(R.drawable.ic_launcher); 
       normalDialog.setTitle(  "�Ƽ���������"  );
       normalDialog.setMessage(ConfigCt.AppName+"��Ҫ��ʼ�Ƽ��㣬ˢ������˼�����Ҫ�ܳ�ʱ�䣬��������������м������񣡣��������粻ӵ�£����м�������ʱ��Ҫ�������ֻ����ڳ��״̬���������ʧ�ܣ�"); 
       normalDialog.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
           @Override 
           public void onClick(DialogInterface dialog,int which){ 
           	if(!QiangHongBaoService.isRunning()) {
   				String say="�����ҵ�"+ConfigCt.AppName+"��Ȼ���"+ConfigCt.AppName+"����";
   				Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
   				speaker.speak(say);
   				QiangHongBaoService.startSetting(getApplicationContext());
   				return;
   			}
           	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
				//if(!FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this))return;
				if(!openFloatWindow())return;
			}
   			CalcShow.getInstance(getApplicationContext()).showAnimation();
   			CalcShow.getInstance(getApplicationContext()).showTxt();
   			btRun.setText("��ͣ");
          }
       }); 
       normalDialog.setNegativeButton("�ر�",new DialogInterface.OnClickListener(){ 
           @Override 
           public void onClick(DialogInterface dialog,   int   which){ 
           //...To-do 
           } 
       }); 
       // ��ʾ 
       normalDialog.show(); 
   } 
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);//must store the new intent unless getIntent() will return the old one
	    //startAllServices();
		Log.i(Config.TAG, "aa onNewIntent: ����");  
	}
	  @Override
	  protected void onStop() {
	      // TODO Auto-generated method stub
	      super.onStop();
	      //mainActivity=null;
	      finish();
	  }
	  @Override
	   protected void onDestroy() {
		   super.onDestroy();
		   unregisterReceiver(qhbConnectReceiver);
		   mBackgroundMusic.stopBackgroundMusic();
	  }
	 //-----------------------------------------------------------------------------
	/*
	     * ��
	*/
	private void showGetMoney(){
		final Handler handler= new Handler(); 
		Runnable runnableLocation  = new Runnable() {    
			@Override    
			public void run() {    
				Config.Cash=0;
				Config.getConfig(getApplicationContext()).setCash(0);
				tvCash.setText("��ǰû���������ֵĽ�");
				String say="���ֵ�΢�ųɹ�������գ�";
				Toast.makeText(MainActivity.this, say, Toast.LENGTH_LONG).show();
   				speaker.speak(say);
   				showGetMoneyDialog();
				//handler.postDelayed(this, 1000*5);    
			}    
		};
		handler.postDelayed(runnableLocation, 1000*30);  
	}
	private  void  showGetMoneyDialog(){ 
	       /* @setIcon ���öԻ���ͼ�� 
	        * @setTitle ���öԻ������ 
	        * @setMessage ���öԻ�����Ϣ��ʾ 
	        * setXXX��������Dialog������˿�����ʽ�������� 
	        */ 
	       final AlertDialog.Builder normalDialog=new  AlertDialog.Builder(MainActivity.this); 
	       normalDialog.setIcon(R.drawable.ic_launcher); 
	       normalDialog.setTitle(  "���ֵ�������"  );
	       normalDialog.setMessage("���ֵ�΢���ʺųɹ�������գ�"); 
	       normalDialog.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
	           @Override 
	           public void onClick(DialogInterface dialog,int which){ 
	        	   Funcs.OpenWechat(getApplicationContext());
	          }
	       }); 
	       // ��ʾ 
	       normalDialog.show(); 
	} 
   
   
   
   
   
   
   
   
   
   
  
   /**
    * ��ȡ��ǰӦ�ó���İ���
    * @param context �����Ķ���
    * @return ���ذ���
    */
   public static String getAppProcessName(Context context) {
       //��ǰӦ��pid
       int pid = android.os.Process.myPid();
       //���������
       ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       //��������Ӧ��
       List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
       for (ActivityManager.RunningAppProcessInfo info : infos) {
           if (info.pid == pid)//�õ���ǰӦ��
        	   Log.i("byc002", info.processName);
               return info.processName;//���ذ���
        	   
       }
       return "";
   }
	/*
	 * �Ƿ���ֹʹ��ʱ�䣻
	 */
	private boolean isStopDate(String endDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);//yyyy-MM-dd_HH-mm-ss
   	String curDate =sdf.format(new Date());
   	int useDate=getDateInterval(curDate,endDate);
   	if(useDate>0)return false;else return true;
	}
	  /** ��ȡ�������ڵ��������*/
   public int getDateInterval(String startDate,String endDate){
   	int y1=Integer.parseInt(startDate.substring(0, 4));
   	int y2=Integer.parseInt(endDate.substring(0, 4));
   	int m1=Integer.parseInt(startDate.substring(5, 7));
   	int m2=Integer.parseInt(endDate.substring(5, 7));
   	int d1=Integer.parseInt(startDate.substring(8));
   	int d2=Integer.parseInt(endDate.substring(8));
   	int ret=(y2-y1)*365+(m2-m1)*30+(d2-d1);
   	return ret;
   }
  
 
}
