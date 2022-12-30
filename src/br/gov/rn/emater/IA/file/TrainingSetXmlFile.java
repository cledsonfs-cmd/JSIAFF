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

import java.io.File;
import java.io.IOException;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.util.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Handles Training Set Xml Serialization
 *
 * @author Zoran Sevarac
 * @author Ivan Jovanovic
 * @author Nemanja Joksovic
 */
public class TrainingSetXmlFile implements FileInterface<TrainingSet> {

	@Override
	public void save(TrainingSet trainingSet, String filePath) {
		if (trainingSet == null) {
			throw new IllegalArgumentException("Training set cannot be null");
		}

		if (filePath == null) {
			throw new IllegalArgumentException("File cannot be null");
		}

		try {
			XStream xstream = getXStream();
			String xml = xstream.toXML(trainingSet);
			FileUtils.writeStringToFile(new File(filePath), xml);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public TrainingSet load(String filePath) {
		if (filePath == null) {
			throw new IllegalArgumentException("File cannot be null");
		}

		try {
			XStream xstream = getXStream();
			String xml = FileUtils.readStringFromFile(new File(filePath));
			TrainingSet trainingSet = (TrainingSet) xstream.fromXML(xml);
			return trainingSet;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private XStream getXStream() {
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("trainingSet", TrainingSet.class);
		xstream.alias("trainingElement", TrainingElement.class);
		xstream.alias("supervisedTrainingElement", SupervisedTrainingElement.class);
		return xstream;
	}
}
