package com.example.androidbootcampiwatepref.data.api

interface NewApi {

    suspend fun getNews(): NewsResponse
}