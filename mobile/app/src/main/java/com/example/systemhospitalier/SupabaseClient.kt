package com.example.systemhospitalier

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://rogfplheaoaibrumrgla.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJvZ2ZwbGhlYW9haWJydW1yZ2xhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU5NzkzMDksImV4cCI6MjA5MTU1NTMwOX0.047PKNoMZmJ09i7XXvpy9_ltY8e90Gx-wHwHKcY3Lzs"
    ) {
        install(Postgrest)
    }
}