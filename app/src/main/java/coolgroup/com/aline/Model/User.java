package coolgroup.com.aline.Model;

import java.util.ArrayList;

public class User {

    private String email,password,name,phone;
    private ArrayList<String> contactsList;

    public User() {

    }

    public User(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.contactsList = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public ArrayList<String> getContactsList() {
        return contactsList;
    }

    public void setContactsList(ArrayList<String> contactsList) {
        this.contactsList = contactsList;
    }

    public boolean addContact(String contactID) {
        return this.contactsList.add(contactID);
    }

    public boolean deleteContact(String contactID) {
        return this.contactsList.remove(contactID);
    }
}
