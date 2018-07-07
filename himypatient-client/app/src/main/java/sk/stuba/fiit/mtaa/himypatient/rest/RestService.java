package sk.stuba.fiit.mtaa.himypatient.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sk.stuba.fiit.mtaa.himypatient.model.Patient;

/**
 * REST JSON API routes request-response definition
 */
interface RestService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("patients")
    Call<List<Patient>> listPatients();

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("patients")
    Call<Patient> createPatient(@Body Patient patient);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("patients/{patient_id}")
    Call<Patient> getPatient(@Path("patient_id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @PUT("patients/{patient_id}")
    Call<Patient> updatePatient(@Path("patient_id") int id, @Body Patient patient);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("patients/{patient_id}")
    Call<Patient> removePatient(@Path("patient_id") int id);
}
