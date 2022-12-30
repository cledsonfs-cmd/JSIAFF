/*
 * NeuronsApplication.java
 */
package br.gov.rn.emater.IA;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class NeuronsApplication extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        NeuronsApplicationView easyNeuronsApplicationView = new NeuronsApplicationView();
        //NeuronsApplicationView easyNeuronsApplicationView = new NeuronsApplicationView(this);
        show(easyNeuronsApplicationView);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     *
     * @return the instance of NeuronsApplication
     */
    public static NeuronsApplication getApplication() {
        return Application.getInstance(NeuronsApplication.class);
    }

    /**
     * Main method launching the application.
     */
    public void start() {
        launch(NeuronsApplication.class, null);
    }
}
