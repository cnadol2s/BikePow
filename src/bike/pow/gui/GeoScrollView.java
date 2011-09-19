package bike.pow.gui;

import bike.pow.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ScrollView;

public class GeoScrollView extends ScrollView {

	public GeoScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}
	
	private void init() {
		LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.geo_inflate, this, true);
	}
	
	public int computeVerticalScrollOffset() {
		return super.computeVerticalScrollOffset();
	}

}
