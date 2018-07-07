package sk.stuba.fiit.mtaa.himypatient.rest;

import android.content.Context;

import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.stuba.fiit.mtaa.himypatient.R;
import sk.stuba.fiit.mtaa.himypatient.util.Utilities;

public abstract class RetrofitCallback<T> implements Callback<T> {

    private Context mContext;

    protected RetrofitCallback(Context context) {
        mContext = context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        // ... do some work here
        if (!response.isSuccessful()) {
            // unexpected to reach
            // 4xx, 5xx
            Utilities.showToast(mContext, mContext.getString(R.string.error_unavailable));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        // To prevent getting here check connection before every call!
        if (t instanceof ConnectException) {
            // No response when connecting server.
            // Uniquely may occur also if connection has been interrupted
            // just after check on that, before whatever call
            Utilities.showToast(mContext, mContext.getString(R.string.error_unavailable));
        } else {
            // unexpected to reach
            t.printStackTrace();
        }
    }
}
