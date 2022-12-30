/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.rn.emater.IA;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.TrainingSet;

/**
 * Mozda dodati EasyNeuronsProjectTree i EasyNeuronsProjectItem
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NeuronsProject {

    private Vector<NeuralNetwork> neuralNetworks;
    private Vector<TrainingSet> trainingSets;
    private Vector testSets;

    public NeuronsProject() {
        this.neuralNetworks = new Vector<NeuralNetwork>();
        this.trainingSets = new Vector<TrainingSet>();
        this.testSets = new Vector();
    }

    public Vector<NeuralNetwork> getNeuralNetworks() {
        return neuralNetworks;
    }

    public void setNeuralNetworks(Vector<NeuralNetwork> neuralNetworks) {
        this.neuralNetworks = neuralNetworks;
    }

    public Vector getTestSets() {
        return testSets;
    }

    public void setTestSets(Vector testSets) {
        this.testSets = testSets;
    }

    public Vector<TrainingSet> getTrainingSets() {
        return trainingSets;
    }

    public void setTrainingSets(Vector<TrainingSet> trainingSets) {
        this.trainingSets = trainingSets;
    }

    public void addNeuralNetwork(NeuralNetwork nnet) {
        this.neuralNetworks.add(nnet);
    }

    public void addTrainingSet(TrainingSet trainingSet) {
        this.trainingSets.add(trainingSet);
    }

    public void addTestSet() {
    }

    public void removeNeuralNetwork(NeuralNetwork nnet) {
        this.neuralNetworks.remove(nnet);
    }

    public void removeTrainingSet(TrainingSet trainingSet) {
        this.trainingSets.remove(trainingSet);
    }

    public void removeTestSet() {
    }
}
