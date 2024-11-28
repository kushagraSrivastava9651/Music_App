import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import com.example.musicappui.ui.theme.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val sharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    // Define a mutableStateList to hold the list of favorites
    private val _favoriteList = mutableStateListOf<Item>()

    // Expose the favoriteList as a SnapshotStateList
    val favoriteList: SnapshotStateList<Item> get() = _favoriteList

    init {
        loadFavorites()  // Load the favorites from SharedPreferences when the ViewModel is created
    }

    // Load the favorite list from SharedPreferences
    private fun loadFavorites() {
        val json = sharedPreferences.getString("favorite_list", "[]")  // Default to empty list
        val type = object : TypeToken<List<Item>>() {}.type
        val savedList: List<Item> = Gson().fromJson(json, type)
        _favoriteList.addAll(savedList)
    }

    // Save the favorite list to SharedPreferences
    private fun saveFavorites() {
        val json = Gson().toJson(_favoriteList)
        sharedPreferences.edit().putString("favorite_list", json).apply()
    }

    // Add or remove an item from the favorite list
    fun toggleFavorite(item: Item) {
        if (_favoriteList.contains(item)) {
            _favoriteList.remove(item)  // Remove item if already in the list
        } else {
            _favoriteList.add(item)  // Add item if not in the list
        }
        saveFavorites()  // Save the updated list to SharedPreferences
    }
}
