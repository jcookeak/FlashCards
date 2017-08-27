# FlashCards

README

Running:
This app was tested on a 4.71 emulator running android 6.0 with the x86_64 image.
To test the app with images, images must be added to the primary media gallery.  This could be the google photos app or the android gallery.  Please see included img folder for sample images that can be used for testing purposes.  Images are my own.

Building:
This app uses the following third-party libraries:
* Fab menu
* Gson
These libraries were adding by modifying the gradle build script as follows:
compile 'com.google.code.gson:gson:2.6.2'
compile 'com.getbase:floatingactionbutton:1.10.1'
The two lines above were added.  This gradle build has been tested across multiple systems and should compile the necessary packages.

Difficulties and Changes:
The ModifyCategoryActivity has had a design change to add a delete icon rather than a checkbox for usability.  The ReviewActivity now produces a toast when selecting an answer.  This makes the result of the action clearer to the user.  Due to the amount of IO handling the JSON conversion, the system may occasionally skip frames.  

Errors:
Currently the only part of the project that is nonfunctional is the editing of categories.  The changes are applied correctly, but the reviewActvity cannot handle the change in content.

Things to do:
In the future the following features would be added.
* This application should have processing of JSON data moved to a separate thread to keep the main thread free.
* Create dummy data for first run
o App has little visual impact on first run
* Refactor passing extra data to send json representation for smaller objects instead of getting entire json list and converting to an object array to find one entry for speed reasons.
