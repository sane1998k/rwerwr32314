package com.automat.manager.activities.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automat.manager.R;
import com.automat.manager.responses.PidorasObj1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NewOrderAdapterPidoras2 extends RecyclerView.Adapter<NewOrderAdapterPidoras2.ViewHolder> implements Filterable {
    public static int rowIndex = -1;
    private PidorasObj1.OrderDetail getCreateOrdersResponse;
    List<PidorasObj1.OrderDetail> moviesList;
    List<PidorasObj1.OrderDetail> moviesListAll;

    public interface OnNewOrderClickListener{
        void onOrderClick(PidorasObj1.OrderDetail order, int position, NewOrderAdapterPidoras2.ViewHolder holder);
    }

    private final NewOrderAdapterPidoras2.OnNewOrderClickListener onClickListener;
    private final LayoutInflater inflater;
    private List<PidorasObj1.OrderDetail> orders;

    public NewOrderAdapterPidoras2(Context context, List<PidorasObj1.OrderDetail> orders, NewOrderAdapterPidoras2.OnNewOrderClickListener onClickListener) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        this.moviesList = orders;
        moviesListAll = new ArrayList<>();
        moviesListAll.addAll(moviesList);
        getCreateOrdersResponse = new PidorasObj1.OrderDetail();
    }

    @NonNull
    @Override
    public NewOrderAdapterPidoras2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_logistician_order_item, parent, false);
        //view.findViewById(R.id.)
        return new NewOrderAdapterPidoras2.ViewHolder(view);
    }

    public void setOrders(List<PidorasObj1.OrderDetail> orders) {
        this.orders = orders;
    }

    public void setGetCreateOrdersResponse(PidorasObj1.OrderDetail getCreateOrdersResponse) {
        this.getCreateOrdersResponse = getCreateOrdersResponse;
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderAdapterPidoras2.ViewHolder holder, int position) {

        PidorasObj1.OrderDetail order = orders.get(position);
        setGetCreateOrdersResponse(order);

        holder.tvDateOfOrder.setText("Дата: " + order.getCreationDate());
        //.tvCounterpartyName.setText("Наименование компании: " + order.getCounterpartyName());
        holder.tvCounterpartyName.setVisibility(View.GONE);
        holder.tvGasTypeName.setText(order.getGasTypeName() != null ? "Газ: " + order.getGasTypeName() : "");
        holder.tvBalloonCount.setText("Количество баллонов: " + order.getBalloonCount());
        holder.tvDriveName.setText("Объём: " + order.getVolumeName());
        //holder.tvDriveName.setVisibility(View.GONE);
        holder.tvCarName.setVisibility(View.GONE);
//        if (holder.tvDriveName.getText().toString().equals("Имя водителя: ")) holder.tvDriveName.setTextColor(Color.RED);
//        if (holder.tvCarName.getText().toString().equals("Машина: ")) holder.tvCarName.setTextColor(Color.RED);
        holder.itemView.setOnClickListener(view -> {

            notifyDataSetChanged();
            onClickListener.onOrderClick(order, position, holder);
        });
        holder.itemView.setBackgroundColor(Color.parseColor(rowIndex != position ? "#EDEDED":"#C0D6E8"));

    }


    @Override
    public Filter getFilter() {

        return myFilter;
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<PidorasObj1.OrderDetail> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(moviesListAll);
            } else {
                for (PidorasObj1.OrderDetail movie: moviesListAll) {
                    //int count = isParsable(charSequence.toString()) ? Integer.parseInt(charSequence.toString()) : 0;
                    if (movie.getCarName().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            movie.getDriverName().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            movie.getCounterpartyName().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            movie.getGasTypeName().toLowerCase().contains(charSequence.toString().toLowerCase())
                    ) {
                        filteredList.add(movie);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            moviesList.clear();
            moviesList.addAll((Collection<? extends PidorasObj1.OrderDetail>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDateOfOrder, tvCounterpartyName, tvGasTypeName, tvBalloonCount, tvDriveName, tvCarName;
        public ViewHolder(View view) {
            super(view);
            tvDateOfOrder = view.findViewById(R.id.tv_date_of_order);
            tvCounterpartyName = view.findViewById(R.id.tv_counterparty_name);
            tvGasTypeName = view.findViewById(R.id.tv_gas_type_name);
            tvBalloonCount = view.findViewById(R.id.tv_balloon_count);
            tvDriveName = view.findViewById(R.id.tv_drive_name);
            tvCarName = view.findViewById(R.id.tv_car_name);
        }
    }
}
