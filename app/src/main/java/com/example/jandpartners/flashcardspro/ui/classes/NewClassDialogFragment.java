package com.example.jandpartners.flashcardspro.ui.classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.jandpartners.flashcardspro.R;
import com.example.jandpartners.flashcardspro.objects.ent_Class;

public class NewClassDialogFragment extends DialogFragment {

    private static final String TAG = "NewClassDialogFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_new_class,container,false);

        prepareButtons(view,getParentFragment().getView());

        return view;
    }

    private void prepareButtons(View view, final View parentFragment){

        final Button btn_cancel =view.findViewById(R.id.btn_cancel);
        final Button btn_ok =view.findViewById(R.id.btn_ok);
        final EditText et_name =view.findViewById(R.id.et_name);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(parentFragment).navigateUp();

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ent_Class c = new ent_Class(getContext());
                c.setName(et_name.getText().toString());
                c.create();

                Navigation.findNavController(parentFragment).navigateUp();

            }
        });
    }
}
