package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GridImage extends Fragment {
    public GridImage() {
        // Required empty public constructor
    }

    String currentPhotoPath;  // 외부 파일 디렉토리
    static final int REQUEST_TAKE_PHOTO = 1;    // 사진 찍었는지 파악 용도
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 현재 view(grid view)를 inflate 해서 main view로 올려준다
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        // adapter를 설정해준다
        gridView = (GridView) view.findViewById(R.id.grid_picture);
        gridView.setAdapter(new ImageAdapter(getActivity()));

        // 버튼 클릭을 통해 카메라를 호출하려 함
        Button button = (Button)view.findViewById(R.id.camera_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        return view;
    }

    // 사진 찍어오기 intent 함수
    private void dispatchTakePictureIntent() {
        //사진 찍기용 intent - 내가 실행시키고싶은 액션 뷰를 인자로 받아들인다
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 내가 실행한 intent가 실제로 존재하긴 하는가?
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // 사진찍은 내용을 담기 위함
            File photoFile = null;

            try {
                // 사진 찍은 내용을 담아준다
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            Log.d("여기까진 됨", photoFile + "");

            // 제대로 사진이 찍혔을 경우에만 진행
            if (photoFile != null) {
                // 사진의 uri 값을 받아온다
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.myapplication.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // 요청했던 activity로부터 응답 받기 위한 함수
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                galleryAddPic();
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

    // 갤러리에 사진 저장하기
    private void galleryAddPic() {
        File f = new File(currentPhotoPath);    // 방금 찍은 사진의 주소를 받아온다
        Uri contentUri = Uri.fromFile(f);   // 사진을 uri 형식으로 변환한다
        Log.d("여기까진 됨2", contentUri + "");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); // 갤러리 intent 를 불러온다
        mediaScanIntent.setData(contentUri);    // 갤러리 intent에게 현재 사진을 준다
        Log.d("여기까진 됨3", mediaScanIntent + "");
        getActivity().sendBroadcast(mediaScanIntent);   // 내가 설정한 intent를 전송한다??
    }

    // image 전시용
    public class ImageAdapter extends BaseAdapter{
        private Context context;

        private Integer[] images = {R.drawable.pic_001,R.drawable.pic_002,R.drawable.pic_003,
                R.drawable.pic_004};

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


