package project.paveltoy.noteapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;

public class CloseNoteWithoutSavingDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_note_close_without_save, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialButton buttonCancel = view.findViewById(R.id.dialog_close_button_cancel);
        buttonCancel.setOnClickListener(v -> {});

        MaterialButton buttonSave = view.findViewById(R.id.dialog_close_button_save_and_close);
        buttonSave.setOnClickListener(v -> {});

        MaterialButton buttonNotSave = view.findViewById(R.id.dialog_close_button_close_without_save);
        buttonNotSave.setOnClickListener(v -> {});
    }
}
