package com.example.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

        return view;
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
            //TextView age;

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
}