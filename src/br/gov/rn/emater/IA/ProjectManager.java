/*
TODO... work in progress, singlrton project nmanager
 */
package br.gov.rn.emater.IA;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.util.plugins.LabelsPlugin;

/**
 *
 * @author zoran
 */
public class ProjectManager {

    public static ProjectManager instance;
    private NeuronsProject easyNeuronsProject;

    public ProjectManager(NeuronsProject easyNeuronsProject) {
        this.easyNeuronsProject = easyNeuronsProject;
        this.instance = this;
    }

    public static ProjectManager getInstance() {
        return instance;
    }

    public void addNewNetworkToProject(NeuralNetwork nnet) {
        int count = easyNeuronsProject.getNeuralNetworks().size() + 1;
        LabelsPlugin labels = (LabelsPlugin) nnet.getPlugin("LabelsPlugin");
        if ((labels.getLabel(nnet) == null) || (labels.getLabel(nnet).equals(""))) {
            labels.setLabel(nnet, "NewNetwork" + count);
        }

        this.addNetworkToProject(nnet);
    }

    public void addNetworkToProject(NeuralNetwork nnet) {
        easyNeuronsProject.addNeuralNetwork(nnet);
        NeuronsApplicationView.getInstance().updateProjectTree();
        NeuronsApplicationView.getInstance().openNetworkViewFrame(nnet);
    }

    public void updateTrainingSets(TrainingSet trainingSet) {
        if (!easyNeuronsProject.getTrainingSets().contains(trainingSet)) {
            easyNeuronsProject.addTrainingSet(trainingSet);
        }
        NeuronsApplicationView.getInstance().updateProjectTree();
    }
}
