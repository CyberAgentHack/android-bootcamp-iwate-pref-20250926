package com.example.androidbootcampiwatepref.data.api

import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class NewApiImpl : NewApi {
    override suspend fun getNews(): NewsResponse {
        val json = """
            {
                "news": [
                    {
                        "title": "title1",
                        "body": "body1"
                    },
                    {
                        "title": "title2",
                        "body": "body2"
                    }
                ]
            }
        """.trimIndent()

        // 実際のAPIリクエストのように数秒間待機する
        delay(2_000)
        return Json.decodeFromString<NewsResponse>(json)
    }
}