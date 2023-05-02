package classes

import entities.Ctf
import interfaces.DataSource
import interfaces.IDataAccess

class CCtf(private val dataSource: DataSource) : IDataAccess<Ctf> {
    override fun create(entity: Ctf): Ctf {
        val sql = "INSERT INTO CTFS (dni, name, phone, mail) VALUES (?, ?, ?, ?)"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.dni)
                stmt.setString(2, entity.name)
                stmt.setString(3, entity.phone.toString())
                stmt.setString(4, entity.mail)
                when(stmt.executeUpdate()) {
                    else -> entity
                }
            }
        }
    }

    override fun getByName(name: String): Ctf? {
        val sql = "SELECT * FROM CTFS WHERE dni = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(2, name)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    Ctf(
                        dni = rs.getString("dni"),
                        name = rs.getString("name"),
                        phone = rs.getInt("phone"),
                        mail = rs.getString("mail")
                    )
                } else {
                    null
                }
            }
        }
    }

    override fun getAll(): MutableList<Ctf> {
        val sql = "SELECT * FROM CTFS"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                val users = mutableListOf<Ctf>()
                while (rs.next()) {
                    users.add(
                        Ctf(
                            dni = rs.getString("dni"),
                            name = rs.getString("name"),
                            phone = rs.getInt("phone"),
                            mail = rs.getString("mail")
                        )
                    )
                }
                users
            }
        }
    }

    override fun update(entity: Ctf): Ctf {
        val sql = "UPDATE CTFS SET name = ?, phone = ?, mail = ? WHERE dni = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.mail)
                stmt.setString(2, entity.phone.toString())
                stmt.setString(3, entity.name)
                stmt.setString(4, entity.dni)
                stmt.executeUpdate()
                entity
            }
        }
    }

    override fun delete(id: Any) {
        val sql = "DELETE FROM CTFS WHERE dni = ?"
        dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.executeUpdate()
            }
        }
    }
}
