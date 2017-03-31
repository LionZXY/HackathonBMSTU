package ru.skafcats.hackathon2017.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.skafcats.hackathon2017.R;
import ru.skafcats.hackathon2017.models.ItemModel;

/**
 * Created by vasidmi on 31/03/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {


    private ArrayList<ItemModel> itemsData;
    private Context mContext;


    public ItemsAdapter(Context context, ArrayList<ItemModel> data) {
        this.mContext = context;
        this.itemsData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        ViewHolder mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemModel itemModel = itemsData.get(position);
        holder.mItemIcon.setText(itemModel.getIcon());
        holder.mItemTitle.setText(itemModel.getTitle());
        holder.mItemDescription.setText(itemModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView mItemIcon, mItemTitle, mItemDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemIcon = (AppCompatTextView) itemView.findViewById(R.id.ItemIcon);
            mItemTitle = (AppCompatTextView) itemView.findViewById(R.id.ItemTitle);
            mItemDescription = (AppCompatTextView) itemView.findViewById(R.id.ItemDescription);
        }
    }
}
