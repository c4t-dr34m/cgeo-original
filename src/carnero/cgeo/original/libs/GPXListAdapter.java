package carnero.cgeo.original.libs;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.util.Log;
import carnero.cgeo.original.R;
import carnero.cgeo.original.gpxes;
import java.io.File;

public class GPXListAdapter extends ArrayAdapter<File> {
	private GPXView holder = null;
	private gpxes parent = null;
	private Settings settings = null;
	private LayoutInflater inflater = null;

	public GPXListAdapter(gpxes parentIn, Settings settingsIn, List<File> listIn) {
		super(parentIn, 0, listIn);

		parent = parentIn;
		settings = settingsIn;
	}

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
		if (inflater == null) inflater = ((Activity)getContext()).getLayoutInflater();

		if (position > getCount()) {
			Log.w(Settings.tag, "cgGPXListAdapter.getView: Attempt to access missing item #" + position);
			return null;
		}

		File file = getItem(position);

		if (rowView == null) {
			rowView = (View)inflater.inflate(R.layout.gpx_item, null);

			holder = new GPXView();
			holder.filepath = (TextView)rowView.findViewById(R.id.filepath);
			holder.filename = (TextView)rowView.findViewById(R.id.filename);
			
			rowView.setTag(holder);
		} else {
			holder = (GPXView)rowView.getTag();
		}

		final touchListener touchLst = new touchListener(file);
		rowView.setOnClickListener(touchLst);

		holder.filepath.setText(file.getParent());
		holder.filename.setText(file.getName());

		return rowView;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	private class touchListener implements View.OnClickListener {
		private File file = null;

		public touchListener(File fileIn) {
			file = fileIn;
		}

		// tap on item
		public void onClick(View view) {
			parent.loadGPX(file);
		}
	}
}
