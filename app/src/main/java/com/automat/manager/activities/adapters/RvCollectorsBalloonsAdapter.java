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
import com.automat.manager.classes.OrderAdapter;
import com.automat.manager.responses.GetAllGottenBalloons;
import com.automat.manager.responses.GetCreateOrdersResponse;

import java.util.List;

public class RvCollectorsBalloonsAdapter extends RecyclerView.Adapter<RvCollectorsBalloonsAdapter.RvCollectorsBalloonsViewHolder>  {
    public interface IClickReturnBalloon {
        void onItemClick(int position);
    }

    IClickReturnBalloon iClickReturnBalloon;

    private List<Balloon> listItems;
    Context mContext;


    public RvCollectorsBalloonsAdapter(Context context, List<Balloon> balloons) {
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
        View view = inflater.inflate(R.layout.return_item_balloons, parent, false);
        //view.findViewById(R.id.)
        return new RvCollectorsBalloonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCollectorsBalloonsAdapter.RvCollectorsBalloonsViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.tvBalloonId.setText(listItems.get(position).getINameScan() + ": " + listItems.get(position).getINameBalloon());
            holder.itemView.findViewById(R.id.bt_delete_return_balloon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickReturnBalloon.onItemClick(position);
                }
            });
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
