package com.automat.manager.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.BaseAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.automat.manager.R;
import com.automat.manager.responses.GetCreateOrdersResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements Filterable {
    public static int rowIndex = -1;
    private GetCreateOrdersResponse getCreateOrdersResponse;
    List<GetCreateOrdersResponse> moviesList;
    List<GetCreateOrdersResponse> moviesListAll;

    public interface OnOrderClickListener{
        void onOrderClick(GetCreateOrdersResponse order, int position, ViewHolder holder);
    }

    private final OnOrderClickListener onClickListener;
    private final LayoutInflater inflater;
    private List<GetCreateOrdersResponse> orders;

    public OrderAdapter(Context context, List<GetCreateOrdersResponse> orders,  OnOrderClickListener onClickListener) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        this.moviesList = orders;
        moviesListAll = new ArrayList<>();
        moviesListAll.addAll(moviesList);
        getCreateOrdersResponse = new GetCreateOrdersResponse();
    }

    public void setOrders(List<GetCreateOrdersResponse> orders) {
        this.orders = orders;
    }

    public void setGetCreateOrdersResponse(GetCreateOrdersResponse getCreateOrdersResponse) {
        this.getCreateOrdersResponse = getCreateOrdersResponse;
    }

    public GetCreateOrdersResponse getGetCreateOrdersResponse() {
        return getCreateOrdersResponse;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_logistician_order_item, parent, false);
        //view.findViewById(R.id.)
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {

        GetCreateOrdersResponse order = orders.get(position);
        setGetCreateOrdersResponse(order);

        holder.tvDateOfOrder.setText("Дата: " + order.getCreationDate());
        holder.tvCounterpartyName.setText("Наименование компании: " + order.getCounterpartyName());
        holder.tvGasTypeName.setText("Тип газа: " + order.getGasTypeName());
        holder.tvBalloonCount.setText("Количество баллонов: " + order.getBalloonCount());
        holder.tvDriveName.setText("Имя водителя: " + order.getDriverName());
        holder.tvCarName.setText("Машина: " + order.getCarName());
//        if (holder.tvDriveName.getText().toString().equals("Имя водителя: ")) holder.tvDriveName.setTextColor(Color.RED);
//        if (holder.tvCarName.getText().toString().equals("Машина: ")) holder.tvCarName.setTextColor(Color.RED);
        holder.itemView.setOnClickListener(view -> {

            notifyDataSetChanged();
            onClickListener.onOrderClick(order, position, holder);
        });
        holder.itemView.setBackgroundColor(Color.parseColor(rowIndex != position ? "#EDEDED":"#C0D6E8"));

    }



    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Filter getFilter() {

        return myFilter;
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<GetCreateOrdersResponse> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(moviesListAll);
            } else {
                for (GetCreateOrdersResponse movie: moviesListAll) {
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
            moviesList.addAll((Collection<? extends GetCreateOrdersResponse>) filterResults.values);
            notifyDataSetChanged();
        }
    };
    public static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDateOfOrder, tvCounterpartyName, tvGasTypeName, tvBalloonCount, tvDriveName, tvCarName;
        public ViewHolder(View view){
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
