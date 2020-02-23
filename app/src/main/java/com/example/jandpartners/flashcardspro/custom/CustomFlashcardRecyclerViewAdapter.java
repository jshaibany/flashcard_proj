package com.example.jandpartners.flashcardspro.custom;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.objects.ISwipeable;
import com.example.jandpartners.flashcardspro.objects.ent_FlashCard;
import com.example.jandpartners.flashcardspro.objects.ent_Section;
import com.google.android.material.snackbar.Snackbar;

public class CustomFlashcardRecyclerViewAdapter extends RecyclerView.Adapter<CustomFlashcardRecyclerViewAdapter.HeaderViewHolder> implements ISwipeable {

    private Cursor dataSet;
    private String CID;
    private String SID;
    private String DID;
    private ent_FlashCard recentDeletedFlashCard;
    private View.OnClickListener onItemClickListener;
    private View coordinator_Layout;

    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView tv_id;
        TextView tv_term;
        TextView tv_date;


        public HeaderViewHolder(View cardView){
            super(cardView);
            cardView.setTag(this);
            cardView.setOnClickListener(onItemClickListener);

            this.tv_id=cardView.findViewById(R.id.tv_id);
            this.tv_term=cardView.findViewById(R.id.tv_term);
            this.tv_date=cardView.findViewById(R.id.tv_date);

        }
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public void setAdapterData(Cursor dataSet){

        this.dataSet = dataSet;
    }

    public void setFlashCardKeys(String CID,String SID,String DID){

        this.CID=CID;
        this.SID=SID;
        this.DID=DID;
    }

    public void setCoordinator_Layout(View view){

        this.coordinator_Layout=view;
    }

    @NonNull
    @Override
    public CustomFlashcardRecyclerViewAdapter.HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_flashcard, parent, false);

        return new CustomFlashcardRecyclerViewAdapter.HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomFlashcardRecyclerViewAdapter.HeaderViewHolder holder, int position) {

        dataSet.moveToPosition(position);

        try{

            holder.tv_id.setText(dataSet.getString(dataSet.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_ID)));
            holder.tv_term.setText(dataSet.getString(dataSet.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_TERM)));
            holder.tv_date.setText(dataSet.getString(dataSet.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_CREATED_ON)));

        }
        catch (Exception e){


        }
    }

    public void RemoveItem(int position, Context context){

        Cursor data = dataSet;
        data.moveToPosition(position);
        Integer FID = data.getInt(data.getColumnIndex(ent_FlashCard.dataLayer.COLUMN_ID));
        ent_FlashCard flashCard = new ent_FlashCard(context);
        flashCard.setId(FID);
        flashCard.get();
        recentDeletedFlashCard = flashCard;
        flashCard.delete();

        dataSet = flashCard.getAllFlashCards(DID,SID,CID);
        this.notifyDataSetChanged();

        callUndoSnackbar(context);
    }

    private void callUndoSnackbar(Context context){

        Snackbar snackbar = Snackbar.make(coordinator_Layout,context.getResources().getString(R.string.snack_undo_delete),
                Snackbar.LENGTH_LONG);
        snackbar.setAction(context.getResources().getString(R.string.action_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callItemRestore();
            }
        });
        snackbar.show();
    }

    private void callItemRestore(){

        recentDeletedFlashCard.create();
        dataSet = recentDeletedFlashCard.getAllFlashCards(DID,SID,CID);
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return dataSet.getCount();
    }
}
