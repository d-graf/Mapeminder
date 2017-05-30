package ch.sario.mapeminder;

/**
 * Note Object, store all information.
 *
 * @version 1.0
 */
public class Note {

    /**
     * a unique id.
     */
    private String id;
    /**
     * Y Coordinates, String because db.
     */
    private String latitude;

    /**
     * X Coordinates, String because db.
     */
    private String longitude;
    /**
     *  content of note.
     */
    private String note;


    public Note(String id, String latitude, String longitude, String note) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
    }

    public Note(String latitude, String longitude, String note) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
