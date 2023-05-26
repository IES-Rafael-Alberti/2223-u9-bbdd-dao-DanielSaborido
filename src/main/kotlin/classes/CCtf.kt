package classes

import entities.Ctf
import interfaces.DataSource
import interfaces.IDataAccess

class CCtf(private val dataSource: DataSource) : IDataAccess<Ctf> {
    override fun create(entity: Ctf): Ctf {
        val sql = "INSERT INTO CTFS (CTFID, GRUPOID, PUNTUACION) VALUES (?, ?, ?)"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.id.toString())
                stmt.setString(2, entity.grupoId.toString())
                stmt.setString(3, entity.puntuacion.toString())
                when(stmt.executeUpdate()) {
                    else -> entity
                }
            }
        }
    }

    override fun selectById(id: Int): Ctf? {
        val sql = "SELECT * FROM CTFS WHERE CTFID = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    Ctf(
                        id = rs.getInt("CTFID"),
                        grupoId = rs.getInt("GRUPOID"),
                        puntuacion = rs.getInt("PUNTUACION")
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
                            id = rs.getInt("CTFID"),
                            grupoId = rs.getInt("GRUPOID"),
                            puntuacion = rs.getInt("PUNTUACION")
                        )
                    )
                }
                users
            }
        }
    }

    override fun update(entity: Ctf): Ctf {
        val sql = "UPDATE CTFS SET GRUPOID = ?, PUNTUACION = ? WHERE CTFID = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, entity.puntuacion.toString())
                stmt.setString(2, entity.grupoId.toString())
                stmt.setString(3, entity.id.toString())
                stmt.executeUpdate()
                entity
            }
        }
    }

    override fun delete(id: Int) {
        val sql = "DELETE FROM CTFS WHERE CTFID = ?"
        dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.executeUpdate()
            }
        }
    }

    fun deleteGrupo(id: Int) {
        val sql = "DELETE FROM CTFS WHERE GRUPOID = ?"
        dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.executeUpdate()
            }
        }
    }

    fun deleteParticipacion(id: Int, grupo: Int) {
        val sql = "DELETE FROM CTFS WHERE CTFID = ? AND GRUPOID = ?"
        dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.setString(2, grupo.toString())
                stmt.executeUpdate()
            }
        }
    }
}
