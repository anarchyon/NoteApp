package project.paveltoy.noteapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note(
        var id: String?,
        var subject: String?,
        var text: String?,
        var creationDate: Date,
        var isImportant: Int
) : Parcelable {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val note = o as Note
        return id == note.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    companion object {
        const val NOTE_IMPORTANT = 1
        const val NOTE_NOT_IMPORTANT = 0

        fun getCurrentDate(): Date {
            return Calendar.getInstance().time
        }

        fun generateId(): String {
            return UUID.randomUUID().toString()
        }
    }
}