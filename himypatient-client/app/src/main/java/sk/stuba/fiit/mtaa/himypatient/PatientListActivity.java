package sk.stuba.fiit.mtaa.himypatient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.stuba.fiit.mtaa.himypatient.model.Patient;
import sk.stuba.fiit.mtaa.himypatient.rest.RestCallBuilder;
import sk.stuba.fiit.mtaa.himypatient.rest.RetrofitCallback;
import sk.stuba.fiit.mtaa.himypatient.util.Utilities;

import java.net.ConnectException;
import java.util.List;

public class PatientListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.patient_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_patient_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                toLoginActivity();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        if (!Utilities.isConnected(this)) {
            Utilities.showToast(this, getString(R.string.error_internet));
            toLoginActivity();
            return;
        }

        Call<List<Patient>> call = RestCallBuilder.listPatientsCall();
        call.enqueue(new RetrofitCallback<List<Patient>>(this) {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful()) {
                    PatientRecyclerViewAdapter adapter =
                            new PatientRecyclerViewAdapter(response.body());
                    if (recyclerView.getAdapter() == null) {
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Ensure illusion that data has not been fetched
                        // (with consequent adapter swap)
                        // by restored scroll position
                        GridLayoutManager manager = ((GridLayoutManager) recyclerView.getLayoutManager());
                        int positionToRemember = manager.findFirstCompletelyVisibleItemPosition();
                        recyclerView.swapAdapter(adapter, true);
                        manager.scrollToPosition(positionToRemember);
                    }
                } else {
                    super.onResponse(call, response);
                    toLoginActivityWithDelay();
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                super.onFailure(call, t);
                toLoginActivityWithDelay();
            }
        });
    }

    public static class PatientRecyclerViewAdapter
            extends RecyclerView.Adapter<PatientRecyclerViewAdapter.ViewHolder> {

        private final List<Patient> mPatientList;

        PatientRecyclerViewAdapter(List<Patient> patientList) {
            mPatientList = patientList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.itemView.setTag(mPatientList.get(position));

            Patient patient = mPatientList.get(position);
            Picasso.get()
                    .load(patient.getImageUrl())
                    .resize(360, 480)
                    .centerCrop()
                    .placeholder(R.drawable.profile_default)
                    .into(holder.mImageView);
            String commaName = Utilities.buildCommaName(
                    holder.mNameView.getContext(), patient.getFirstName(), patient.getLastName());
            holder.mNameView.setText(commaName);
            holder.mBloodTypeView.setText(patient.getBloodType());
        }

        @Override
        public int getItemCount() {
            return mPatientList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final ImageView mImageView;
            final TextView mNameView;
            final TextView mBloodTypeView;

            ViewHolder(View view) {
                super(view);
                mImageView = view.findViewById(R.id.image);
                mNameView = view.findViewById(R.id.name);
                mBloodTypeView = view.findViewById(R.id.blood_type);
            }
        }
    }

    private void toLoginActivity() {
        // new instance of another activity, destroy this activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void toLoginActivityWithDelay() {
        (new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3500); // 3500 because of Toast.LENGTH_LONG
                } catch (Exception e) {
//                            e.printStackTrace();
                }
                toLoginActivity();
            }
        }).start();
    }

    // onClick events below

    public void toDetailActivity(View v) {
        if (!Utilities.isConnected(this)) {
            Utilities.showToast(this, getString(R.string.error_internet));
            return;
        }
        Intent intent = new Intent(this, PatientDetailActivity.class);
        if (v.getTag() != null) {
            // Patient detail
            Patient patient = (Patient) v.getTag();
            intent.putExtra(PatientDetailFragment.ARG_PATIENT_ID, patient.getId());
        } else {
            // New patient
        }
        startActivity(intent);
    }
}
