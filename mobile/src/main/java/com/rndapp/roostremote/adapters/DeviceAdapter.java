package com.rndapp.roostremote.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rndapp.roostremote.R;
import com.rndapp.roostremote.interfaces.OnDeviceClickedListener;
import com.rndapp.roostremote.models.Device;
import com.rndapp.roostremote.models.DeviceHolder;

import java.util.List;

/**
 * Created by ell on 1/11/16.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceHolder> {
    private OnDeviceClickedListener listener;
    private List<Device> devices;

    public DeviceAdapter(List<Device> devices, OnDeviceClickedListener listener) {
        this.devices = devices;
        this.listener = listener;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_view, parent, false);
        return new DeviceHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        holder.displayDevice(devices.get(position));
    }

    @Override
    public int getItemCount() {
        return devices != null ? devices.size() : 0;
    }
}
