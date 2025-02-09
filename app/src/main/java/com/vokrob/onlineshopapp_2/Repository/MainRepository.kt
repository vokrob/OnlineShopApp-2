package com.vokrob.onlineshopapp_2.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.vokrob.onlineshopapp_2.Model.CategoryModel
import com.vokrob.onlineshopapp_2.Model.ItemsModel
import com.vokrob.onlineshopapp_2.Model.SliderModel

class MainRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        val listData = MutableLiveData<MutableList<SliderModel>>()
        val ref = firebaseDatabase.getReference("Banner")

        ref.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lists = mutableListOf<SliderModel>()

                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(SliderModel::class.java)
                        item?.let { lists.add(it) }
                    }

                    listData.value = lists
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )

        return listData
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        val listData = MutableLiveData<MutableList<CategoryModel>>()
        val ref = firebaseDatabase.getReference("Category")

        ref.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lists = mutableListOf<CategoryModel>()

                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(CategoryModel::class.java)
                        item?.let { lists.add(it) }
                    }

                    listData.value = lists
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )

        return listData
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")
        val query: Query = ref.orderByChild("showRecommended").equalTo(true)

        query.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lists = mutableListOf<ItemsModel>()

                    for (childSnapshot in snapshot.children) {
                        val list = childSnapshot.getValue(ItemsModel::class.java)
                        if (list != null) lists.add(list)
                    }

                    listData.value = lists
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )

        return listData
    }
}























