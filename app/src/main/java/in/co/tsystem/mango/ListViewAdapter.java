package in.co.tsystem.mango;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/*
import static example.multicolumnlist.Constants.FIRST_COLUMN;
import static example.multicolumnlist.Constants.SECOND_COLUMN;
import static example.multicolumnlist.Constants.THIRD_COLUMN;
import static example.multicolumnlist.Constants.FOURTH_COLUMN;
*/


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
        //NumberPicker np;
        Button inc;
        TextView txtSecond;
        Button dec;
        TextView txtThird;

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
            //holder.np = (NumberPicker) convertView.findViewById(R.id.numberPicker1);
            holder.inc = (Button) convertView.findViewById(R.id.inc);
            holder.txtSecond=(TextView) convertView.findViewById(R.id.TextSecond);
            holder.dec = (Button) convertView.findViewById(R.id.dec);
            holder.txtThird=(TextView) convertView.findViewById(R.id.TextThird);


            convertView.setTag(holder);
        }else{

            holder=(ViewHolder) convertView.getTag();
        }

        mangoGlobals mg = mangoGlobals.getInstance();

        HashMap<String, String> map=list.get(position);
        holder.txtFirst.setText(map.get(mg.FIRST_COLUMN));
        holder.txtSecond.setText(map.get(mg.SECOND_COLUMN));
        holder.txtThird.setText(map.get(mg.THIRD_COLUMN));

        //holder.np.setMinValue(0);
        //holder.np.setMaxValue(10);
        //holder.np.setWrapSelectorWheel(false);


        /*
        holder.np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                String Old = "Old Value : ";
                String New = "New Value : ";

                tv1.setText(Old.concat(String.valueOf(oldVal)));
                tv2.setText(New.concat(String.valueOf(newVal)));

            }
        });*/

        return convertView;
    }

}

