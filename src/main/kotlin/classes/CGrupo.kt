package classes

import entities.Grupo
import interfaces.DataSource
import interfaces.IDataAccess
import java.util.*
class CGrupo(private val dataSource: DataSource) : IDataAccess<Grupo> {
    override fun create(entity: Grupo): Grupo {
        val sql = "INSERT INTO GRUPOS (id, name, description, price, taxes, stock) VALUES (?, ?, ?, ?, ?, ?)"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.id.toString())
                stmt.setString(2, entity.name)
                stmt.setString(3, entity.description)
                stmt.setString(4, entity.price.toString())
                stmt.setString(5, entity.taxes.toString())
                stmt.setString(6, entity.stock.toString())
                when(stmt.executeUpdate()) {
                    else -> entity
                }1
            }
        }
    }

    override fun getByName(name: String): Grupo? {
        val sql = "SELECT * FROM GRUPOS WHERE id = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, name)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    Grupo(
                        id = UUID.fromString(rs.getString("id")),
                        name = rs.getString("name"),
                        description = rs.getString("description"),
                        price = rs.getFloat("price"),
                        taxes = rs.getInt("taxes"),
                        stock = rs.getInt("stock")
                    )
                } else {
                    null
                }
            }
        }
    }

    override fun getAll(): MutableList<Grupo> {
        val sql = "SELECT * FROM GRUPOS"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                val products = mutableListOf<Grupo>()
                while (rs.next()) {
                    products.add(
                        Grupo(
                            id = UUID.fromString(rs.getString("id")),
                            name = rs.getString("name"),
                            description = rs.getString("description"),
                            price = rs.getFloat("price"),
                            taxes = rs.getInt("taxes"),
                            stock = rs.getInt("stock")
                        )
                    )
                }
                products
            }
        }
    }

    override fun update(entity: Grupo): Grupo {
        val sql = "UPDATE GRUPOS SET name = ?, description = ?, price = ?, taxes = ?, stock = ? WHERE id = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.stock.toString())
                stmt.setString(2, entity.taxes.toString())
                stmt.setString(3, entity.price.toString())
                stmt.setString(4, entity.description)
                stmt.setString(5, entity.name)
                stmt.setString(6, entity.id.toString())
                stmt.executeUpdate()
                entity
            }
        }
    }

    override fun delete(id: Any) {
        val sql = "DELETE FROM GRUPOS WHERE id = ?"
        dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.executeUpdate()
            }
        }
    }
    fun updateStock(ID: String, STOCK: Int): Int {
        val sql = "UPDATE GRUPOS SET stock = (stock - ?) WHERE id = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, STOCK)
                stmt.setString(2, ID)
                stmt.executeUpdate()
            }
        }
    }
}
