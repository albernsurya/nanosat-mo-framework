/* ----------------------------------------------------------------------------
 * Copyright (C) 2015      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under the European Space Agency Public License, Version 2.0
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
package esa.mo.nmf.ctt.services.mc;

import esa.mo.com.impl.consumer.ArchiveConsumerServiceImpl;
import esa.mo.com.impl.provider.ArchivePersistenceObject;
import esa.mo.nmf.ctt.stuff.SharedTablePanel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mc.alert.structures.AlertDefinitionDetails;

/**
 *
 * @author Cesar Coelho
 */
public class AlertTablePanel extends SharedTablePanel {

    public AlertTablePanel(ArchiveConsumerServiceImpl archiveService) {
        super(archiveService);
    }

    @Override
    public void addEntry(final Identifier name, final ArchivePersistenceObject comObject) {
        if (comObject == null){
            Logger.getLogger(SharedTablePanel.class.getName()).log(Level.SEVERE, "The table cannot process a null COM Object.");
            return;
        }

        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(SharedTablePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        AlertDefinitionDetails pDef = (AlertDefinitionDetails) comObject.getObject();
        
        tableData.addRow(new Object[]{
            comObject.getArchiveDetails().getInstId(),
            name.toString(),
            pDef.getDescription(),
            pDef.getSeverity().toString(),
            pDef.getGenerationEnabled()
        });

        comObjects.add(comObject);
        semaphore.release();
    }

    public void switchEnabledstatus(boolean status){
        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(SharedTablePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 4 because it is where generationEnabled is!
        tableData.setValueAt(status, this.getSelectedRow(), 4);
        ((AlertDefinitionDetails) this.getSelectedCOMObject().getObject()).setGenerationEnabled(status);
        
        semaphore.release();
    }

    public void switchEnabledstatusAll(boolean status){
        
        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(SharedTablePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // 4 because it is where generationEnabled is!
        for (int i = 0; i < this.getTable().getRowCount() ; i++){
            tableData.setValueAt(status, i, 4);
            ((AlertDefinitionDetails) this.getCOMObjects().get(i).getObject()).setGenerationEnabled(status);
        }
        
        semaphore.release();
        
    }
    
    
    @Override
    public void defineTableContent() {
    
        String[] tableCol = new String[]{
            "Obj Inst Id", "name", "description", "Severity", "generationEnabled" };

        tableData = new javax.swing.table.DefaultTableModel(
                new Object[][]{}, tableCol) {
                    Class[] types = new Class[]{
                        java.lang.Integer.class, java.lang.String.class, java.lang.String.class,
                        java.lang.String.class, java.lang.Boolean.class
                    };

                    @Override               //all cells false
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }

                    @Override
                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }
                };

        super.getTable().setModel(tableData);

    }
    
}
