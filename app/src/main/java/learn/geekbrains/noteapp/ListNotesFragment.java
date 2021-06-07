package learn.geekbrains.noteapp;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListNotesFragment extends Fragment {

    private static final String ARG_NOTES = "notes";
    private List<Note> notes;

    public ListNotesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notes = getArguments().getParcelableArrayList(ARG_NOTES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareNotes();
        initList(view);
    }

    private void prepareNotes() {
        notes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = getResources().getString(R.string.header) + (i + 1);
            String text = getResources().getString(R.string.text) + (i + 1);
            notes.add(new Note(name, text));
        }

    }

    private void initList(View view) {
        LinearLayoutCompat linearLayoutCompat = (LinearLayoutCompat) view;
        for (Note note : notes) {
            TextView tv = new TextView(getContext());
            tv.setText(note.getName());
            tv.setTextSize(30);
            linearLayoutCompat.addView(tv);

            tv.setOnClickListener(textView -> {
                Controller controller = (Controller) getActivity();
                if (controller != null) {
                    controller.getNote(note);
                }
            });
        }
    }

    public interface Controller {
        void getNote(Note note);
    }
}