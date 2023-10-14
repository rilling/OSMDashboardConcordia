package de.storchp.opentracks.osmplugin.dashboardapi;

import static de.storchp.opentracks.osmplugin.dashboardapi.APIConstants.LAT_LON_FACTOR;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import org.mapsforge.core.model.LatLong;

import java.util.ArrayList;
import java.util.List;

import de.storchp.opentracks.osmplugin.utils.MapUtils;
import de.storchp.opentracks.osmplugin.utils.TrackPointsDebug;

public class TrackPoint {

    public static final String _ID = "_id";
    public static final String TRACKID = "trackid";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String SPEED = "speed";
    public static final double PAUSE_LATITUDE = 100.0;

    public static final String[] PROJECTION_V1 = {
            _ID,
            TRACKID,
            LATITUDE,
            LONGITUDE,
            TIME,
            SPEED
    };

    public static final String[] PROJECTION_V2 = {
            _ID,
            TRACKID,
            LATITUDE,
            LONGITUDE,
            TIME,
            TYPE,
            SPEED
    };

    private final long trackPointId;
    private final long pointTrackCode;
    private final LatLong latLong;
    private final boolean pause;
    private final double speed;

    public TrackPoint(long pointTrackCode, long trackPointId, double latitude, double longitude, Integer type, double speed) {
        this.pointTrackCode = pointTrackCode;
        this.trackPointId = trackPointId;
        if (MapUtils.isValid(latitude, longitude)) {
            this.latLong = new LatLong(latitude, longitude);
        } else {
            latLong = null;
        }
        this.pause = type != null ? type != 0 : latitude == PAUSE_LATITUDE;
        this.speed = speed;
    }

    public boolean hasValidLocation() {
        return latLong != null && !isPause();
    }

    public boolean isPause() {
        return pause;
    }

    /**
     * Reads the TrackPoints from the Content Uri and split by segments.
     * Pause TrackPoints and different Track IDs split the segments.
     */
    public static TrackPointsBySegments readTrackPointsBySegments(ContentResolver resolver, Uri data, long lastTrackPointId, int protocolVersion) {
        var debug = new TrackPointsDebug();
        var segments = new ArrayList<List<TrackPoint>>();
        var projection = PROJECTION_V2;
        var typeQuery = " AND " + TrackPoint.TYPE + " IN (-2, -1, 0, 1)";
        if (protocolVersion < 2) { // fallback to old Dashboard API
            projection = PROJECTION_V1;
            typeQuery = "";
        }
        try (Cursor cursor = resolver.query(data, projection, TrackPoint._ID + "> ?" + typeQuery, new String[]{Long.toString(lastTrackPointId)}, null)) {
            TrackPoint lastTrackPoint = null;
            List<TrackPoint> segment = null;
            while (cursor.moveToNext()) {
                debug.trackpointsReceived++;
                var trackPointId = cursor.getLong(cursor.getColumnIndexOrThrow(TrackPoint._ID));
                var trackId = cursor.getLong(cursor.getColumnIndexOrThrow(TrackPoint.TRACKID));
                var latitude = cursor.getInt(cursor.getColumnIndexOrThrow(TrackPoint.LATITUDE)) / LAT_LON_FACTOR;
                var longitude = cursor.getInt(cursor.getColumnIndexOrThrow(TrackPoint.LONGITUDE)) / LAT_LON_FACTOR;
                var typeIndex = cursor.getColumnIndex(TrackPoint.TYPE);
                var speed = cursor.getDouble(cursor.getColumnIndexOrThrow(TrackPoint.SPEED));

                Integer type = null;
                if (typeIndex > -1) {
                    type = cursor.getInt(typeIndex);
                }

                if (lastTrackPoint == null || lastTrackPoint.pointTrackCode != trackId) {
                    segment = new ArrayList<>();
                    segments.add(segment);
                }

                lastTrackPoint = new TrackPoint(trackId, trackPointId, latitude, longitude, type, speed);
                if (lastTrackPoint.hasValidLocation()) {
                    segment.add(lastTrackPoint);
                } else {
                    debug.trackpointsInvalid++;
                }
                if (lastTrackPoint.isPause()) {
                    debug.trackpointsPause++;
                    lastTrackPoint = null;
                }
            }
        }
        debug.segments = segments.size();

        return new TrackPointsBySegments(segments, debug);
    }

    public long getTrackPointId() {
        return trackPointId;
    }

    public long getPointTrackCode() {
        return pointTrackCode;
    }

    public LatLong getLatLong() {
        return latLong;
    }

    public double getSpeed() {
        return speed;
    }
    
}
