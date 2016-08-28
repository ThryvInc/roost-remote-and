package com.rndapp.roostremote.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rndapp.roostremote.R;
import com.rndapp.roostremote.interfaces.OnDeviceClickedListener;

/**
 * Created by ell on 1/11/16.
 */
public class DeviceHolder extends RecyclerView.ViewHolder  {
    private OnDeviceClickedListener listener;
    private TextView nameTextView;
    private TextView urlTextView;

    public DeviceHolder(View itemView, OnDeviceClickedListener listener) {
        super(itemView);
        this.listener = listener;
        nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
        urlTextView = (TextView) itemView.findViewById(R.id.tv_subtitle);
    }

    public void displayDevice(final Device device){
        if (device != null){
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onDeviceClicked(device);
                }
            });
            display(device);
        }
    }

    private void display(Device device){
        nameTextView.setText(device.getName());
        urlTextView.setText(device.getDescriber());
    }
}
