package ch.heigvd.daa.labo4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotes
import ch.heigvd.daa.labo4.viewmodels.ViewModelNotesFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}