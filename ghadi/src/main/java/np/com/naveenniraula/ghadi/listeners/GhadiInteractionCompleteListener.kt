package np.com.naveenniraula.ghadi.listeners

import np.com.naveenniraula.ghadi.miti.Date

interface GhadiInteractionCompleteListener {
    fun onDateSelectionComplete(date: Date)
    fun onDateSelectionCancelled(date: Date)
}