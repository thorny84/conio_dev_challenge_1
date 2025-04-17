class LibraryActivity : ComponentActivity() {
    private val viewModel: LibraryViewModel by viewModels {
        // or use dependece injection
        LibraryViewModelFactory(
            repository = LibraryRepositoryImpl(
                localDataSource = LocalDataSourceImpl(),
                remoteDataSource = RemoteDataSourceImpl()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryTheme {
                Surface() {
                    LibraryScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun LibraryTheme(
) {
    // TODO create custom theme
}
