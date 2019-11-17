package labs.com.mdfoto.models;

/**
 * Created by Sever on 10/27/2019.
 */

public class PatientList {

    private String name;
    private String id;

    public PatientList(String name,String id) {
        this.name = name;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

}