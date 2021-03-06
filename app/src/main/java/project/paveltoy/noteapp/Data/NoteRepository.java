package project.paveltoy.noteapp.Data;

import project.paveltoy.noteapp.UI.NoteSourceCallback;

public interface NoteRepository {
    NoteRepository init(NoteSourceCallback noteSourceCallback);

    void deleteNote(int position);

    void deleteNote(Note note);

    void editNote(int position, Note note);

    void editNote(Note note);

    void addNote(Note newNote);

    Note getNote(int position);

    int size();

    int getPosition(Note note);

    void insertNote(int position, Note note);
}
