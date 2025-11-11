# 🧭 TaskBoard - Mini Assignment

## 🛠 Tech Stack

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Material3)
* **Architecture:** MVVM + Repository Pattern
* **Database:** Room
* **Dependency Injection:** Hilt
* **Concurrency:** Coroutines + Flow

---

## 🏗 Architecture Overview

This project follows a **clean and modular MVVM architecture** with clear separation of concerns:

```
com.android.bbassignment
├── core
│   ├── data          # Room entities, DAO, database
│   ├── network       # Dummy network layer simulating API
│   └── repository    # Repository combining local + remote data
└── feature
    └── taskboard     # UI (Compose), ViewModel, and user interaction
```

* **ViewModel** → Holds UI state and business logic using `StateFlow`
* **Repository** → Acts as a single source of truth, managing both Room and Dummy Network
* **Room (DAO)** → Persists data locally
* **DummyNetworkService** → Simulates network sync with artificial delay
* **UI (Compose)** → Fully reactive using `collectAsState()` on flows

---

## 🎯 Features Implemented

✅ **Task List Screen**

* Displays all tasks in a scrollable list (title, description, completion status)
* Supports marking tasks as *complete/incomplete*
* Supports *deleting* tasks
* Shows *empty state* message with image when no tasks exist
* Includes a **Sync button** on the top bar to fetch dummy tasks

✅ **Add/Edit Task Screen**

* Create or update tasks
* Pre-fills data when editing existing task
* Input validation and save button enable/disable logic
* Detects unsaved changes before saving
* UI built using Material 3 components

✅ **Dummy Network Sync**

* Simulates API call with artificial delay
* Fetches dummy task list and merges into local database
* Merge logic preserves local updates (`isDone`, `updatedAt`)
* Network handled asynchronously via `Coroutines` and `Flow`

---

## 🔄 Merge Strategy

The repository ensures data consistency between local and network sources:

* **New Remote Tasks** → Inserted into Room
* **Existing Tasks** → Compare timestamps

    * If remote `updatedAt` is newer → Replace local, but keep `isDone`
    * Otherwise → Keep local copy

---

## 💡 UI/UX Behavior

* Uses **Material 3 Design** throughout
* **FloatingActionButton** for adding new tasks
* **TopAppBar** with sync action (icon changes to a loading indicator during sync)
* Displays proper **empty state** for first-time users
* Full **reactive UI** — updates automatically when Room database changes

---

## 🧩 How to Run

1. **Clone the repository**

   ```bash
   git clone --still not added --
   cd TaskBoard
   ```

2. **Open in Android Studio**

    * Recommended version: **Android Studio Koala | Giraffe+**
    * Ensure Kotlin + Compose setup is enabled

3. **Build and Run**

    * Choose an emulator or physical device (API 24+)
    * Hit **Run ▶**

---

## ⚙️ Setup Notes

* No extra configuration needed — app runs out of the box
* Hilt handles dependency injection automatically
* DummyNetworkService simulates network delay using `delay()`
* All operations are asynchronous, keeping the UI smooth

---

## 🧠 Code Quality

* **Kotlin best practices** followed
* **Composable functions** are stateless and reusable
* **UI state** is driven by immutable data classes
* **Sealed state management** for loading, empty, and error states
* **MVVM separation** ensures testability and modularity

---

## 📁 Project Highlights

| Layer            | Description                                    |
| ---------------- | ---------------------------------------------- |
| **UI (Compose)** | TaskListScreen, AddEditTaskScreen              |
| **ViewModel**    | TaskViewModel (StateFlow-based reactive logic) |
| **Repository**   | Handles Room + Dummy Network sync logic        |
| **Data**         | Task Entity, TaskDao, AppDatabase              |
| **Network**      | DummyNetworkService simulates server API       |

---

## 🧾 Example Flow

1. User opens app → Tasks load from Room
2. User adds or edits a task → Persisted via DAO
3. Sync button clicked → Fetches dummy network tasks
4. Repository merges remote + local tasks safely
5. UI auto-updates via Flow

---

## 📦 Libraries Used

| Library                         | Purpose                  |
| ------------------------------- | ------------------------ |
| **Jetpack Compose (Material3)** | UI and Layout            |
| **Room Database**               | Local persistence        |
| **Hilt (Dagger)**               | Dependency Injection     |
| **Kotlin Coroutines / Flow**    | Async and reactive state |
| **Navigation Compose**          | In-app navigation        |

---

## 🧑‍💻 Author

**Prudhvi Naidu**
Android Developer | Kotlin Enthusiast | Clean Architecture Advocate
📩 [prudhvinaidu193@gmail.com](mailto:prudhvinaidu193@gmail.com)

---

## 🪶 License

This project is developed solely for assignment and demonstration purposes.
All code is open for educational and evaluation use only.

```
MIT License
```

---
