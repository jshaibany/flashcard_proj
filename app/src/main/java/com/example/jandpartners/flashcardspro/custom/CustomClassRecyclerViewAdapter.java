package com.example.jandpartners.flashcardspro.custom;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.objects.ISwipeable;
import com.example.jandpartners.flashcardspro.objects.ent_Class;
import com.example.jandpartners.flashcardspro.objects.ent_FlashCard;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CustomClassRecyclerViewAdapter extends RecyclerView.Adapter<CustomClassRecyclerViewAdapter.HeaderViewHolder> implements ISwipeable {

    private Cursor dataSet;
    private View.OnClickListener onItemClickListener;
    private View.OnLongClickListener onLongClickListener;
    private View coordinator_Layout;
    private ent_Class ent_class;

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        TextView tv_date;


        public HeaderViewHolder(View cardView){
            super(cardView);
            cardView.setTag(this);
            cardView.setOnClickListener(onItemClickListener);
            cardView.setOnLongClickListener(onLongClickListener);

            this.tv_name=cardView.findViewById(R.id.tv_name);
            this.tv_date=cardView.findViewById(R.id.tv_date);

        }
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener){
        onLongClickListener = longClickListener;
    }

    public void setAdapterData(Cursor dataSet){

        this.dataSet = dataSet;
    }

    public void setCoordinator_Layout(View view){

        this.coordinator_Layout=view;
    }

    @NonNull
    @Override
    public CustomClassRecyclerViewAdapter.HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_class, parent, false);

        return new CustomClassRecyclerViewAdapter.HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomClassRecyclerViewAdapter.HeaderViewHolder holder, int position) {

        dataSet.moveToPosition(position);

        try{

            holder.tv_name.setText(dataSet.getString(dataSet.getColumnIndex(ent_Class.dataLayer.COLUMN_NAME)));
            holder.tv_date.setText(dataSet.getString(dataSet.getColumnIndex(ent_Class.dataLayer.COLUMN_CREATED_ON)));

        }
        catch (Exception e){


        }
    }

    public void RemoveItem(int position, Context context){

        Cursor data = dataSet;
        data.moveToPosition(position);
        String CID = data.getString(data.getColumnIndex(ent_Class.dataLayer.COLUMN_NAME));
        ent_class = new ent_Class(context);
        ent_class.setName(CID);

        callUndoSnackbar(context);
    }

    private void callUndoSnackbar(Context context){

        Snackbar snackbar = Snackbar.make(coordinator_Layout,context.getResources().getString(R.string.snack_cascade_delete),
                Snackbar.LENGTH_LONG);
        snackbar.setAction(context.getResources().getString(R.string.action_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ent_class.CascadeDelete();
                dataSet = ent_class.getAllClasses();
                notifyDataSetChanged();
            }
        });
        snackbar.show();

        dataSet = ent_class.getAllClasses();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.getCount();
    }
}
