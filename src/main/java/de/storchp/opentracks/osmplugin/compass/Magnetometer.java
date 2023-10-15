package de.storchp.opentracks.osmplugin.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Derived from <a href="https://github.com/kylecorry31/Trail-Sense/blob/master/app/src/main/java/com/kylecorry/trail_sense/shared/sensors/Magnetometer.kt">...</a>
 */
public class Magnetometer extends AbstractLowPassSensor {

    private static final int SENSOR_TYPE = Sensor.TYPE_MAGNETIC_FIELD;
    private static final int SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME;

    public Magnetometer(Context context) {
        super(context, SENSOR_TYPE, SENSOR_DELAY, 0.03f);
    }

}



