package np.com.naveenniraula.ghadi.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.library.baseAdapters.BR

class UiProperty : BaseObservable() {

    @get:Bindable()
    @set:Bindable()
    var isBs: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.bs)
        }

    

}