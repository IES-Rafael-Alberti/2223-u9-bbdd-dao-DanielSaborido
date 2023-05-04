package classes

import entities.Ctf
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

    override fun selectById(id: Int): Grupo? {
        val sql = "SELECT * FROM GRUPOS WHERE GRUPOID = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
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

    override fun delete(id: Int) {
        CCtf(dataSource).deleteGrupo(id)
        val sql = "DELETE FROM GRUPOS WHERE GRUPOID = ?"
        dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                stmt.executeUpdate()
            }
        }
    }
    fun updatePuntos() {
        val ctfs = CCtf(dataSource).getAll()
        val mejoresResultados = calculaMejoresResultados(ctfs)
        mejoresResultados.values.forEach { mejorResultado ->
            var grupo = CGrupo(dataSource).selectById(mejorResultado.second.grupoId)
            grupo?.let { elGrupo ->
                elGrupo.mejorCtfId = mejorResultado.second.id
                CGrupo(dataSource).update(elGrupo)
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
