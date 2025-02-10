package com.example.models


import com.example.models.User
import com.example.db.UserDAO
import com.example.db.UserTable
import com.example.db.daoToModelUser
import com.example.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresUserRepository : UserRepository {
     override suspend fun allUsers(): List<User> = suspendTransaction {
         UserDAO.all().map(::daoToModelUser)
    }

    override suspend fun userById(id: Int): User? = suspendTransaction {
        UserDAO
            .find({ UserTable.id eq id})
            .limit(1)
            .map(::daoToModelUser)
            .firstOrNull()
    }

    override suspend fun userByUserName(username: String): User? = suspendTransaction {
        UserDAO
            .find { (UserTable.username eq username) }
            .limit(1)
            .map(::daoToModelUser)
            .firstOrNull()
    }

    override suspend fun userByInvitationCode(invitationCode: String): User? = suspendTransaction {
        UserDAO
            .find { (UserTable.invitationCode eq invitationCode) }
            .limit(1)
            .map(::daoToModelUser)
            .firstOrNull()
    }

    override suspend fun addUser(user: NewUser ): User = suspendTransaction {
        UserDAO.new {
            username = user.username
            password = user.password
            role = user.role
            inviterUserId = user.inviterUserId
            invitationCode = user.invitationCode
            kpi = 0
        }.toUser()
    }

    override suspend fun updateUser(id: Int, user: User): Unit = suspendTransaction {
        UserDAO.findByIdAndUpdate(id) {
            it.username = user.username
            it.password = user.password
            it.role = user.role
            it.inviterUserId = user.inviterUserId
            it.invitationCode = user.invitationCode
            it.kpi = user.kpi
        }

    }

     override suspend fun removeUser(id: Int): Unit = suspendTransaction {
         UserTable.deleteWhere { UserTable.id.eq(id) }
    }
}