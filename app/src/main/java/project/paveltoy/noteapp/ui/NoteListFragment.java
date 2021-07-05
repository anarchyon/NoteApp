package project.paveltoy.noteapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import project.paveltoy.noteapp.data.FirebaseNoteRepository;
import project.paveltoy.noteapp.data.Note;
import project.paveltoy.noteapp.data.NoteRepository;
import project.paveltoy.noteapp.R;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class NoteListFragment extends Fragment implements CallbackContract {
    private static final String ARG_NOTES = "notes";
    public static final String TAG = "list_fragment";
    private NoteRepository notes;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View listNotes = inflater.inflate(R.layout.fragment_list_notes, container, false);
        recyclerView = listNotes.findViewById(R.id.recycler_view_note_list);
        parentView = container;
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
        notes = new FirebaseNoteRepository().init(notes -> adapter.notifyDataSetChanged());
        renderList();
    }

    private void setMenuListener() {
        BottomAppBar bottomAppBar = requireActivity().findViewById(R.id.bottom_menu);
        if (bottomAppBar != null) {

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

    private void renderList() {
        adapter.setData(notes);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(notes.size() - 1);
    }

    public interface Contract {
        void showReceivedNote(Note note);
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
        Note newNote = notes.getNote(adapter.getItemPosition());
        if (newNote.getIsImportant() == 0) {
            menu.removeItem(R.id.menu_context_set_not_important);
        } else {
            menu.removeItem(R.id.menu_context_set_important);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        int itemPosition = adapter.getItemPosition();
        if (itemId == R.id.menu_context_delete) {
            deleteNote(itemPosition);
        } else if (itemId == R.id.menu_context_open_note) {
            getContract().showReceivedNote(notes.getNote(itemPosition));
        } else if (itemId == R.id.menu_context_set_important) {
            Note note = notes.getNote(itemPosition);
            note.setIsImportant(Note.NOTE_IMPORTANT);
            editNote(itemPosition, note);
        } else if (itemId == R.id.menu_context_set_not_important) {
            Note note = notes.getNote(itemPosition);
            note.setIsImportant(Note.NOTE_NOT_IMPORTANT);
            editNote(itemPosition, note);
        }
        return super.onContextItemSelected(item);
    }

    private void editNote(int itemPosition, Note note) {
        notes.editNote(itemPosition, note);
        adapter.notifyItemChanged(itemPosition);
    }

    private void deleteNote(int itemPosition) {
        Note note = notes.getNote(itemPosition);
        notes.deleteNote(itemPosition);
        adapter.notifyItemRemoved(itemPosition);
        Snackbar.make(parentView, R.string.snackbar_delete_note, BaseTransientBottomBar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction(R.string.snackbar_action_undo, view -> {
                    notes.insertNote(itemPosition, note);
                    adapter.notifyItemInserted(itemPosition);
                })
                .show();
    }

    @Override
    public void deleteNote(Note note) {
        if (note.getId() == null) return;
        deleteNote(notes.getPosition(note));
    }

    @Override
    public void saveNote(Note note) {
        if (note.getId() == null) {
            notes.addNote(note);
            adapter.notifyItemInserted(notes.size() - 1);
        } else {
            notes.editNote(note);
        }
    }
}