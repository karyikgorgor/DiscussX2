package com.fyp.discussx.ui.activities.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.discussx.R;


import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends BaseAdapter {
    private Activity context;
    private List <String> groupName = new ArrayList<>();
    private List <String> groupId = new ArrayList<>();


    public CustomAdapter (Activity context, List<String> groupName, List<String> groupId) {
        super();
        this.context = context;
        this.groupName = groupName;
        this.groupId = groupId;
    }

    @Override
    public int getCount() {
        return groupName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       ViewHolder viewHolder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.groupName = convertView.findViewById(R.id.text1);
            viewHolder.groupId = convertView.findViewById(R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.groupName.setText(groupName.get(position));
        viewHolder.groupId.setText(groupId.get(position));

        return convertView;
    }
    private class ViewHolder  {
        TextView groupName;
        TextView groupId;
    }

}
