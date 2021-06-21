package learn.geekbrains.noteapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListNotesFragment extends Fragment {
    private static final String ARG_NOTES = "notes";
    public static final String TAG = "list_fragment";

    private List<Note> notes;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

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
        if (getArguments() != null && getArguments().containsKey(ARG_NOTES)) {
            notes = getArguments().getParcelableArrayList(ARG_NOTES);
        } else {
            notes = TemporaryClassNotes.getNotes();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View listNotes = inflater.inflate(R.layout.fragment_list_notes, container, false);
        recyclerView = listNotes.findViewById(R.id.recycler_view_note_list);
        return listNotes;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setMenuListener();
        adapter = new NoteAdapter(this);
        adapter.setOnItemClickListener(getContract()::showReceivedNote);
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

    private void setMenuListener() {
        bottomAppBar = requireActivity().findViewById(R.id.bottom_menu);
        if (bottomAppBar != null) {
            initBottomAppBarAndFabState();

            bottomAppBar.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_main_search_button) {
                    Toast.makeText(getContext(), "тут будет поиск", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            });
        }
    }

    private void initBottomAppBarAndFabState() {
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        bottomAppBar.getMenu().findItem(R.id.menu_main_search_button).setVisible(true);

        fab = requireActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.setImageResource(R.drawable.ic_baseline_add_24);
            fab.setOnClickListener(view1 -> getContract().showReceivedNote(null));
        }
    }

    @Override
    public void onStop() {
        if (bottomAppBar != null) {
            bottomAppBar.getMenu().findItem(R.id.menu_main_search_button).setVisible(false);
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            bottomAppBar.setNavigationIcon(null);
        }
        if (fab != null) {
            fab.setImageResource(R.drawable.ic_baseline_reply_24);
            fab.setOnClickListener(view -> requireActivity().onBackPressed());
        }
        super.onStop();
    }

    private void renderList() {
        adapter.setData(notes);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(notes.size() - 1);
    }

    private void renderListChangeItem() {

    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        renderList();
    }

    public interface Contract {
        void showReceivedNote(Note note);

        void deleteNote(Note note);

        void saveNote(Note note);
    }

    private Contract getContract() {
        return (Contract) getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof Contract)) {
            throw new IllegalStateException(
                    "Activity must implements ListNotesFragment.Contract"
            );
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu,
                                    @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        Note newNote = adapter.getNote();
        if (newNote.getIsImportant() == 0) {
            menu.removeItem(R.id.menu_context_set_not_important);
        } else {
            menu.removeItem(R.id.menu_context_set_important);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Note newNote = adapter.getNote();
        if (itemId == R.id.menu_context_delete) {
            getContract().deleteNote(newNote);
        } else if (itemId == R.id.menu_context_open_note) {
            getContract().showReceivedNote(newNote);
        } else if (itemId == R.id.menu_context_set_important) {
            newNote.setIsImportant(Note.NOTE_IMPORTANT);
            getContract().saveNote(newNote);
        } else if (itemId == R.id.menu_context_set_not_important) {
            newNote.setIsImportant(Note.NOTE_NOT_IMPORTANT);
            getContract().saveNote(newNote);
        }
        return super.onContextItemSelected(item);
    }
}