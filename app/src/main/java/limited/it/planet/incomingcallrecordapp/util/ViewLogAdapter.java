package limited.it.planet.incomingcallrecordapp.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import limited.it.planet.incomingcallrecordapp.R;

/**
 * Created by Tarikul on 3/12/2018.
 */

public class ViewLogAdapter extends BaseAdapter {
    ArrayList<ViewLogModel> mViewLogList;
    Context mContext;


    public ViewLogAdapter(Context context,ArrayList<ViewLogModel> viewloglist){
        this.mContext = context;
        this.mViewLogList = viewloglist;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
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


        return convertView;
    }

    private class ViewHolder {
        TextView txtUserNumber;
        TextView txtSMSText;
        //TextView txtDate;
        TextView txtTime;
        TextView txtSyncStatus;
    }
}
