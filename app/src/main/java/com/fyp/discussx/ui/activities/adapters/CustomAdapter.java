package com.fyp.discussx.ui.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fyp.discussx.R;
import com.fyp.discussx.model.GroupNameAndId;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<GroupNameAndId> listData;
    private LayoutInflater layoutInflater;

    public CustomAdapter(Context aContext, ArrayList<GroupNameAndId> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.groupName =  convertView.findViewById(R.id.text1);
            holder.groupId =  convertView.findViewById(R.id.text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.groupName.setText(listData.get(position).getGroupName());
        holder.groupId.setText(listData.get(position).getGroupId());

        return convertView;
    }

    static class ViewHolder {
        TextView groupName;
        TextView groupId;
    }
}
