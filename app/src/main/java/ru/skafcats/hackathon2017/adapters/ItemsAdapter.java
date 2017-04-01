package ru.skafcats.hackathon2017.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import ru.skafcats.crypto.models.InfoAboutSecureInfo;
import ru.skafcats.hackathon2017.R;

/**
 * Created by vasidmi on 31/03/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> implements RealmChangeListener<InfoAboutSecureInfo> {


    private RealmResults<InfoAboutSecureInfo> itemsData;
    private Context mContext;


    public ItemsAdapter(Context context, RealmResults<InfoAboutSecureInfo> data) {
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
        InfoAboutSecureInfo itemModel = itemsData.get(position);
        holder.mItemIcon.setText(itemModel.getName().charAt(0));
        holder.mItemTitle.setText(itemModel.getName());
        holder.mItemDescription.setText(itemModel.getLogin());
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    @Override
    public void onChange(InfoAboutSecureInfo element) {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView mItemTitle, mItemDescription, mItemIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemIcon = (AppCompatTextView) itemView.findViewById(R.id.ItemIcon);
            mItemTitle = (AppCompatTextView) itemView.findViewById(R.id.ItemTitle);
            mItemDescription = (AppCompatTextView) itemView.findViewById(R.id.ItemDescription);
        }
    }
}
