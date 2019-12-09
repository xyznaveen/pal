package np.com.naveenniraula.ghadi.utils

import np.com.naveenniraula.ghadi.miti.DateUtils
import java.lang.NumberFormatException

object ConversionUtil {

    fun toNepali(value: String): String? {
        val stringBuilder = StringBuilder()
        for (c in value.toCharArray()) {
            try {
                val num = (c.toString()).toInt()
                stringBuilder.append(DateUtils.NUMBER_NEP[num])
            } catch (ex: NumberFormatException) {
                stringBuilder.append(c)
            }
        }
        return stringBuilder.toString()
    }

    fun toEnglish(value: String): String? {
        val stringBuilder = StringBuilder()
        for (c in value.toCharArray()) {
            stringBuilder.append(DateUtils.NUMBER_ENG[c.toString()])
        }
        return stringBuilder.toString()
    }

}