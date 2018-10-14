package coolgroup.com.aline.model;

public class Users {

    private String name, email, image, status, thumbnail, phone, location;


    // Empty Constructor
    public Users() {}

    public Users(String name, String email, String image, String status, String thumbnail, String phone, String location) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.status = status;
        this.thumbnail = thumbnail;
        this.phone = phone;
        this.location = location;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
