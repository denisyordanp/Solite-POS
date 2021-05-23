package com.socialite.solite_pos.data.source.local.entity.room.master

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.google.firebase.firestore.QuerySnapshot
import com.socialite.solite_pos.data.source.remote.response.helper.RemoteClassUtils
import java.io.Serializable

@Entity(
		tableName = User.DB_NAME,
		primaryKeys = [User.ID],
		indices = [
			Index(value = [User.ID])
		]
)
data class User(
		@ColumnInfo(name = ID)
		var id: String,

		@ColumnInfo(name = NAME)
		var name: String,

		@ColumnInfo(name = EMAIL)
		var email: String,

		@ColumnInfo(name = AUTHORITY)
		var authority: String,
) : Serializable {
	companion object : RemoteClassUtils<User> {
		const val ID = "id_user"
		const val AUTHORITY = "authority"
		const val NAME = "name"
		const val EMAIL = "email"

		private const val CASHIER = "cashier"
		const val ADMIN = "admin"

		const val DB_NAME = "user"

		fun isNotAdmin(authority: String?): Boolean {
			return authority == CASHIER
		}

		override fun toHashMap(data: User): HashMap<String, Any?> {
			return hashMapOf(
					ID to data.id,
					NAME to data.name,
					EMAIL to data.email,
					AUTHORITY to data.authority
			)
		}

		override fun toListClass(result: QuerySnapshot): List<User> {
			val array: ArrayList<User> = ArrayList()
			for (document in result) {
				val user = User(
						document.data[ID] as String,
						document.data[NAME] as String,
						document.data[EMAIL] as String,
						document.data[AUTHORITY] as String
				)
				array.add(user)
			}
			return array
		}
	}
}
