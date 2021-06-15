package learn.geekbrains.noteapp;

import java.util.ArrayList;
import java.util.List;

public class TemporaryClassNotes {
    public static final String HEADER = "Заметка";
    public static final String TEXT = "Текст";

    public static List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        Note firstNote = new Note(
                "Добро пожаловать!",
                "Приветствуем Вас в программе бла-бла-бла, тут можно бла-бла-бла"
        );
        notes.add(firstNote);
        return notes;
    }
}
