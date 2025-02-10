package com.vokrob.onlineshopapp_2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vokrob.onlineshopapp_2.Model.CategoryModel
import com.vokrob.onlineshopapp_2.Model.ItemsModel
import com.vokrob.onlineshopapp_2.Model.SliderModel
import com.vokrob.onlineshopapp_2.Repository.MainRepository

class MainViewModel() : ViewModel() {
    private val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    fun loadFiltered(id: String): LiveData<MutableList<ItemsModel>> {
        return repository.loadFiltered(id)
    }
}
























