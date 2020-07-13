package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Vector;

public class YouTube extends Fragment {
    public YouTube() {

    }

    RecyclerView recyclerView;
    Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);

        // 리사이클러 뷰 선언
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // 레이아웃 매니저 설정
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));

        // 보고싶은 유튜브 목록을 저장함
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/oKUEbsJDvuo\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/jPFrHYXYxak\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Is7glC9Jp7Q\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ioNng23DkIM\" frameborder=\"0\" allowfullscreen></iframe>") );

        // Adapter를 정의함 + 리사이클러뷰에 적용
        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);
        recyclerView.setAdapter(videoAdapter);

        return view;
    }

    // 아이템 데이터 클래스 정의
    public class YouTubeVideos {
        String videoUrl;

        public YouTubeVideos() {
        }

        public YouTubeVideos(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }

    // 어댑터 상속 및 구현 - 아이템 뷰를 생성해준다
    // 그러기 위해선 뷰홀더, 뷰, 아이템 갯수에 대한 체크가 필요하다
    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
        // 아이템 데이터들을 임시로 담을 리스트
        List<YouTubeVideos> youtubeVideoList;

        public VideoAdapter() {
        }

        // 메인 함수로부터, 아이템 데이터들을 받아준다
        public VideoAdapter(List<YouTubeVideos> youtubeVideoList) {
            this.youtubeVideoList = youtubeVideoList;
        }

        // 뷰홀더 객체 생성한다
        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.fragment_youtube_sub, parent, false);

            return new VideoViewHolder(view);
        }

        // 데이터를 뷰 홀더에 바인딩한다
        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {

            holder.videoWeb.loadData( youtubeVideoList.get(position).getVideoUrl(), "text/html" , "utf-8" );

        }

        // 전체 아이템 갯수를 리턴한다
        @Override
        public int getItemCount() {
            return youtubeVideoList.size();
        }

        public class VideoViewHolder extends RecyclerView.ViewHolder{

            WebView videoWeb;

            public VideoViewHolder(View itemView) {
                super(itemView);

                videoWeb = (WebView) itemView.findViewById(R.id.videoWebView);

                videoWeb.getSettings().setJavaScriptEnabled(true);
                videoWeb.setWebChromeClient(new WebChromeClient() {

                } );
            }
        }
    }
}

