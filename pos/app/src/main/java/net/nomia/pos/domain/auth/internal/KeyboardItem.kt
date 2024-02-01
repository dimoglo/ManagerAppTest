package net.nomia.pos.domain.auth.internal

sealed interface KeyboardItem {

    sealed interface Digit : KeyboardItem {
        val value: Int

        object Zero : Digit {
            override val value: Int
                get() = 0
        }

        object One : Digit {
            override val value: Int
                get() = 1
        }

        object Two : Digit {
            override val value: Int
                get() = 2
        }

        object Three : Digit {
            override val value: Int
                get() = 3
        }

        object Four : Digit {
            override val value: Int
                get() = 4
        }

        object Five : Digit {
            override val value: Int
                get() = 5
        }

        object Six : Digit {
            override val value: Int
                get() = 6
        }

        object Seven : Digit {
            override val value: Int
                get() = 7
        }

        object Eight : Digit {
            override val value: Int
                get() = 8
        }

        object Nine : Digit {
            override val value: Int
                get() = 9
        }
    }

    object LogOut : KeyboardItem

    object Erase : KeyboardItem
}
