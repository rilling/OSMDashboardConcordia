package de.storchp.opentracks.osmplugin.utils;

public class TrackPointsDebug {
    public static final int trackpointsReceived = 0;
    public static final int trackpointsInvalid = 0;
    public static final int trackpointsDrawn = 0;
    public int trackpointsPause = 0;
    public int segments = 0;

    public void add(final TrackPointsDebug other) {
        this.trackpointsReceived += other.trackpointsReceived;
        this.trackpointsInvalid += other.trackpointsInvalid;
        this.trackpointsDrawn += other.trackpointsDrawn;
        this.trackpointsPause += other.trackpointsPause;
        this.segments += other.segments;
    }
}
