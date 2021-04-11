# MonstersRecyclerViewWithSQLite3-start
In this Project we will add Internationalisation, Splash screen and set material themes for the app.


## TODO:   Add a listener of each button inside the recyclerView
 *      1) modify the ViewHolder, add a button listener inside of the bind method
 *      2) use a Snackbar to display the name of the monster when this button is clicked / tapped

## TODO:    Add a splash screen
 *      1) create a theme for your splash screen in styles.xml
 *      2) add it to your AndroidManifest.xml
 *      3) in MainActitivy.java restore the old theme, otherwise you will see the splash screen as a background image
 
##  TODO:   set a new Material theme for your app
 *      1) go to any of these websites to select a pre defined set of colors, or create your own one
    *          https://www.materialpalette.com/
    *          https://material.io/resources/color/#!/?view.left=0&view.right=0
    *          https://codecrafted.net/randommaterial
    *          additional info: https://material.io/develop/android
 *      2) Export the colors as xml files
 *      3) replace those colors in your colors.xml file ( be aware that android uses as color names:
    *          colorPrimary, colorPrimaryDark and colorAccent

## TODO:  Internationalisation
 *      1) Remove all hardcoded text in the app, create a key for every text in strings.xml
    *      there will be some cases where you want to pass parameters to the text defined in strings.xml ie:
        *              <string name="my_welcome_messages">Welcome, %1$s! You have %2$d new notifications.</string>
        *              where
            *                  %1 and %2 are the possitions of the parameters
            *                  $s, $d are the datatypes expected for each parameter
        *               In code you should call it in this way:
            *                  rootView.getContext().getString(R.string.view_holder_monster_votes_label_value, monster.getVotes()) *      2) right click on the file -> Open Translation Editor
    *          click the globe icon
    *          select thelanguage you want to translate the file to
    *          fill up the translations
 *      3) Finally change the language of your device to test the internationalisation:
    *          settings -> Language & Input -> Language
    *          and set the new language for your device.
    *          Run your app and check the new language of your app
 
