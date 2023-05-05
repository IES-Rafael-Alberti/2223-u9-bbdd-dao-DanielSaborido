import classes.CCtf
import classes.CGrupo
import classes.DataSourceImpl
import entities.Ctf



fun main(args: Array<String>) {
    val myDataSource = DataSourceImpl()
    /*
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
     */
    println("Inserte comando:")
    var comando = readln()
    var instrucciones = comando.split(" ")
    while (instrucciones[0] != args[3]) {//escribir -e para salir
        if (instrucciones[0] == args[0]) {//aqui esta el -a para añadir participaciones
            if (instrucciones.size != 4) {
                println("[ERROR] Comando no ingresado correctamente.")
            } else {
                CCtf(myDataSource).create(Ctf(instrucciones[1].toInt(), instrucciones[2].toInt(), instrucciones[3].toInt()))
                println("Procesado: Añadida participación del grupo en el CTF ${instrucciones[1]} con una puntuación de ${instrucciones[3]} puntos.")
                CGrupo(myDataSource).updatePuntos()
            }
        }
        else if (instrucciones[0] == args[1]) {//aqui esta el -d para eliminar participaciones
            if (instrucciones.size != 3) {
                println("[ERROR] Comando no ingresado correctamente.")
            } else {
                CCtf(myDataSource).deleteParticipacion(instrucciones[1].toInt(), instrucciones[2].toInt())
                println("Procesado: Eliminada participación del grupo en el CTF ${instrucciones[1]}")
                CGrupo(myDataSource).updatePuntos()
            }
        }
        else if (instrucciones[0] == args[2]) {//aqui esta el -l para mostrar informacion
            if (instrucciones.size == 1) {
                println("Procesado: Listado participación de todos los grupos")
                CGrupo(myDataSource).getAll()
            }
            else if (instrucciones.size == 2) {
                println("Procesado: Listado participación del grupo")
                CGrupo(myDataSource).selectById(instrucciones[1].toInt())
            }
            else {
                println("[ERROR] Comando no ingresado correctamente.")
            }

        }
        else {
            println("[ERROR] Este comando no existe.")
        }
        println("Inserte comando:")
        comando = readln()
        instrucciones = comando.split(" ")
    }
    println("Programa terminado.")
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