package com.example.v3033128.bluetoothcontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<ButtonData> buttonList;

    public MyAdapter(Context context){
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void setButtonList(ArrayList<ButtonData> buttonList){
        this.buttonList = buttonList;
    }

    @Override
    public int getCount(){
        return buttonList.size();
    }

    @Override
    public Object getItem(int position){
        return buttonList.get(position);
    }

    @Override
    public  long getItemId(int position){
        return buttonList.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = layoutInflater.inflate(R.layout.list,parent,false);

        ((TextView)convertView.findViewById(R.id.name)).setText(buttonList.get(position).getName());
        ((TextView)convertView.findViewById(R.id.data)).setText(buttonList.get(position).getData());

        return convertView;
    }
}
