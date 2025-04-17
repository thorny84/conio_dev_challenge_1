sealed class RepositoryResult<out T> {
    data class Success<T>(val data: T) : RepositoryResult<T>()
    data class Error(val exception: Exception) : RepositoryResult<Nothing>()
}
/**
I decided to implement an interface that contains the request methods so that 
i can easily create a mock and for the SOLID abstraction principle.
For requests, i use runchaching that replaces the try/catch that I find too verbose, 
it also includes various useful methods such as fold
*/
interface LibraryRepository {
    suspend fun fetchBooks(): RepositoryResult<List<Book>>
    //...
}

class LibraryRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : LibraryRepository {
    
    override suspend fun getBooks(): RepositoryResult<List<Book>> = runCatching {
        remoteDataSource.getBooks()
            .onSuccess { books -> localDataSource.saveBooks(books) }
            .getOrThrow()
    }.fold(
        onSuccess = { RepositoryResult.Success(it) },
        onFailure = { RepositoryResult.Error(it as Exception) }
    )

    //...
} 
