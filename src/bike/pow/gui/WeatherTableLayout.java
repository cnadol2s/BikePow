package bike.pow.gui;

import bike.pow.R;
import bike.pow.R.layout;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class WeatherTableLayout extends TableLayout {
	
	public WeatherTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}
	
	private void init() {
		LayoutInflater li = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.weather_provider_data, this, true);
	}

}
