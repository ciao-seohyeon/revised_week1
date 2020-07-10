package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class GridImage extends Fragment {
    public GridImage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_picture);
        gridView.setAdapter(new ImageAdapter(getActivity()));
        return view;
        //return null;
    }

    // image 전시용
    public class ImageAdapter extends BaseAdapter{
        private Context context;

        private Integer[] images = {R.drawable.pic_001,R.drawable.pic_002,R.drawable.pic_003,
                R.drawable.pic_004,R.drawable.pic_005,R.drawable.pic_006,R.drawable.pic_007,
                R.drawable.pic_008,R.drawable.pic_009,R.drawable.pic_010,R.drawable.pic_011,
                R.drawable.pic_012,R.drawable.pic_013,R.drawable.pic_014,R.drawable.pic_015,
                R.drawable.pic_016,R.drawable.pic_017,R.drawable.pic_018,R.drawable.pic_019,
                R.drawable.pic_020,};

        public ImageAdapter(Context con){
            this.context = con;
        }

        public int getCount(){
            return images.length;
        }

        public Object getItem(int pos){
            return null;
        }

        public long getItemId(int pos){
            return 0;
        }

        public View getView(int pos, View convertView, ViewGroup parent){
            ImageView imageView;

            if(convertView==null){
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(5, 5, 5, 5);
            }else{
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(images[pos]);

            return imageView;
        }
    }

}


