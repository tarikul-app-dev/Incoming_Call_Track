package limited.it.planet.incomingcallrecordapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import limited.it.planet.incomingcallrecordapp.constant.Constants;
import limited.it.planet.incomingcallrecordapp.database.DataHelper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static limited.it.planet.incomingcallrecordapp.util.SharedPreferenceSaveAndGet.getValueFromSharedPreferences;

/**
 * Created by Tarikul on 3/10/2018.
 */

public class SendSMSSendToServer {
    public static final String RESPONSE_LOG = Constants.LOG_TAG_RESPONSE;
    String sendSMSAPI = "";
    String paramSMSText = "";
    String paramSmsNumber = "";

    Context mContext;
    DataHelper dataHelper;

    public  SendSMSSendToServer(Context context){
        this.mContext = context;
        sendSMSAPI = getValueFromSharedPreferences("base_api",mContext);
        paramSMSText = getValueFromSharedPreferences("sms_param",mContext);
        paramSmsNumber = getValueFromSharedPreferences("mobile_param",mContext);

        dataHelper = new DataHelper(mContext);
    }

    public void smsSendToServer(String smsText ,String SmsNumber){

      if((sendSMSAPI!=null && !sendSMSAPI.isEmpty())&&(paramSMSText!=null && !paramSMSText.isEmpty())&&(paramSmsNumber!=null && !paramSmsNumber.isEmpty())){
            SendSMSTask sendSMSTask = new SendSMSTask(smsText,SmsNumber);
            sendSMSTask.execute();
        }


    }

    public class SendSMSTask extends AsyncTask<String, Integer, String> {
        String mSMSNumber;
        String mSms;
       // private Dialog loadingDialog;
        public SendSMSTask (String sms,String smsNumber){
            mSms = sms;
            mSMSNumber = smsNumber;
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
                        .add(paramSmsNumber,mSMSNumber)
                        .add(paramSMSText,mSms)
                        .build();


                Request request = new Request.Builder()
                        .url(sendSMSAPI)
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
//                                 String mobNumber = getValueFromSharedPreferences("mob_number",mContext);
//                                 String id = dataHelper.selectRowID(mobNumber);

                                //if(id!=null && !id.isEmpty()){
                                    // long ROWID = Long.parseLong(rowId);
                                    dataHelper.updateSyncStatus(responseResult);

                             //   }


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
