package com.example.jandpartners.flashcardspro.ui.flashcards;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.custom.CustomFlashcardRecyclerViewAdapter;
import com.example.jandpartners.flashcardspro.custom.CustomSectionRecyclerViewAdapter;
import com.example.jandpartners.flashcardspro.custom.CustomSwipeToDeleteCallBack;
import com.example.jandpartners.flashcardspro.objects.ent_FlashCard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class flashcardsListFragment extends Fragment {

    private View root;

    private Integer FID;
    private String CID;
    private String SID;
    private String DID;
    private CustomFlashcardRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Cursor data;

    public flashcardsListFragment() {
        // Required empty public constructor
    }


    public static flashcardsListFragment newInstance(String param1, String param2) {
        flashcardsListFragment fragment = new flashcardsListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_flashcards_list, container, false);

        if(savedInstanceState == null){

            initObjects();
            prepareButtons();
            getData();
            prepareRecyclerView(root);
        }
        else {

            getData();
            updateRecyclerView();
        }
        return root;
    }

    private void initObjects(){

        CID = flashcardsListFragmentArgs.fromBundle(getArguments()).getCID();
        SID = flashcardsListFragmentArgs.fromBundle(getArguments()).getSID();
        DID = flashcardsListFragmentArgs.fromBundle(getArguments()).getDID();
    }
    private void prepareButtons(){

        FloatingActionButton fab_add = root.findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToNewFlashCard();
            }
        });
    }
    private void getData(){

        data = new ent_FlashCard(getContext()).getAllFlashCards(DID,SID,CID);
    }
    private void prepareRecyclerView(View root){

        recyclerView = root.findViewById(R.id.rv_flashcards);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CustomFlashcardRecyclerViewAdapter();
        adapter.setFlashCardKeys(CID,SID,DID);
        adapter.setCoordinator_Layout(root);
        adapter.setAdapterData(data);
        adapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToEditFlashCard(v);
            }
        });

        recyclerView.setClickable(true);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new CustomSwipeToDeleteCallBack(adapter,getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void updateRecyclerView(){


        getData();

        adapter.setAdapterData(data);
        adapter.notifyDataSetChanged();


    }
    private void navigateToNewFlashCard(){

        flashcardsListFragmentDirections.ActionFlashcardsListFragmentToMaintainFlashCardFragment action =
                flashcardsListFragmentDirections.actionFlashcardsListFragmentToMaintainFlashCardFragment(DID,SID,CID).setFID(0);

        Navigation.findNavController(root).navigate(action);
    }
    private void navigateToEditFlashCard(View v){

        final TextView tv_id = v.findViewById(R.id.tv_id);
        FID = Integer.decode(tv_id.getText().toString());

        flashcardsListFragmentDirections.ActionFlashcardsListFragmentToMaintainFlashCardFragment action =
                flashcardsListFragmentDirections.actionFlashcardsListFragmentToMaintainFlashCardFragment(DID,SID,CID).setFID(FID);

        Navigation.findNavController(root).navigate(action);
    }


}
