/*
 * NeuronsApplicationView.java
 * odraditi serijalizaciju i snimanje (treba dodati lokaciju kao promenjivu u neural net i training set)
 */
package br.gov.rn.emater.IA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.Timer;
import javax.swing.event.InternalFrameEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.learning.TrainingSet;
import br.gov.rn.emater.IA.dialog.NeuronPropertiesFrame;
import br.gov.rn.emater.IA.errorgraph.GraphFrame;
import br.gov.rn.emater.IA.file.WindowObserver;
import br.gov.rn.emater.IA.dialog.SupervisedTrainingMonitorFrame;
import br.gov.rn.emater.IA.file.FileFilterAdapter;
import br.gov.rn.emater.IA.file.FileIO;
import br.gov.rn.emater.IA.file.FileObserver;
import javax.swing.JFileChooser;

/*
 * 
 */
public class NeuronsApplicationView extends JFrame implements
        Serializable {

    public NeuronsApplicationView() {
        //public NeuronsApplicationView(SingleFrameApplication app) {
        //super(app);
        initComponents();

        //ResourceMap resourceMap = getResourceMap();
        //int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        int messageTimeout = 0;
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        messageTimer.setRepeats(false);

        //JFrame mainFrame2 = this.getFrame();
        //mainFrame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.easyNeuronsProject = new NeuronsProject();
        this.updateProjectTree();
        desktopPanel.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        this.myInstance = this;
        projectManager = new ProjectManager(easyNeuronsProject);
        imageRecognitionSample();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPanel = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(NeuronsApplicationView.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N

        desktopPanel.setName("desktopPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(382, 382, 382)
                .addComponent(jLabel1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(desktopPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desktopPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-855)/2, (screenSize.height-556)/2, 855, 556);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SaveNNT();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    public void updateProjectTree() {
    }

    public void updateFrameTitles() {
        JInternalFrame frames[] = desktopPanel.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            ((NeuralNetworkViewFrame) frames[i]).updateTitle();
        }
    }

    public void openNetworkViewFrame(NeuralNetwork nnet) {
        NeuralNetworkViewFrame networkViewFrame = new NeuralNetworkViewFrame(
                nnet, easyNeuronsProject.getTrainingSets());

        networkViewFrame.addInternalFrameListener(new WindowObserver(networkViewFrame) {

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                netActiveWindow = nn;
                traActiveWindow = null;
            }
        });

        networkViewFrame.setVisible(true);

        desktopPanel.add(networkViewFrame);
        try {
            networkViewFrame.setSelected(true);
            networkViewFrame.setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    public static NeuronsApplicationView getInstance() {
        return myInstance;
    }

    public void openNeuronPropertiesFrame(Neuron neuron) {
        NeuronPropertiesFrame neuronPropertiesFrame = new NeuronPropertiesFrame(neuron);

        neuronPropertiesFrame.setVisible(true);

        desktopPanel.add(neuronPropertiesFrame);
        try {
            neuronPropertiesFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    public void showTrainingSetEditFrame(int inputs, int outputs, String type, String label) {

        TrainingSet trainingSet = new TrainingSet(label);
        TrainingSetEditFrame trainingSetEditFrame = new TrainingSetEditFrame(trainingSet, type, inputs, outputs);
        trainingSetEditFrame.setVisible(true);

        desktopPanel.add(trainingSetEditFrame);
        try {
            trainingSetEditFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    public SupervisedTrainingMonitorFrame openMonitorFrame(NeuralNetworkTraining trainingController) {
        SupervisedTrainingMonitorFrame monitorFrame = new SupervisedTrainingMonitorFrame(trainingController);

        monitorFrame.setVisible(true);
        desktopPanel.add(monitorFrame);
        try {
            monitorFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }

        return monitorFrame;

    }

    public GraphFrame openErrorGraphFrame() {
        JInternalFrame iframes[] = desktopPanel.getAllFrames();
        for (int i = 0; i < iframes.length; i++) {
            if (iframes[i] instanceof GraphFrame) {

                try {
                    iframes[i].setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }

                return (GraphFrame) iframes[i];
            }
        }

        GraphFrame graphFrame = new GraphFrame();

        graphFrame.setVisible(true);
        desktopPanel.add(graphFrame);
        try {
            graphFrame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(NeuronsApplicationView.class.getName()).log(Level.SEVERE, null, ex);
        }


        return graphFrame;
    }

    public void imageRecognitionSample() {
        br.gov.rn.emater.IA.imgrec.ImageRecognitionFrame sample = new br.gov.rn.emater.IA.imgrec.ImageRecognitionFrame();
        sample.setVisible(true);
        desktopPanel.add(sample);
        try {
            sample.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }

    }

    public void SaveNNT() {

        if (netActiveWindow != null) { // prvo proveravamo da li je aktivan
            // neural net prozor

            if (fileObserver.containsKey(netActiveWindow.getFilePath())) // ako jeste da li ga
            // ima u mapi
            {
                FileIO io = new FileIO();
                io.saveNeuralNetwork(netActiveWindow.getNeuralNetwork(), netActiveWindow.getNeuralNetwork().toString(), netActiveWindow.getFilePath());
            } else { // otvaranje prozora za Neural net posto ga nema u mapi
                fileChooser.resetChoosableFileFilters();
                fileChooser.addChoosableFileFilter(new FileFilterAdapter.NeuralNetworkBinaryFileFilter());
                fileChooser.addChoosableFileFilter(new FileFilterAdapter.NeuralNetworkXmlFileFilter());
                int option = fileChooser.showSaveDialog(this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    int count = fileChooser.getCurrentDirectory().toString().length();
                    String name = fileChooser.getSelectedFile().toString().substring(count + 1);
                    String location = fileChooser.getSelectedFile().toString() + fileChooser.getFileFilter().getDescription();
                    FileIO io = new FileIO();
                    io.saveNeuralNetwork(netActiveWindow.getNeuralNetwork(), name, location);
                    fileObserver.put(location, name);
                    System.out.println("Neural network saved");
                }
                updateProjectTree();
                //updateFrameTitles();
            }
        }

        if (traActiveWindow != null) { // proveravamo da li je training prozor
            // otvoren
            if (fileObserver.containsKey(traActiveWindow.getFilePath())) // ako
            // jeste
            // da
            // li
            // je
            // u
            // mapi
            // promeni
            {
                FileIO io = new FileIO();
                io.saveTrainingSet(traActiveWindow, traActiveWindow.getLabel(),
                        traActiveWindow.getFilePath());
            } else { // otvaranje prozora za Training posto ga nema u mapi
                fileChooser.resetChoosableFileFilters();
                fileChooser.addChoosableFileFilter(new FileFilterAdapter.TrainingSetBinaryFileFilter());
                fileChooser.addChoosableFileFilter(new FileFilterAdapter.TrainingSetXmlFileFilter());
                int option = fileChooser.showSaveDialog(this);

                if (option == JFileChooser.APPROVE_OPTION) {
                    int count = fileChooser.getCurrentDirectory().toString().length();
                    String name = fileChooser.getSelectedFile().toString().substring(count + 1);
                    String location = fileChooser.getSelectedFile().toString();
                    FileIO io = new FileIO();
                    io.saveTrainingSet(traActiveWindow, name, location);
                    fileObserver.put(location, name);
                    System.out.println("Training set saved");
                }
                updateProjectTree();
                updateFrameTitles();
            }
        }
    }

    public NeuronsProject getProject() {
        return this.easyNeuronsProject;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    protected DefaultMutableTreeNode SelectedNode;
    private static final long serialVersionUID = 1L;
    int counter = 0;
    public static NeuronsApplicationView myInstance;
    private NeuronsProject easyNeuronsProject;
    private NeuralNetworkViewFrame netActiveWindow = null;
    private TrainingSet traActiveWindow = null;
    private ProjectManager projectManager;
    private FileObserver fileObserver = new FileObserver(); // ovo je dodato
    private JFileChooser fileChooser = new JFileChooser();
// DragNDrop - end
}
