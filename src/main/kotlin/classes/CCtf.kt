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

    override fun getByName(name: String): Ctf? {
        val sql = "SELECT * FROM CTFS WHERE CTFID = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(2, name)
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

    override fun delete(id: Any) {
        val sql = "DELETE FROM CTFS WHERE CTFID = ?"
        dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.executeUpdate()
            }
        }
    }
    fun calculaMejoresResultados(participaciones: List<Ctf>): MutableMap<Int, Pair<Int, Ctf>> {
        val participacionesByCTFId = participaciones.groupBy { it.id }
        var participacionesByGrupoId = participaciones.groupBy { it.grupoId }
        val mejoresCtfByGroupId = mutableMapOf<Int, Pair<Int, Ctf>>()
        participacionesByCTFId.values.forEach { ctfs ->
            val ctfsOrderByPuntuacion = ctfs.sortedBy { it.puntuacion }.reversed()
            participacionesByGrupoId.keys.forEach { grupoId ->
                val posicionNueva = ctfsOrderByPuntuacion.indexOfFirst { it.grupoId == grupoId }
                if (posicionNueva >= 0) {
                    val posicionMejor = mejoresCtfByGroupId.getOrDefault(grupoId, null)
                    if (posicionMejor != null) {
                        if (posicionNueva < posicionMejor.first)
                            mejoresCtfByGroupId.set(grupoId, Pair(posicionNueva, ctfsOrderByPuntuacion.get(posicionNueva)))
                    } else
                        mejoresCtfByGroupId.set(grupoId, Pair(posicionNueva, ctfsOrderByPuntuacion.get(posicionNueva)))

                }
            }
        }
        return mejoresCtfByGroupId
    }
}
