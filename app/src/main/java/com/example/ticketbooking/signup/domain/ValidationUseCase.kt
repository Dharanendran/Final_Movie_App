package com.example.ticketbooking.signup.domain

object ValidationUseCase{

    fun isEmailValid(email:String):Boolean
    {
        val validEmailString = "[A-Za-z\\d._%+-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,}"
        return validEmailString.toRegex().matches(email.trim())
    }

    fun isPhoneNumberValid(number:String):Boolean
    {
        fun isAnyAlphaContains(number:String):Boolean
        {
            number.forEach {
                if(!it.isDigit())
                    return false
            }
            return true
        }

        return number.length == 10 && isAnyAlphaContains(number) && 6 <= number[0].toInt()
    }

    fun isPasswordStrong(password: String):Boolean
    {
        var isContainsUpper = false
        var isContainsDigit = false
        var isContainsSpecial = false

        password.forEach {
            if(it.isDigit())
                isContainsDigit = true
            if(it.isUpperCase())
                isContainsUpper = true
            if(!(it.isLetter() || it.isDigit()))
                isContainsSpecial = true
        }

        if(password.length>=8 && isContainsUpper && isContainsDigit && isContainsSpecial)
            return true
        return false


    }

}