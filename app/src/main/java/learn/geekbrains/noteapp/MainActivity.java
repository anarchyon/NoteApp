package learn.geekbrains.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListNotesFragment.Controller {
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new ListNotesFragment())
                .commit();

        if (note != null) showNote();

        super.onStart();
    }

    @Override
    public void getNote(Note note) {
        this.note = note;
        showNote();
    }

    private void showNote() {
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int fragmentIdForNote = isLandscape ? R.id.fragment_note_details : R.id.fragment_container;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragmentIdForNote, NoteFragment.newInstance(note))
                .addToBackStack(null)
                .commit();
    }
}