package `interfaces`

import java.sql.Connection

interface DataSource {
    fun connection() : Connection
}