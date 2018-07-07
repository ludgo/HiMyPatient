package sk.stuba.fiit.mtaa.himypatient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import sk.stuba.fiit.mtaa.himypatient.util.Utilities;

public class PatientDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FILE = 10;

    private Menu mMenu;
    private PatientDetailFragment mPatientDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mPatientDetailFragment = new PatientDetailFragment();

            Integer patientId = null;
            if (getIntent().hasExtra(PatientDetailFragment.ARG_PATIENT_ID)) {
                // Patient detail
                patientId = getIntent().getIntExtra(PatientDetailFragment.ARG_PATIENT_ID, -999); // default never used
            }
            mPatientDetailFragment.assignPatientId(patientId);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.patient_detail_container, mPatientDetailFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        assert mMenu != null;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_patient_detail, menu);
        if (mPatientDetailFragment.getPatientId() == null) {
            // New patient
            setEditable(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                toParentActivity();
                return true;
            }
            case R.id.action_edit: {
                setEditable(true);
                return true;
            }
            case R.id.action_delete: {
                mPatientDetailFragment.deletePatient();
                return true;
            }
            case R.id.action_confirm: {
                if (mPatientDetailFragment.savePatient()) {
                    setEditable(false);
                }
                return true;
            }
            case R.id.action_cancel: {
                if (mPatientDetailFragment.getPatientId() == null) {
                    // Currently editing not yet saved patient
                    toParentActivity();
                } else {
                    // Patient detail
                    setEditable(false);
                    mPatientDetailFragment.populateFields();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_FILE) {
            if (data == null || data.getData() == null) {
                Utilities.showToast(this, getString(R.string.error_pick_image));
            } else {
                String filePath = Utilities.convertPath(this, data.getData());
                if (BuildConfig.DEBUG) System.out.println(data.getData() + " --> " + filePath);
                if (filePath != null) {
                    mPatientDetailFragment.setPickedFilePath(filePath);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void setEditable(boolean editable) {
        mMenu.findItem(R.id.action_edit).setVisible(!editable);
        mMenu.findItem(R.id.action_delete).setVisible(!editable);
        mMenu.findItem(R.id.action_confirm).setVisible(editable);
        mMenu.findItem(R.id.action_cancel).setVisible(editable);

        mPatientDetailFragment.setEditable(editable);
    }

    void toParentActivity() {
        // existing (preferably) instance of parent activity, destroy this activity
        Intent intent = getParentActivityIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        navigateUpTo(intent);
        finish();
    }

    // onClick events below

    public void initPickImage(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }
}
