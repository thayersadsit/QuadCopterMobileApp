package com.example.quadcoptermobileapp;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;




public class MainActivity extends Activity implements SensorEventListener, OnClickListener{

	public float xRot;
	public float yRot;
	public float zRot;
	private MyBringBackSurface ourSurfaceView;
	public Canvas canvas;
	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;

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
		 mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	      accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSurfaceView.pause();
		 mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
		ourSurfaceView.resume();
	}

	
	public class MyBringBackSurface extends SurfaceView implements Runnable {
		
		
		Thread ourThread = null;
		boolean isRunning = false;
		private SurfaceHolder ourHolder;
		
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
					
					ourHolder.unlockCanvasAndPost(canvas);	
					
			}
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	float[] mGravity = null;
	 float[] mGeomagnetic = null;
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		      mGravity = event.values;
		   
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		      mGeomagnetic = event.values;
		    if (mGravity != null && mGeomagnetic != null) {
		      float R[] = new float[9];
		      float I[] = new float[9];
		      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
		      if (success) {
		        float orientation[] = new float[3];
		        SensorManager.getOrientation(R, orientation);
		       zRot = orientation[0]; // orientation contains: azimut, pitch and roll
		       xRot = orientation[1];
		       yRot= orientation[2];
		      
		      }
		    }
		
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}


}

