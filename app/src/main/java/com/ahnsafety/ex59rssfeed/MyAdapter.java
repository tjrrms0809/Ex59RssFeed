package com.ahnsafety.ex59rssfeed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter {

    ArrayList<Item> items;
    Context context;

    public MyAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);

        VH vh= new VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH)holder;

        //현재번째(position) 아이템 얻어오기
        Item item= items.get(position);

        vh.tvTitle.setText(item.getTitle());
        vh.tvDesc.setText(item.getDesc());
        vh.tvDate.setText(item.getDate());

        //이미지는 없을 수도 있음.
        if( item.getImgUrl()==null){
            vh.iv.setVisibility(View.GONE);
        }else{
            vh.iv.setVisibility(View.VISIBLE);
            //네트워크에 있는 이미지를 보여주려면
            //별도의 Thread가 필요한데 이를 편하게
            //해주는 Library사용( Glide library )
            Glide.with(context).load(item.getImgUrl()).into(vh.iv);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //이너클래스
    class VH extends RecyclerView.ViewHolder{

        TextView tvTitle;
        ImageView iv;
        TextView tvDesc;
        TextView tvDate;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvTitle= itemView.findViewById(R.id.tv_title);
            iv= itemView.findViewById(R.id.iv);
            tvDesc= itemView.findViewById(R.id.tv_desc);
            tvDate= itemView.findViewById(R.id.tv_date);

            //리사클러뷰의 아이템뷰를 클릭했을 때
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link= items.get(getLayoutPosition()).getLink();

                    //웹뷰를 가진 새로운 액티비티 실행
                    Intent intent= new Intent(context,ItemActivity.class);
                    intent.putExtra("Link",link);
                    context.startActivity(intent);
                }
            });
        }
    }
}
