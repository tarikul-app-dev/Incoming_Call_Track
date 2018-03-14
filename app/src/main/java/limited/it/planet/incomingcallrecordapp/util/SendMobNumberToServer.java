package limited.it.planet.incomingcallrecordapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import limited.it.planet.incomingcallrecordapp.constant.Constants;
import limited.it.planet.incomingcallrecordapp.database.DataHelper;
import limited.it.planet.incomingcallrecordapp.fragments.DashboardFragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static limited.it.planet.incomingcallrecordapp.util.SharedPreferenceSaveAndGet.getValueFromSharedPreferences;

/**
 * Created by Tarikul on 3/1/2018.
 */

public class SendMobNumberToServer {
    String sendMobNumberAPI = "";
    String paramMobile = "";

    public static final String RESPONSE_LOG = Constants.LOG_TAG_RESPONSE;

    Context mContext;
    DataHelper dataHelper;
      public  SendMobNumberToServer(Context context){
            this.mContext = context;
          sendMobNumberAPI = getValueFromSharedPreferences("base_api",mContext);
          paramMobile = getValueFromSharedPreferences("mobile_param",mContext);

          dataHelper = new DataHelper(mContext);
        }

        public void mobileNumberSendToServer(String mIncomingNumber){

            if((sendMobNumberAPI!=null && !sendMobNumberAPI.isEmpty())&& (paramMobile!=null && !paramMobile.isEmpty() && (mIncomingNumber!=null && !mIncomingNumber.isEmpty())) ){
                SendIncomingNumberTask sendIncomingNumberTask = new SendIncomingNumberTask(mIncomingNumber);
                sendIncomingNumberTask.execute();
            }


        }

    public class SendIncomingNumberTask extends AsyncTask<String, Integer, String> {

            String mIncomingNumber ;

        public  SendIncomingNumberTask (String incomingNumber){
            this.mIncomingNumber = incomingNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            try {
                RequestBody requestBody = new FormBody.Builder()
                        .add(paramMobile,mIncomingNumber)
                        .build();


                Request request = new Request.Builder()
                        .url(sendMobNumberAPI)
                        .post(requestBody)
                        .build();


                Response response = null;
                //client.setRetryOnConnectionFailure(true);
                response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    final String result = response.body().string();
                    Log.d(RESPONSE_LOG,result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String responseResult = jsonObject.getString("response");
                        if(responseResult.equals("success")){
                            dataHelper.open();

                            try {
//                                String mobNumber = getValueFromSharedPreferences("mob_number",mContext);
//                                String id = dataHelper.selectRowID(mobNumber);

                                //if(id!=null && !id.isEmpty()){
                                    // long ROWID = Long.parseLong(rowId);
                                    dataHelper.updateSyncStatus(responseResult);

                              //  }


                            }catch (NumberFormatException e){
                                e.getMessage();
                            }



                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

    }
}
