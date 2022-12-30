package br.gov.rn.emater.IA;

import java.util.Vector;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.BinaryDeltaRule;
import org.neuroph.nnet.learning.SupervisedHebbianLearning;
import org.neuroph.util.VectorParser;

public class NeuralNetworkTraining {

	NeuralNetwork neuralNet;
	TrainingSet trainingSet;

	public NeuralNetworkTraining(NeuralNetwork neuralNet, TrainingSet trainingSet) {
		this.neuralNet = neuralNet;
		this.trainingSet = trainingSet;
	}

	public NeuralNetworkTraining(NeuralNetwork neuralNet) {
		this.neuralNet = neuralNet;
	}

	public TrainingSet getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(TrainingSet trainingSet) {
		this.trainingSet = trainingSet;
	}

	public void setInput(String netIn) {
		Vector<Double> inVect = VectorParser.parseDouble(netIn);
		neuralNet.setInput(inVect);
		neuralNet.calculate();
		neuralNet.notifyChange();
	}

	public void stopTraining() {
		neuralNet.stopLearning();
	}

        public void pause() {
            neuralNet.pauseLearning();
        }

        public void resume() {
            neuralNet.resumeLearning();
        }
	
	public boolean isStoppedTraining() {
		return neuralNet.getLearningRule().isStopped();
	}

	public void setLmsParams(Double learningRate, Double maxError,
			Integer maxIterations) {
		LMS lms = (LMS) this.neuralNet.getLearningRule();
		lms.setLearningRate(learningRate);
		lms.setMaxError(maxError);
		lms.setMaxIterations(maxIterations);
	}

	public void setHebbianParams(Double learningRate, Double maxError,
			Integer maxIterations) {
		SupervisedHebbianLearning hebbian = (SupervisedHebbianLearning) this.neuralNet
				.getLearningRule();
		hebbian.setLearningRate(learningRate);
		// lms.setMaxError(maxError);
		// lms.setMaxIterations(iterations);
	}

	public void setKohonenParams(Double learningRate, Integer Iphase,
			Integer IIphase) {
		KohonenLearning kl = (KohonenLearning) this.neuralNet.getLearningRule();
		kl.setLearningRate(learningRate.doubleValue());
		kl.setIterations(Iphase.intValue(), IIphase.intValue());
	}

	public void setStepDRParams(Double learningRate, Double maxError, Integer maxIterations) {
		BinaryDeltaRule sdr = (BinaryDeltaRule) this.neuralNet.getLearningRule();
		sdr.setLearningRate(learningRate);
		sdr.setMaxError(maxError);
		sdr.setMaxIterations(maxIterations);
	}

	public void calculate() {
		neuralNet.calculate();
		neuralNet.notifyChange();
	}

	public void train() {
		neuralNet.learnInNewThread(trainingSet);
	}

	public NeuralNetwork getNetwork() {
		return this.neuralNet;
	}

	public void randomize() {
		neuralNet.randomizeWeights();
		neuralNet.notifyChange();
	}

	public void reset() {
		neuralNet.reset();
		neuralNet.notifyChange();
	}

}
