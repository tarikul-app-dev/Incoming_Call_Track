package limited.it.planet.incomingcallrecordapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import limited.it.planet.incomingcallrecordapp.R;

/**
 * Created by Tarikul on 3/12/2018.
 */

public class ViewLogAdapter extends BaseAdapter {
    ArrayList<ViewLogModel> mViewLogList;
    Context mContext;
    SendMobNumberToServer sendMobNumberToServer;
    SendSMSSendToServer sendSMSSendToServer;

    public ViewLogAdapter(Context context,ArrayList<ViewLogModel> viewloglist){
        this.mContext = context;
        this.mViewLogList = viewloglist;
        sendMobNumberToServer = new SendMobNumberToServer(mContext);
        sendSMSSendToServer = new SendSMSSendToServer(mContext);
    }

    @Override
    public int getCount() {
        int count=mViewLogList.size(); //counts the total number of elements from the arrayList
        return count;

    }

    @Override
    public Object getItem(int i) {
        return mViewLogList.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_row_item, null);
            holder = new ViewHolder();

            holder.txtUserNumber = (TextView) convertView.findViewById(R.id.tv_user_number);
            holder.txtSMSText = (TextView) convertView.findViewById(R.id.tv_sms_text);
           // holder.txtDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.txtTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.txtSyncStatus = (TextView) convertView.findViewById(R.id.tv_sync_status);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewLogModel contactModel = (ViewLogModel) getItem(position);
        holder.txtUserNumber .setText(contactModel.getUserNumber());
        holder.txtSMSText.setText(contactModel.getSmsText());
       // holder.txtDate.setText(contactModel.getDate());

        holder.txtTime.setText(contactModel.getTime());
        holder.txtSyncStatus.setText(contactModel.getSyncStatus());

        String syncStatus = contactModel.getSyncStatus();
        if(syncStatus.equals("failled")){
            holder.txtSyncStatus.setPaintFlags(holder.txtSyncStatus.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.txtSyncStatus.setText(syncStatus);
            holder.txtSyncStatus.setTextColor(Color.RED);
        }else {
            holder.txtSyncStatus.setTextColor(Color.GREEN);
        }

        holder.txtSyncStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewLogModel     listItem =  mViewLogList.get(position);

                String userNumber = listItem.getUserNumber();
                String syncStatus = listItem.getSyncStatus();
                String smsText = listItem.getSmsText();

                if(syncStatus.equals("failled")){
                    if(smsText.isEmpty()){
                        if(checkInternet(mContext)){
                            sendMobNumberToServer.mobileNumberSendToServer(userNumber);
                        }else {
                            Toast.makeText(mContext, "Your Device is Offline", Toast.LENGTH_LONG).show();
                        }

                    }
                    if(!userNumber.isEmpty()&&!smsText.isEmpty()){
                        if(checkInternet(mContext)){
                            sendSMSSendToServer.smsSendToServer(smsText,userNumber);
                        }else {
                            Toast.makeText(mContext, "Your Device is Offline", Toast.LENGTH_LONG).show();
                        }


                    }
                }

               //Toast.makeText(mContext, listItem, Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView txtUserNumber;
        TextView txtSMSText;
        //TextView txtDate;
        TextView txtTime;
        TextView txtSyncStatus;
    }
    protected boolean checkInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void refresh(ArrayList<ViewLogModel> items)
    {
        this.mViewLogList = items;
        notifyDataSetChanged();
    }
}
