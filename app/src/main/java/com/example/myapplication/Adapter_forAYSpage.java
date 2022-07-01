package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_forAYSpage extends RecyclerView.Adapter<ViewHolder_forAYSpage> {

    private final List<JobItem_foratyourservice> people;
    private final Context context;

    /**
     * Creates a WebsiteAdapter with the provided arraylist of Website objects.
     *
     * @param people    arraylist of Website object.
     * @param context   context of the activity used for inflating layout of the viewholder.
     */
    public Adapter_forAYSpage(List<JobItem_foratyourservice> people, Context context) {
        this.people = people;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder_forAYSpage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create an instance of the viewholder by passing it the layout inflated as view and no root.
        return new ViewHolder_forAYSpage(LayoutInflater.from(context).inflate(R.layout.activity_item_job_for_main, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_forAYSpage holder, int position) {
        holder.bindThisData(people.get(position));

    }

    @Override
    public int getItemCount() {
        // Returns the size of the recyclerview that is the list of the arraylist.
        return people.size();
    }






}
