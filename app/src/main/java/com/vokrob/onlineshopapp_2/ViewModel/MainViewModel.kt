package com.vokrob.onlineshopapp_2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vokrob.onlineshopapp_2.Model.SliderModel
import com.vokrob.onlineshopapp_2.Repository.MainRepository

class MainViewModel() : ViewModel() {
    private val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        return repository.loadBanner()
    }
}
























