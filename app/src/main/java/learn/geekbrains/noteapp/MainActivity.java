package learn.geekbrains.noteapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements ListNotesFragment.Controller, NoteFragment.Controller {
    public static final String KEY_NOTE = "key_note";
    private static final String KEY_LIST_NOTES = "key_list_notes";
    private List<Note> notes;
    private Note note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList(KEY_LIST_NOTES);
            note = savedInstanceState.getParcelable(KEY_NOTE);
        }

        if (notes == null) {
            notes = TemporaryClassNotes.getNotes();
        }

        initListNotes();
        if (note != null) showNote();
    }

    private void initListNotes() {
        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ListNotesFragment.newInstance(notes))
                .commit();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_main_search_button) {
            Toast.makeText(this, "тут будет поиск", Toast.LENGTH_LONG).show();
        } else if (itemId == R.id.menu_add_note) {
            Toast.makeText(this, "добавить новую заметку", Toast.LENGTH_LONG).show();
        } else if (itemId == R.id.menu_important) {
            Toast.makeText(this, "открыть список важных заметок", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (note != null) {
            outState.putParcelable(KEY_NOTE, note);
            outState.putParcelableArrayList(KEY_LIST_NOTES, (ArrayList<? extends Parcelable>) notes);
        }
    }

    @Override
    public void getNote(Note note) {
        this.note = note;
        showNote();
    }

    private void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void showNote() {
        boolean isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        int fragmentIdForNote = isLandscape ? R.id.fragment_note_details : R.id.fragment_container;

        clearBackStack();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentIdForNote, NoteFragment.newInstance(note))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void saveNote(Note note) {
        // TODO: 09.06.2021
    }
}