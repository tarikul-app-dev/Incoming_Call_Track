package limited.it.planet.incomingcallrecordapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import limited.it.planet.incomingcallrecordapp.R;
import limited.it.planet.incomingcallrecordapp.constant.Constants;
import limited.it.planet.incomingcallrecordapp.database.DataHelper;
import limited.it.planet.incomingcallrecordapp.util.SendMobNumberToServer;
import limited.it.planet.incomingcallrecordapp.util.SendSMSSendToServer;

import static limited.it.planet.incomingcallrecordapp.util.SharedPreferenceSaveAndGet.getBoleanValueSharedPreferences;
import static limited.it.planet.incomingcallrecordapp.util.SharedPreferenceSaveAndGet.getValueFromSharedPreferences;
import static limited.it.planet.incomingcallrecordapp.util.SharedPreferenceSaveAndGet.saveBoleanValueSharedPreferences;
import static limited.it.planet.incomingcallrecordapp.util.SharedPreferenceSaveAndGet.saveToSharedPreferences;


public class SettingsFragment extends AppFragment {

    static EditText edtHTTPURLCall,editTextParamMobile,editTextParamSMS;
    CheckBox checkBoxMobile,checkBoxSMS;
    TextView txvHelpCall ;

    static String sendMobNumberAPI = "";
    static String sendSmsAPI = "";

    public static String httpURLNumerfromEDT = "";
    //public static String httpURLSMSfromEDT = "";
    public static String paramMobile = "";
    public static String paramSMS = "";

   // public static String mobile = "";
    static SendMobNumberToServer sendMobNumberToServer;
    static SendSMSSendToServer sendSMSSendToServer;
//   static boolean chageHTTPByUser;
//   static boolean chageHTTPSMSByUser;
//    static boolean chageMobileByUser;
//    static boolean chageSMSByUser;
   static boolean isCheckMobileNumber;
   static boolean isCheckSMS ;

   Button btnSave;
   DataHelper dataHelper;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendMobNumberToServer = new SendMobNumberToServer(getActivity());
        sendSMSSendToServer= new SendSMSSendToServer(getActivity());
        dataHelper = new DataHelper(getActivity());
        dataHelper.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        edtHTTPURLCall = (EditText)rootView.findViewById(R.id.edt_http_url_call);
       // edtHTTPURLSms = (EditText)rootView.findViewById(R.id.edt_http_url_sms);
        editTextParamMobile = (EditText)rootView.findViewById(R.id.edt_param_call);
        editTextParamSMS = (EditText)rootView.findViewById(R.id.edt_param_sms);
        checkBoxMobile =(CheckBox)rootView.findViewById(R.id.checkbox_send_incoming_number);
        checkBoxSMS = (CheckBox)rootView.findViewById(R.id.checkbox_send_sms);

        //edtShowNumber = (EditText)rootView.findViewById(R.id.edt_show_number);
        txvHelpCall = (TextView)rootView.findViewById(R.id.txv_help_call);
        btnSave = (Button)rootView.findViewById(R.id.btn_save);


        sendMobNumberAPI = Constants.baseAPI;
        sendSmsAPI = Constants.baseAPI;
        //saveToSharedPreferences("mob_number_api",sendMobNumberAPI,getActivity());



//        editTextParamMobile.setText("mobile");
//        editTextParamSMS.setText("sms");


        checkBoxMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxMobile.isChecked()){
                    saveBoleanValueSharedPreferences("is_mobile",true,getActivity());

                }else {
                    saveBoleanValueSharedPreferences("is_mobile",false,getActivity());
                }
            }
        });
        checkBoxSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxSMS.isChecked()){
                    saveBoleanValueSharedPreferences("is_sms",true,getActivity());
                }else {
                    saveBoleanValueSharedPreferences("is_sms",false,getActivity());
                }
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpURLNumerfromEDT = edtHTTPURLCall.getText().toString();
                paramMobile = editTextParamMobile.getText().toString();
                paramSMS = editTextParamSMS.getText().toString();
                txvHelpCall.setText(httpURLNumerfromEDT );
                saveToSharedPreferences("base_api",httpURLNumerfromEDT,getActivity());
                saveToSharedPreferences("mobile_param",paramMobile,getActivity());
                saveToSharedPreferences("sms_param",paramSMS,getActivity());

                Toast.makeText(getActivity(), "Your Information Save Successfully ", Toast.LENGTH_LONG).show();

            }
        });


        return rootView;
    }




    @Override
    public void onResume() {
        super.onResume();
        isCheckMobileNumber =getBoleanValueSharedPreferences("is_mobile",getActivity());
         isCheckSMS = getBoleanValueSharedPreferences("is_sms",getActivity());

        if(isCheckMobileNumber){
            checkBoxMobile.setChecked(isCheckMobileNumber);
        }

        if(isCheckSMS){
            checkBoxSMS.setChecked(isCheckSMS);
        }

        String saveHttpNumberAPI = getValueFromSharedPreferences("base_api",getActivity());
        if(saveHttpNumberAPI!=null && !saveHttpNumberAPI.isEmpty()){
            edtHTTPURLCall.setText(saveHttpNumberAPI);
            txvHelpCall.setText(saveHttpNumberAPI );
        }


        String saveParamMobile = getValueFromSharedPreferences("mobile_param",getActivity());
        if(saveParamMobile!=null && !saveParamMobile.isEmpty()){
            editTextParamMobile.setText(saveParamMobile );
        }
        String saveParamSMS = getValueFromSharedPreferences("sms_param",getActivity());
        if(saveParamSMS!=null && !saveParamSMS.isEmpty()){
            editTextParamSMS.setText(saveParamSMS );
        }


    }

}
