 package de.storchp.opentracks.osmplugin.utils;

public class TrackPointsDebug {

    private int trackpointsReceived = 0;
    private int trackpointsInvalid = 0;
    private int trackpointsDrawn = 0;
    private int trackpointsPause = 0;
    private int segments = 0;

    public int getTrackpointsReceived() {
        return trackpointsReceived;
    }

    public int getTrackpointsInvalid() {
        return trackpointsInvalid;
    }

    public int getTrackpointsDrawn() {
        return trackpointsDrawn;
    }

    public int getTrackpointsPause() {
        return trackpointsPause;
    }

    public int getSegments() {
        return segments;
    }

    public void add(final TrackPointsDebug other) {
        if (other != null) {
            this.trackpointsReceived += other.getTrackpointsReceived();
            this.trackpointsInvalid += other.getTrackpointsInvalid();
            this.trackpointsDrawn += other.getTrackpointsDrawn();
            this.trackpointsPause += other.getTrackpointsPause();
            this.segments += other.getSegments();
        }
    }
  
}
