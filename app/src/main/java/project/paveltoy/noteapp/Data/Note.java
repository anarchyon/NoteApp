package project.paveltoy.noteapp.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Note implements Parcelable {
    public static final int NOTE_IMPORTANT = 1;
    public static final int NOTE_NOT_IMPORTANT = 0;

    private String id;
    private String subject;
    private String text;
    private Date creationDate;
    private int isImportant;

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }
    public static String generateNewId() {
        return UUID.randomUUID().toString();
    }

    public Note(String id, String subject, String text, Date creationDate, int isImportant) {
        this.id = id;
        this.subject = subject;
        this.text = text;
        this.creationDate = creationDate;
        this.isImportant = isImportant;
    }

    protected Note(Parcel in) {
        subject = in.readString();
        text = in.readString();
        creationDate = new Date(in.readLong());
        id = in.readString();
        isImportant = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getId() {
        return id;
    }

    public int getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(int isImportant) {
        this.isImportant = isImportant;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getSubject());
        parcel.writeString(getText());
        parcel.writeLong(getCreationDate().getTime());
        parcel.writeString(getId());
        parcel.writeInt(getIsImportant());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id.equals(note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(String id) {
        this.id = id;
    }
}
