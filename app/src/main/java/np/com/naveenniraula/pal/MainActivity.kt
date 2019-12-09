package np.com.naveenniraula.pal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import np.com.naveenniraula.ghadi.Ghadi
import np.com.naveenniraula.ghadi.data.GhadiResult
import np.com.naveenniraula.ghadi.listeners.DatePickCompleteListener
import np.com.naveenniraula.ghadi.ui.GhadiPickerFragment

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
        Ghadi.Builder(supportFragmentManager)
            .fromEnglish(System.currentTimeMillis())
            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            .setForegroundColor(ContextCompat.getColor(this, android.R.color.white))
            .withCallback(object : DatePickCompleteListener {
                override fun onDateSelectionComplete(result: GhadiResult) {
                    englishDate.text = result.humanReadableAd
                    nepaliDate.text = result.humanReadableBs

                    Log.i("BQ7CH72", "$result")
                }

                override fun onDateSelectionCancelled(result: GhadiResult) {

                }
            }).build().show(supportFragmentManager, "tag")
    }

    private fun replaceFragment(fragment: GhadiPickerFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.amShowCalendar, fragment, fragment.tag).commit()
    }
}
