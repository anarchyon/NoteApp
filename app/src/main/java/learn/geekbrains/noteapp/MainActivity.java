package learn.geekbrains.noteapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements ListNotesFragment.Contract, NoteFragment.Contract {
    public static final String KEY_NOTE = "key_note";
    private static final String KEY_LIST_NOTES = "key_list_notes";
    private static final String NOTE_LIST_FRAGMENT_TAG = "list_fragment";
    private List<Note> notes;
    private Note note = null;
    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_root_container, new RootLandFragment())
                    .commit();
        }

        initToolbarAndNavigationDrawer();
        initFab();
        loadStates(savedInstanceState);
        initListFragment();

        if (note != null) showNote(note);
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab_popup_menu);
        fab.setOnClickListener(v -> {
            showNote(null);
        });
    }

    private void loadStates(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList(KEY_LIST_NOTES);
            note = savedInstanceState.getParcelable(KEY_NOTE);
        }

        if (notes == null) notes = TemporaryClassNotes.getNotes();
    }

    private void initListFragment() {
        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_main_container, ListNotesFragment.newInstance(notes), NOTE_LIST_FRAGMENT_TAG)
                .commit();
    }

    private void initToolbarAndNavigationDrawer() {
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_menu);
        setSupportActionBar(bottomAppBar);
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
        } else if (itemId == android.R.id.home) {
            BottomNavigationDrawer bottomNavigationDrawer = new BottomNavigationDrawer();
            bottomNavigationDrawer.show(getSupportFragmentManager(), BottomNavigationDrawer.TAG);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_NOTE, note);
        outState.putParcelableArrayList(KEY_LIST_NOTES, (ArrayList<? extends Parcelable>) notes);
    }

    @Override
    public void getNoteAndShow(Note note) {
        this.note = note;
        showNote(note);
    }

    private void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void showNote(Note note) {
        int fragmentIdForNote = isLandscape ? R.id.fragment_additional_container : R.id.fragment_main_container;

        clearBackStack();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentIdForNote, NoteFragment.newInstance(note))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void saveNote(Note note) {
        clearBackStack();
        ListNotesFragment listNotesFragment =
                (ListNotesFragment) getSupportFragmentManager()
                        .findFragmentByTag(NOTE_LIST_FRAGMENT_TAG);

        notes.remove(note);
        notes.add(note);

        if (listNotesFragment != null) {
            listNotesFragment.setNotes(notes);
        }
    }

    @Override
    public void onBackPressed() {
        note = null;
        super.onBackPressed();
    }
}