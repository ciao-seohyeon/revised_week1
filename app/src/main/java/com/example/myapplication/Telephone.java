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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Telephone extends Fragment {
    ArrayList<phone> phoneList = new ArrayList<phone>();

    public Telephone() {
        // Required empty public constructor
    }

    ArrayList<phone> datas = new ArrayList<phone>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_telephone, container, false);

        // 전화번호부 1
        String json_str = getJsonString();
        jsonParsing(json_str);

        // 첫 번째 tab
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new MyAdapter(phoneList));  // Adapter 등록

        getContacts(getActivity());

        // 네 번째 tab
        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler_view3);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerView2.setLayoutManager(manager2); // LayoutManager 등록
        recyclerView2.setAdapter(new Telephone.MyAdapter2(datas));  // Adapter 등록

        return view;
    }

    public void getContacts(Context context){
        // 데이터베이스 혹은 content resolver 를 통해 가져온 데이터를 적재할 저장소를 먼저 정의

        // 1. Resolver 가져오기(데이터베이스 열어주기)
        // 전화번호부에 이미 만들어져 있는 ContentProvider 를 통해 데이터를 가져올 수 있음
        // 다른 앱에 데이터를 제공할 수 있도록 하고 싶으면 ContentProvider 를 설정
        // 핸드폰 기본 앱 들 중 데이터가 존재하는 앱들은 Content Provider 를 갖는다
        // ContentResolver 는 ContentProvider 를 가져오는 통신 수단
        ContentResolver resolver = context.getContentResolver();

        // 2. 전화번호가 저장되어 있는 테이블 주소값(Uri)을 가져오기
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        // 3. 테이블에 정의된 칼럼 가져오기
        // ContactsContract.CommonDataKinds.Phone 이 경로에 상수로 칼럼이 정의
        String[] projection = { ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
                ,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ,  ContactsContract.CommonDataKinds.Phone.NUMBER};

        // 4. ContentResolver로 쿼리를 날림 -> resolver 가 provider 에게 쿼리하겠다고 요청
        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);

        // 4. 커서로 리턴된다. 반복문을 돌면서 cursor 에 담긴 데이터를 하나씩 추출
        if(cursor != null){
            while(cursor.moveToNext()){
                // 4.1 이름으로 인덱스를 찾아준다
                int idIndex = cursor.getColumnIndex(projection[0]); // 이름을 넣어주면 그 칼럼을 가져와준다.
                int nameIndex = cursor.getColumnIndex(projection[1]);
                int numberIndex = cursor.getColumnIndex(projection[2]);
                // 4.2 해당 index 를 사용해서 실제 값을 가져온다.
                String age = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);

                Telephone.phone phoneBook = new Telephone.phone();
                phoneBook.setName(name);
                phoneBook.setPhone_num(number);

                datas.add(phoneBook);
            }
        }
        // 데이터 계열은 반드시 닫아줘야 한다.
        cursor.close();
    }

    // json 파싱용 클래스
    public class phone {
        private String name;
        private String phone_num;
        private int imageResource;

        public String getName() {
            return name;
        }

        public String getPhone_num() {
            return phone_num;
        }

        public int getImageResource() { return imageResource; }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone_num(String _phone_num) {
            phone_num = _phone_num;
        }

    }

    //json -> str 변환용
    private String getJsonString() {
        String json = "";

        try {
            InputStream is = getContext().getAssets().open("phone_number.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }

    // json 실제 파싱
    private void jsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray phoneArray = jsonObject.getJSONArray("phone_number");

            for (int i = 0; i < phoneArray.length(); i++) {
                JSONObject movieObject = phoneArray.getJSONObject(i);

                phone phone_arr = new phone();

                phone_arr.setName(movieObject.getString("name"));
                phone_arr.setPhone_num(movieObject.getString("phone_num"));
                //phone_arr.setAge(movieObject.getString("age"));

                phoneList.add(phone_arr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // tab1 - recyclerview 어답터
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<phone> myDataList = null;

        MyAdapter(ArrayList<phone> dataList) {
            myDataList = dataList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.cardview, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
            viewHolder.name.setText(myDataList.get(position).getName());
            viewHolder.phone_num.setText(myDataList.get(position).getPhone_num());
            viewHolder.image.setImageResource(R.drawable.character2);
            //viewHolder.age.setText(myDataList.get(position).getAge());
        }

        @Override
        public int getItemCount() {
            //Adapter가 관리하는 전체 데이터 개수 반환
            return myDataList.size();
        }

        // tab1 - recyclerview : view holder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name;
            TextView phone_num;
            ImageView image;
            ImageView deleteImageIcon;
            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                phone_num = itemView.findViewById(R.id.phone_num);
                image = itemView.findViewById(R.id.imageView5);

                deleteImageIcon = itemView.findViewById(R.id.image_delete);
                deleteImageIcon.setOnClickListener(this);

            }
            @Override
            public void onClick(View view) {
                if (view.equals(deleteImageIcon)) {
                    removeAt(getAdapterPosition());
                }
            }
        }

        public void removeAt(int position) {
            myDataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, myDataList.size());
        }
    }

    // 주소록 adapter
    public class MyAdapter2 extends RecyclerView.Adapter<Telephone.MyAdapter2.ViewHolder> {

        private ArrayList<Telephone.phone> myDataList = null;

        MyAdapter2(ArrayList<Telephone.phone> dataList)
        {
            myDataList = dataList;
        }

        @Override
        public Telephone.MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.cardview, parent, false);
            Telephone.MyAdapter2.ViewHolder viewHolder = new Telephone.MyAdapter2.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Telephone.MyAdapter2.ViewHolder holder, int position) {
            holder.name.setText( myDataList.get(position).getName());
            holder.phone_num.setText(myDataList.get(position).getPhone_num());
            holder.image.setImageResource(R.drawable.character);
        }

        @Override
        public int getItemCount()
        {
            //Adapter가 관리하는 전체 데이터 개수 반환
            return myDataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView name;
            TextView phone_num;
            ImageView image;
            ImageView deleteImageIcon;

            //TextView age;

            ViewHolder(View itemView)
            {
                super(itemView);

                name = itemView.findViewById(R.id.name);
                phone_num = itemView.findViewById(R.id.phone_num);
                image = itemView.findViewById(R.id.imageView5);
                deleteImageIcon = itemView.findViewById(R.id.image_delete);
                deleteImageIcon.setOnClickListener(this);
                //age = itemView.findViewById(R.id.age);
            }

            @Override
            public void onClick(View view) {
                removeAt(getAdapterPosition());
            }
        }

        public void removeAt(int position) {
            myDataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, myDataList.size());
        }
    }

}