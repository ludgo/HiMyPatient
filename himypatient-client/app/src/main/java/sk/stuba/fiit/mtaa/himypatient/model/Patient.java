package sk.stuba.fiit.mtaa.himypatient.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import sk.stuba.fiit.mtaa.himypatient.util.Utilities;

public class Patient {

    private static final String BLOOD_TYPE_DEFAULT = "A";

    private Integer id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("birth_date")
    private String birthDate;
    @SerializedName("blood_type")
    private String bloodType;
    @SerializedName("last_glucose")
    private double lastGlucose;
    @SerializedName("image_url")
    private String imageUrl;
    private Set<Integer> allergens;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public double getLastGlucose() {
        return lastGlucose;
    }

    public void setLastGlucose(double lastGlucose) {
        this.lastGlucose = lastGlucose;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<Integer> getAllergens() {
        return allergens;
    }

    public void setAllergens(Set<Integer> allergens) {
        this.allergens = allergens;
    }

    public static Patient dummyPatient() {
        Patient patient = new Patient();
        patient.setFirstName("");
        patient.setLastName("");
        patient.setBirthDate(Utilities.buildDateFormat().format(new Date()));
        patient.setBloodType(BLOOD_TYPE_DEFAULT);
        Set<Integer> emptySet = new HashSet<>();
        patient.setAllergens(emptySet);
        return patient;
    }
}
