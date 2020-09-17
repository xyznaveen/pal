package np.com.naveenniraula.ghadi.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class UiProperty : BaseObservable() {

    @get:Bindable()
    @set:Bindable()
    var isCalculating: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.calculating)
        }

    @get:Bindable()
    @set:Bindable()
    var isBs: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.bs)
        }

    @get:Bindable()
    @set:Bindable()
    var bsYear: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bsYear)
        }

    @get:Bindable()
    @set:Bindable()
    var adYear: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.adYear)
        }

    @get:Bindable()
    @set:Bindable()
    var bsMonth: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bsMonth)
        }

    @get:Bindable()
    @set:Bindable()
    var adMonth: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.adMonth)
        }

}