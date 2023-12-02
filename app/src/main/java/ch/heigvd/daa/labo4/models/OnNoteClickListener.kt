package ch.heigvd.daa.labo4.models

import ch.heigvd.daa.labo4.models.NoteAndSchedule

interface OnNoteClickListener {
    fun onNoteClicked(noteAndSchedule: NoteAndSchedule)
}