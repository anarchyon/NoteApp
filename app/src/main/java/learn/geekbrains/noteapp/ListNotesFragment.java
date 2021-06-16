package learn.geekbrains.noteapp;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ListNotesFragment extends Fragment {
    private static final String ARG_NOTES = "notes";

    private List<Note> notes;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;

    public ListNotesFragment() {
    }

    public static ListNotesFragment newInstance(List<Note> notes) {
        ListNotesFragment listNotesFragment = new ListNotesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_NOTES, (ArrayList<? extends Parcelable>) notes);
        listNotesFragment.setArguments(args);
        return listNotesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notes = getArguments().getParcelableArrayList(ARG_NOTES);
        } else {
            notes = TemporaryClassNotes.getNotes();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listNotes = inflater.inflate(R.layout.fragment_list_notes, container, false);
        recyclerView = listNotes.findViewById(R.id.recycler_view_note_list);
        return listNotes;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new NoteAdapter();
        adapter.setOnItemClickListener(getContract()::getNoteAndShow);
        boolean isLandscape =
                getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE;
        RecyclerView.LayoutManager layoutManager =
                isLandscape ?
                        new LinearLayoutManager(getContext()) :
                        new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
        renderList();
    }

    private void renderList() {
        adapter.setData(notes);
        adapter.notifyDataSetChanged();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        renderList();
    }

    public interface Contract {
        void getNoteAndShow(Note note);
    }

    private Contract getContract() {
        return (Contract) getActivity();
    }
}