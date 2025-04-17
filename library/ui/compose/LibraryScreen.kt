@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier
) {
// use of collectAsState to manage the recomposition when they change
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.processIntent(LibraryIntent.LoadData)
    }

    Box() {
        when (state) {
            is LibraryState.Loading -> {
                // TODO Loading screen
            }
            is LibraryState.Success -> {
                // TODO Success state implementation
            }
            is LibraryState.Error -> {
               // TODO Error state implementation
            }
        }
    }
}
