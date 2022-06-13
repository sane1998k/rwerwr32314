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
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetReturnOrdersResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NewOrderAdapter2 extends RecyclerView.Adapter<NewOrderAdapter2.ViewHolder> implements Filterable {
    public static int rowIndex = -1;
    private GetReturnOrdersResponse getCreateOrdersResponse;
    List<GetReturnOrdersResponse> moviesList;
    List<GetReturnOrdersResponse> moviesListAll;

    public interface OnNewOrderClickListener{
        void onOrderClick(GetReturnOrdersResponse order, int position, NewOrderAdapter2.ViewHolder holder);
    }

    private final NewOrderAdapter2.OnNewOrderClickListener onClickListener;
    private final LayoutInflater inflater;
    private List<GetReturnOrdersResponse> orders;

    public NewOrderAdapter2(Context context, List<GetReturnOrdersResponse> orders, NewOrderAdapter2.OnNewOrderClickListener onClickListener) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        this.moviesList = orders;
        moviesListAll = new ArrayList<>();
        moviesListAll.addAll(moviesList);
        getCreateOrdersResponse = new GetReturnOrdersResponse();
    }

    @NonNull
    @Override
    public NewOrderAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_logistician_order_item, parent, false);
        //view.findViewById(R.id.)
        return new NewOrderAdapter2.ViewHolder(view);
    }

    public void setOrders(List<GetReturnOrdersResponse> orders) {
        this.orders = orders;
    }

    public void setGetCreateOrdersResponse(GetReturnOrdersResponse getCreateOrdersResponse) {
        this.getCreateOrdersResponse = getCreateOrdersResponse;
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderAdapter2.ViewHolder holder, int position) {

        GetReturnOrdersResponse order = orders.get(position);
        setGetCreateOrdersResponse(order);

        holder.tvDateOfOrder.setText("Дата: " + order.getCreationDate());
        holder.tvCounterpartyName.setText("Наименование компании: " + order.getCounterpartyName());
        holder.tvGasTypeName.setText("Тип газа: ");
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
    public Filter getFilter() {

        return myFilter;
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<GetReturnOrdersResponse> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(moviesListAll);
            } else {
                for (GetReturnOrdersResponse movie: moviesListAll) {
                    //int count = isParsable(charSequence.toString()) ? Integer.parseInt(charSequence.toString()) : 0;
                    if (movie.getCarName().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            movie.getDriverName().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            movie.getCounterpartyName().toLowerCase().contains(charSequence.toString().toLowerCase())
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
            moviesList.addAll((Collection<? extends GetReturnOrdersResponse>) filterResults.values);
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
