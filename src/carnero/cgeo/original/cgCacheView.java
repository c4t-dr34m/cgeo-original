package carnero.cgeo.original;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class cgCacheView {
	// layouts & views
	public RelativeLayout oneCache;
	public RelativeLayout oneInfo;
	public RelativeLayout oneCheckbox;
	public CheckBox checkbox;
	public ImageView foundMark;
	public ImageView offlineMark;
	public TextView text;
	public TextView favourite;
	public TextView info;
	public RelativeLayout inventory;
	public RelativeLayout directionLayout;
	public cgDistanceView distance;
	public cgCompassMini direction;
	public RelativeLayout dirImgLayout;
	public ImageView dirImg;

	// status
	public float startX = -1;
	public float prevX = -1;
}
