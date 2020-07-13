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

// intent를 이용해서 activity 전환을 하는 탭. (ContentViewer 안씀!!)
public class My_GridImage extends Fragment {
    // next intent에게 전달할때 보내는 신호
    private static final int REQUEST_CODE = 0;

    // 선택된 사진을 보여주기 위한 레이아웃
    private ImageView imageView;

    public My_GridImage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 현재 xml을 대응되는 fragment에 맞도록 하기 위해, inflate 과정 필요
        View view = inflater.inflate(R.layout.fragment_my_gridimage, container, false);

        // 다섯번째 tab
        imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 내가 실행하려고 하는 또 다른 activity
                // 암시적 intent를 사용하려고 함
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                // 내가 보낸 신호에 대해, 답변을 받기 위함. 아래에 onActivityResult에서 신호를 받는다
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 내가 보낸 요청이 맞는지 판단한다
        if (requestCode == REQUEST_CODE) {
            // 요청에 대한 답이 있는지 확인한다
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    // 제대로 된 신호가 있었다면, resolver를 이용해서 데이터를 받아온다
                    InputStream in = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());

                    // 사진 데이터를 2진화 해주고, 스트림을 닫는다 (사진은 2진화 처리를 해줘야 한다)
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // 요청에 대한 답이 없었을 경우, 취소해준다.
                Toast.makeText(getActivity(), "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}


