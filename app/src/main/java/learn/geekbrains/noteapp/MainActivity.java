package learn.geekbrains.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListNotesFragment.Controller {
    public static final String KEY_NOTE = "key_note";
    private Note note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(KEY_NOTE);
        }

        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new ListNotesFragment())
                .commit();

        if (note != null) showNote();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (note != null) {
            outState.putParcelable(KEY_NOTE, note);
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
}