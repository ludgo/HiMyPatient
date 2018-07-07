package sk.stuba.fiit.mtaa.himypatient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import sk.stuba.fiit.mtaa.himypatient.util.Utilities;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView logoTextView = findViewById(R.id.logo);
        logoTextView.setTypeface(
                Typeface.createFromAsset(getAssets(), "fonts/IndieFlower.ttf")
        );
    }

    public void login(View v) {
        if (Utilities.isPermissionMissing(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Utilities.showToast(this, getString(R.string.error_permission));
            // TODO when images loading properly from server-side (not this device), permission request can be moved to pick image action
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1); // unused request code
        } else if (!Utilities.isConnected(this)) {
            Utilities.showToast(this, getString(R.string.error_internet));
        } else if (!verifyCredentials()) {
            Utilities.showToast(this, getString(R.string.error_credentials));
        } else {
            toMainActivity();
        }
    }

    // TODO replace this method by on server user account check
    private boolean verifyCredentials() {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        // TODO remove true
        return getString(R.string.user_email_dummy).equals(email) &&
                getString(R.string.user_password_dummy).equals(password);
    }

    private void toMainActivity() {
        // new instance of another activity, destroy this activity
        Intent intent = new Intent(this, PatientListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
