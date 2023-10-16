package de.storchp.opentracks.osmplugin.compass;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Derived from <a href="https://github.com/kylecorry31/Trail-Sense/blob/master/app/src/main/java/com/kylecorry/trail_sense/shared/sensors/BaseSensor.kt">...</a>
 */
public abstract class AbstractLowPassSensor extends AbstractSensor {

    private final SensorManager sensorManager;
    private final int sensorType;
    private final int sensorDelay;
    private final LowPassFilter[] filters;
    private Vector3 value = Vector3.ZERO;

    protected AbstractLowPassSensor(Context context, int sensorType, int sensorDelay, float filterSize) {
        this.sensorType = sensorType;
        this.sensorDelay = sensorDelay;
        this.sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        int numFilters  = 3;
        this.filters = new LowPassFilter[numFilters];
        for(int i = 0; i < numFilters; i++){
            this.filters[i] = new LowPassFilter(filterSize);
        }
    }

    protected void handleSensorEvent(SensorEvent event) {
        value = new Vector3(
                filters[0].filter(event.values[0]),
                filters[1].filter(event.values[1]),
                filters[2].filter(event.values[2])
        );
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            handleSensorEvent(event);
            notifyListeners();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int newAccuracy) {
            //Do Nothing
            throw new UnsupportedOperationException();
        }

    };

    protected void startImpl() {
        var sensor = sensorManager.getDefaultSensor(sensorType);
        if (sensor != null) {
            sensorManager.registerListener(
                    sensorListener,
                    sensor,
                    sensorDelay
            );
        }
    }

    protected void stopImpl() {
        sensorManager.unregisterListener(sensorListener);
    }

    public Vector3 getValue() {
        return value;
    }

}
