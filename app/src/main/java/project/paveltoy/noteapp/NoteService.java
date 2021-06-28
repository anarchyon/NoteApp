package project.paveltoy.noteapp;

public interface NoteService {
    NoteService init(NoteSourceCallback noteSourceCallback);

    void deleteNote(int position);

    void deleteNote(Note note);

    void editNote(int position, Note note);

    void editNote(Note note);

    void addNote(Note newNote);

    Note getNote(int position);

    int size();
}
