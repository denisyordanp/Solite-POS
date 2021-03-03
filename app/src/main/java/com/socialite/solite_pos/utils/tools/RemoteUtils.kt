package com.socialite.solite_pos.utils.tools

import com.google.firebase.firestore.QuerySnapshot

interface RemoteUtils<T> {
    fun toHashMap(data: T): HashMap<String, Any?>
    fun toListClass(result: QuerySnapshot): List<T>
}