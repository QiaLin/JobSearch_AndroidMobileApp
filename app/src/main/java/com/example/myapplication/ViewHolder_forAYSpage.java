package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_forAYSpage extends RecyclerView.ViewHolder {

    public TextView nameTV;
    public TextView ageTV;

    public ViewHolder_forAYSpage(@NonNull View itemView) {
        super(itemView);
        this.nameTV = itemView.findViewById(R.id.name);
        this.ageTV = itemView.findViewById(R.id.age);
    }


    public void bindThisData(JobItem_foratyourservice thePersonToBind) {
        // sets the name of the person to the name textview of the viewholder.
        nameTV.setText(thePersonToBind.getJobtitle());
        // sets the age of the person to the websiteItem textview of the viewholder.
        ageTV.setText(thePersonToBind.getCompany()+"\n"+thePersonToBind.getLocation());
    }
}