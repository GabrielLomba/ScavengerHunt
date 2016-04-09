package rbh9dm.cs4720.com.scavengerhunt;

/**
 * Created by Student User on 3/23/2016.
 */
public class LineItem {
    private String name;
    private String description;
    private boolean pictureRequired;
    private boolean locationRequired;
    private boolean pictureOk;
    private boolean locationOk;
    private String nameOfLocation;
    private float latitude;
    private float longitude;

    public LineItem(String name, String description, boolean pictureRequired, boolean locationRequired, String nameOfLocation, float latitude, float longitude) {
        this.name = name;
        this.description = description;
        this.pictureRequired = pictureRequired;
        this.locationRequired= locationRequired;
        this.nameOfLocation = nameOfLocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LineItem(String name, String description, int pictureRequired, int locationRequired, String nameOfLocation, float latitude, float longitude) {
        this.name = name;
        this.description = description;
        if(pictureRequired == 0)
            this.pictureRequired = false;
        else
            this.pictureRequired = true;
        if(locationRequired == 0)
            this.locationRequired = false;
        else
            this.locationRequired = true;
        this.nameOfLocation = nameOfLocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return name;
    }

    public boolean isPictureRequired() {
        return pictureRequired;
    }

    public void setPictureRequired(boolean pictureRequired) {
        this.pictureRequired = pictureRequired;
    }

    public boolean isLocationRequired() {
        return locationRequired;
    }

    public void setLocationRequired(boolean locationRequired) {
        this.locationRequired = locationRequired;
    }

    public boolean isPictureOk() {
        return pictureOk;
    }

    public void setPictureOk(boolean pictureOk) {
        this.pictureOk = pictureOk;
    }

    public String getNameOfLocation() {
        return nameOfLocation;
    }

    public void setNameOfLocation(String nameOfLocation) {
        this.nameOfLocation = nameOfLocation;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public boolean isLocationOk() {
        return locationOk;
    }

    public void setLocationOk(boolean locationOk) {
        this.locationOk = locationOk;
    }


}
