# Android Project Rules

## Architecture
This project follows strict MVI.

Flow must always be:

UI
→ Action
→ ViewModel
→ Intent
→ Repository

[//]: # (→ Data Source)

Never skip Intent.

## ViewModel
- ViewModel only exposes StateFlow<UiState>
- UI sends Actions only
- ViewModel converts Action -> function calls
- Function calls repository
- Repository returns Result<T>

## UI
- Stateless composables
- No business logic
- No repository access
- Collect state using collectAsStateWithLifecycle()

## State
Always use immutable data classes.

sealed class UiAction
data class UiState(...)

## Coroutines
Use viewModelScope.launch.
Never use GlobalScope.

## Dependency Injection
Use Koin.

[//]: # ()
[//]: # (## Effects)

[//]: # (Use SharedFlow<UiEffect>.)

## Error handling
Repository returns Result<T>.
ViewModel maps Result into UiState.
UI never catches exceptions.

## Compose
- Material3
- Remember only UI state
- No mutableStateOf inside ViewModel