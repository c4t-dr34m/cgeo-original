package carnero.cgeo.original.libs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.view.Display;

public class Direction {

	private Resources res = null;
	private Direction dir = null;
	private App app = null;
	private Context context = null;
	private Warning warning = null;
	private SensorManager sensorManager = null;
	private cgeoSensorListener sensorListener = null;
	private UpdateDir dirUpdate = null;
	public Double directionNow = null;

	public Direction(App appIn, Context contextIn, UpdateDir dirUpdateIn, Warning warningIn) {
		app = appIn;
		context = contextIn;
		dirUpdate = dirUpdateIn;
		warning = warningIn;
		res = context.getResources();

		sensorListener = new cgeoSensorListener();
	}

	public void initDir() {
		dir = this;

		if (sensorManager == null) {
			sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		}
		sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void closeDir() {
		if (sensorManager != null && sensorListener != null) {
			sensorManager.unregisterListener(sensorListener);
		}
	}

	public void replaceUpdate(UpdateDir dirUpdateIn) {
		dirUpdate = dirUpdateIn;

		if (dirUpdate != null && directionNow != null) {
			dirUpdate.updateDir(dir);
		}
	}

	private class cgeoSensorListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			/* There is a bug in Android, which appearently causes this method to be called every
			 * time the sensor _value_ changed, even if the _accuracy_ did not change. So logging
			 * this event leads to the log being flooded with multiple entries _per second_,
			 * which I experienced when running cgeo in a building (with GPS and network being
			 * unreliable).
			 *
			 * See for example https://code.google.com/p/android/issues/detail?id=14792
			 */
			//Log.i(cgSettings.tag, "Compass' accuracy is low (" + accuracy + ")");
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			Double directionNowPre = new Double(event.values[0]);

			final Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
			final int rotation = display.getOrientation();
			if (rotation == Configuration.ORIENTATION_LANDSCAPE) {
				directionNowPre = directionNowPre + 90;
			}

			directionNow = directionNowPre;

			if (dirUpdate != null && directionNow != null) {
				dirUpdate.updateDir(dir);
			}
		}
	}
}
