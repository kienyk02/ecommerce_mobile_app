package com.example.ecomapp.network

import android.content.Context

object JwtManager {
    private const val PREF_NAME = "jwt_pref"
    private const val KEY_JWT_TOKEN = "jwt_token"
    private const val KEY_USER_ID = "user_id"
    var CURRENT_JWT = ""
    var CURRENT_USER = ""

    // Lưu trữ JWT vào SharedPreferences
    fun saveJwtToken(context: Context, jwtToken: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_JWT_TOKEN, jwtToken)
        editor.apply()
    }

    fun removeJwtToken(context: Context){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_JWT_TOKEN)
        editor.apply()
    }

    // Lấy JWT từ SharedPreferences
    fun getJwtToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_JWT_TOKEN, "none").toString()
    }

    fun saveUserId(context: Context, userId: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }

    fun getUserId(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_ID, "none").toString()
    }

    fun removeUserId(context: Context){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_USER_ID)
        editor.apply()
    }

}
