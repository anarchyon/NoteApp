package project.paveltoy.noteapp.ui;

import project.paveltoy.noteapp.data.Note;

public interface CallbackContract {
    void saveNote(Note note);

    void deleteNote(Note note);
}
