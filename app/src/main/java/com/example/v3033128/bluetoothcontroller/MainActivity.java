package com.example.v3033128.bluetoothcontroller;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText editTextName,editTextWidth,editTextHeight,editTextX,editTextY,editTextData;

    private String sBefName,sBefWidth,sBefHeight,sBefX,sBefY,sBefData;

    private ArrayList<ButtonData> list;
    private MyAdapter myAdapter;

    private int mode = 0;//0:追加、1:削除、2:変更
    private int tappedPosition;
    static SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addButton = findViewById(R.id.add);
        Button deleteButton = findViewById(R.id.delete);
        Button editButton = findViewById(R.id.editButton);
        Button compButton = findViewById(R.id.completion);
        final EditText editText = findViewById(R.id.device);
        editText.setText(BluetoothService.connectDevice);
        ListView listView = findViewById(R.id.listView);

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this);

        myAdapter.setButtonList(list);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);

        MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(MySQLiteOpenHelper.TABLE,new String[]{"_id","name","width","height","x","y","send"},null,null,null,null,null);
        while(cursor.moveToNext()) {
            ButtonData bData = new ButtonData();
            bData.setId(cursor.getInt(0));
            bData.setAllData(cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6));
            list.add(bData);
        }
        cursor.close();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                sBefName = "";
                sBefWidth = "";
                sBefHeight = "";
                sBefX = "";
                sBefY = "";
                sBefData = "";
                showDialog(0);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 1;
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 2;
            }
        });
        compButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ControllActivity.class);
                BluetoothService.connectDevice = editText.getText().toString();
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent,View v, int position, long id){
        ButtonData data = list.get(position);
        tappedPosition=position;
        setSBef(data);
        if(mode == 1||mode==2)showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        switch (id){
            case 0:
                editTextName = new EditText(getApplicationContext());
                editTextWidth = new EditText(getApplicationContext());
                editTextHeight = new EditText(getApplicationContext());
                editTextX = new EditText(getApplicationContext());
                editTextY = new EditText(getApplicationContext());
                editTextData = new EditText(getApplicationContext());

                LinearLayout linearLayout = new LinearLayout(getApplicationContext());

                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(make_TextView("Name"), new LinearLayout.LayoutParams(300, 40));
                linearLayout.addView(editTextName, new LinearLayout.LayoutParams(720, 60));
                linearLayout.addView(make_TextView("Width"), new LinearLayout.LayoutParams(300, 40));
                linearLayout.addView(editTextWidth, new LinearLayout.LayoutParams(720, 60));
                linearLayout.addView(make_TextView("Height"), new LinearLayout.LayoutParams(300, 40));
                linearLayout.addView(editTextHeight, new LinearLayout.LayoutParams(720, 60));
                linearLayout.addView(make_TextView("X"), new LinearLayout.LayoutParams(300, 40));
                linearLayout.addView(editTextX, new LinearLayout.LayoutParams(720, 60));
                linearLayout.addView(make_TextView("Y"), new LinearLayout.LayoutParams(300, 40));
                linearLayout.addView(editTextY, new LinearLayout.LayoutParams(720, 60));
                linearLayout.addView(make_TextView("SendData"), new LinearLayout.LayoutParams(300, 40));
                linearLayout.addView(editTextData, new LinearLayout.LayoutParams(720, 60));

                dialog.setView(linearLayout);

                if(mode==0)dialog.setTitle("追加");
                if(mode==1)dialog.setTitle("削除");
                if(mode==2)dialog.setTitle("変更");

                editTextName.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextWidth.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextHeight.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextX.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextY.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextData.setInputType(InputType.TYPE_CLASS_TEXT);

                editTextName.setText(sBefName);
                editTextWidth.setText(sBefWidth);
                editTextHeight.setText(sBefHeight);
                editTextX.setText(sBefX);
                editTextY.setText(sBefY);
                editTextData.setText(sBefData);

                editTextName.setTextColor(Color.BLACK);
                editTextWidth.setTextColor(Color.BLACK);
                editTextHeight.setTextColor(Color.BLACK);
                editTextX.setTextColor(Color.BLACK);
                editTextY.setTextColor(Color.BLACK);
                editTextData.setTextColor(Color.BLACK);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int error = 0;
                        ButtonData newData = new ButtonData();
                        sBefName = editTextName.getText().toString();
                        sBefWidth = editTextWidth.getText().toString();
                        sBefHeight = editTextHeight.getText().toString();
                        sBefX = editTextX.getText().toString();
                        sBefY = editTextY.getText().toString();
                        sBefData = editTextData.getText().toString();
                        if(sBefName==""){
                            Toast.makeText(getApplicationContext(),"Nameに文字を入力してください",Toast.LENGTH_SHORT).show();
                            error++;
                        }
                        try{
                            Integer.parseInt(sBefWidth);
                        }catch (NumberFormatException e){
                            Toast.makeText(getApplicationContext(), "Widthには数字を入力してください", Toast.LENGTH_SHORT).show();
                            error++;
                        }
                        try{
                            Integer.parseInt(sBefHeight);
                        }catch (NumberFormatException e){
                            Toast.makeText(getApplicationContext(), "Heightには数字を入力してください", Toast.LENGTH_SHORT).show();
                            error++;
                        }
                        try{
                            Integer.parseInt(sBefX);
                        }catch (NumberFormatException e){
                            Toast.makeText(getApplicationContext(), "Xには数字を入力してください", Toast.LENGTH_SHORT).show();
                            error++;
                        }
                        try{
                            Integer.parseInt(sBefY);
                        }catch (NumberFormatException e){
                            Toast.makeText(getApplicationContext(), "Yには数字を入力してください", Toast.LENGTH_SHORT).show();
                            error++;
                        }
                        if(sBefData==""){
                            Toast.makeText(getApplicationContext(),"SendDataに文字を入力してください",Toast.LENGTH_SHORT).show();
                            error++;
                        }
                        switch (mode){
                            case 0:
                                if(error==0){
                                    newData.setAllData(sBefName,Integer.valueOf(sBefWidth),Integer.valueOf(sBefHeight),Integer.valueOf(sBefX),Integer.valueOf(sBefY),sBefData);
                                    newData.setId(insertValues(db,sBefName,Integer.valueOf(sBefWidth),Integer.valueOf(sBefHeight),Integer.valueOf(sBefX),Integer.valueOf(sBefY),sBefData));
                                    list.add(newData);
                                    myAdapter.notifyDataSetChanged();
                                }
                                break;
                            case 1:
                                int position = tappedPosition;
                                db.delete(MySQLiteOpenHelper.TABLE,"_id ="+String.valueOf(list.get(position).getId()),null);
                                list.remove(position);
                                myAdapter.notifyDataSetChanged();
                                break;
                            case 2:
                                if(error==0){
                                    list.get(tappedPosition).setAllData(sBefName,Integer.valueOf(sBefWidth),Integer.valueOf(sBefHeight),Integer.valueOf(sBefX),Integer.valueOf(sBefY),sBefData);
                                    updateValues(db,list.get(tappedPosition));
                                    myAdapter.notifyDataSetChanged();
                                }
                                break;
                        }
                        // ダイアログを消す
                        removeDialog(0);
                    }
                });
                dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeDialog(0);
                    }
                });
                break;
        }
        return  dialog.create();
    }

    private TextView make_TextView(String sMessage){
        //テキストビューの生成
        TextView tv = new TextView(getApplicationContext());
        tv.setTextColor(Color.GRAY);
        tv.setWidth(500);
        //メッセージの設定
        tv.setText(sMessage);
        return tv;
    }
    private void setSBef(ButtonData buttonData){
        sBefName = buttonData.getName();
        sBefWidth = String.valueOf(buttonData.getWidth());
        sBefHeight = String.valueOf(buttonData.getHeight());
        sBefX = String.valueOf(buttonData.getMarginX());
        sBefY = String.valueOf(buttonData.getMarginY());
        sBefData = buttonData.getData();
    }
    public long insertValues(SQLiteDatabase db, String name, int width, int height, int marginX, int marginY, String data)
    {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("width",width);
        values.put("height",height);
        values.put("x",marginX);
        values.put("y",marginY);
        values.put("send", data);
        //Toast.makeText(getApplicationContext(),"ins",Toast.LENGTH_SHORT);
        return db.insert(MySQLiteOpenHelper.TABLE, null, values);
        //データベースの行を挿入
    }
    public void updateValues(SQLiteDatabase db,ButtonData data){
        ContentValues values = new ContentValues();
        values.put("name", data.getName());
        values.put("width",data.getWidth());
        values.put("height",data.getHeight());
        values.put("x",data.getMarginX());
        values.put("y",data.getMarginY());
        values.put("send", data.getData());
        db.update(MySQLiteOpenHelper.TABLE,values,"_id ="+String.valueOf(data.getId()),null);
    }
}