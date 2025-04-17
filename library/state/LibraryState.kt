sealed class LibraryState {
    object Loading : LibraryState()
    data class Success(val books: List<Book>) : LibraryState()
    data class Error(val message: String) : LibraryState()
}

sealed class LibraryIntent {
    object LoadData : LibraryIntent()
    object RefreshData : LibraryIntent()
} 
