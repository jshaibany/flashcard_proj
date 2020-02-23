package com.example.jandpartners.flashcardspro.ui.sections;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.objects.ent_Class;
import com.example.jandpartners.flashcardspro.objects.ent_DeckSet;
import com.example.jandpartners.flashcardspro.objects.ent_Section;


public class maintainSectionFragment extends Fragment {

    View root;
    private String classID;

    public maintainSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_maintain_section, container, false);

        try{

            classID=maintainSectionFragmentArgs.fromBundle(getArguments()).getClassID();
            prepareButtons();
        }
        catch (Exception e){

        }
        return root;
    }

    private void prepareButtons(){

        final Button btn_cancel =root.findViewById(R.id.btn_cancel);
        final Button btn_ok =root.findViewById(R.id.btn_ok);
        final EditText et_name =root.findViewById(R.id.et_name);

        final ent_Class c = new ent_Class(getContext());
        c.setName(classID);
        c.get();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(root).navigateUp();

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ent_Section s = new ent_Section(getContext());
                s.setSectionClass(c);
                s.setName(et_name.getText().toString());
                s.create();

                ent_DeckSet e = new ent_DeckSet(getContext());
                e.setDecksetClass(c);
                e.setDecksetSection(s);
                e.create();

                Navigation.findNavController(root).navigateUp();

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
