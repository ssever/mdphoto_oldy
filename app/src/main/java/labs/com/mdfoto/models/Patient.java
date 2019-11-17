package labs.com.mdfoto.models;

import java.util.ArrayList;
import java.util.List;

public class Patient {


    List<Image> imageList;

    public Patient() {
        imageList = new ArrayList<>();
    }

    public List<Image> getImageList() {
        return imageList;

    }

    public void addImage(Image url) {
        if (!imageList.contains(url)) imageList.add(url);
    }

    public void removeImage(Image url){
        if (imageList.contains(url)) imageList.remove(url);

    }


    public void updateImage(Image url){// sadece url e göre kontrol ettğimiz için diğer alanları update edebliyoruz.
        if (imageList.contains(url)) {
            int index = imageList.indexOf(url);
            imageList.set(index,url);
        }

    }

    private String name;

    private String surname;

    private String cardno;

    private String phone;

    private String age;

    private String weight;

    private String tel;

    private String note;

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
