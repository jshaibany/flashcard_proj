package com.example.jandpartners.flashcardspro.ui.flashcards;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.objects.ent_Class;
import com.example.jandpartners.flashcardspro.objects.ent_DeckSet;
import com.example.jandpartners.flashcardspro.objects.ent_FlashCard;
import com.example.jandpartners.flashcardspro.objects.ent_Section;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class maintainFlashCardFragment extends Fragment {


    private static final String TAG = "maintainFlashCardFragment";
    private Integer FID;
    private String CID;
    private String SID;
    private String DID;
    private ent_Class ent_class;
    private ent_Section ent_section;
    private ent_DeckSet ent_deckSet;
    private ent_FlashCard ent_flashCard;
    private View root;
    private Context context;

    private com.google.android.material.textfield.TextInputEditText et_def;
    private com.google.android.material.textfield.TextInputEditText et_term;
    private com.warkiz.widget.IndicatorSeekBar seekbar_importance;
    private com.warkiz.widget.IndicatorSeekBar seekbar_hardness;


    public maintainFlashCardFragment() {
        // Required empty public constructor
    }


    public static maintainFlashCardFragment newInstance(String param1, String param2) {
        maintainFlashCardFragment fragment = new maintainFlashCardFragment();

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
        context = getContext();
        root=inflater.inflate(R.layout.fragment_maintain_flash_card, container, false);

        if(null == savedInstanceState){

            initObjects();
            initViews();
            prepareButtons();
        }


        return root;
    }

    private void initObjects(){

        try{

            FID = maintainFlashCardFragmentArgs.fromBundle(getArguments()).getFID();
            CID = maintainFlashCardFragmentArgs.fromBundle(getArguments()).getCID();
            SID = maintainFlashCardFragmentArgs.fromBundle(getArguments()).getSID();
            DID = maintainFlashCardFragmentArgs.fromBundle(getArguments()).getDID();

            if(null == ent_flashCard){

                ent_class = new ent_Class(context);
                ent_class.setName(CID);
                ent_class.get();

                ent_section = new ent_Section(context);
                ent_section.setSectionClass(ent_class);
                ent_section.setName(SID);
                ent_section.get();

                ent_deckSet = new ent_DeckSet(context);
                ent_deckSet.setDecksetClass(ent_class);
                ent_deckSet.setDecksetSection(ent_section);
                ent_deckSet.get();


                ent_flashCard = new ent_FlashCard(context);
            }



        }
        catch (Exception e){

        }

    }
    private void initViews(){

        et_term = root.findViewById(R.id.et_term);
        et_def=root.findViewById(R.id.et_def);
        seekbar_importance=root.findViewById(R.id.seekbar_importance);
        seekbar_hardness=root.findViewById(R.id.seekbar_hardness);

        final TextView tv_action_title = root.findViewById(R.id.tv_action_title);

        switch (FID){

            case 0:
                tv_action_title.setText(R.string.action_fc_new);
                break;
                default:
                    tv_action_title.setText(R.string.action_fc_edit);
                    populateFields();
        }

    }
    private void populateFields(){

        ent_flashCard.setId(FID);
        ent_flashCard.get();

        et_term.setText(ent_flashCard.getName());
        et_def.setText(ent_flashCard.getDefinition());
        seekbar_importance.setProgress(Float.valueOf(String.valueOf(ent_flashCard.getImportance())));
        seekbar_hardness.setProgress(Float.valueOf(String.valueOf(ent_flashCard.getHardness())));
    }
    private void prepareButtons(){

        final FloatingActionButton fab_save=root.findViewById(R.id.fab_save);

        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ent_flashCard.setName(et_term.getText().toString());
                ent_flashCard.setDefinition(et_def.getText().toString());
                ent_flashCard.setImportance(seekbar_importance.getProgress());
                ent_flashCard.setHardness(seekbar_hardness.getProgress());

                if(FID <= 0) {

                    ent_flashCard.setFlashcardClass(ent_class);
                    ent_flashCard.setFlashcardSection(ent_section);
                    ent_flashCard.setFlashcardDeckset(ent_deckSet);
                    ent_flashCard.create();
                }
                else ent_flashCard.update();

                Navigation.findNavController(getParentFragment().getView()).navigateUp();

            }
        });
    }

}
