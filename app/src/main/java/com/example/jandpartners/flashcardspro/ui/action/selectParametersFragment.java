package com.example.jandpartners.flashcardspro.ui.action;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jandpartners.flashcardspro.R;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class selectParametersFragment extends Fragment {

    public final String TAG=selectParametersFragment.class.getSimpleName();
    private String CID;
    private String SID;
    private String DID;
    private Integer paramType;
    private Integer paramCount=0;
    private Integer paramOption=1;



    private View root;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_select_parameters, container, false);

        if(savedInstanceState == null){

            initObjects();
            prepareButtons();
            prepareChipGroups();
        }
        return root;
    }

    private void initObjects(){

        paramType = selectParametersFragmentArgs.fromBundle(getArguments()).getParamType();
        CID = selectParametersFragmentArgs.fromBundle(getArguments()).getCID();
        SID = selectParametersFragmentArgs.fromBundle(getArguments()).getSID();
        DID = selectParametersFragmentArgs.fromBundle(getArguments()).getDID();
    }
    private void prepareButtons(){

        final FloatingActionButton fab_go=root.findViewById(R.id.fab_go);

        fab_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    switch (paramType){

                        case 0:
                            navigateToReviewFragment();
                            break;
                        case 1:
                            navigateToQuizFragment();
                            break;
                    }
                }
                catch (Exception e){

                    Log.d(TAG,e.toString());
                }

            }
        });

    }

    private void prepareChipGroups(){

        final ChipGroup chipGroup_option;
        final ChipGroup chipGroup_count;

        chipGroup_count = root.findViewById(R.id.chipGroup_count);
        chipGroup_option = root.findViewById(R.id.chipGroup_option);

        chipGroup_count.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                switch (checkedId){

                    case R.id.chip_10:
                        paramCount=10;
                        break;
                    case R.id.chip_20:
                        paramCount=20;
                        break;
                    case R.id.chip_40:
                        paramCount=40;
                        break;
                    case R.id.chip_80:
                        paramCount=80;
                        break;
                    case R.id.chip_100:
                        paramCount=100;
                        break;
                    default:
                        paramCount=0;
                }
            }
        });
        chipGroup_option.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                switch (checkedId){

                    case R.id.chip_sequence:
                        paramOption=1;
                        break;
                    case R.id.chip_random:
                        paramOption=2;
                        break;
                    case R.id.chip_importance:
                        paramOption=3;
                        break;
                    case R.id.chip_hardness:
                        paramOption=4;
                        break;
                }
            }
        });
    }
    private void navigateToReviewFragment(){

        selectParametersFragmentDirections.ActionSelectParametersFragmentToReviewFlashcardsFragment action =
                selectParametersFragmentDirections.actionSelectParametersFragmentToReviewFlashcardsFragment(CID,SID,DID);
        action.setParamCount(paramCount);
        action.setParamOption(paramOption);

        Navigation.findNavController(root).navigate(action);
    }
    private void navigateToQuizFragment(){

        selectParametersFragmentDirections.ActionSelectParametersFragmentToQuizFlashcardsFragment action =
                selectParametersFragmentDirections.actionSelectParametersFragmentToQuizFlashcardsFragment(CID,SID,DID);
        action.setParamCount(paramCount);
        action.setParamOption(paramOption);

        Navigation.findNavController(root).navigate(action);
    }


}
