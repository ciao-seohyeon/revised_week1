package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Gallery_Adapter extends RecyclerView.Adapter<Gallery_Adapter.ViewHolder> {
    ArrayList<galley_item> item_list = null;

    // 텔레비전같은 것이라고 이해하자
    public class ViewHolder extends RecyclerView.ViewHolder{
        // 일종의 채널. 어떤 신호가 오느냐에따라 다른 채널을 틀 수 있다.
        ImageView icon;

        // 그렇다면, 어떻게 신호를 채널과 연결할 수 있는가? 이걸 바로 이 생성자를 통해서 한다.
        ViewHolder(View gallery_item){  // gallery_item 이라는 신호를 받으면
            super(gallery_item);        // 상위 뷰홀더에 일단 신호를 주고

            icon = gallery_item.findViewById(R.id.item_gallery);    // 이 신호를 텔레비전의 채널에 연결시켜준다
        }
    }

    // 텔레비전을 어떤 벽에 달아서 통째로 준다고 생각하자. 즉, 뷰홀더를 어떤 뷰 그룹에 달아서 반환할 것인가?
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 그러기 위해선, 가장 먼저 벽을 설정하자 - 뷰 그룹의 context를 전달하자
        Context context = parent.getContext();

        // 벽에 달아주는 설치 아저씨가 필요하다. 해당 벽에 대응되는 inflater를 호출하자
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 텔레비전을 벽에 달아주자 - 이대로 바로 줄 순 없다. 어느정도 포장은 해줘야 한다
        View view = inflater.inflate(R.layout.fragment_gallery_item, parent, false);

        // (텔레비전 + 벽) 을 포장해주자 - 이 역할은 아답터가 해준다.
        Gallery_Adapter.ViewHolder vh = new Gallery_Adapter.ViewHolder(view);

        // 포장까지 마쳤으면, 이제 반환해주자
        return vh;
    }

    // 일종의 리모콘이다. position이라는 신호 번호가 왔으면, 이에 대응되는 텔레비전의 채널을 틀어준다
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 몇 번쨰 신호인지 먼저 받자
        galley_item item = item_list.get(position);

        // 해당 신호에 있는 내용들을 각각의 채널들에게 전달해주자
        // 실제 신호와 연결지어주기 위해, 상속한 아답터의 기능 중 하나인 setImageDrawable을 사용해줘야 한다
        holder.icon.setImageResource(item.getIcon());
    }

    // 총 몇 개의 신호가 들어왔는지 반환해준다
    @Override
    public int getItemCount() {
        return item_list.size();
    }

    // 위의 모든 과정들은, 이 어답터 객체가 생성되는 순간 자연스럽게 모두 실행된다.
    // 따라서, 이제 나는 아이템 데이터만 받게 되면 여러개의 채널들이 한꺼번에 떠오른 + 벽에 달린 + 텔레비전 을 갖게 된다
    // 아이템 데이터는 반드시 받아야 하는 것이기 떄문에, 생성자 형식으로 데이터를 바로 받아준다.
    Gallery_Adapter(ArrayList<galley_item> list){
        item_list = list;
    }
}
