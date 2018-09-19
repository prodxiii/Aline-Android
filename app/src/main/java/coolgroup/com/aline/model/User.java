package coolgroup.com.aline.model;

public class User {

    private String email, name, phone, uID;

    public User() {
    }

    public User(String email, String name, String phone, String uID) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.uID = uID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
