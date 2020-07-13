package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// intent를 이용해서 사진을 찍고, 해당 사진을 external primal directory에 저장한다
// 해당 사진들을 grid imgage를 이용해서 여러 장 표현한다
public class GridImage extends Fragment {
    public GridImage() {
        // Required empty public constructor
    }

    String currentPhotoPath;  // 외부 파일 디렉토리
    static final int REQUEST_TAKE_PHOTO = 1;    // 사진 찍었는지 파악 용도
    LinearLayout row_layout;    // 사진을 나열하기 위한 레이아웃

    File[] files;    // 사진 보여주기 위해 리스트로 만들었음
    ArrayList<Bitmap> bit_files = new ArrayList<>(); // files들을 bitmap 형식으로 바꿔준다

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 현재 view(grid view)를 inflate 해서 main view로 올려준다
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        // 외부 디렉토리에서 파일을 가져와서, 파일 리스트를 만들어준다 (bit_files)
        total_file_bit();

        // 사진을 표현하기 위한 전체적인 layout
        row_layout = (LinearLayout) view.findViewById(R.id.row_layout);

        // 사진을 photo_layout에 담는 과정
        photo_to_linLayout();

        // 버튼 클릭을 통해 카메라를 호출하려 함
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.fab_camera);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사진 찍는 intent 수행 후, 외부 디렉토리로 파일을 저장한다
                dispatchTakePictureIntent();

                // 새로 사진을 찍었으므로, 디렉토리를 refresh 해줘야 한다
                total_file_bit();

                // refresh된 디렉토리에서, 다시 사진들을 가져온다
                row_layout.removeAllViewsInLayout();
                photo_to_linLayout();
            }
        });
        return view;
    }

    // 다른 activity에 요청한 것에 대한 답변을 받는다
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {

        }
    }

    /* 사진을 layout에 담아주는 함수 */
    public void photo_to_linLayout() {
        // 매 row마다 사진을 담아주기 위해 임시 layout을 만든다
        LinearLayout tmp_layout = new LinearLayout(getActivity());

        // 사진들을 layout에 담는 과정
        for (int i = 0; i < bit_files.size(); i++) {
            ImageView iv = new ImageView(getActivity());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(10, 10, 10, 10);
            iv.setLayoutParams(param);

            iv.setImageBitmap(bit_files.get(i));
            iv.setAdjustViewBounds(true);

            if ((i % 3 == 0) || (i == bit_files.size() - 1)) {
                row_layout.addView(tmp_layout);

                tmp_layout = new LinearLayout(getActivity());
                tmp_layout.addView(iv);
            } else {
                tmp_layout.addView(iv);
            }
            /*
            if(i < 3){
                row_layout.addView(tmp_layout);
            }*/
        }
    }

    /* bitmap 사진 변형을 위한 함수들 */
    // bitmap 사이즈 조절하기
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /* 여러개의 사진들을 한번에 표현하기 위한 함수들*/
    // external directory에 있는 파일들로 리스트 만들어오기
    public void make_file_list() {
        // external directory 를 string으로 받아온다
        String path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "";

        // 해당 디렉토리에 있는 모든 파일들을 files에 저장해준다
        File directory = new File(path);
        files = directory.listFiles();
        Log.d("길이는??", files.length + "");
    }

    // File 자료형을 bitmap 자료형으로 변환해준다
    public void file_to_bit() {
        int limit_length;
        if (files.length > 18)
            limit_length = 18;
        else
            limit_length = files.length;

        for (int i = 0; i < limit_length; i++) {
            Log.d("여긴 될까", files[i].getAbsolutePath() + "넘버" + i);
            Bitmap tmp_bitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());

            if (tmp_bitmap == null)
                continue;

            tmp_bitmap = getResizedBitmap(tmp_bitmap, 200, 200);

            bit_files.add(tmp_bitmap);
        }
    }

    // 위의 두 함수를 모두 수행해준다
    public void total_file_bit() {
        // external storage로부터 파일 리스트를 만들어온다
        make_file_list();
        Log.d("여긴 되냐? 1", "제발 ㅠㅠ");
        // File 자료형을 Bitmap 자료형으로 바꿔준다
        file_to_bit();
        Log.d("여긴 되냐? 22", "제발 ㅠㅠ");
    }

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


