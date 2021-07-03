package project.paveltoy.noteapp.UI;

import project.paveltoy.noteapp.Data.NoteRepository;

public interface NoteSourceCallback {
    void initialized(NoteRepository notes);
}
