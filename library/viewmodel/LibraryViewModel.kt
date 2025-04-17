/**
use of viewmodel for separate lifecycle management and state maintenance.
Here only one intent enters, is processed and only one state exits as required by the MVI architecture
*/
class LibraryViewModel(
    private val repository: LibraryRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    // State
    private val _state = MutableStateFlow<LibraryState>(LibraryState.Loading)
    val state: StateFlow<LibraryState> = _state.asStateFlow()

    // Intent
    private val _intent = MutableSharedFlow<LibraryIntent>()
    private val scope = viewModelScope

    init {
        handleIntents()
        loadInitialData()
    }

    private fun loadInitialData() {
        processIntent(LibraryIntent.LoadData)
    }

    fun processIntent(intent: LibraryIntent) {
        scope.launch {
            _intent.emit(intent)
        }
    }

    private fun handleIntents() {
        scope.launch {
            _intent.collect { intent ->
                when (intent) {
                    is LibraryIntent.LoadData -> loadData()
                    is LibraryIntent.RefreshData -> refreshData()
                }
            }
        }
    }

    private suspend fun loadData() {
        withContext(dispatcher) {
            _state.value = LibraryState.Loading
            
            runCatching {
                val booksResult = repository.fetchBooks()
                val comicBooksResult = repository.fetchComicBooks()

                when {
                    booksResult is RepositoryResult.Error -> {
                        _state.value = LibraryState.Error(booksResult.exception.message ?: "error message")
                    }
                    else -> {
                        val books = (booksResult as RepositoryResult.Success).data
                        _state.value = LibraryState.Success(books)
                    }
                }
            }.onFailure { exception ->
                _state.value = LibraryState.Error(exception.message ?: "error message")
            }
        }
    }

    private suspend fun refreshData() {
        loadData()
    }
} 
