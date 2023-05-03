import classes.CCtf
import classes.DataSourceImpl
import entities.Ctf



fun main() {
    val myDataSource = DataSourceImpl()
    val participaciones = listOf(
        Ctf(1, 1, 3),
        Ctf(1, 2, 101),
        Ctf(2, 2, 3),
        Ctf(2, 1, 50),
        Ctf(2, 3, 1),
        Ctf(3, 1, 50),
        Ctf(3, 3, 5)
    )//para entenderlo, el de id 1 se va con el grupoId 2 ya que en el resto de CTFs las puntuaciones de este grupo son menores a 101
    for (participantes in participaciones) {
        CCtf(myDataSource).create(participantes)
    }
    val mejoresCtfByGroupId = CCtf(myDataSource).calculaMejoresResultados(participaciones)
    println(mejoresCtfByGroupId)

}

/**
 * TODO
 *
 * @param participaciones
 * @return devuelve un mutableMapOf<Int, Pair<Int, Ctf>> donde
 *      Key: el grupoId del grupo
 *      Pair:
 *          first: Mejor posición
 *          second: Objeto CTF el que mejor ha quedado
 */