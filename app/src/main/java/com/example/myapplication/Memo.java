package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Memo extends Fragment {
    public Memo() {
        // Required empty public constructor
    }

    // 리사이클러뷰 부분
    RecyclerView recyclerView;
    Memo_Adapter memo_adapter;
    ArrayList<Memo_data_class> item_list;

    // 데이터베이스 부분
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    String tableName;
    String dbName;

    DatabaseHelper_pwd dbHelper_pwd;
    SQLiteDatabase passwordDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        // 로그인 화면 창을 띄워준다
        grant();

        /* 데이터베이스 관련 */
        // 데이터베이스와 테이블을 만들어 준다
        dbName = "memo.db";
        tableName = "noteData";

        // dbHelper를 호출한다
        dbHelper = new DatabaseHelper(getActivity());
        dbHelper_pwd = new DatabaseHelper_pwd(getActivity());

        // dbHelper를 이용해서, 이에 맞는 데이터베이스를 생성한다.
        database = dbHelper.getWritableDatabase();
        passwordDB = dbHelper_pwd.getWritableDatabase();

        // database에 테이블을 생성한다.
        database.execSQL( "CREATE TABLE if not exists noteData (" +
                "`title` TEXT PRIMARY KEY AUTOINCREMENT," +
                "`date` TEXT);");
        passwordDB.execSQL( "CREATE TABLE if not exists password_table (" +
                "`pwd` TEXT PRIMARY KEY AUTOINCREMENT);");

        // 비밀번호를 데이터베이스에 저장해준다
        // 저장이 되어있지 않은 경우에만 DB에 작성. 저장이 이미 되어 있는 경우엔 저장 안한다.
        String real_pwd = "1234";
        Cursor cursor = passwordDB.rawQuery("select * from password_table", null);
        if(cursor.getCount()==0)
            passwordDB.execSQL("insert into password_table values ("+real_pwd+");" );

        // 데이터베이스에 있는 모든 내용들을 아이템에 넣어준다
        database_to_itemList();

        /* 리사이클러 뷰 관련 */
        // 리사이클러 뷰를 만든다
        recyclerView = view.findViewById(R.id.recycler_memo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 메모 아답터를 생성해준다
        memo_adapter = new Memo_Adapter(item_list);

        // 리사이클러 뷰에 대해 아답터를 설정해준다.
        recyclerView.setAdapter(memo_adapter);

        /* 버튼 관련 */
        // 버튼에 대한 리스너를 설정해준다.
        FloatingActionButton _fab_memo = view.findViewById(R.id.fab_memo);
        _fab_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_box();
            }
        });

        return view;
    }

    // 아이템을 아이템 리스트에 추가해준다
    public void addItem(String title, String date){
        Memo_data_class item = new Memo_data_class();

        item.setTitle(title);
        item.setDate(date);

        item_list.add(item);
    }

    // 데이터베이스에 있는 모든 내용들을 아이템에 넣어준다
    public void database_to_itemList(){
        item_list = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from " +tableName, null);
        int recordCount = cursor.getCount();

        for (int i=0;i<recordCount;i++){
            cursor.moveToNext();
            String title = cursor.getString(0);
            String date = cursor.getString(1);

            addItem(title, date);
        }
    }

    public void grant(){
        // 비밀번호가 저장된 테이블을 찾기 위한 변수
        String password_table = "password_table";

        // AlertDialog를 만들어준다
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_grant, null);
        builder.setView(view);

        // 버튼, 비밀번호 칸
        final Button _grant_button = (Button)view.findViewById(R.id.grant_button);
        final EditText _password = (EditText)view.findViewById(R.id.text_password);

        // 모든 설정이 완료되었으므로, Dialog를 호출한다.
        final AlertDialog dialog = builder.create();

        // 버튼에 대한 리스너를 설정한다
        _grant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 작성한 비밀번호를 받아들인다
                String written_password = _password.getText().toString();

                // SQL 문장을 작성해서, 비밀번호가 담긴 데이터베이스에 접근한다
                String sql = "select * from " + password_table + ";";
                Cursor cursor = passwordDB.rawQuery(sql, null);
                cursor.moveToNext();

                Log.d(written_password + "", cursor.getString(0) + "");
                // 비밀번호와, 사용자가 작성한 비밀번호가 일치할 경우 통과한다
                if(written_password.equals(cursor.getString(0)))
                    dialog.dismiss();
                else{
                    // 일치하지 않을 경우, 에러 메세지가 뜬다
                    Toast error_msg = Toast.makeText(getActivity(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT);
                    error_msg.show();
                }
            }
        });
        dialog.show();
    }

    // 쓸 메모 내용을 작성하기 위해, AlertDialog를 작성해준다
    public void show_box(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_memo, null);
        builder.setView(view);

        final Button submit = (Button)view.findViewById(R.id.button_submit);
        final EditText title = (EditText)view.findViewById(R.id.edittextEmailAddress);
        final EditText date = (EditText)view.findViewById(R.id.edittextPassword);

        final  AlertDialog dialog = builder.create();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _date = date.getText().toString();
                String _title = title.getText().toString();

                String sql = "insert into "+tableName+" values ('"+_title+"', '"+_date+"');";
                database.execSQL(sql);

                // 데이터베이스에 있는 모든 내용들을 아이템에 넣어준다
                database_to_itemList();

                // 메모 아답터를 생성해준다
                memo_adapter = new Memo_Adapter(item_list);

                // 리사이클러 뷰에 대해 아답터를 설정해준다.
                recyclerView.setAdapter(memo_adapter);

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}