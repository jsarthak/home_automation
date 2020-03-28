package com.example.sarthakj.home_automation_mark1;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SarthakJ on 8/12/2017.
 */

public class SwitchRecyclerAdapter extends RecyclerView.Adapter<SwitchRecyclerAdapter.SwitchViewHolder>{

    Context mContext;
    ArrayList<Entity> entities;
    BluetoothSocket mBluetoothSocket;


    public SwitchRecyclerAdapter(Context context, ArrayList<Entity> entities, BluetoothSocket bluetoothSocket) {
        this.mContext = context;
        this.entities = entities;
        this.mBluetoothSocket = bluetoothSocket;
    }

    @Override
    public SwitchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.switch_layout_item, parent, false);
        return new SwitchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SwitchViewHolder holder, int position) {
        Entity entity = entities.get(position);
        holder.name.setText(entity.getName());
        holder.xSwitch.setChecked(entity.isStatus());
        holder.xSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mBluetoothSocket != null) {
                        try {
                            mBluetoothSocket.getOutputStream().write("0".toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (mBluetoothSocket != null) {
                        try {
                            mBluetoothSocket.getOutputStream().write("1".toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SwitchViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        Switch xSwitch;

        public SwitchViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            xSwitch = (Switch)itemView.findViewById(R.id.switch_main);
        }




    }
}
