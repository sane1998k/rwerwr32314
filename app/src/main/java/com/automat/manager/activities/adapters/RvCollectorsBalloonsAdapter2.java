package com.automat.manager.activities.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automat.manager.R;
import com.automat.manager.classes.Balloon;

import java.util.List;

public class RvCollectorsBalloonsAdapter2 extends RecyclerView.Adapter<RvCollectorsBalloonsAdapter2.RvCollectorsBalloonsViewHolder>  {
    public interface IClickReturnBalloon {
        void onItemClick(int position);
    }

    IClickReturnBalloon iClickReturnBalloon;

    private List<Balloon> listItems;
    Context mContext;


    public RvCollectorsBalloonsAdapter2(Context context, List<Balloon> balloons) {
        this.listItems = balloons;
        this.mContext = context;
    }

    public void setIClickReturnBalloon(IClickReturnBalloon iClickReturnBalloon) {
        this.iClickReturnBalloon = iClickReturnBalloon;
    }

    @NonNull
    @Override
    public RvCollectorsBalloonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.return_item_balloons2, parent, false);
        //view.findViewById(R.id.)
        return new RvCollectorsBalloonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCollectorsBalloonsAdapter2.RvCollectorsBalloonsViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.tvBalloonId.setText("Баллон: " + listItems.get(position).getINameBalloon());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class RvCollectorsBalloonsViewHolder extends RecyclerView.ViewHolder {
        TextView tvBalloonId;
        public RvCollectorsBalloonsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBalloonId = itemView.findViewById(R.id.tv_return_balloon);
        }
    }
}
