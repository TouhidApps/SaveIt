# SaveIt-Android-Kotlin
A library to save in Shared Preference with crypto support
===

## How to use:

Initialize the shared preference inside the onCreate of the Application class of your app.


Kotlin:

```Kotlin
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize the Prefs class
              
        // For general data
         
        SaveIt.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName + "_new_name")
            .setIsCommitEnabled(true) // commit enable true means save instantly, false means save asynchronously
            .setUseDefaultSharedPreference(false) // if false use setPrefsName() method to set name
            .build()


        
        // For Secured data like userName, password, token, email etc
        
        SaveItS.Builder()
            .setContext(this)
            .setPrefsName(packageName + "_new_name_secured")
            .setIsCommitEnabled(true) // commit enable true means save instantly, false means save asynchronously
            .setUseDefaultSharedPreference(false) // if false use setPrefsName() method to set name
            .build()

    }

}
```


After initialization, you can use simple one-line methods to save values to the shared preferences anywhere in your app, such as:

- `SaveIt.putString(key, string)`
- `SaveIt.putLong(key, long)`
- `SaveIt.putBoolean(key, boolean)` 

or for secured Shared Pref:

- `SaveItS.putString(key, string)`
- `SaveItS.putLong(key, long)`
- `SaveItS.putBoolean(key, boolean)` 

Retrieving data from the Shared Preferences can be as simple as:

	val data: String = SaveIt.getString(key, "")

to get secured data if saved in SaveItS:

	val data: String = SaveItS.getString(key, "")


For some examples, see the sample App.



# Add to project

Step 1. Add it in build.gradle (Project) file:

```Groovy
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

Step 2. Add the dependency in build.gradle (App) file:

```Groovy
dependencies {
    implementation 'com.github.TouhidApps:SaveIt-Android-Kotlin:0.0.1'
}
```



#### Inspired from:
[EasyPrefs](https://github.com/Pixplicity/EasyPrefs)
