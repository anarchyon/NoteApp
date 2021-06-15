package learn.geekbrains.noteapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements ListNotesFragment.Contract, NoteFragment.Controller {
    public static final String KEY_NOTE = "key_note";
    private static final String KEY_LIST_NOTES = "key_list_notes";
    private List<Note> notes;
    private Note note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbarAndNavigationDrawer();
        loadStates(savedInstanceState);
        initListNotes();

        if (note != null) showNote();
    }

    private void loadStates(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList(KEY_LIST_NOTES);
            note = savedInstanceState.getParcelable(KEY_NOTE);
        }

        if (notes == null) {
            notes = TemporaryClassNotes.getNotes();
        }
    }

    private void initListNotes() {
        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ListNotesFragment.newInstance(notes))
                .commit();
    }

    private void initToolbarAndNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_menu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        addListenerToNavigationDrawer();
    }

    private void addListenerToNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_menu_home) {
                Toast.makeText(this, "главная страница", Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.nav_menu_about) {
                Toast.makeText(this, "о приложении", Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.menu_settings) {
                Toast.makeText(this, "окно настроек", Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.menu_important) {
                Toast.makeText(this, "открыть список важных заметок", Toast.LENGTH_LONG).show();
            }
            return true;
        });
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
    public void getNoteAndShow(Note note) {
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

    @Override
    public void onBackPressed() {
        note = null;
        super.onBackPressed();
    }
}