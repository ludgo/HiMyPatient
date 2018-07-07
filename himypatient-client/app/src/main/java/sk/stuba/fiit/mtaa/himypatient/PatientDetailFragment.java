package sk.stuba.fiit.mtaa.himypatient;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.net.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.stuba.fiit.mtaa.himypatient.model.Patient;
import sk.stuba.fiit.mtaa.himypatient.rest.RestCallBuilder;
import sk.stuba.fiit.mtaa.himypatient.rest.RetrofitCallback;
import sk.stuba.fiit.mtaa.himypatient.util.Utilities;

public class PatientDetailFragment extends Fragment {

    static final String ARG_PATIENT_ID = "fragment_arg_patient_id";

    private Patient mPatient;

    private ImageView mImageView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mBirthDateView;
    private EditText mBloodTypeView;
    private EditText mLastGlucoseView;
    private LinearLayout mAllergensGroup;

    private Button mChangeButton;
    private String mPickedFilePath;

    /**
     * Keep empty constructor! - MANDATORY
     */
    public PatientDetailFragment() {
    }

    Integer getPatientId() {
        if (getArguments().containsKey(ARG_PATIENT_ID)) {
            return getArguments().getInt(ARG_PATIENT_ID);
        }
        return null;
    }

    void assignPatientId(Integer id){
        Bundle arguments = new Bundle();
        if (id != null) {
            arguments.putInt(ARG_PATIENT_ID, id);
        }
        setArguments(arguments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_detail, container, false);

        mImageView = rootView.findViewById(R.id.image);
        mFirstNameView = rootView.findViewById(R.id.first_name);
        mLastNameView = rootView.findViewById(R.id.last_name);
        mBirthDateView = rootView.findViewById(R.id.birth_date);
        mBloodTypeView = rootView.findViewById(R.id.blood_type);
        mLastGlucoseView = rootView.findViewById(R.id.last_glucose);
        mAllergensGroup = rootView.findViewById(R.id.allergens);

        mChangeButton = rootView.findViewById(R.id.button_change);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Integer patientId = getPatientId();
        if (patientId == null) {
            // New patient
            mPatient = Patient.dummyPatient();
            populateFields();
        } else {
            // Patient detail
            loadPatient(patientId);
        }
    }

    private void displayImage(String path) {
        Picasso.get()
                .load(path)
                .resize(120, 160)
                .centerCrop()
                .placeholder(R.drawable.profile_default)
                .into(mImageView);
    }

    void setPickedFilePath(@NonNull String filePath){
        mPickedFilePath = filePath;
        displayImage(filePath);
    }

    void setEditable(boolean editable) {

        mFirstNameView.setEnabled(editable);
        mLastNameView.setEnabled(editable);
        mBirthDateView.setEnabled(editable);
        mBloodTypeView.setEnabled(editable);
        mLastGlucoseView.setEnabled(editable);
        for (int i = 0; i < mAllergensGroup.getChildCount(); i++) {
            mAllergensGroup.getChildAt(i).setEnabled(editable);
        }

        mChangeButton.setVisibility(editable ? View.VISIBLE : View.GONE);
    }

    void populateFields() {
        assert mPatient != null;

        displayImage(mPatient.getImageUrl());
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangeButton.getVisibility() == View.GONE) {
                    if (!Utilities.isConnected(getContext())) {
                        Utilities.showToast(getContext(), getString(R.string.error_internet));
                        return;
                    }
                    Intent intent = new Intent(getContext(), ImageActivity.class);
                    if (mPatient.getImageUrl() != null) {
                        intent.putExtra(ImageActivity.INTENT_EXTRA_IMAGE_URL, mPatient.getImageUrl());
                    }
                    startActivity(intent);
                }
            }
        });

        mFirstNameView.setText(mPatient.getFirstName());

        mLastNameView.setText(mPatient.getLastName());

        final DateFormat dateFormat = Utilities.buildDateFormat();
        try {
            Date birthDate = dateFormat.parse(mPatient.getBirthDate());
            mBirthDateView.setText(dateFormat.format(birthDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mBirthDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = (EditText) v;
                String[] year_month_day = editText.getText().toString().split(Pattern.quote("-"));
                int year = Integer.parseInt(year_month_day[0]);
                int month = Integer.parseInt(year_month_day[1]);
                int day = Integer.parseInt(year_month_day[2]);
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        Date pickedDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
                        editText.setText(dateFormat.format(pickedDate));
                    }
                };
                new DatePickerDialog(
                        getContext(), listener, year, month - 1, day) // month index form 0 here!
                        .show();
            }
        });

        mBloodTypeView.setText(mPatient.getBloodType());
        mBloodTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = (EditText) v;
                new AlertDialog.Builder(getActivity())
                        .setItems(R.array.blood_types, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editText.setText(getResources().getStringArray(R.array.blood_types)[which]);
                            }
                        })
                        .create()
                        .show();
            }
        });

        mLastGlucoseView.setText(String.valueOf(mPatient.getLastGlucose()));

        mAllergensGroup.removeAllViews();
        Set<Integer> patientAllergens = mPatient.getAllergens();
        String[] allergenNames = getResources().getStringArray(R.array.allergens);
        for (int allergenNumber = 1; allergenNumber <= allergenNames.length; allergenNumber++) {
            CheckBox allergenView = (CheckBox) getLayoutInflater().inflate(R.layout.checkbox_allergen, null);
            allergenView.setTag(allergenNumber);
            allergenView.setText(allergenNames[allergenNumber - 1]);
            if (patientAllergens.contains(allergenNumber)) {
                allergenView.setChecked(true);
            }
            mAllergensGroup.addView(allergenView);
        }
    }

    private Patient collectFromFields() {
        Patient patient = new Patient();

        String firstName = mFirstNameView.getText().toString();
        if (firstName.length() < 3 || firstName.length() > 32) {
            Utilities.showToast(getContext(), "First name should be 3-32 characters.");
            return null;
        }
        patient.setFirstName(firstName);

        String lastName = mLastNameView.getText().toString();
        if (lastName.length() < 3 || lastName.length() > 32) {
            Utilities.showToast(getContext(), "Last name should be 3-32 characters.");
            return null;
        }
        patient.setLastName(lastName);

        patient.setBirthDate(mBirthDateView.getText().toString());

        patient.setBloodType(mBloodTypeView.getText().toString());

        try {
            double lastGlucose = Double.parseDouble(mLastGlucoseView.getText().toString());
            if (lastGlucose < 0.0) {
                Utilities.showToast(getContext(), "Glucose level cannot be negative.");
                return null;
            } else if (lastGlucose > 1000) {
                Utilities.showToast(getContext(), "Glucose level unreal, too high.");
                return null;
            }
            patient.setLastGlucose(lastGlucose);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Utilities.showToast(getContext(), "Glucose level should be decimal.");
            return null;
        }

        if (mPickedFilePath != null) {
            // TODO upload image here and set returned url instead
            patient.setImageUrl(mPickedFilePath);
        }

        Set<Integer> allergens = new HashSet<>();
        for (int i = 0; i < mAllergensGroup.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) mAllergensGroup.getChildAt(i);
            if (checkBox.isChecked()) {
                allergens.add((Integer) checkBox.getTag());
            }
        }
        patient.setAllergens(allergens);
        return patient;
    }

    private void loadPatient(int id) {
        Call<Patient> call = RestCallBuilder.getPatientCall(id);
        call.enqueue(new RetrofitCallback<Patient>(getActivity()) {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    mPatient = response.body();
                    populateFields();
                } else if (response.code() == 404) {
                    // Patient does not exist anymore
                    getOwnActivity().toParentActivity();
                } else {
                    super.onResponse(call, response);
                }
            }
        });
    }

    boolean savePatient() {
        Patient patient = collectFromFields();
        if (patient == null) return false;

        if (!Utilities.isConnected(getActivity())) {
            Utilities.showToast(getActivity(), getString(R.string.error_internet));
            return false;
        }

        Call<Patient> call;
        if (mPatient.getId() == null) {
            call = RestCallBuilder.createPatientCall(patient);
        } else {
            call = RestCallBuilder.updatePatientCall(mPatient.getId(), patient);
        }
        call.enqueue(new RetrofitCallback<Patient>(getActivity()) {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    mPatient = response.body();
                    assert mPatient != null && mPatient.getId() != null;
                    assignPatientId(mPatient.getId());
                    populateFields();
                } else if (response.code() == 404) {
                    // Patient does not exist anymore
                    getOwnActivity().toParentActivity();
                } else {
                    getOwnActivity().setEditable(true); // IMPORTANT
                    super.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                getOwnActivity().setEditable(true); // IMPORTANT
                super.onFailure(call, t);
            }
        });
        return true;
    }

    void deletePatient() {
        String dialogTitle = getString(R.string.dialog_delete_title,
                mPatient.getFirstName(), mPatient.getLastName());

        AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.StyleAlertDialogDelete)
                .setMessage(dialogTitle)
                .setPositiveButton(R.string.dialog_delete_button_positive, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!Utilities.isConnected(getActivity())) {
                            Utilities.showToast(getActivity(), getString(R.string.error_internet));
                            return;
                        }

                        Call<Patient> call = RestCallBuilder.removePatientCall(mPatient.getId());
                        call.enqueue(new RetrofitCallback<Patient>(getActivity()) {
                            @Override
                            public void onResponse(Call<Patient> call, Response<Patient> response) {
                                if (response.isSuccessful() || response.code() == 404) {
                                    // Same also if patient does not exist anymore
                                    getOwnActivity().toParentActivity();
                                } else {
                                    super.onResponse(call, response);
                                }
                            }
                        });
                    }})
                .setNegativeButton(R.string.dialog_delete_button_negative, null)
                .create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setAllCaps(true);

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setAllCaps(false);
        negativeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
    }

    private PatientDetailActivity getOwnActivity(){
        return ((PatientDetailActivity) getActivity());
    }
}
