package limited.it.planet.incomingcallrecordapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import limited.it.planet.incomingcallrecordapp.R;
import limited.it.planet.incomingcallrecordapp.database.DataHelper;
import limited.it.planet.incomingcallrecordapp.util.CsvFileExport;
import limited.it.planet.incomingcallrecordapp.util.ViewLogAdapter;
import limited.it.planet.incomingcallrecordapp.util.ViewLogModel;

public class LogViewActivity extends AppCompatActivity {
    ArrayList<ViewLogModel> rowItems;
    ListView listViewLog;
    ViewLogAdapter viewLogAdapter;
    DataHelper dataHelper;
    Toolbar toolbar;
    Button btnClearLog ,btnExport;
    CsvFileExport csvFileExport;
    public static final int REQUEST_PERM_WRITE_STORAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar_log_activity) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        initializeUI();
        listViewLog.setAdapter(viewLogAdapter);

        btnClearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LogViewActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_custom_view,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);

                // Create the alert dialog
                final AlertDialog dialog = builder.create();

                // Set positive/yes button click listener
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the alert dialog
                        dialog.cancel();
                        dataHelper.open();
                        dataHelper.clearLog();
                        recreate();

                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();

                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
             //   recreate();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(LogViewActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERM_WRITE_STORAGE);

                } else{
                    csvFileExport.csvFileExport();
                }

            }
        });


        listViewLog.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

            }
        });




    }

    public void initializeUI(){
        dataHelper = new DataHelper(LogViewActivity.this);
        listViewLog = (ListView)findViewById(R.id.lv_show_log_view);
        btnClearLog = (Button)findViewById(R.id.btn_clear_log);
        btnExport = (Button)findViewById(R.id.btn_export);

        dataHelper.open();
        rowItems = dataHelper.getInputData();
        viewLogAdapter = new ViewLogAdapter(LogViewActivity.this,rowItems);
        csvFileExport = new CsvFileExport(LogViewActivity.this);


    }
}
