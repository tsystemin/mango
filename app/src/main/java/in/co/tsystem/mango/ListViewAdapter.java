package in.co.tsystem.mango;

import android.content.Context;
import android.widget.BaseAdapter;

/*
import static example.multicolumnlist.Constants.FIRST_COLUMN;
import static example.multicolumnlist.Constants.SECOND_COLUMN;
import static example.multicolumnlist.Constants.THIRD_COLUMN;
import static example.multicolumnlist.Constants.FOURTH_COLUMN;
*/
import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by stheetharappan on 5/8/2015.
 */
public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder{
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.activity_list_view_adapter, null);
            holder=new ViewHolder();

            holder.txtFirst=(TextView) convertView.findViewById(R.id.TextFirst);
            holder.txtSecond=(TextView) convertView.findViewById(R.id.TextSecond);
            holder.txtThird=(TextView) convertView.findViewById(R.id.TextThird);
            holder.txtFourth=(TextView) convertView.findViewById(R.id.TextFourth);

            convertView.setTag(holder);
        }else{

            holder=(ViewHolder) convertView.getTag();
        }

        mangoGlobals mg = mangoGlobals.getInstance();

        HashMap<String, String> map=list.get(position);
        holder.txtFirst.setText(map.get(mg.FIRST_COLUMN));
        holder.txtSecond.setText(map.get(mg.SECOND_COLUMN));
        holder.txtThird.setText(map.get(mg.THIRD_COLUMN));
        holder.txtFourth.setText(map.get(mg.FOURTH_COLUMN));
        return convertView;
    }

}

