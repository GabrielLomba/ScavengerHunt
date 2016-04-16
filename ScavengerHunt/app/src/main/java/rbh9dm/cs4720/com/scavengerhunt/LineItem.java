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
    private double latitude;
    private double longitude;
    private boolean complete;

    public LineItem(String name, String description, boolean pictureRequired, boolean locationRequired, boolean pictureOk, boolean locationOk, String nameOfLocation, double latitude, double longitude, boolean complete) {
        this.name = name;
        this.description = description;
        this.pictureRequired = pictureRequired;
        this.locationRequired= locationRequired;
        this.pictureOk = pictureOk;
        this.locationOk = locationOk;
        this.nameOfLocation = nameOfLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.complete = complete;
    }

    public LineItem(String name, String description, int pictureRequired, int locationRequired, int pictureOk, int locationOk, String nameOfLocation, double latitude, double longitude, int complete) {
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
        if(pictureOk == 0)
            this.pictureOk = false;
        else
            this.pictureOk = true;
        if(locationOk == 0)
            this.locationOk = false;
        else
            this.locationOk = true;
        this.nameOfLocation = nameOfLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        if(complete == 0)
            this.complete = false;
        else
            this.complete = true;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isLocationOk() {
        return locationOk;
    }

    public void setLocationOk(boolean locationOk) {
        this.locationOk = locationOk;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }


}
