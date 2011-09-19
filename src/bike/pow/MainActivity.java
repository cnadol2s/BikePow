
package bike.pow;

import bike.pow.R;
import bike.pow.gui.GeoScrollView;
import bike.pow.gui.SolverScrollView;
import bike.pow.gui.SolverTableLayout;
import bike.pow.gui.WeatherScrollView;
import bike.pow.gui.WeatherTableLayout;
import bike.pow.sensor.SensorService;
import bike.pow.weather.WeatherService;
import bike.pow.weather.data.GoogleWeather_Data;
import bike.pow.weather.data.WeatherBug_Data;
import bike.pow.weather.data.WeatherUnderground_Data;
import bike.pow.weather.data.WorldWeather_Data;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.SQLException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnClickListener, OnTouchListener, OnSeekBarChangeListener, SensorEventListener {
    
	private LinearLayout mainLayout;
	private GeoScrollView geoScrollView;
	private WeatherScrollView weatherScrollView;
	private SolverScrollView solverScrollView;
	private float downXValue;
	private float downYValue;
	private int geoOffset;
	private int weatherOffset;
	private int solverOffset;
	private Button startButton;
	private Button stopButton;
	private TextView statusView;
	private TextView providerListView;
	private TextView providerView;
	private TextView coordinatesView;
	private TextView addressView;
	private TextView speedView;
	private TextView bearingView;
	private TextView altitudeView;
	private TextView accuracyView;
	private MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
	private TextView weatherBugView;
	private TextView weatherBugDistanceView;
	private TextView googleWeatherView;
	private TextView weatherUndergroundView;
	private TextView weatherUndergroundDistanceView;
	private TextView worldWeatherView;
	private Button getWeatherButton;
	private EditText editLat;
	private EditText editLng;
	private double lat = 50.734139;		// dummy value
	private double lng = 7.09725;		// dummy value
	private WeatherService.WeatherServiceBinder weatherBinder;
	private SensorService.SensorServiceBinder sensorBinder;
	private double hillGradeRaw = 0.0;
	private double hillGradeAvg = 0.0;
	private double hillGradeMed = 0.0;
	private Handler handler = new Handler();
	private WeatherBug_Data weatherBug_Data;
	private GoogleWeather_Data googleWeather_Data;
	private WeatherUnderground_Data weatherUnderground_Data;
	private WorldWeather_Data worldWeather_Data;
	private double distanceToWeatherBugStation;
	private double distanceToWeatherUndergroundStation;
	private Solver solver;
	private Intent geoIntent;
	private WeatherTableLayout googleWeatherTable;
	private WeatherTableLayout weatherBugTable;
	private WeatherTableLayout weatherUndergroundTable;
	private WeatherTableLayout worldWeatherTable;
	private SolverTableLayout solverTable;
	
	// Preference screen
	private SensorManager prefSensorManager;
	private Sensor prefOrientationSensor;
	
	private double prefSensorValue = 0.0;
	
	private TextView pref_label_calibration_new;
	
	private EditText pref_edit_calibration;
	private EditText pref_edit_calibration_new;
	private EditText pref_edit_a1;
	private EditText pref_edit_wc;
	private EditText pref_edit_wm;
	private EditText pref_edit_r;
	private EditText pref_edit_t;
	
	private Button pref_button_calibration;
	private Button pref_button_calibration_reset;
	private Button pref_button_a1;
	private Button pref_button_wc;
	private Button pref_button_wm;
	private Button pref_button_r;
	private Button pref_button_t;
	private Button pref_button_defaults;
	
	private SeekBar pref_seek_a1;
	private SeekBar pref_seek_wc;
	private SeekBar pref_seek_wm;
	private SeekBar pref_seek_r;
	private SeekBar pref_seek_t;
	
	// Database
	private String timestampDB = "-";
	private String latDB = "-";
	private String lngDB = "-";
	private String altDB = "-";
	private String accuracyDB = "-";
	private String velocityDB = "-";
	private String headwindDB = "-";
	private String hillgraderawDB = "-";
	private String hillgradeavgDB = "-";
	private String hillgrademedDB = "-";
	private String bearingDB = "-";
	private String windspeedDB = "-";
	private String winddegreesDB = "-";
	private String forceDB = "-";
	private String poweroutputDB = "-";
	private String a1DB = "-";
	private String a2DB = "-";
	private String rDB = "-";
	private String tDB = "-";
	private String wcDB = "-";
	private String wmDB = "-";
	private DataBaseHelper mDataBaseHelper;
	
	
	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			MainActivity.this.geoIntent = intent;
			MainActivity.this.providerListView.setText(intent.getStringExtra("myProviderList"));
			MainActivity.this.providerView.setText(intent.getStringExtra("myProvider"));
			MainActivity.this.coordinatesView.setText(intent.getStringExtra("myCoordinates"));
			
			MainActivity.this.editLat.setText(intent.getDoubleExtra("myLatitude", -1) + "");
			MainActivity.this.latDB = Double.toString(intent.getDoubleExtra("myLatitude", -1));
			
			MainActivity.this.editLng.setText(intent.getDoubleExtra("myLongitude", -1) + "");
			MainActivity.this.lngDB = Double.toString(intent.getDoubleExtra("myLongitude", -1));
			
			MainActivity.this.addressView.setText(intent.getStringExtra("myAddress"));
			MainActivity.this.speedView.setText(intent.getStringExtra("mySpeed"));
			
			MainActivity.this.bearingView.setText(intent.getStringExtra("myBearing"));
			MainActivity.this.bearingDB = Double.toString(intent.getDoubleExtra("bearing", -1.0));
			
			MainActivity.this.altitudeView.setText(intent.getStringExtra("myAltitude"));
			MainActivity.this.altDB = Double.toString(intent.getDoubleExtra("altitude", -1.0));
			
			MainActivity.this.accuracyView.setText(intent.getStringExtra("myAccuracy"));
			MainActivity.this.accuracyDB = Double.toString(Helper.formatDouble(intent.getDoubleExtra("accuracy", -1.0)));
			
			MainActivity.this.startWeatherService();
			
			MainActivity.this.startSensorService();
			
			MainActivity.this.startSolverService();
			
			// Database Insert
			MainActivity.this.feedDatabase();
		}
	};
	
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName cname) {
			if (cname.getClassName().equals("bike.pow.weather.WeatherService")) {
				MainActivity.this.weatherBinder = null;
			}
			if (cname.getClassName().equals("bike.pow.sensor.SensorService")) {
				MainActivity.this.sensorBinder = null;
			}
		}
		@Override
		public void onServiceConnected(ComponentName cname, IBinder service) {
			if (cname.getClassName().equals("bike.pow.weather.WeatherService")) {
				MainActivity.this.weatherBinder = (WeatherService.WeatherServiceBinder) service;
			}
			if (cname.getClassName().equals("bike.pow.sensor.SensorService")) {
				MainActivity.this.sensorBinder = (SensorService.SensorServiceBinder) service;
			}
		}
	};
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.setViews();
        this.setViewListeners();
        
        // Init Physical Model
        this.solver = new Solver();
        
        // Init GeoService (Local Sevice with Callback)
        this.getApplicationContext().registerReceiver(
        		this.broadcastReceiver,
        		new IntentFilter(GeoService.SERVICE_GEO_ACTION));
        
        // Read custom coordinates
        try {
        	this.lat = Double.parseDouble(this.editLat.getText().toString());
        	this.lng = Double.parseDouble(this.editLng.getText().toString());
        } catch (Exception e) {
        	Log.e("MainActivity", "Invalid lat/lng values!", e);
        	this.lat = -1;
        	this.lng = -1;
        }
        
        // Init Database
        this.initDB();
     }
    
    private void setViews() {
    	this.mainLayout = (LinearLayout) findViewById(R.id.main_layout);
    	
    	this.geoScrollView = (GeoScrollView) findViewById(R.id.geo_scroll_view);
    	this.weatherScrollView = (WeatherScrollView) findViewById(R.id.weather_scroll_view);
    	this.solverScrollView = (SolverScrollView) findViewById(R.id.solver_scroll_view);
    	this.getWeatherButton = (Button) findViewById(R.id.getWeatherButton);
    	this.editLat = (EditText) findViewById(R.id.editLat);
    	this.editLng = (EditText) findViewById(R.id.editLng);

    	this.weatherBugView = (TextView) findViewById(R.id.weatherBugView);
    	this.weatherBugDistanceView = (TextView) findViewById(R.id.weatherBugDistanceView);
    	this.googleWeatherView = (TextView) findViewById(R.id.googleWeatherView);
    	this.weatherUndergroundView = (TextView) findViewById(R.id.weatherUndergroundView);
    	this.weatherUndergroundDistanceView = (TextView) findViewById(R.id.weatherUndergroundDistanceView);
    	this.worldWeatherView = (TextView) findViewById(R.id.worldWeatherView);

    	this.startButton = (Button) findViewById(R.id.startButton);
    	this.stopButton = (Button) findViewById(R.id.stopButton);

    	this.statusView = (TextView) findViewById(R.id.statusView);
    	this.providerListView = (TextView) findViewById(R.id.providerListView);
    	this.providerView = (TextView) findViewById(R.id.providerView);
    	this.coordinatesView = (TextView) findViewById(R.id.coordinatesView);
    	this.addressView = (TextView) findViewById(R.id.addressView);
    	this.speedView = (TextView) findViewById(R.id.speedView);
    	this.bearingView = (TextView) findViewById(R.id.bearingView);
    	this.altitudeView = (TextView) findViewById(R.id.altitudeView);
    	this.accuracyView = (TextView) findViewById(R.id.accuracyView);

    	this.googleWeatherTable = (WeatherTableLayout) findViewById(R.id.googleWeatherTable);
    	this.weatherBugTable = (WeatherTableLayout) findViewById(R.id.weatherBugTable);
    	this.weatherUndergroundTable = (WeatherTableLayout) findViewById(R.id.weatherUndergroundTable);
    	this.worldWeatherTable = (WeatherTableLayout) findViewById(R.id.worldWeatherTable);

    	this.solverTable = (SolverTableLayout) findViewById(R.id.solverTable);

    	
    	// Prefs
    	this.pref_label_calibration_new = (TextView) findViewById(R.id.pref_label_calibration_new);

    	this.pref_edit_calibration = (EditText) findViewById(R.id.pref_edit_calibration);
    	this.pref_edit_calibration_new = (EditText) findViewById(R.id.pref_edit_calibration_new);
    	this.pref_edit_a1 = (EditText) findViewById(R.id.pref_edit_a1);
    	this.pref_edit_wc = (EditText) findViewById(R.id.pref_edit_wc);
    	this.pref_edit_wm = (EditText) findViewById(R.id.pref_edit_wm);
    	this.pref_edit_r = (EditText) findViewById(R.id.pref_edit_r);
    	this.pref_edit_t = (EditText) findViewById(R.id.pref_edit_t);

    	this.pref_button_calibration = (Button) findViewById(R.id.pref_button_calibration);
    	this.pref_button_calibration_reset = (Button) findViewById(R.id.pref_button_calibration_reset);
    	this.pref_button_a1 = (Button) findViewById(R.id.pref_button_a1);
    	this.pref_button_wc = (Button) findViewById(R.id.pref_button_wc);
    	this.pref_button_wm = (Button) findViewById(R.id.pref_button_wm);
    	this.pref_button_r = (Button) findViewById(R.id.pref_button_r);
    	this.pref_button_t = (Button) findViewById(R.id.pref_button_t);
    	this.pref_button_defaults = (Button) findViewById(R.id.pref_button_defaults);

    	this.pref_seek_a1 = (SeekBar) findViewById(R.id.pref_seek_a1);
    	this.pref_seek_wm = (SeekBar) findViewById(R.id.pref_seek_wm);
    	this.pref_seek_wc = (SeekBar) findViewById(R.id.pref_seek_wc);
    	this.pref_seek_r = (SeekBar) findViewById(R.id.pref_seek_r);
    	this.pref_seek_t = (SeekBar) findViewById(R.id.pref_seek_t);


    }
    
    private void setViewListeners() {
    	this.mainLayout.setOnTouchListener(this);
    	this.geoScrollView.setOnTouchListener(this);
    	this.weatherScrollView.setOnTouchListener(this);
    	this.solverScrollView.setOnTouchListener(this);
    	this.getWeatherButton.setOnClickListener(this);
    	this.startButton.setOnClickListener(this);
    	this.stopButton.setOnClickListener(this);
    	
    	this.pref_button_calibration.setOnClickListener(this);
    	this.pref_button_calibration_reset.setOnClickListener(this);
    	this.pref_button_a1.setOnClickListener(this);
    	this.pref_button_wc.setOnClickListener(this);
    	this.pref_button_wm.setOnClickListener(this);
    	this.pref_button_r.setOnClickListener(this);
    	this.pref_button_t.setOnClickListener(this);
    	this.pref_button_defaults.setOnClickListener(this);

    	this.pref_seek_a1.setOnSeekBarChangeListener(this);
    	this.pref_seek_wm.setOnSeekBarChangeListener(this);
    	this.pref_seek_wc.setOnSeekBarChangeListener(this);
    	this.pref_seek_r.setOnSeekBarChangeListener(this);
    	this.pref_seek_t.setOnSeekBarChangeListener(this);
    }
    

	protected void onResume() {
    	Intent weatherIntent = new Intent(this, WeatherService.class);
    	this.bindService(weatherIntent, conn, Context.BIND_AUTO_CREATE);
    	Intent sensorIntent = new Intent(this, SensorService.class);
    	this.bindService(sensorIntent, conn, Context.BIND_AUTO_CREATE);
    	if (this.weatherBinder != null) {
    		Log.d("Init", "Main: onResume: initWeatherTasks()");
    		this.weatherBinder.initWeatherTasks();
    	}
    	super.onResume();
    }
    
	protected void onPause() {
    	try {
    		if (this.weatherBinder != null) {
        		this.weatherBinder.cancelWeatherTasks();
        		this.weatherBinder.cancelWeatherTimers();
        	}
    	} catch (IllegalStateException ise) {
    		Log.e("MyException", "Main: onPause: IllegalStateException: " + ise.getMessage());
    	} catch(RuntimeException e) {
    		Log.e("MyException", "Main: onPause: RuntimeException");
    	}
    	this.unbindService(conn);
    	super.onPause();
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
	}
    
    @Override
	public void onClick(View v) {
		int vis;
		double prog;
    	switch (v.getId()) {
		
		case R.id.startButton:
			this.startGeoService();
			break;
		
		case R.id.stopButton:
			this.stopGeoService();
			break;
		
		case R.id.getWeatherButton:
			this.startWeatherService();
			break;
			
		case R.id.pref_button_calibration:
			vis = this.pref_edit_calibration_new.getVisibility();
			
			if (vis == View.VISIBLE) {
				this.pref_edit_calibration_new.setVisibility(View.GONE);
				this.pref_label_calibration_new.setVisibility(View.GONE);
				this.pref_button_calibration_reset.setVisibility(View.GONE);
				this.pref_button_calibration.setText("Change");
				
				this.solver.setHillGradeOffset(this.prefSensorValue);
				this.pref_edit_calibration.setText(this.prefSensorValue + "");
				this.killPrefSensor();
			}
			if (vis == View.GONE) {
				this.pref_edit_calibration_new.setVisibility(View.VISIBLE);
				this.pref_label_calibration_new.setVisibility(View.VISIBLE);
				this.pref_button_calibration_reset.setVisibility(View.VISIBLE);
				this.pref_button_calibration.setText("Apply");
				this.initPrefSensor();
			}
			break;
			
		case R.id.pref_button_calibration_reset:
			
			this.solver.setHillGradeOffset(0.0);
			this.pref_edit_calibration.setText(this.solver.getHillGradeOffset() + "");
			
			this.pref_edit_calibration_new.setVisibility(View.GONE);
			this.pref_label_calibration_new.setVisibility(View.GONE);
			this.pref_button_calibration_reset.setVisibility(View.GONE);
			this.pref_button_calibration.setText("Change");
			
			break;
		
		case R.id.pref_button_a1:
			vis = this.pref_seek_a1.getVisibility();
			if (vis == View.VISIBLE) {
				this.pref_seek_a1.setVisibility(View.GONE);
				this.pref_button_a1.setText("Change");
				prog = this.pref_seek_a1.getProgress() / 100.0;
				this.solver.setAirResistanceLinear(prog);
			}
			if (vis == View.GONE) {
				this.pref_seek_a1.setVisibility(View.VISIBLE);
				this.pref_button_a1.setText("Apply");
			}
			break;
			
		case R.id.pref_button_wc:
			vis = this.pref_seek_wc.getVisibility();
			if (vis == View.VISIBLE) {
				this.pref_seek_wc.setVisibility(View.GONE);
				this.pref_button_wc.setText("Change");
				prog = this.pref_seek_wc.getProgress();
				this.solver.setWeightCyclist(prog);
			}
			if (vis == View.GONE) {
				this.pref_seek_wc.setVisibility(View.VISIBLE);
				this.pref_button_wc.setText("Apply");
			}
			break;
			
		case R.id.pref_button_wm:
			vis = this.pref_seek_wm.getVisibility();
			if (vis == View.VISIBLE) {
				this.pref_seek_wm.setVisibility(View.GONE);
				this.pref_button_wm.setText("Change");
				prog = this.pref_seek_wm.getProgress();
				this.solver.setWeightRest(prog);
			}
			if (vis == View.GONE) {
				this.pref_seek_wm.setVisibility(View.VISIBLE);
				this.pref_button_wm.setText("Apply");
			}
			break;
			
		case R.id.pref_button_r:
			vis = this.pref_seek_r.getVisibility();
			if (vis == View.VISIBLE) {
				this.pref_seek_r.setVisibility(View.GONE);
				this.pref_button_r.setText("Change");
				prog = this.pref_seek_r.getProgress() / 1000.0;
				this.solver.setRollingFriction(prog);
			}
			if (vis == View.GONE) {
				this.pref_seek_r.setVisibility(View.VISIBLE);
				this.pref_button_r.setText("Apply");
			}
			break;
			
		case R.id.pref_button_t:
			vis = this.pref_seek_t.getVisibility();
			if (vis == View.VISIBLE) {
				this.pref_seek_t.setVisibility(View.GONE);
				this.pref_button_t.setText("Change");
				prog = (this.pref_seek_t.getProgress() + 75) / 100.0;
				this.solver.setTransmissionEfficiency(prog);
			}
			if (vis == View.GONE) {
				this.pref_seek_t.setVisibility(View.VISIBLE);
				this.pref_button_t.setText("Apply");
			}
			break;
			
		case R.id.pref_button_defaults:
			this.solver.setHillGradeOffset(0.0);
			this.solver.setAirResistanceLinear(0.51);
			this.solver.setAirResistanceQuadratic(0.26);
			this.solver.setWeightCyclist(70);
			this.solver.setWeightRest(15);
			this.solver.setRollingFriction(0.004);
			this.solver.setTransmissionEfficiency(0.95);
			this.updatePrefGUI();
			break;
		}
	}
    
    private void updatePrefGUI() {
    	this.pref_edit_calibration.setText(this.solver.getHillGradeOffset() + "");
    	this.pref_edit_a1.setText(this.solver.getAirResistanceLinear() + "");
    	this.pref_edit_wc.setText(this.solver.getWeightCyclist() + "");
    	this.pref_edit_wm.setText(this.solver.getWeightEquipment() + "");
    	this.pref_edit_r.setText(this.solver.getRollingFriction() + "");
    	this.pref_edit_t.setText((this.solver.getTransmissionEfficiency() * 100) + "");
    }
    
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
        
        	case MotionEvent.ACTION_DOWN:
                downXValue = event.getX();
                downYValue = event.getY();
                geoOffset = this.geoScrollView.computeVerticalScrollOffset();
                weatherOffset = this.weatherScrollView.computeVerticalScrollOffset();
                solverOffset = this.solverScrollView.computeVerticalScrollOffset(); 
                break;

            case MotionEvent.ACTION_UP:
                float currentX = event.getX();            

                // going backwards: pushing stuff to the right
                if (downXValue < currentX - 90) {
                     ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewFlipper);
                     vf.setInAnimation(view.getContext(), R.anim.push_right_in);
                     vf.setOutAnimation(view.getContext(), R.anim.push_right_out);
                     vf.showPrevious();
                }

                // going forwards: pushing stuff to the left
                if (downXValue > currentX + 90) {
                	ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewFlipper);
                    vf.setInAnimation(view.getContext(), R.anim.push_left_in);
                    vf.setOutAnimation(view.getContext(), R.anim.push_left_out);
                    vf.showNext();
                }
                break;
                
            case MotionEvent.ACTION_MOVE:
				float curYValue = event.getY();
				float deltaY = curYValue - downYValue;

				if (view.getId() == this.geoScrollView.getId()) {
					this.geoScrollView.smoothScrollTo(0, geoOffset - (int)deltaY);
				}
				if (view.getId() == this.weatherScrollView.getId()) {
					this.weatherScrollView.smoothScrollTo(0, weatherOffset - (int)deltaY);
				}
				if (view.getId() == this.solverScrollView.getId()) {
					this.solverScrollView.smoothScrollTo(0, solverOffset - (int)deltaY);
				}
				break;
            }
                
        // if you return false, these actions will not be recorded
        return true;
    }
    
    @Override
	public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
    	switch (bar.getId()) {
		
		case R.id.pref_seek_a1:
			double a1 = bar.getProgress() / 100.0;
			this.pref_edit_a1.setText(a1 + "");
			break;
			
		case R.id.pref_seek_wc:
			int wc = bar.getProgress();
			this.pref_edit_wc.setText(wc + "");
			break;
			
		case R.id.pref_seek_wm:
			int wm = bar.getProgress();
			this.pref_edit_wm.setText(wm + "");
			break;
			
		case R.id.pref_seek_r:
			double r = bar.getProgress() / 1000.0;
			this.pref_edit_r.setText(r + "");
			break;
			
		case R.id.pref_seek_t:
			int t = bar.getProgress() + 75;
			this.pref_edit_t.setText(t + "");
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar bar) {
		// Nothing to do
	}

	@Override
	public void onStopTrackingTouch(SeekBar bar) {
		// Nothing to do
	}
    
    private void startGeoService() {
		ComponentName cn = this.startService(new Intent(this, GeoService.class));
		if (cn != null) {
			this.statusView.setText("Service started");
		} else {
			this.statusView.setText("Service start failed");
		}
	}
	
	private void stopGeoService() {
		boolean stopped = this.stopService(new Intent(this, GeoService.class));
		if (stopped) {
			this.statusView.setText("Service stopped");
		} else {
			this.statusView.setText("Service already stopped");
		}
	}
    
    private void startWeatherService() {
    	try {
        	this.lat = Double.parseDouble(this.editLat.getText().toString());
        	this.lng = Double.parseDouble(this.editLng.getText().toString());
        
		} catch (Exception e) {
        	this.lat = -1;
        	this.lng = -1;
        }
        
		Thread weatherThread = new Thread(null, doGetWeatherData, "WeatherDataThread");
		weatherThread.start();
    }
    
    private Runnable doGetWeatherData = new Runnable() {
    	@Override
		public void run() {
			MainActivity.this.getWeatherData();
		}
	};

	private void getWeatherData() {
		if (this.lat != -1 && this.lng != -1) {
			
			if (this.weatherBinder != null) {
				MainActivity.this.weatherBug_Data = this.weatherBinder.getWeatherBugData(this.lat, this.lng);
				MainActivity.this.googleWeather_Data = this.weatherBinder.getGoogleWeatherData(this.lat, this.lng);
				MainActivity.this.weatherUnderground_Data = this.weatherBinder.getWeatherUndergroundData(this.lat, this.lng);
				MainActivity.this.worldWeather_Data = this.weatherBinder.getWorldWeatherData(this.lat, this.lng);
			}
			
			if (MainActivity.this.weatherBug_Data != null) {
				MainActivity.this.distanceToWeatherBugStation = MainActivity.this.weatherBug_Data.distanceToStation(this.lat, this.lng);
			}
			
			if (MainActivity.this.weatherUnderground_Data != null) {
				MainActivity.this.distanceToWeatherUndergroundStation = MainActivity.this.weatherUnderground_Data.distanceToStation(this.lat, this.lng);
			}
			
			handler.post(doRefreshGUI);
		}
	}
	
	private Runnable doRefreshGUI = new Runnable() {
		@Override
		public void run() {
			MainActivity.this.refreshGUI();
		}
	};
	
	private void refreshGUI() {
		TextView tv;
		
		// WeatherBug
		if (MainActivity.this.weatherBug_Data != null) {
			MainActivity.this.weatherBugView.setText(weatherBug_Data.toString());
			MainActivity.this.weatherBugDistanceView.setText("Distance: " + this.distanceToWeatherBugStation + " km");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.temperatureView);
			tv.setText(this.weatherBug_Data.getTemperature() + "° C");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.humidityView);
			tv.setText(this.weatherBug_Data.getHumidity() + "%");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.pressureView);
			tv.setText(this.weatherBug_Data.getPressure() + " mbar");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.windSpeedView);
			tv.setText(this.weatherBug_Data.getWindSpeed() + " m/s");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.windDirectionView);
			tv.setText(this.weatherBug_Data.getWindDirection() + "");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.windDegreesView);
			tv.setText(this.weatherBug_Data.getWindDirectionDegrees() + "°");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.stationView);
			tv.setText(this.weatherBug_Data.getStationShort() + "");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.stationDistanceView);
			tv.setText(MainActivity.this.distanceToWeatherBugStation + " km");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.stationLatView);
			tv.setText(this.weatherBug_Data.getStationLatitude() + "");
			tv = (TextView) MainActivity.this.weatherBugTable.findViewById(R.id.stationLngView);
			tv.setText(this.weatherBug_Data.getStationLongitude() + "");
		}
		
		// Google Weather
		if (googleWeather_Data != null) {
			MainActivity.this.googleWeatherView.setText(googleWeather_Data.toString());
			tv = (TextView) MainActivity.this.googleWeatherTable.findViewById(R.id.temperatureView);
			tv.setText(this.googleWeather_Data.getTemperature() + "° C");
			tv = (TextView) MainActivity.this.googleWeatherTable.findViewById(R.id.humidityView);
			tv.setText(this.googleWeather_Data.getHumidity() + "%");
			tv = (TextView) MainActivity.this.googleWeatherTable.findViewById(R.id.windSpeedView);
			tv.setText(this.googleWeather_Data.getWindSpeed() + " m/s");
			tv = (TextView) MainActivity.this.googleWeatherTable.findViewById(R.id.windDirectionView);
			tv.setText(this.googleWeather_Data.getWindDirection() + "");
			tv = (TextView) MainActivity.this.googleWeatherTable.findViewById(R.id.windDegreesView);
		}
		
		// Weather Underground
		if (weatherUnderground_Data != null) {
			MainActivity.this.weatherUndergroundView.setText(weatherUnderground_Data.toString());
			MainActivity.this.weatherUndergroundDistanceView.setText("Distance: " + this.distanceToWeatherUndergroundStation + " km");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.temperatureView);
			tv.setText(this.weatherUnderground_Data.getTemperature() + "° C");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.humidityView);
			tv.setText(this.weatherUnderground_Data.getHumidity() + "%");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.pressureView);
			tv.setText(this.weatherUnderground_Data.getPressure() + " mbar");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.windSpeedView);
			tv.setText(this.weatherUnderground_Data.getWindSpeed() + " m/s");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.windDirectionView);
			tv.setText(this.weatherUnderground_Data.getWindDirection() + "");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.windDegreesView);
			tv.setText(this.weatherUnderground_Data.getWindDegrees() + "°");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.stationView);
			tv.setText(this.weatherUnderground_Data.getStationShort() + "");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.stationDistanceView);
			tv.setText(MainActivity.this.distanceToWeatherUndergroundStation + " km");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.stationLatView);
			tv.setText(this.weatherUnderground_Data.getStationLatitude() + "");
			tv = (TextView) MainActivity.this.weatherUndergroundTable.findViewById(R.id.stationLngView);
			tv.setText(this.weatherUnderground_Data.getStationLongitude() + "");
		}
		
		// World Weather
		if (worldWeather_Data != null) {
			MainActivity.this.worldWeatherView.setText(worldWeather_Data.toString());
			tv = (TextView) MainActivity.this.worldWeatherTable.findViewById(R.id.temperatureView);
			tv.setText(this.worldWeather_Data.getTemperature() + "° C");
			tv = (TextView) MainActivity.this.worldWeatherTable.findViewById(R.id.humidityView);
			tv.setText(this.worldWeather_Data.getHumidity() + "%");
			tv = (TextView) MainActivity.this.worldWeatherTable.findViewById(R.id.pressureView);
			tv.setText(this.worldWeather_Data.getPressure() + " mbar");
			tv = (TextView) MainActivity.this.worldWeatherTable.findViewById(R.id.windSpeedView);
			tv.setText(this.worldWeather_Data.getWindSpeed() + " m/s");
			tv = (TextView) MainActivity.this.worldWeatherTable.findViewById(R.id.windDirectionView);
			tv.setText(this.worldWeather_Data.getWindDirection() + "");
			tv = (TextView) MainActivity.this.worldWeatherTable.findViewById(R.id.windDegreesView);
			tv.setText(this.worldWeather_Data.getWindDegrees() + "°");
		}
	}
	
	private void startSolverService() {
    	Thread solverThread = new Thread(null, doSolving, "SolverThread");
		solverThread.start();
	}
    
    private Runnable doSolving = new Runnable() {
		@Override
		public void run() {
			MainActivity.this.solve();
		}
	};
	
	private void solve() {
		if (this.geoIntent != null) {
			
			this.solver.setVelocity(Helper.formatDouble(this.geoIntent.getDoubleExtra("speed", 0.0)));
			MainActivity.this.velocityDB = Double.toString(Helper.formatDouble(this.geoIntent.getDoubleExtra("speed", 0.0)));
			
			this.solver.setBearing(this.geoIntent.getDoubleExtra("bearing", 0.0));
			
			this.solver.setHillGrade(this.hillGradeMed);
			
			MainActivity.this.hillgraderawDB = Double.toString(Helper.formatDouble(this.hillGradeRaw - this.solver.getHillGradeOffset()));
			MainActivity.this.hillgradeavgDB = Double.toString(Helper.formatDouble(this.hillGradeAvg - this.solver.getHillGradeOffset()));
			MainActivity.this.hillgrademedDB = Double.toString(Helper.formatDouble(this.hillGradeMed - this.solver.getHillGradeOffset()));
			
			if (this.weatherUnderground_Data != null) {
				
				this.solver.setWindSpeed(this.weatherUnderground_Data.getWindSpeed());
				MainActivity.this.windspeedDB = Double.toString(Helper.formatDouble(this.weatherUnderground_Data.getWindSpeed()));
				
				this.solver.setWindDegrees(this.weatherUnderground_Data.getWindDegrees());
				MainActivity.this.winddegreesDB = Double.toString(this.weatherUnderground_Data.getWindDegrees());
			}
			// More solver params here ...
			this.solver.computeHeadWind();
			MainActivity.this.headwindDB = Double.toString(this.solver.getHeadwind());
			
			this.solver.computeForce();
			MainActivity.this.forceDB = Double.toString(this.solver.getForce());
			
			this.solver.computePowerOutput();
			MainActivity.this.poweroutputDB = Double.toString(this.solver.getPowerOutput());
			
			// DB Extras: static parameters
			MainActivity.this.a1DB = Double.toString(this.solver.getAirResistanceLinear());
			MainActivity.this.a2DB = Double.toString(this.solver.getAirResistanceQuadratic());
			MainActivity.this.rDB = Double.toString(this.solver.getRollingFriction());
			MainActivity.this.a1DB = Double.toString(this.solver.getAirResistanceLinear());
			MainActivity.this.tDB = Double.toString(this.solver.getTransmissionEfficiency());
			MainActivity.this.wcDB = Double.toString(this.solver.getWeightCyclist());
			MainActivity.this.wmDB = Double.toString(this.solver.getWeightEquipment());
			
		}
		handler.post(doRefreshSolverGUI);
	}
	
	private Runnable doRefreshSolverGUI = new Runnable() {
		@Override
		public void run() {
			MainActivity.this.refreshSolverGUI();
		}
	};
	
	private void refreshSolverGUI() {
		TextView tv;
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.forceView);
		tv.setText(this.solver.getForce() + " N");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.powerOutputView);
		tv.setText(this.solver.getPowerOutput() +" Watt");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.a1View);
		tv.setText(this.solver.getAirResistanceLinear() +" m^2");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.a2View);
		tv.setText(this.solver.getAirResistanceQuadratic() +"");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.velocityView);
		tv.setText(this.solver.getVelocity() +" m/s");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.headWindView);
		tv.setText(this.solver.getHeadwind() +" m/s");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.rollingFrictionView);
		tv.setText(this.solver.getRollingFriction() +"");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.hillGradeView);
		tv.setText(this.solver.getHillGrade() +"");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.weightCyclistView);
		tv.setText(this.solver.getWeightCyclist() +" kg");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.weightEquipmentView);
		tv.setText(this.solver.getWeightEquipment() +" kg");
		tv = (TextView) MainActivity.this.solverTable.findViewById(R.id.transmissionEfficiencyView);
		tv.setText(this.solver.getTransmissionEfficiency() +"");
	}
	
	private void startSensorService() {
		Thread sensorThread = new Thread(null, doGetHillGrade, "SensorThread");
		sensorThread.start();
	}
    
	private Runnable doGetHillGrade = new Runnable() {
		@Override
		public void run() {
			MainActivity.this.getHillGrade();
		}
	};

	private void getHillGrade() {
		if (this.sensorBinder != null) {
			MainActivity.this.hillGradeRaw = MainActivity.this.sensorBinder.getHillgradeRaw();
			MainActivity.this.hillGradeAvg = MainActivity.this.sensorBinder.computeAverageValue();
			MainActivity.this.hillGradeMed = MainActivity.this.sensorBinder.computeMedianValue();
		}
	}
	
	private void initDB() {
        this.mDataBaseHelper = new DataBaseHelper(this);
 
        try {
        	this.mDataBaseHelper.openDataBase();
        } catch(SQLException sqle) {
        	throw sqle;
        }
	}
	
	private void feedDatabase() {
		this.mDataBaseHelper.insertData(
				latDB,
				lngDB,
				altDB,
				accuracyDB,
				velocityDB,
				headwindDB,
				hillgraderawDB,
				hillgradeavgDB,
				hillgrademedDB,
				bearingDB,
				windspeedDB,
				winddegreesDB,
				forceDB,
				poweroutputDB,
				a1DB,
				a2DB,
				rDB,
				tDB,
				wcDB,
				wmDB);
	}


	/*
	 * The Orientation Sensor implementation below is only used for the preference screen
	 * For power calculation check the SensorService
	 */
	
	private void initPrefSensor() {
		this.prefSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		this.prefOrientationSensor = prefSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		this.prefSensorManager.registerListener(this, prefOrientationSensor, SensorManager.SENSOR_DELAY_UI);
	}
	
	private void killPrefSensor() {
		this.prefSensorManager.unregisterListener(this, this.prefOrientationSensor);
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		MainActivity.this.prefSensorValue = Helper.formatDouble(Helper.angle2HillGrade(event.values[1]));
		int vis = this.pref_edit_calibration_new.getVisibility();
		if (vis == View.VISIBLE) {
			this.pref_edit_calibration_new.setText(this.prefSensorValue + "");
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// Nothing to do
	}
	
}