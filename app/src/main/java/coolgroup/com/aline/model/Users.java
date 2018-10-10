package coolgroup.com.aline.model;

public class Users {

    private String name;
    private String image;
    private String status;
    private String thumbnail;

    // Empty Constructor
    public Users() {}

    public Users(String name, String image, String status, String thumbnail) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumbnail = thumbnail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;

    }
}
