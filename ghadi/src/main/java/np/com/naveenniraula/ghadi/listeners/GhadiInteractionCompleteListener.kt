package np.com.naveenniraula.ghadi.listeners

import np.com.naveenniraula.ghadi.data.GhadiResult

interface GhadiInteractionCompleteListener {
    fun onDateSelectionComplete(result: GhadiResult)
    fun onDateSelectionCancelled(result: GhadiResult)
}