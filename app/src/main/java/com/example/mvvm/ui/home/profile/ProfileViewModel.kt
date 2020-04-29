package com.example.mvvm.ui.home.profile

import androidx.lifecycle.ViewModel;
import com.example.mvvm.data.repositories.UserRepository

class ProfileViewModel(
    repository: UserRepository
) : ViewModel() {

    val user = repository.getUser()

}
