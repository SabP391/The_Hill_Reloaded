package com.example.thehillreloaded.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thehillreloaded.R;

import java.util.ArrayList;

public class BTDeviceListAdapter extends ArrayAdapter<BluetoothDevice> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<BluetoothDevice> mDevices;
    private int mViewResouceId;

    public BTDeviceListAdapter(Context context, int resouceId, ArrayList<BluetoothDevice> devices) {
        super(context, resouceId, devices);
        this.mDevices = devices;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResouceId = resouceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResouceId, null);

        BluetoothDevice device = mDevices.get(position);

        if (device != null) {
            TextView deviceName = (TextView) convertView.findViewById(R.id.BTDeviceName);

            if (deviceName != null) {
                deviceName.setText(device.getName());
            }
        }

        return convertView;
    }
}
