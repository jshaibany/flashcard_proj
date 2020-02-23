package com.example.jandpartners.flashcardspro.ui.action;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jandpartners.flashcardspro.MainActivity;
import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.objects.proc_Quiz;
import com.example.jandpartners.flashcardspro.objects.proc_Review;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;


public class quizFlashcardsFragment extends Fragment {

    private View root;
    private quizFlashcardsFragmentDirections.ActionQuizFlashcardsFragmentToDeckSetControlFragment action;
    private proc_Quiz proc_quiz;
    private String CID;
    private String SID;
    private String DID;
    private Integer paramCount;
    private Integer paramOption;
    private boolean cardFace=true;

    private TextView tv_card_content;
    private TextView tv_counter;

    public quizFlashcardsFragment() {
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
        root= inflater.inflate(R.layout.fragment_quiz_flashcards, container, false);

        if(savedInstanceState == null){

            initObjects();
            initViews();
            prepareButtons();
            this.proc_quiz = getQuizObject();
            goToNextCard();

        }





        return root;
    }

    private void initObjects(){

        CID = quizFlashcardsFragmentArgs.fromBundle(getArguments()).getCID();
        SID = quizFlashcardsFragmentArgs.fromBundle(getArguments()).getSID();
        DID = quizFlashcardsFragmentArgs.fromBundle(getArguments()).getDID();
        paramCount=quizFlashcardsFragmentArgs.fromBundle(getArguments()).getParamCount();
        paramOption=quizFlashcardsFragmentArgs.fromBundle(getArguments()).getParamOption();

        action= quizFlashcardsFragmentDirections.actionQuizFlashcardsFragmentToDeckSetControlFragment(CID,SID);
    }
    private void initViews(){

        tv_card_content = root.findViewById(R.id.tv_card_content);
        tv_counter=root.findViewById(R.id.tv_counter);
    }
    private void prepareButtons() {

        final FloatingActionButton fab_flip = root.findViewById(R.id.fab_flip);

        fab_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flipCard();
            }
        });

        final FloatingActionButton fab_true = root.findViewById(R.id.fab_true);

        fab_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitAnswer(true);

            }
        });

        final FloatingActionButton fab_flase = root.findViewById(R.id.fab_false);

        fab_flase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitAnswer(false);

            }
        });




    }
    private proc_Quiz getQuizObject(){

        proc_Quiz p = new proc_Quiz(getContext(),CID,SID,DID);

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
    private void NavigateToDeckSet(){

        //initObjects();

        try{


            NavHostFragment.findNavController(this).navigate(action);

        }
        catch (Exception e){

            Log.d("TAG",e.toString());
        }

    }
    private void flipCard(){

        if(proc_quiz.getCurrentTerm_Def() != null){

            tv_card_content.setText(cardFace ? proc_quiz.getCurrentTerm_Def()[1] : proc_quiz.getCurrentTerm_Def()[0]);
            cardFace = !cardFace;
        }

    }
    private void goToNextCard(){

        this.proc_quiz.getNext();

        tv_card_content.setText(proc_quiz.getCurrentTerm_Def()[0]);
        tv_counter.setText(String.format(Locale.ENGLISH,"%d/%d",proc_quiz.getCurrentPosition()+1,proc_quiz.getCount()));
    }
    private void submitAnswer(boolean answer){

        proc_quiz.submitAnswer(answer);

        if(!proc_quiz.isLast()){

            goToNextCard();
        }
        else {

            Snackbar.make(root,getResources().getString(R.string.snack_quiz_done), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.action_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            proc_quiz.setDone(true);
                            proc_quiz.evaluateQuiz();
                            NavigateToDeckSet();

                        }
                    }).show();



        }

    }



    @Override
    public void onDetach() {

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
