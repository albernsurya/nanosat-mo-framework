/*
 * ----------------------------------------------------------------------------
 * Copyright (C) 2021 European Space Agency
 * European Space Operations Centre
 * Darmstadt
 * Germany
 * ----------------------------------------------------------------------------
 * System : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under European Space Agency Public License (ESA-PL) Weak Copyleft – v2.4
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ----------------------------------------------------------------------------
 */
package esa.mo.nmf.apps.pictureprocessor;

import static esa.mo.nmf.apps.pictureprocessor.utils.FileUtils.createDirectoriesIfNotExist;

import java.nio.file.Path;
import java.nio.file.Paths;

import esa.mo.nmf.apps.pictureprocessor.mo.PictureProcessorMCAdapter;
import esa.mo.nmf.nanosatmoconnector.NanoSatMOConnectorImpl;

/**
 * An NMF App that that takes pictures and processes them through a python script
 */
public class PictureProcessorApp {

    public PictureProcessorApp() {
        Path outputFolder = createDirectoriesIfNotExist(Paths.get("pictures"));

        NanoSatMOConnectorImpl connector = new NanoSatMOConnectorImpl();

        connector.init(new PictureProcessorMCAdapter(connector, outputFolder));
    }

    /**
     * Main command line entry point.
     *
     * @param args the command line arguments
     * @throws java.lang.Exception If there is an error
     */
    public static void main(final String args[]) throws Exception {
        new PictureProcessorApp();
    }

}
