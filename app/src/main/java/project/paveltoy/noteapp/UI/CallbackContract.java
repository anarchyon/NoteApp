package project.paveltoy.noteapp.UI;

import project.paveltoy.noteapp.Data.Note;

public interface CallbackContract {
    void saveNote(Note note);

    void deleteNote(Note note);
}
