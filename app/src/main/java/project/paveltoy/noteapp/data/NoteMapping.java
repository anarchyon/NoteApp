package project.paveltoy.noteapp.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteMapping {

    public static Note parseFirestoreDocumentToNote(String id, Map<String, Object> document) {
        Timestamp timestamp = (Timestamp) document.get(Fields.CREATION_DATE);
        boolean isImportant = (boolean) document.get(Fields.IMPORTANT);
        return new Note(
                id,
                (String) document.get(Fields.SUBJECT),
                (String) document.get(Fields.TEXT),
                timestamp.toDate(),
                isImportant ? Note.NOTE_IMPORTANT : Note.NOTE_NOT_IMPORTANT
        );
    }

    public static Map<String, Object> exportNoteToFirestoreDocument(Note note) {
        Map<String, Object> document = new HashMap<>();
        document.put(Fields.CREATION_DATE, note.getCreationDate());
        document.put(Fields.SUBJECT, note.getSubject());
        document.put(Fields.TEXT, note.getText());
        boolean isImportant = note.isImportant() != 0;
        document.put(Fields.IMPORTANT, isImportant);
        return document;
    }

    public static class Fields {
        public static final String CREATION_DATE = "creation_date";
        public static final String SUBJECT = "subject";
        public static final String TEXT = "text";
        public static final String IMPORTANT = "important";
    }
}
