package project.paveltoy.noteapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NoteFragment extends Fragment implements OnBackPressedListener {
    public static final String TAG = "note_fragment";
    private static final String ARG_NOTE = "note";
    private Note note;
    private AppCompatEditText subjectNote, textNote;
    private AppCompatTextView creationDate;
    private AppCompatToggleButton isImportant;

    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        subjectNote = view.findViewById(R.id.subject_note);
        textNote = view.findViewById(R.id.text);
        creationDate = view.findViewById(R.id.creation_date);
        isImportant = view.findViewById(R.id.toggle_important);

        fillNoteFields();
        setMenuListener();

        isImportant.setOnCheckedChangeListener((btnView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), R.string.note_is_imortant, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.note_isnt_important, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMenuListener() {
        BottomAppBar bottomAppBar = requireActivity().findViewById(R.id.bottom_menu);
        if (bottomAppBar != null) {

            bottomAppBar.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_bottom_save_note) {
                    getContract().saveNote(gatherNote());
                    return true;
                } else if (itemId == R.id.menu_bottom_delete_note) {
                    getContract().deleteNote(note);
                    return true;
                }
                return false;
            });
        }
    }

    private Note gatherNote() {
        String subject = getStringFromField(subjectNote.getText());
        String text = getStringFromField(textNote.getText());
        return new Note(
                note == null ? null : note.getId(),
                subject,
                text,
                note == null ? Note.getCurrentDate() : note.getCreationDate(),
                isImportant.isChecked() ? Note.NOTE_IMPORTANT : Note.NOTE_NOT_IMPORTANT
        );
    }

    private String getStringFromField(Editable fieldContent) {
        if (fieldContent == null || fieldContent.toString().trim().isEmpty()) {
            return getResources().getString(R.string.note);
        }
        return fieldContent.toString();
    }

    private void fillNoteFields() {
        if (note == null) return;
        subjectNote.setText(note.getName());
        textNote.setText(note.getText());
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(note.getCreationDate());
        creationDate.setText(dateString);
        if (note.getIsImportant() != 0) {
            isImportant.setChecked(true);
        }
    }

    private Contract getContract() {
        return (Contract) getActivity();
    }

    public interface Contract {

        void saveNote(Note note);

        void deleteNote(Note note);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof Contract)) {
            throw new IllegalStateException(
                    "Activity must implements NoteFragment.Contract"
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getContext(), "Test onBackPressed from Fragment", Toast.LENGTH_LONG).show();
        CloseNoteWithoutSavingDialog dialog = new CloseNoteWithoutSavingDialog();
        getChildFragmentManager()
                .beginTransaction()
                .add(dialog, null)
                .commit();
    }
}