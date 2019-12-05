package np.com.naveenniraula.ghadi.utils

import np.com.naveenniraula.ghadi.miti.DateUtils

object ConversionUtil {

    fun toNepali(value: String): String? {
        val stringBuilder = StringBuilder()
        for (c in value.toCharArray()) {
            val num = (c.toString()).toInt()
            stringBuilder.append(DateUtils.NUMBER_NEP[num])
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