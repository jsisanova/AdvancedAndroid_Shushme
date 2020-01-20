package com.example.android.shushme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private Context mContext;
    private ArrayList<MyPlace> mData;

    /**
     * Constructor using the context and the db cursor
     *
     * @param context the calling context/activity
     */
    public PlaceListAdapter(Context context) {
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item
     *
     * @param parent   The ViewGroup into which the new View will be added
     * @param viewType The view type of the new View
     * @return A new PlaceViewHolder that holds a View with the item_place_card layout
     */
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_place_card, parent, false);
        return new PlaceViewHolder(view);
    }

    /**
     * Binds the data from a particular position in the cursor to the corresponding view holder
     *
     * @param holder   The PlaceViewHolder instance corresponding to the required position
     * @param position The current position that needs to be loaded with data
     */
    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        // TODO (6) Implement onBindViewHolder to set the view holder's Name and Address text fields
        // from the MyPlace object at the specified position in mData
        MyPlace place = mData.get(position);
        holder.nameTextView.setText(place.getName());
        holder.addressTextView.setText(place.getAddress());
    }

    /**
     * Returns the number of items in the cursor
     *
     * @return Number of items in the cursor, or 0 if null
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmData(ArrayList<MyPlace> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    /**
     * PlaceViewHolder class for the recycler view item
     */
    class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView addressTextView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            addressTextView = (TextView) itemView.findViewById(R.id.address_text_view);
        }

    }
}