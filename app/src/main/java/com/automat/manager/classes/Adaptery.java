package com.automat.manager.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automat.manager.R;
import com.automat.manager.responses.GetCounterpartyResponse;

import java.util.ArrayList;
import java.util.List;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> implements Filterable {
    private Context context;
    private List<GetCounterpartyResponse> getCounterpartyResponseList;
    private List<GetCounterpartyResponse> getCounterpartyResponseListCopy;
    private List<GetCounterpartyResponse> contactList;
    private List<GetCounterpartyResponse> contactListFiltered;
    private ContactsAdapterListener listener;

    public Adaptery(Context context, List<GetCounterpartyResponse> getCounterpartyResponseList) {
        this.context = context;
        this.getCounterpartyResponseList = getCounterpartyResponseList;
        this.getCounterpartyResponseListCopy = getCounterpartyResponseList;
    }

    @NonNull
    @Override
    public Adaptery.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        v = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptery.MyViewHolder holder, int position) {
        holder.tvId.setText(getCounterpartyResponseList.get(position).getId());
        holder.tvName.setText(getCounterpartyResponseList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return getCounterpartyResponseList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filter(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
  //      getCounterpartyResponseList.clear();
        if(text.isEmpty()){
            getCounterpartyResponseList.addAll(getCounterpartyResponseListCopy);
        } else{
            text = text.toLowerCase();
            for(GetCounterpartyResponse item: getCounterpartyResponseListCopy){
                if(item.getName().toLowerCase().contains(text) || item.getId().toLowerCase().contains(text)){
                    getCounterpartyResponseList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<GetCounterpartyResponse> filteredList = new ArrayList<>();
                    for (GetCounterpartyResponse row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    notifyDataSetChanged();
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<GetCounterpartyResponse>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
    public interface ContactsAdapterListener {
        void onContactSelected(GetCounterpartyResponse contact);
    }
}
