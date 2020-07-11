package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import java.io.InputStream;

public class My_GridImage extends Fragment {
    private static final int REQUEST_CODE = 0;
    private ImageView imageView;

    public My_GridImage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_gridimage, container, false);

        // 다섯번째 tab
        imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    InputStream in = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);


                } catch (Exception e) {

                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}


