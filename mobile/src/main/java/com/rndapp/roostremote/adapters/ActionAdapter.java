package com.rndapp.roostremote.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rndapp.roostremote.R;
import com.rndapp.roostremote.models.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 5/17/15.
 */
public class ActionAdapter extends BaseAdapter{
    private List<Option> options = new ArrayList<>();
    private LayoutInflater mLayoutInflator;

    public ActionAdapter(Context context, List<Option> options) {
        mLayoutInflator = LayoutInflater.from(context);
        this.options = options;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Option getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mLayoutInflator.inflate(R.layout.action_item, parent, false);
        }

        Option option = getItem(position);
        ((TextView)convertView.findViewById(R.id.tv_action_name)).setText(option.getName());

        return convertView;
    }
}
