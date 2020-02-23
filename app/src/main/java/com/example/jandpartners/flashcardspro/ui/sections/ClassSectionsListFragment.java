package com.example.jandpartners.flashcardspro.ui.sections;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jandpartners.flashcardspro.R;

import com.example.jandpartners.flashcardspro.custom.CustomClassRecyclerViewAdapter;
import com.example.jandpartners.flashcardspro.custom.CustomSectionRecyclerViewAdapter;
import com.example.jandpartners.flashcardspro.custom.CustomSwipeToDeleteCallBack;
import com.example.jandpartners.flashcardspro.objects.ent_Section;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;



public class ClassSectionsListFragment extends Fragment {

    private View root;
    private String classID;

    private CustomSectionRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Cursor data;

    public ClassSectionsListFragment() {
        // Required empty public constructor
    }


    public static ClassSectionsListFragment newInstance(String param1, String param2) {
        ClassSectionsListFragment fragment = new ClassSectionsListFragment();

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
        root = inflater.inflate(R.layout.fragment_class_sections_list, container, false);
        classID = ClassSectionsListFragmentArgs.fromBundle(getArguments()).getID();

        if(savedInstanceState == null){

            prepareButtons();
            getData();
            prepareRecyclerView();
            //addListener();
        }
        else {

            getData();
            updateRecyclerView();
        }


        return root;


    }

    private void prepareButtons(){

        FloatingActionButton fab_add = root.findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToNewSectionDialog();
            }
        });
    }
    private void prepareRecyclerView(){

        recyclerView = root.findViewById(R.id.rv_sections);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CustomSectionRecyclerViewAdapter();
        adapter.setAdapterData(data);
        adapter.setCoordinator_Layout(root);
        adapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToDeckSetsList(v);
            }
        });
        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showUpdateAlertDialog(v);
                return false;
            }
        });

        recyclerView.setClickable(true);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new CustomSwipeToDeleteCallBack(adapter,getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void showUpdateAlertDialog(View v){

        final TextView name = v.findViewById(R.id.tv_name);
        final String SID=name.getText().toString();


        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(SID);

        if(Build.VERSION.SDK_INT >16){

            input.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            input.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.class_name);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(R.string.action_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        updateSection(classID,SID,input.getText().toString());
                        getData();
                        updateRecyclerView();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setView(input);
        alertDialog.show();
    }
    private void updateSection(String CID,String SID,String newSID){

        ent_Section ent_section = new ent_Section(getContext());
        ent_section.changeName(CID,SID,newSID);
    }
    private void updateRecyclerView(){


        adapter.setAdapterData(data);
        adapter.notifyDataSetChanged();


    }
    private void getData(){

        data = new ent_Section(getContext()).getAllSections(classID);
    }
    private void navigateToNewSectionDialog(){

        ClassSectionsListFragmentDirections.
                ActionClassSectionsListFragmentToMaintainSectionFragment2 action =
                ClassSectionsListFragmentDirections.actionClassSectionsListFragmentToMaintainSectionFragment2(classID);

        Navigation.findNavController(root).navigate(action);
    }
    private void navigateToDeckSetsList(View v){

        final TextView tv_name = v.findViewById(R.id.tv_name);
        String sectionID = tv_name.getText().toString();

        ClassSectionsListFragmentDirections.
                ActionClassSectionsListFragmentToDeckSetControlFragment action =
                ClassSectionsListFragmentDirections.actionClassSectionsListFragmentToDeckSetControlFragment(classID,sectionID);

        Navigation.findNavController(root).navigate(action);
    }


}
