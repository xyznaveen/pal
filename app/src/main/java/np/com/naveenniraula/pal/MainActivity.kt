package np.com.naveenniraula.pal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import np.com.naveenniraula.ghadi.ui.GhadiPickerFragment

class MainActivity : AppCompatActivity() {

    private val gf = GhadiPickerFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
