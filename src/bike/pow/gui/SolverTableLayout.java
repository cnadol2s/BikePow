package bike.pow.gui;

import bike.pow.R;
import bike.pow.R.layout;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;

public class SolverTableLayout extends TableLayout {

	public SolverTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	private void init() {
		LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.solver_data, this, true);
	}

}
