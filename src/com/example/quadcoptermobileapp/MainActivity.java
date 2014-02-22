package com.example.quadcoptermobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity implements SensorEventListener{

	MyBringBackSurface ourSurfaceView;
	
	Canvas canvas;
	float hexHeight;
	float hexWidth;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	Sensor sX;
	Sensor sY;
	Sensor sZ;
	float xRot;
	float yRot;
	float zRot;
	SurfaceHolder ourHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
		ourSurfaceView = new MyBringBackSurface(this);	
		setContentView(ourSurfaceView);
		SensorManager sm =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if(sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).size()!=0){
			sX=sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(0);
			//sY=sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(1);
			//sZ=sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(2);
			sm.registerListener(this,sX,SensorManager.SENSOR_DELAY_NORMAL);
			//sm.registerListener(this,sY,SensorManager.SENSOR_DELAY_NORMAL);
			//sm.registerListener(this,sZ,SensorManager.SENSOR_DELAY_NORMAL);
			
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSurfaceView.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ourSurfaceView.resume();
	}

	
	public class MyBringBackSurface extends SurfaceView implements Runnable {
		
		
		Thread ourThread = null;
		boolean isRunning = false;
		
		public MyBringBackSurface(Context context) {
			super(context);
			ourHolder = getHolder();
			canvas = ourHolder.lockCanvas();
		}
		
		
		public void pause(){
			isRunning = false;
			while(true){
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;
		}
		
		public void resume(){
			isRunning = true;
			ourThread = new Thread(this);
			ourThread.start();
		}
		
		
		public void run() {
			// TODO Auto-generated method stub
			
				while(isRunning){
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(!ourHolder.getSurface().isValid()){
						continue;
					}
				
					Canvas canvas = ourHolder.lockCanvas();
					canvas.drawRGB(120,120,120);
					//Gets Basic Screen Parameters
					float width=canvas.getWidth();
					float height=canvas.getHeight();
					
					
					Paint paint = new Paint();
					paint.setStyle(Style.FILL);
					paint.setColor(Color.RED);
					Paint paint2 = new Paint();
					paint.setStyle(Style.FILL);
					paint.setColor(Color.BLUE);
					paint.setTextSize((float) 60);
					
					//canvas.drawCircle(500,500, 100, paint2);
					//canvas.drawCircle(500+event.values[0]*200,500+ event.values[0]*200, 100, paint);
					
					canvas.drawText("X Rotation: "+xRot*360/2/3.14, 100, 500, paint);
					canvas.drawText("Y Rotation: "+yRot*360/2/3.14, 100, 700, paint);
					canvas.drawText("Z Rotation: "+zRot*360/2/3.14, 100, 900, paint);
			
					
					mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
					mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
					
					
					ourHolder.unlockCanvasAndPost(canvas);	
					
			}
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.RED);
		Paint paint2 = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLUE);
		paint.setTextSize((float) 30);
		
		//canvas = ourHolder.lockCanvas();
		//canvas.drawCircle(500,500, 100, paint2);
		//canvas.drawCircle(500+event.values[0]*200,500+ event.values[0]*200, 100, paint);
		
		xRot=event.values[0];
		yRot=event.values[1];
		zRot=event.values[2];
	}
}

