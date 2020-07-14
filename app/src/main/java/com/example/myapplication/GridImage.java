package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


// intent를 이용해서 사진을 찍고, 해당 사진을 external primal directory에 저장한다
// 해당 사진들을 grid imgage를 이용해서 여러 장 표현한다
public class GridImage extends Fragment {
    private static final String TAG = "GridImage";

    public GridImage() {
        // Required empty public constructor
    }

    String currentPhotoPath;  // 외부 파일 디렉토리
    static final int REQUEST_TAKE_PHOTO = 1;    // 사진 찍었는지 파악 용도

    RecyclerView _recycle_image;
    Gallery_Adapter gallery_adapter = null;
    ArrayList<galley_item> imageViews = new ArrayList<galley_item>();

    ArrayList<String> files;    // 사진 보여주기 위해 리스트로 만들었음

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 현재 view(grid view)를 inflate 해서 main view로 올려준다
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        files = new ArrayList<>();
        // 사진을 photo_layout에 담는 과정
        photo_to_linLayout();

        // 사진을 표현하기 위한 전체적인 layout
        _recycle_image = (RecyclerView) view.findViewById(R.id.recycle_image);
        gallery_adapter = new Gallery_Adapter(files, getContext());
        _recycle_image.setAdapter(gallery_adapter);

        _recycle_image.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        // 버튼 클릭을 통해 카메라를 호출하려 함
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.recycle_camera);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사진 찍는 intent 수행 후, 외부 디렉토리로 파일을 저장한다
                dispatchTakePictureIntent();

                // refresh된 디렉토리에서, 다시 사진들을 가져온다
                _recycle_image.removeAllViewsInLayout();
                photo_to_linLayout();
            }
        });
        return view;
    }

    // 다른 activity에 요청한 것에 대한 답변을 받는다
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            Toast.makeText(getContext(), "사진을 가져왔어요.", Toast.LENGTH_SHORT).show();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    /* 사진을 layout에 담아주는 함수 */
    public void photo_to_linLayout() {
        // external directory 를 string으로 받아온다
        String path = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        Log.d(TAG, "photo_to_linLayout: 여기가 어디야?:"+path);
        // 해당 디렉토리에 있는 모든 파일들을 files에 저장해준다
        File directory = new File(path);
        File[] arr = directory.listFiles();
        Log.d(TAG, "photo_to_linLayout: 왜안돼: " + directory.list());
        for (File s : arr) {
            Log.d(TAG, "photo_to_linLayout: IEIWHOTRIWHEFKJLH>>>>>>>" + s.getPath());
            files.add(s.getPath());
        }

//        // files 내용물을 이용해서, 각각의 imageView를 만들어낸다
//        for(int i=0;i<files.length;i++){
//            ImageView iv = new ImageView(getActivity());
//            Glide.with(getActivity()).load(files[i].getAbsolutePath()).error(R.drawable.memo).into(iv);
//            Log.d("나오나? ", files[i].getAbsolutePath() + "");
//
//            addItem(iv);
//        }
    }
//
//    public void addItem(ImageView icon){
//        galley_item item = new galley_item();
//
//        item.setIcon(icon);
//        imageViews.add(item);
//    }

    /* 사진 찍고 외부 경로에 저장하기 위한 함수들 */
    // 사진 찍어오기 intent 함수
    private void dispatchTakePictureIntent() {
        //사진 찍기용 intent - 내가 실행시키고싶은 액션 뷰를 인자로 받아들인다
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 내가 실행한 intent가 내부 intent로 존재하긴 하는가? (=실제 사진이 찍히긴 했는가?)
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // 사진 찍을 경로
            File photoFile = null;

            try {
                // 사진 찍을 경로를 받는다
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            // 경로를 제대로 받았을 경우만 진행
            if (photoFile != null) {
                // 사진 intent에 전달하기 위한 uri를 받는다
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.myapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // 요청했던 activity로부터 응답 받기 위한 함수
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // 파일 디렉토리 만들기 + 이미지/파일 이름 지정하기
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}