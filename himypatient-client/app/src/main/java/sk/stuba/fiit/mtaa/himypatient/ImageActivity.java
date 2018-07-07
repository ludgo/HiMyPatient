package sk.stuba.fiit.mtaa.himypatient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_IMAGE_URL = "intent_extra_image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageView imageView = findViewById(R.id.image_full_screen);
        imageView.setBackgroundColor(getColor(R.color.colorBackground));

        if (getIntent().hasExtra(INTENT_EXTRA_IMAGE_URL)) {
            String imageUrl = getIntent().getStringExtra(INTENT_EXTRA_IMAGE_URL);
            if (imageUrl != null && !"".equals(imageUrl)) {
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.profile_default)
                        .error(R.drawable.profile_default)
                        .into(imageView);
            }
        }
    }
}
