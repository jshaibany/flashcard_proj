package com.example.jandpartners.flashcardspro.ui.decksets;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.objects.ent_Class;
import com.example.jandpartners.flashcardspro.objects.ent_DeckSet;
import com.example.jandpartners.flashcardspro.objects.ent_Section;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.chip.Chip;


public class DeckSetControlFragment extends Fragment {



    private static final String TAG = "DeckSetControlFragment";
    private String classID;
    private String sectionID;
    private View root;
    private Context context;
    private ent_Class ent_class;
    private ent_Section ent_section;
    private ent_DeckSet ent_deckSet;
    private Chip chip_new_card;
    private Chip chip_flashcards_list;
    private Chip chip_review;
    private Chip chip_quiz;


    public DeckSetControlFragment() {
        // Required empty public constructor
    }


    public static DeckSetControlFragment newInstance(String param1, String param2) {
        DeckSetControlFragment fragment = new DeckSetControlFragment();

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
        root= inflater.inflate(R.layout.fragment_deck_set_control, container, false);
        context = getContext();

        initObjects();
        prepareButtons();
        return root;
    }

    private void initObjects(){

        try{

            classID = DeckSetControlFragmentArgs.fromBundle(getArguments()).getCID();
            sectionID = DeckSetControlFragmentArgs.fromBundle(getArguments()).getSID();

            ent_class = new ent_Class(context);
            ent_class.setName(classID);
            ent_class.get();

            ent_section = new ent_Section(context);
            ent_section.setSectionClass(ent_class);
            ent_section.setName(sectionID);
            ent_section.get();

            ent_deckSet = new ent_DeckSet(context);
            ent_deckSet.setDecksetClass(ent_class);
            ent_deckSet.setDecksetSection(ent_section);
            ent_deckSet.get();

        }
        catch (Exception e){

        }
    }

    private void prepareButtons(){

        chip_new_card = root.findViewById(R.id.chip_new_card);
        chip_flashcards_list = root.findViewById(R.id.chip_flashcards_list);
        chip_review = root.findViewById(R.id.chip_review);
        chip_quiz = root.findViewById(R.id.chip_quiz);

        chip_new_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToMaintainFlashCard();
            }
        });

        chip_flashcards_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToFlashcardsList();
            }
        });

        chip_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToParamFragment(0);
            }
        });

        chip_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToParamFragment(1);
            }
        });

    }

    private void navigateToMaintainFlashCard(){

        //Pass Zero value as FID to create new Flashcard
        DeckSetControlFragmentDirections.ActionDeckSetControlFragmentToMaintainFlashCardFragment action =
                DeckSetControlFragmentDirections.actionDeckSetControlFragmentToMaintainFlashCardFragment(ent_deckSet.getName(),ent_section.getName(),ent_class.getName()).setFID(0);

        Navigation.findNavController(root).navigate(action);
    }

    private void navigateToFlashcardsList(){

        DeckSetControlFragmentDirections.ActionDeckSetControlFragmentToFlashcardsListFragment action =
                DeckSetControlFragmentDirections.actionDeckSetControlFragmentToFlashcardsListFragment(ent_class.getName(),ent_section.getName(),ent_deckSet.getName());

        Navigation.findNavController(root).navigate(action);
    }

    private void navigateToParamFragment(Integer type){

        // 0 type for Review
        // 1 type for Quiz

        DeckSetControlFragmentDirections.ActionDeckSetControlFragmentToSelectParametersFragment action =
                DeckSetControlFragmentDirections.actionDeckSetControlFragmentToSelectParametersFragment(
                        ent_class.getName()
                        ,ent_section.getName()
                        ,ent_deckSet.getName()
                        ,type);
        Navigation.findNavController(root).navigate(action);
    }

}
