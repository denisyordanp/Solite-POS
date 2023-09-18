package com.socialite.domain.domain.impl

import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.IsUserStaff
import com.socialite.domain.menu.UserAuthority
import com.socialite.domain.menu.toAuthority
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsUserStaffImpl @Inject constructor(
    private val userRepository: UserRepository
) : IsUserStaff {
    override fun invoke() = userRepository.getLoggedInUser().map {
        it?.let {
            it.authority.toAuthority() == UserAuthority.STAFF
        } ?: false
    }
}