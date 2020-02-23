package com.example.jandpartners.flashcardspro.ui.action;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.objects.proc_Review;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;


public class reviewFlashcardsFragment extends Fragment {


    private View root;
    private proc_Review proc_review;
    private String CID;
    private String SID;
    private String DID;
    private Integer paramCount;
    private Integer paramOption;
    private boolean cardFace=true;

    private TextView tv_card_content;
    private TextView tv_counter;

    reviewFlashcardsFragmentDirections.ActionReviewFlashcardsFragmentToDeckSetControlFragment action;

    public reviewFlashcardsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_review_flashcards, container, false);

        if(savedInstanceState == null){

            initObjects();
            initViews();
            prepareButtons();
            this.proc_review = getReviewObject();

            getNextCard();
        }


        return root;
    }

    private void initObjects(){

        CID = reviewFlashcardsFragmentArgs.fromBundle(getArguments()).getCID();
        SID = reviewFlashcardsFragmentArgs.fromBundle(getArguments()).getSID();
        DID = reviewFlashcardsFragmentArgs.fromBundle(getArguments()).getDID();
        paramCount = reviewFlashcardsFragmentArgs.fromBundle(getArguments()).getParamCount();
        paramOption= reviewFlashcardsFragmentArgs.fromBundle(getArguments()).getParamOption();

        action = reviewFlashcardsFragmentDirections.actionReviewFlashcardsFragmentToDeckSetControlFragment(CID,SID);
    }

    private void initViews(){

        tv_card_content = root.findViewById(R.id.tv_card_content);
        tv_counter=root.findViewById(R.id.tv_counter);
    }
    private proc_Review getReviewObject(){

        proc_Review p = new proc_Review(getContext(),CID,SID,DID);

        switch (paramOption){

            case 1:
                p.getSequencialSet(paramCount);
                break;
            case 2:
                p.getRandomSet(paramCount);
                break;
            case 3:
                p.getByImportanceSet(paramCount);
                break;
            case 4:
                p.getByHardnessSet(paramCount);
                break;
                default:p.getSequencialSet(0);

        }
        return p;
    }

    private void prepareButtons(){

        FloatingActionButton fab_flip = root.findViewById(R.id.fab_flip);
        fab_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flipCard();
            }
        });

        FloatingActionButton fab_next = root.findViewById(R.id.fab_next);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getNextCard();
            }
        });

        FloatingActionButton fab_prev = root.findViewById(R.id.fab_prev);
        fab_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPrevCard();
            }
        });
    }

    private void NavigateToDeckSet(){

        try{


            NavHostFragment.findNavController(this).navigate(action);

        }
        catch (Exception e){

            Log.d("TAG",e.toString());
        }

    }

    private void flipCard(){

        if(proc_review.getCurrentTerm_Def() != null){

            tv_card_content.setText(cardFace ? proc_review.getCurrentTerm_Def()[1] : proc_review.getCurrentTerm_Def()[0]);
            cardFace = !cardFace;
        }

    }

    private void getNextCard(){

        proc_review.getNext();

        tv_card_content.setText(proc_review.getCurrentTerm_Def()[0]);
        tv_counter.setText(String.format(Locale.ENGLISH,"%d/%d",proc_review.getCurrentPosition()+1,proc_review.getCount()));
    }

    private void getPrevCard(){

        proc_review.getPrev();

        tv_card_content.setText(proc_review.getCurrentTerm_Def()[0]);
        tv_counter.setText(String.format(Locale.ENGLISH,"%d/%d",proc_review.getCurrentPosition()+1,proc_review.getCount()));
    }

    @Override
    public void onDetach() {
        proc_review.finishProcess();
        super.onDetach();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                NavigateToDeckSet();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onResume(){
        super.onResume();
    }


}
