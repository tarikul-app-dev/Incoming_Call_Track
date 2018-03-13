package limited.it.planet.incomingcallrecordapp.constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tarikul on 3/1/2018.
 */

public class Constants {
    public  static  final String baseAPI = "https://apps.planetgroupbd.com/ords/accounts/tracking/device/data";
    public static final String  LOG_TAG_RESPONSE = "LOG_TAG_RESPONSE";
    public static final String  LOG_TAG_INSERT = "LOG_TAG_INSERT";
    public static final String  LOG_TAG_UPDATE = "LOG_TAG_UPDATE";
    public static final String  LOG_TAG_ONLINE = "LOG_TAG_ONLINE";


    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String getCurrentEntryDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


}
