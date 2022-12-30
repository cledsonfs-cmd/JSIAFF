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

import javax.swing.filechooser.FileFilter;

/**
 * This class with its subclasses provides file filters for neural networks and training sets
 *
 * @author Zoran Sevarac
 * @author Ivan Jovanovic
 * @author Nemanja Joksovic
 */
public abstract class FileFilterAdapter extends FileFilter {

	public abstract String getDescription();

	public abstract String getExtension();

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = FileUtils.getExtension(f);
		if (extension != null) {
			if (extension.equals(getExtension())) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public static class NeuralNetworkBinaryFileFilter extends FileFilterAdapter {
		@Override
		public String getDescription() {
			return ".nnet";
		}

		@Override
		public String getExtension() {
			return FileUtils.nn;
		}
	}

	public static class NeuralNetworkXmlFileFilter extends FileFilterAdapter {
		@Override
		public String getDescription() {
			return ".nxml";
		}

		@Override
		public String getExtension() {
			return FileUtils.nxml;
		}
	}

	public static class TrainingSetBinaryFileFilter extends FileFilterAdapter {
		@Override
		public String getDescription() {
			return ".tset";
		}

		@Override
		public String getExtension() {
			return FileUtils.ts;
		}
	}

	public static class TrainingSetXmlFileFilter extends FileFilterAdapter {
		@Override
		public String getDescription() {
			return ".txml";
		}

		@Override
		public String getExtension() {
			return FileUtils.txml;
		}
	}
}
