package np.com.naveenniraula.pal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import np.com.naveenniraula.ghadi.data.GhadiResult
import np.com.naveenniraula.ghadi.listeners.DatePickCompleteListener
import np.com.naveenniraula.ghadi.ui.GhadiPickerFragment

class MainActivity : AppCompatActivity() {

    private val gf = GhadiPickerFragment.newInstance(2075,12,17)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gf.setDatePickCompleteListener(object : DatePickCompleteListener {
            override fun onDateSelectionComplete(result: GhadiResult) {
                englishDate.text = result.humanReadableAd
                nepaliDate.text = result.humanReadableBs

                Log.i("BQ7CH72", "$result")
            }

            override fun onDateSelectionCancelled(result: GhadiResult) {
                //TODO handle date picking cancel
            }
        })

        showPicker()

        amShowCalendar.setOnClickListener {
            showPicker()
        }
    }

    private fun showPicker() {
        gf.show(supportFragmentManager, gf.tag)
    }

    private fun replaceFragment(fragment: GhadiPickerFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.amShowCalendar, fragment, fragment.tag).commit()
    }
}
