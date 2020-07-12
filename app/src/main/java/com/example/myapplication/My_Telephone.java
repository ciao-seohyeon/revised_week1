package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class My_Telephone extends Fragment {
    public My_Telephone() {

    }

    ArrayList<phone> datas = new ArrayList<phone>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 현재 xml을 fragment에 연결지어주기 위해 inflate 한다
        View view = inflater.inflate(R.layout.fragment_my_telephone, container, false);

        // Content Provider를 이용해서, 내 단말기의 db에 접근하는 함수 (후술)
        getContacts(getActivity());

        // 네 번째 tab
        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler_view2);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerView2.setLayoutManager(manager2); // LayoutManager 등록
        recyclerView2.setAdapter(new MyAdapter(datas));  // Adapter 등록

        return view;
    }

    // Content Provider를 호출해서 전화번호를 다루는 함수
    public void getContacts(Context context){
        // Content Provider를 만들어주기 위해, Content Resolver를 호출한다 (for 객체화)
        ContentResolver resolver = context.getContentResolver();

        // 전화번호가 저장된 db의 테이블 URI를 가져온다
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        // db 쿼리문을 이용해서, 가져오고 싶은 컬럼들을 저장해둔다
        String[] projection = { ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
                ,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ,  ContactsContract.CommonDataKinds.Phone.NUMBER};

        // 쿼리문을 수행하겠다고 선언한다 (Content Provider 이용) - (테이블 주소, 쿼리문, 그외 널값)
        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);

        if(cursor != null){
            // 쿼리문 하나하나 수행될때마다, 커서로 반환됨
            while(cursor.moveToNext()){
                // 쿼리문을 수행해준다 (SQL 쿼리는 아니지만, 정보 하나하나 가져오게 하는 함수들을 이용한 것)
                int idIndex = cursor.getColumnIndex(projection[0]);
                int nameIndex = cursor.getColumnIndex(projection[1]);
                int numberIndex = cursor.getColumnIndex(projection[2]);

                // 위에서 얻은 index 를 사용해서 실제 값을 가져온다.
                String age = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);

                phone phoneBook = new phone();
                phoneBook.setAge(age);
                phoneBook.setName(name);
                phoneBook.setPhone_num(number);

                datas.add(phoneBook);
            }
        }
        // 데이터 계열은 반드시 닫아줘야 한다.
        cursor.close();
    }

    // 전화번호부 관련 클래스
    public class phone{
        private String name;
        private String phone_num;
        private String age;

        public String getName() {
            return name;
        }

        public String getPhone_num() {
            return phone_num;
        }

        public String getAge() {
            return age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone_num(String _phone_num) {
            phone_num = _phone_num;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<My_Telephone.ViewHolder> {

        private ArrayList<My_Telephone.phone> myDataList = null;

        MyAdapter(ArrayList<My_Telephone.phone> dataList)
        {
            myDataList = dataList;
        }

        @Override
        public My_Telephone.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.cardview, parent, false);
            My_Telephone.ViewHolder viewHolder = new My_Telephone.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
            holder.name.setText( myDataList.get(position).getName());
            holder.phone_num.setText(myDataList.get(position).getPhone_num());
            holder.age.setText(myDataList.get(position).getAge());
        }

        @Override
        public int getItemCount()
        {
            //Adapter가 관리하는 전체 데이터 개수 반환
            return myDataList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView phone_num;
        TextView age;

        ViewHolder(View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            phone_num = itemView.findViewById(R.id.phone_num);
            age = itemView.findViewById(R.id.age);
        }
    }
}



