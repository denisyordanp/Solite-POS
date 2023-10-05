package com.socialite.domain.domain.impl

import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.GetStoreMenus
import com.socialite.domain.menu.UserAuthority
import com.socialite.domain.menu.toAuthority
import com.socialite.schema.menu.StoreMenus
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetStoreMenusImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetStoreMenus {
    override fun invoke() = userRepository.getLoggedInUser().map { userData ->
        if (userData != null) {
            val storeMenus = StoreMenus.values()
            when (userData.authority.toAuthority()) {
                UserAuthority.OWNER -> storeMenus.toList()
                UserAuthority.ADMIN -> storeMenus.filterNot {
                    it == StoreMenus.STORE_USER
                }.toList()

                UserAuthority.STAFF -> storeMenus.filterNot {
                    it == StoreMenus.PAYMENT ||
                            it == StoreMenus.STORE_USER
                }.toList()
            }
        } else {
            emptyList()
        }
    }
}