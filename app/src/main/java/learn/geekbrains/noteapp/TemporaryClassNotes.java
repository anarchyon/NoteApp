package learn.geekbrains.noteapp;

import java.util.ArrayList;
import java.util.List;

public class TemporaryClassNotes {
    public static final String HEADER = "Заметка";
    public static final String TEXT = "Текст";

    public static List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = HEADER + (i + 1);
            String text = TEXT + (i + 1);
            notes.add(new Note(name, text));
        }
        return notes;
    }
}
