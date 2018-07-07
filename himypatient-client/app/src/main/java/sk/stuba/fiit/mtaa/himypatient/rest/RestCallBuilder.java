package sk.stuba.fiit.mtaa.himypatient.rest;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import sk.stuba.fiit.mtaa.himypatient.model.Patient;

/**
 * REST call builder class. Builds request bodies and prepares whole call objects
 */
public class RestCallBuilder {

    public static Call<List<Patient>> listPatientsCall() {
        return RestHelper.buildRestService().listPatients();
    }

    public static Call<Patient> createPatientCall(@NonNull Patient patient) {
        return RestHelper.buildRestService().createPatient(patient);
    }

    public static Call<Patient> getPatientCall(int id) {
        return RestHelper.buildRestService().getPatient(id);
    }

    public static Call<Patient> updatePatientCall(int id, @NonNull Patient patient) {
        return RestHelper.buildRestService().updatePatient(id, patient);
    }

    public static Call<Patient> removePatientCall(int id) {
        return RestHelper.buildRestService().removePatient(id);
    }
}
