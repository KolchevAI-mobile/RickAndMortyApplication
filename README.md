# Rick and Morty Application
Небольшое учебное Android‑приложение для просмотра персонажей из Rick and Morty API.
Проект сделан как тренажёр по современному Android‑стеку: Jetpack Compose, Paging 3, Hilt, Room, Retrofit, Coroutines, Lottie.

## Функциональность
- Список персонажей с бесконечной прокруткой (Paging 3).
- Поиск по имени и фильтрация по статусу и полу.
- Экран деталки персонажа с дополнительной информацией.
- Анимации загрузки и ошибок на основе Lottie.
- Обработка сетевых ошибок с возможностью повторной попытки.
- Базовые unit‑тесты для ViewModel деталки.

## Стек технологий
### UI
- Jetpack Compose (Material 3)
- Lottie Compose (анимации загрузки и ошибок)
- Coil (загрузка изображений)

### Архитектура
- MVVM + разделение по слоям: ui / domain / data
- Hilt (Dependency Injection)
- Kotlin Coroutines + Flow / StateFlow
- Paging 3 (RemoteMediator + PagingSource)

### Data
- Retrofit + OkHttp (с логгером)
- kotlinx.serialization (JSON‑парсинг)
- Room (локальное кеширование персонажей)

## Структура проекта
app/
 ├─ data/
 │   ├─ local/        // Room: DAO, entity, AppDatabase
 │   ├─ remote/       // Retrofit API, DTO, RemoteMediator
 │   ├─ mapper/       // Мапперы DTO/Entity → Domain
 │   └─ repository/   // Реализация CharacterRepository
 │
 ├─ domain/
 │   ├─ model/        // Доменные модели (Character, CharacterFilter)
 │   ├─ repository/   // Интерфейсы репозиториев
 │   └─ use_case/     // Use case'ы (GetCharacters, GetCharacterById, ...)
 │
 ├─ ui/
 │   ├─ characterlist/   // Экран списка персонажей
 │   ├─ characterdetail/ // Экран деталки
 │   ├─ components/      // Общие UI‑компоненты (карточки, состояния, Lottie)
 │   └─ navigation/      // Навигация между экранами
 │
 └─ di/               // Hilt‑модули: Network, Database, Repository

## Скриншоты и анимации
### Экран списка персонажей

<img width="744" height="1528" alt="Экран списка персонажей" src="https://github.com/user-attachments/assets/8a10297d-b308-4269-acfd-bfa8e957da5e" />

### Экран деталей персонажа

<img width="738" height="1522" alt="Экран деталей персонажа" src="https://github.com/user-attachments/assets/010f61cb-36f7-4b17-add6-421ff3a2d0c2" />

### Фильтры

<img width="754" height="1526" alt="Фильтры" src="https://github.com/user-attachments/assets/38ff0c09-46a3-434e-ab46-cf0421fa88bb" />

### Анимация загрузки

![loadingGif](https://github.com/user-attachments/assets/af92c34b-8904-439a-8cb9-dbc00b945bc1)

### Анимация ошибки

![errorGif](https://github.com/user-attachments/assets/75069dce-4df3-403d-a459-b00d978408f4)
