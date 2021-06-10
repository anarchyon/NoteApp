package learn.geekbrains.noteapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    private static final String ARG_NOTE = "note";
    private Note note;

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
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView header = view.findViewById(R.id.header);
        TextView text = view.findViewById(R.id.text);
        TextView creationDate = view.findViewById(R.id.creation_date);

        header.setText(note.getName());
        text.setText(note.getText());
        creationDate.setText(note.getCreationDate());

        super.onViewCreated(view, savedInstanceState);
    }

    public interface Controller {
        void saveNote(Note note);
    }
}