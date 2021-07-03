package project.paveltoy.noteapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import project.paveltoy.noteapp.Data.Note;
import project.paveltoy.noteapp.UI.AboutFragment;
import project.paveltoy.noteapp.UI.BottomNavigationDrawer;
import project.paveltoy.noteapp.UI.CallbackContract;
import project.paveltoy.noteapp.UI.FragmentObserver;
import project.paveltoy.noteapp.UI.NoteFragment;
import project.paveltoy.noteapp.UI.NoteListFragment;
import project.paveltoy.noteapp.UI.OnBackPressedListener;
import project.paveltoy.noteapp.UI.RootLandFragment;
import project.paveltoy.noteapp.UI.SettingsFragment;
import project.paveltoy.noteapp.UI.SignInFragment;

public class MainActivity
        extends AppCompatActivity
        implements
        NoteListFragment.Contract,
        NoteFragment.Contract,
        BottomNavigationDrawer.NavController,
        SignInFragment.SignInController {

    private static final String KEY_USER_SIGN_STATUS = "sign_status";
    public static final String KEY_NOTE = "key_note";

    private Navigation navigation;
    private Note note = null;
    private boolean isLandscape;
    private boolean isUserSignedIn = false;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private final LifecycleObserver noteFragmentObserver =
            new FragmentObserver(this::initBottomAppBarByNoteFragment);
    private final LifecycleObserver noteListFragmentObserver =
            new FragmentObserver(this::initBottomAppBarByNoteListFragment);
    private final LifecycleObserver signInFragmentObserver =
            new FragmentObserver(this::initBottomAppBarBySignInFragment);

    private Fragment lastFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = new Navigation(getSupportFragmentManager());
        initFragmentContainer();
        initBottomAppBar();
        loadStates(savedInstanceState);
//        if (checkLastFragment()) {
//            initLastFragment();
//        } else {
            if (isUserSignedIn) {
                initListFragment();
            } else {
                initSignInFragment(false);
            }
//        }
    }
//
//    private boolean checkLastFragment() {
//        List<Fragment> fragments = navigation.getFragments();
//        int bs = getSupportFragmentManager().getBackStackEntryCount();
//        if (fragments.size() > 0) {
//            Fragment fragment = fragments.get(fragments.size() - 1);
//            if (!(fragment instanceof NoteFragment) && !(fragment instanceof NoteListFragment)) {
//                lastFragment = fragment;
//                return true;
//            }
//        }
//        return false;
//    }
//
    private void initFragmentContainer() {
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            navigation.addFragment(
                    R.id.fragment_root_container, new RootLandFragment(),
                    RootLandFragment.TAG, false);
        }

    }

    private void initBottomAppBarBySignInFragment(boolean isFragmentActive) {
        fab.hide();
    }

    private void initBottomAppBarByNoteListFragment(boolean isFragmentActive) {
        if (isFragmentActive) {
            bottomAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
            bottomAppBar.getMenu().findItem(R.id.menu_main_search_button).setVisible(true);
            fab.show();
            fab.setImageResource(R.drawable.ic_baseline_add_24);
            fab.setOnClickListener(view1 -> showNote(null));
        } else {
            bottomAppBar.getMenu().findItem(R.id.menu_main_search_button).setVisible(false);
            bottomAppBar.setNavigationIcon(null);
            fab.setImageResource(R.drawable.ic_baseline_reply_24);
            fab.setOnClickListener(view -> onBackPressed());
        }
    }

    private void initBottomAppBarByNoteFragment(boolean isFragmentActive) {
        if (isFragmentActive) {
            bottomAppBar.getMenu().findItem(R.id.menu_bottom_delete_note).setVisible(true);
            bottomAppBar.getMenu().findItem(R.id.menu_bottom_save_note).setVisible(true);
        } else {
            bottomAppBar.getMenu().findItem(R.id.menu_bottom_delete_note).setVisible(false);
            bottomAppBar.getMenu().findItem(R.id.menu_bottom_save_note).setVisible(false);
        }
    }

    private void loadStates(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(KEY_NOTE);
            isUserSignedIn = savedInstanceState.getBoolean(KEY_USER_SIGN_STATUS, false);
        }
    }

    private void initSignInFragment(boolean addToBackStack) {
        navigation.clearBackStack();
        SignInFragment signInFragment = new SignInFragment();
        signInFragment.getLifecycle().addObserver(signInFragmentObserver);
        navigation.addFragment(
                isLandscape ? R.id.fragment_root_container : R.id.fragment_main_container,
                signInFragment, SignInFragment.TAG, addToBackStack
        );
    }

    private void initListFragment() {
        navigation.clearBackStack();
        NoteListFragment noteListFragment = new NoteListFragment();
        noteListFragment.getLifecycle().addObserver(noteListFragmentObserver);
        navigation.addFragment(
                R.id.fragment_main_container, noteListFragment,
                NoteListFragment.TAG, false
        );

        if (note != null) showNote(note);
    }

//    private void initLastFragment() {
//        navigation.clearBackStack();
//        if (lastFragment != null) {
//            navigation.moveFragmentToAnotherContainer(
//                    isLandscape ? R.id.fragment_root_container : R.id.fragment_main_container,
//                    lastFragment, SignInFragment.TAG, true
//            );
//        }
//    }
//
    private void initListImportantFragment() {
        Toast.makeText(this, R.string.important, Toast.LENGTH_SHORT).show();
    }

    private void initBottomAppBar() {
        bottomAppBar = findViewById(R.id.bottom_menu);
        bottomAppBar.setNavigationIcon(null);
        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_baseline_reply_24);
        fab.setOnClickListener(view -> onBackPressed());

        bottomAppBar.setNavigationOnClickListener(view -> {
            BottomNavigationDrawer bottomNavigationDrawer = new BottomNavigationDrawer();
            bottomNavigationDrawer.show(getSupportFragmentManager(), BottomNavigationDrawer.TAG);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_NOTE, note);
        outState.putBoolean(KEY_USER_SIGN_STATUS, isUserSignedIn);
    }

    @Override
    public void showReceivedNote(Note note) {
        showNote(note);
    }

    private void showNote(Note note) {
        navigation.clearBackStack();
        this.note = note;

        NoteFragment noteFragment = NoteFragment.newInstance(note);
        noteFragment.getLifecycle().addObserver(noteFragmentObserver);
        navigation.addFragment(
                isLandscape ? R.id.fragment_additional_container : R.id.fragment_main_container,
                noteFragment, NoteFragment.TAG, true
        );
    }

    @Override
    public void saveNote(Note note) {
        this.note = null;
        navigation.clearBackStack();
        NoteListFragment noteListFragment =
                (NoteListFragment) getFragmentByTag(NoteListFragment.TAG);

        ((CallbackContract) noteListFragment).saveNote(note);
    }

    @Override
    public void deleteNote(Note note) {
        this.note = null;
        navigation.clearBackStack();
        NoteListFragment noteListFragment =
                (NoteListFragment) getFragmentByTag(NoteListFragment.TAG);

        ((CallbackContract) noteListFragment).deleteNote(note);
    }

    private Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void onBackPressed() {
        OnBackPressedListener onBackPressedListener = null;
        List<Fragment> fragments = navigation.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof OnBackPressedListener) {
                onBackPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }
        if (onBackPressedListener != null) {
            onBackPressedListener.onBackPressed();
        } else {
            launchSuperBackPressed();
        }
    }

    public void launchSuperBackPressed() {
        NoteFragment noteFragment = (NoteFragment) getFragmentByTag(NoteFragment.TAG);
        if (noteFragment != null && noteFragment.isVisible()) {
            note = null;
        }

        if (isLandscape) {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount == 0 || (noteFragment != null && backStackEntryCount == 1)) {
                initListFragment();
            }
        }
        super.onBackPressed();
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
        } else if (idFragment == R.id.header_view) {
            initSignInFragment(true);
        }
    }

    private void initAboutFragment() {
        navigation.addFragment(
                isLandscape ? R.id.fragment_root_container : R.id.fragment_main_container,
                new AboutFragment(), null, true);
    }

    private void initSettingsFragment() {
        navigation.addFragment(
                isLandscape ? R.id.fragment_root_container : R.id.fragment_main_container,
                new SettingsFragment(), null, true);
    }

    @Override
    public void signedIn() {
        isUserSignedIn = true;
        initListFragment();
    }

    @Override
    public void signedOut() {
        isUserSignedIn = false;
        initSignInFragment(false);
    }
}