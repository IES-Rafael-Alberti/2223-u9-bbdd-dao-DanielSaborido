package interfaces

interface IDataAccess<T> {
    fun create(entity: T): T?
    fun getAll(): List<T>
    fun getByName(name: String): T?
    fun update(entity: T): T
    fun delete(id: Any)
}