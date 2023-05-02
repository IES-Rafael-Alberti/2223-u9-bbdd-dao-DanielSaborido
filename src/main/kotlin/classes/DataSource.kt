package classes

import interfaces.DataSource
import java.sql.Connection
import java.sql.DriverManager

class DataSourceImpl : DataSource {
    private val jdbcUrl = "jdbc:h2:./basededatos"
    override fun connection() : Connection {
        return DriverManager.getConnection(jdbcUrl, "daniel", "saborido")
    }
}