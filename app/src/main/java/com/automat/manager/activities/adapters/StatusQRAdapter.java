package com.automat.manager.activities.adapters;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.automat.manager.R;

import java.util.List;

public class StatusQRAdapter extends RecyclerView.Adapter<StatusQRAdapter.MyView> {

    private final IStatusQRAdapter iStatusQRAdapter;

    public static int rowIndex;
    private final List<String> list;

    public static class MyView extends RecyclerView.ViewHolder {

        public final TextView tvStatusQR;
        CardView cvBackgroundQR;
        RecyclerView rvStatusQR;

        public MyView(View view)
        {
            super(view);

            tvStatusQR = itemView.findViewById(R.id.tv_status_qr_adapter_item);
            cvBackgroundQR = itemView.findViewById(R.id.cv_status_qr_adapter);
            rvStatusQR = itemView.findViewById(R.id.rv_status_qr_items);

            tvStatusQR.setGravity(Gravity.CENTER);
        }
    }

    public StatusQRAdapter(List<String> horizontalList, IStatusQRAdapter iStatusQRAdapter)
    {
        this.list = horizontalList;
        this.iStatusQRAdapter = iStatusQRAdapter;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_qr_status_item, parent, false);

        return new MyView(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull final MyView holder,
                                 @SuppressLint("RecyclerView") int position) {


        holder.cvBackgroundQR.setBackgroundResource(R.color.gray);
        holder.tvStatusQR.setText(list.get(position));
        holder.itemView.setOnClickListener(view -> {
            rowIndex = position;
            notifyDataSetChanged();
            iStatusQRAdapter.getData(holder.tvStatusQR.getText().toString());
        });
        holder.cvBackgroundQR.setBackgroundResource(rowIndex != position ? R.color.gray : R.color.green);
        holder.tvStatusQR.setTextColor(rowIndex != position ? ContextCompat.getColor(holder.itemView.getContext(), R.color.black) :
                ContextCompat.getColor(holder.itemView.getContext(), R.color.white));


    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
    public interface IStatusQRAdapter{
        void getData(String name);
    }
}