/*
 * Imprimir.java
 *
 * Created on 14 de Dezembro de 2006, 09:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package br.gov.rn.emater.Base;

import br.gov.rn.emater.Apoio.Conexao;
import java.awt.Dialog.ModalExclusionType;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Cledsonfs
 */
public class Imprimir {

    public static int CONTADOR = 0;
    private String gravar = "";
    private boolean diretoImpressora = false;
    private boolean html = false;

    public Imprimir(String local, Map parametros) {
        exec(local, parametros);
    }

    public Imprimir(String local, Map parametros, boolean html) {
        this.html = html;
        exec(local, parametros);
    }

    public Imprimir(List lista, String local, JRDataSource ds) {
        exec(lista, local, ds, null);
    }

    public Imprimir(List lista, String local, JRDataSource ds, boolean html) {
        this.html = html;
        exec(lista, local, ds, null);
    }

    public Imprimir(List lista, String local, JRDataSource ds, String gravarArquivo) {
        gravar = gravarArquivo;
        exec(lista, local, ds, null);
    }

    public Imprimir(List lista, String local, JRDataSource ds, String gravarArquivo, boolean html) {
        gravar = gravarArquivo;
        this.html = html;
        exec(lista, local, ds, null);
    }

    public Imprimir(List lista, String local, JRDataSource ds, Map parametros) {
        exec(lista, local, ds, parametros);
    }

    public Imprimir(List lista, String local, JRDataSource ds, Map parametros, boolean diretoImpressora) {
        this.diretoImpressora = diretoImpressora;
        exec(lista, local, ds, parametros);
    }

    private void exec(List lista, String local, JRDataSource ds, Map parametros) {
        File f = new File(local);
        if (f.exists()) {
            if (parametros == null) {
                parametros = new HashMap();
            }
            try {

                if (diretoImpressora) {
                    JasperPrint impressao = JasperFillManager.fillReport(local, parametros, ds);
                    JasperPrintManager.printPage(impressao, 0, false);
                } else if (gravar.trim().length() > 0) {
                    if (html) {
                        JasperPrint impressao = JasperFillManager.fillReport(local, parametros, ds);
                        JasperExportManager.exportReportToHtmlFile(impressao, gravar);
                    } else {
                        JasperPrint impressao = JasperFillManager.fillReport(local, parametros, ds);
                        JasperExportManager.exportReportToPdfFile(impressao, gravar);
                    }
                } else {
                    JasperPrint impressao = JasperFillManager.fillReport(local, parametros, ds);
                    JasperViewer view = new JasperViewer(impressao, false);
                    view.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
                    view.setVisible(true);
                    view.toFront();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("*** ERRO: Nao localizou o arquivo " + local);
            File x = new File(".");
            System.err.println("*** AbsolutePath: " + x.getAbsolutePath());
            System.err.println("*** AbsoluteFile: " + x.getAbsoluteFile());
            System.err.println("*** Name: " + x.getName());
            System.err.println("*** Parent: " + x.getParent());
            String[] aa = x.list();
            for (int k = 0; k < aa.length; k++) {
                System.err.println(k + ": " + aa[k]);
            }
        }
    }

    private void exec(String local, Map parametros) {
        File f = new File(local);
        if (f.exists()) {
            if (parametros == null) {
                parametros = new HashMap();
            }
            try {
                if (parametros == null) {
                    parametros = new HashMap();
                }
                Connection con = new Conexao().getConnection();
                if (gravar.trim().length() > 0) {
                    if (html) {
                        JasperPrint impressao = JasperFillManager.fillReport(local, parametros, con);
                        JasperExportManager.exportReportToHtmlFile(impressao, gravar);
                    } else {
                        JasperPrint impressao = JasperFillManager.fillReport(local, parametros, con);
                        JasperExportManager.exportReportToPdfFile(impressao, gravar);
                    }
                } else {                    
                    JasperPrint impressao = JasperFillManager.fillReport(local, parametros, con);
                    JasperViewer view = new JasperViewer(impressao, false);
                    view.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
                    view.setVisible(true);
                    view.toFront();
                }
            } catch (JRException ex) {
                Logger.getLogger(Imprimir.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Imprimir.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Imprimir.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, local + "\nArquivo nao localizado");
        }
    }
}
