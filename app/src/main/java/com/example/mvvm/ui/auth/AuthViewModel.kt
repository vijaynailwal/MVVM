package com.example.mvvm.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.mvvm.R
import com.example.mvvm.data.repositories.UserRepository
import com.example.mvvm.util.ApiException
import com.example.mvvm.util.Coroutines
import com.example.mvvm.util.NoInternetException
import com.example.mvvm.util.toast


class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var email: String? = null //"probelalkhan@gmail.com"//null
    var password: String? = null//"123456"//null
    var authListener: AuthListener? = null

    var name: String? = null
    var passwordconfirm: String? = null


    fun getLoggedInUser() = repository.getUser()

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure(
                view.context.toast(view.context.resources.getString(R.string.invalid_email_password))
                    .toString()
            )//Invalid email or password
            return
        }

        Coroutines.main {
            try {
                val authResponse = repository.userLogin(email!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e: ApiException) {
                authListener?.onFailure(view.context.toast(e.message!!).toString())

//                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
//                authListener?.onFailure(e.message!!)
                authListener?.onFailure(view.context.toast(e.message!!).toString())

            }
        }

    }

    fun onSignupButtonClick(view: View) {
        authListener?.onStarted()

        if (name.isNullOrEmpty()) {
            authListener?.onFailure(
                view.context.toast(view.context.resources.getString(R.string.name_required))
                    .toString()
            )
            return
        }

        if (email.isNullOrEmpty()) {
            authListener?.onFailure(
                view.context.toast(view.context.resources.getString(R.string.email_required))
                    .toString()
            )
            return
        }
        if (password.isNullOrEmpty()) {
            authListener?.onFailure(
                view.context.toast(view.context.resources.getString(R.string.enter_password))
                    .toString()
            )
            return
        }

        if (password != passwordconfirm) {

            authListener?.onFailure(
                view.context.toast(view.context.resources.getString(R.string.password_not_match))
                    .toString()
            )

            return
        }


        Coroutines.main {
            try {
                val authResponse = repository.userSignup(name!!, email!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }

    }


    //navigate
    fun onLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun onSignup(view: View) {
        Intent(view.context, SignupActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

}