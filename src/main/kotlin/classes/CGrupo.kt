package classes

import entities.Grupo
import interfaces.DataSource
import interfaces.IDataAccess
import java.util.*
class CGrupo(private val dataSource: DataSource) : IDataAccess<Grupo> {
    override fun create(entity: Grupo): Grupo {
        val sql = "INSERT INTO GRUPOS (GRUPOID, GRUPODESC, MEJORPOSCTFID) VALUES (?, ?, ?)"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.grupoid.toString())
                stmt.setString(2, entity.mejorCtfId.toString())
                when(stmt.executeUpdate()) {
                    else -> entity
                }
            }
        }
    }

    override fun getByName(name: String): Grupo? {
        val sql = "SELECT * FROM GRUPOS WHERE GRUPOID = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, name)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    Grupo(
                        grupoid = rs.getInt("GRUPOID"),
                        mejorCtfId = rs.getInt("MEJORPOSCTFID")
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
                            grupoid = rs.getInt("GRUPOID"),
                            mejorCtfId = rs.getInt("MEJORPOSCTFID")
                        )
                    )
                }
                products
            }
        }
    }

    override fun update(entity: Grupo): Grupo {
        val sql = "UPDATE GRUPOS SET GRUPODESC = ?, MEJORPOSCTFID = ? WHERE GRUPOID = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.grupoid.toString())
                stmt.setString(2, entity.mejorCtfId.toString())
                stmt.executeUpdate()
                entity
            }
        }
    }

    override fun delete(id: Any) {
        val sql = "DELETE FROM GRUPOS WHERE GRUPOID = ?"
        dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.executeUpdate()
            }
        }
    }
}
