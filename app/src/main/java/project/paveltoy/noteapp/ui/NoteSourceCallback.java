package project.paveltoy.noteapp.ui;

import project.paveltoy.noteapp.data.NoteRepository;

public interface NoteSourceCallback {
    void initialized(NoteRepository notes);
}
