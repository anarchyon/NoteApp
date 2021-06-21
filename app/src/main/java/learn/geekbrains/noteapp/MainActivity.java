package learn.geekbrains.noteapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity
        extends AppCompatActivity
        implements
        ListNotesFragment.Contract,
        NoteFragment.Contract,
        BottomNavigationDrawer.NavController {

    public static final String KEY_NOTE = "key_note";
    private static final String KEY_LIST_NOTES = "key_list_notes";
    private static final String ROOT_FRAGMENT_BACKSTACK_TAG = "root_fragment_backstack";
    private List<Note> notes;
    private Note note = null;
    private boolean isLandscape;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomAppBar();
//        initFab();
        loadStates(savedInstanceState);
        initListFragment();
    }

//    private void initFab() {
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(v -> {
//            onBackPressed();
//            showNote(null);
//        });
//    }

    private void loadStates(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList(KEY_LIST_NOTES);
            note = savedInstanceState.getParcelable(KEY_NOTE);
        }

        if (notes == null) notes = TemporaryClassNotes.getNotes();
    }

    private void initListFragment() {
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_root_container, new RootLandFragment(), RootLandFragment.TAG)
                    .commit();
        }

        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_main_container,
                        ListNotesFragment.newInstance(notes),
                        ListNotesFragment.TAG
                )
                .commit();

        if (note != null) showNote(note);
    }

    private void initSettingsFragment() {
//        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        isLandscape ?
                                R.id.fragment_root_container :
                                R.id.fragment_main_container,
                        new SettingsFragment()
                )
                .addToBackStack(ROOT_FRAGMENT_BACKSTACK_TAG)
                .commit();
    }

    private void initAboutFragment() {
//        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        isLandscape ?
                                R.id.fragment_root_container :
                                R.id.fragment_main_container,
                        new AboutFragment()
                )
                .addToBackStack(ROOT_FRAGMENT_BACKSTACK_TAG)
                .commit();
    }

    private void initListImportantFragment() {
//        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        isLandscape ?
                                R.id.fragment_root_container :
                                R.id.fragment_main_container,
                        new ImportantNotesFragment()
                )
                .addToBackStack(ROOT_FRAGMENT_BACKSTACK_TAG)
                .commit();
    }

    private void initBottomAppBar() {
        bottomAppBar = findViewById(R.id.bottom_menu);
//        setSupportActionBar(bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(view -> {
            BottomNavigationDrawer bottomNavigationDrawer = new BottomNavigationDrawer();
            bottomNavigationDrawer.show(getSupportFragmentManager(), BottomNavigationDrawer.TAG);
        });
//
//        bottomAppBar.setOnMenuItemClickListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.menu_main_search_button) {
//                return true;
//            }
//            return false;
//        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.menu_main_search_button) {
//            Toast.makeText(this, "тут будет поиск", Toast.LENGTH_LONG).show();
//        } else if (itemId == android.R.id.home) {
//            BottomNavigationDrawer bottomNavigationDrawer = new BottomNavigationDrawer();
//            bottomNavigationDrawer.show(getSupportFragmentManager(), BottomNavigationDrawer.TAG);
//        }
//        return true;
//    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_NOTE, note);
        outState.putParcelableArrayList(KEY_LIST_NOTES, (ArrayList<? extends Parcelable>) notes);
    }

    @Override
    public void showReceivedNote(Note note) {
        showNote(note);
    }

    private void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void showNote(Note note) {
        replaceBottomAppBarMenu();
        clearBackStack();
        this.note = note;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        isLandscape ?
                                R.id.fragment_additional_container :
                                R.id.fragment_main_container,
                        NoteFragment.newInstance(note),
                        NoteFragment.TAG
                )
                .addToBackStack(null)
                .commit();
    }

    private void replaceBottomAppBarMenu() {

    }

    @Override
    public void saveNote(Note note) {
        clearBackStack();
        ListNotesFragment listNotesFragment =
                (ListNotesFragment) getFragmentByTag(ListNotesFragment.TAG);

        notes.remove(note);
        notes.add(note);

        sendNotesToListFragment(listNotesFragment);
    }

    @Override
    public void deleteNote(Note note) {
        clearBackStack();
        ListNotesFragment listNotesFragment =
                (ListNotesFragment) getFragmentByTag(ListNotesFragment.TAG);

        notes.remove(note);

        sendNotesToListFragment(listNotesFragment);
    }

    private Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    private void sendNotesToListFragment(ListNotesFragment listNotesFragment) {
        if (listNotesFragment != null) {
            listNotesFragment.setNotes(notes);
        }
    }

    @Override
    public void onBackPressed() {
        NoteFragment noteFragment = (NoteFragment) getFragmentByTag(NoteFragment.TAG);
        if (noteFragment != null && noteFragment.isVisible()) {
            note = null;
        }
        super.onBackPressed();
        if (isLandscape) {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount == 0 || (noteFragment != null && backStackEntryCount == 1)) {
                initListFragment();
            }
        }
    }

    @Override
    public void showFragment(int idFragment) {
        if (idFragment == R.id.nav_menu_important) {
            initListImportantFragment();
        } else if (idFragment == R.id.nav_menu_settings) {
            initSettingsFragment();
        } else if (idFragment == R.id.nav_menu_about) {
            initAboutFragment();
        } else if (idFragment == R.id.nav_menu_home) {
            note = null;
            initListFragment();
        }
    }
}