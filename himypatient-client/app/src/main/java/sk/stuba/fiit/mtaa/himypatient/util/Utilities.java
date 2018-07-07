package sk.stuba.fiit.mtaa.himypatient.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import sk.stuba.fiit.mtaa.himypatient.R;

public class Utilities {

    /**
     * Show Toast within defined context
     */
    public static void showToast(Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String buildCommaName(Context context,
                                        @NonNull String firstName, @NonNull String lastName) {
        return context.getString(R.string.comma_name, lastName, firstName.charAt(0));
    }

    public static DateFormat buildDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
    }

    private static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    /**
     * Convert (often only temporary, unstable to store) content provider's uri path to file path
     * content://...  -->  file:/...
     * @return real path
     */
    public static String convertPath(Context context, Uri contentUri) {
        Cursor cursor = null;

        // 1st supported option
        // content://com.android.providers.media.documents/document/image%...
        try {
            String wholeId = DocumentsContract.getDocumentId(contentUri); // image:x where x integer

            String splitId[] = wholeId.split(Pattern.quote(":"));
            String id = splitId[1];

            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";

            cursor = context.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                return Constants.PATH_PREFIX_FILE + cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }

        // 2nd supported option
        // content://media/external/images/media/...
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return Constants.PATH_PREFIX_FILE +  cursor.getString(columnIndex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }

        return null;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        for (Network network : cm.getAllNetworks()) {
            NetworkInfo networkInfo = cm.getNetworkInfo(network);
            if (networkInfo != null && networkInfo.isConnected()) return true;
        }
        return false;
    }

    public static boolean isPermissionMissing(Context context, @NonNull String permissionConstant){
        return ContextCompat.checkSelfPermission(context, permissionConstant) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
