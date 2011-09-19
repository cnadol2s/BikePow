package bike.pow.sensor;

import bike.pow.Helper;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SensorService extends Service implements SensorEventListener {

	private final static int SENSOR_BUFFER_LENGTH = 50;
	private IBinder binder = new SensorServiceBinder();
	private SensorManager sensorManager;
	private Sensor orientationSensor;
	private SensorRingBuffer sensorBuffer;
	private double rollPercentRaw;
	private double rollPercentAvg;
	private double rollPercentMed;
	
	public class SensorServiceBinder extends Binder implements ISensor {

		@Override
		public double getHillgradeRaw() {
			return SensorService.this.rollPercentRaw;
		}
		
		@Override
		public double computeAverageValue() {
			return SensorService.this.rollPercentAvg;
		}

		@Override
		public double computeMedianValue() {
			return SensorService.this.rollPercentMed;
		}
		
	}
	
	public void onCreate() {
		//this.init();
		super.onCreate();
	}
	
	private void init() {
		this.sensorBuffer = new SensorRingBuffer(this.SENSOR_BUFFER_LENGTH);
		this.sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		this.orientationSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		this.sensorManager.registerListener(this, this.orientationSensor, SensorManager.SENSOR_DELAY_UI);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		this.init();
		return SensorService.this.binder;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing to do
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			SensorService.this.sensorBuffer.insert(Helper.angle2HillGrade(event.values[1]));
			SensorService.this.rollPercentRaw = SensorService.this.sensorBuffer.getRawValue();
			SensorService.this.rollPercentAvg = SensorService.this.sensorBuffer.computeAverageValue();
			SensorService.this.rollPercentMed = SensorService.this.sensorBuffer.computeMedianValue();
		}
	}
	
	/*
	 * Deprecated! Check Helper class
	 */
	private double angle2HillGrade(float angle_rad) {
		double angle_deg = -angle_rad * Math.PI / 180;
		return Math.tan(angle_deg);	
	}

}
