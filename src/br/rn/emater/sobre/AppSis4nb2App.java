/*
 * AppSis4nb2App.java
 */

package br.rn.emater.sobre;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class AppSis4nb2App extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        //show(new AppSis4nb2View(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of AppSis4nb2App
     */
    public static AppSis4nb2App getApplication() {
        return Application.getInstance(AppSis4nb2App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(AppSis4nb2App.class, args);
    }
}
