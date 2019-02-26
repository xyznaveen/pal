package np.com.naveenniraula.pal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import np.com.naveenniraula.ghadi.ui.GhadiPickerFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gf = GhadiPickerFragment.newInstance()
        gf.show(supportFragmentManager, gf.tag)
    }

    private fun replaceFragment(fragment: GhadiPickerFragment) {

        supportFragmentManager.beginTransaction().replace(R.id.amFragmentContent, fragment, fragment.tag).commit()
    }
}
