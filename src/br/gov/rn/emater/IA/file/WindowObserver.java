/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package br.gov.rn.emater.IA.file;

import javax.swing.event.InternalFrameAdapter;

import org.neuroph.core.learning.TrainingSet;
import br.gov.rn.emater.IA.NeuralNetworkViewFrame;

/**
 * 
 * @author Marko Koprivica
 */
public class WindowObserver extends InternalFrameAdapter {
	public NeuralNetworkViewFrame nn;
	public TrainingSet ts;

	public WindowObserver(NeuralNetworkViewFrame o) {
		nn = o;
	}

	public WindowObserver(TrainingSet o) {
		ts = o;
	}

}
