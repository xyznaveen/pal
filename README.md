# Pal
###### Customizable nepali date picker.


### Features
- Open predefined nepali date.

### To-Do
- [ ] Add a `Builder` class for easy implementation

### How do I use this ?

##### Include In Project

you must be using  jitpack to be able to use this project.

see jitpack.io for more details on jitpack

```
implementation 'com.github.xyznaveen:pal:$latest-version'
// see releases for $latest-version
```

```
// Kotlin

// instantiate
val ghadiFragment = GhadiPickerFragment.newInstance(2075,12,17)

// add listener
ghadiFragment.setDatePickCompleteListener(object : DatePickCompleteListener {
    override fun onDateSelectionComplete(result: GhadiResult) {
        // upon successful date selection this method is fired
    }

    override fun onDateSelectionCancelled(result: GhadiResult) {
        // if the user cancelled the date selection
    }
})

// show the fragment
ghadiFragment.show(supportFragmentManager, ghadiFragment.tag)
```