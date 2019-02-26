package np.com.naveenniraula.ghadi

import java.util.*

class Ghadi {



    internal class GhadiBuilder {

        private val ghadi by lazy { Ghadi() }

        fun withDate(date: Calendar): GhadiBuilder {
            TODO("Implement date setting mechanism.")
            return this
        }

        fun withTitle(title: String): GhadiBuilder {
            TODO("Set title")
            return this
        }

        fun withTitle(titleResId: Int): GhadiBuilder {
            TODO("Set title.")
            return this
        }

        fun withOkButton(titleResId: Int): GhadiBuilder {
            TODO("Set positive action.")
            return this
        }

        fun withOkButton(okText: String): GhadiBuilder {
            TODO("Set positive action.")
            return this
        }

        fun withCancelButton(titleResId: Int): GhadiBuilder {
            TODO("Set negative action.")
            return this
        }

        fun withCancelButton(okText: String): GhadiBuilder {
            TODO("Set negative action.")
            return this
        }

        fun withSelectionCompleteListener(): GhadiBuilder {
            TODO("Set interaction complete listener.")
            return this
        }

        fun create(): Ghadi {
            return ghadi
        }

    }

}