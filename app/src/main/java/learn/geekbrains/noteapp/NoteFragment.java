package learn.geekbrains.noteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomappbar.BottomAppBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    private static final String ARG_NOTE = "note";
    private Note note;
    private AppCompatEditText subjectNote, textNote;
    private AppCompatTextView creationDate;

    public NoteFragment() {
    }

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
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bottom_note_menu, menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        subjectNote = view.findViewById(R.id.subject_note);
        textNote = view.findViewById(R.id.text);
        creationDate = view.findViewById(R.id.creation_date);

        fillNoteFields();
        setMenuListener();
    }

    private void setMenuListener() {
        BottomAppBar bottomAppBar = requireActivity().findViewById(R.id.bottom_menu);
        bottomAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_bottom_save_note) {
                subjectNote.setFocusable(false);
                textNote.setFocusable(false);
                getContract().saveNote(gatherNote());
            } else if (itemId == R.id.menu_bottom_edit_note) {
                subjectNote.setFocusableInTouchMode(true);
                textNote.setFocusableInTouchMode(true);
            }
            return true;
        });
    }

    private Note gatherNote() {
        String subject = getStringFromField(subjectNote.getText());
        String text = getStringFromField(textNote.getText());
        return new Note(
                note == null ? Note.generateNewId() : note.getId(),
                subject,
                text,
                note == null ? Note.getCurrentDate() : note.getCreationDate()
        );
    }

    private String getStringFromField(Editable fieldContent) {
        if (fieldContent == null || fieldContent.toString().trim().isEmpty()) {
            return getResources().getString(R.string.note);
        }
        return fieldContent.toString();
    }

    private void fillNoteFields() {
        if (note == null) {
            subjectNote.setFocusableInTouchMode(true);
            subjectNote.requestFocus();
            textNote.setFocusableInTouchMode(true);
            return;
        };
        subjectNote.setText(note.getName());
        textNote.setText(note.getText());
        creationDate.setText(note.getCreationDate());
    }

    private Contract getContract() {
        return (Contract) getActivity();
    }

    public interface Contract {
        void saveNote(Note note);
    }
}