package np.com.naveenniraula.pal

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import np.com.naveenniraula.ghadi.Pal
import np.com.naveenniraula.ghadi.data.GhadiResult
import np.com.naveenniraula.ghadi.listeners.DatePickCompleteListener
import np.com.naveenniraula.ghadi.ui.CalendarDialogFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showPicker()

        amShowCalendar.setOnClickListener {
            showPicker()
        }
    }

    private fun showPicker() {
        // gf.show(supportFragmentManager, gf.tag)
        Pal.Builder(supportFragmentManager)
            .fromEnglish(System.currentTimeMillis())
            .setBackgroundColor(Color.parseColor("#0098DA"))
            .setForegroundColor(ContextCompat.getColor(this, android.R.color.white))
            .withCallback(object : DatePickCompleteListener {
                override fun onDateSelectionComplete(result: GhadiResult) {
                    englishDate.text = result.humanReadableAd
                    nepaliDate.text = result.humanReadableBs

                    // point to day one
                    // result.pointToDayOne()
                    Log.d("asdasdjhasd", "$result")
                }

                override fun onDateSelectionCancelled(result: GhadiResult) {

                }
            }).build().show(supportFragmentManager, "tag")
    }

    private fun replaceFragment(fragment: CalendarDialogFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.amShowCalendar, fragment, fragment.tag).commit()
    }
}
