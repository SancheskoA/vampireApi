package com.example.models


import com.example.models.Product
import com.example.db.ProductDAO
import com.example.db.ProductTable
import com.example.db.daoToModel
import com.example.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresProductRepository : ProductRepository {
     override suspend fun allProducts(): List<Product> = suspendTransaction {
        ProductDAO.all().map(::daoToModel)
    }

    override suspend fun productById(id: Int): Product? = suspendTransaction {
        ProductDAO
            .find({ ProductTable.id eq id})
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun productByName(name: String): Product? = suspendTransaction {
        ProductDAO
            .find { (ProductTable.name eq name) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addProduct(product: NewProduct ): Unit = suspendTransaction {
        ProductDAO.new {
            name = product.name
            quantity = product.quantity
            brand = product.brand
        }
    }

    override suspend fun updateProduct(id: Int, product: NewProduct): Unit = suspendTransaction {
        ProductDAO.findByIdAndUpdate(id) {
            it.name = product.name
            it.quantity = product.quantity
            it.brand = product.brand
        }

    }

     override suspend fun removeProduct(id: Int): Unit = suspendTransaction {
         ProductTable.deleteWhere { ProductTable.id.eq(id) }

    }
}