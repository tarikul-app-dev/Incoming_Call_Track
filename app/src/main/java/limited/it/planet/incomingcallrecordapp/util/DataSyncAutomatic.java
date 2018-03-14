package limited.it.planet.incomingcallrecordapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;

import limited.it.planet.incomingcallrecordapp.constant.Constants;
import limited.it.planet.incomingcallrecordapp.database.DataHelper;

/**
 * Created by Tarikul on 3/13/2018.
 */

public class DataSyncAutomatic extends BroadcastReceiver{
    public static final String RESPONSE_LOG = Constants.LOG_TAG_ONLINE;
    DataHelper dataHelper;
    String userNumber = "";
    String smsText = "";

    SendMobNumberToServer sendMobNumberToServer ;
    SendSMSSendToServer sendSMSSendToServer;

    @Override
    public void onReceive(Context context, Intent intent) {
        sendMobNumberToServer = new SendMobNumberToServer(context);
        sendSMSSendToServer = new SendSMSSendToServer(context);

        dataHelper = new DataHelper(context);
        dataHelper.open();

        if (checkInternet(context)) {

                ArrayList<ViewLogModel> trackingList = dataHelper.checkFailedDataFromTable("failled");
                for (int i = 0; i < trackingList.size(); i++) {
                    userNumber = trackingList.get(i).getUserNumber();
                    smsText = trackingList.get(i).getSmsText();

                    if(smsText.isEmpty()){
                        sendMobNumberToServer.mobileNumberSendToServer(userNumber);
                    }
                    if(!userNumber.isEmpty()&&!smsText.isEmpty()){
                        sendSMSSendToServer.smsSendToServer(smsText,userNumber);
                    }
                }



                Log.d("Network", "Internet YAY");
        }
    }
    protected boolean checkInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
