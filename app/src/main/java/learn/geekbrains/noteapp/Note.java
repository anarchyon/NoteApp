package learn.geekbrains.noteapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class Note implements Parcelable {
    private String id;
    private String name;
    private String text;
    private String creationDate;

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }
    public static String generateNewId() {
        return UUID.randomUUID().toString();
    }

    public Note(String id, String name, String text, String creationDate) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.creationDate = creationDate;
    }

    protected Note(Parcel in) {
        name = in.readString();
        text = in.readString();
        creationDate = in.readString();
        id = in.readString();
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

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
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
        parcel.writeString(getName());
        parcel.writeString(getText());
        parcel.writeString(getCreationDate());
        parcel.writeString(getId());
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
}
