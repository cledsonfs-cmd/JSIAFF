/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.rn.emater.DataSources;

import br.gov.rn.emater.Classes.Agente;
import br.gov.rn.emater.Classes.Reconhecimento;
import java.awt.Image;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author cledsonfs
 */
public class ReconhecimentoDS implements JRDataSource {

    private Iterator proximo;
    private Object valorAtual;
    private boolean irParaProximoprograma = true;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    private NumberFormat numberFormat = DecimalFormat.getNumberInstance();

    public ReconhecimentoDS(List lista) {
        super();
        this.proximo = lista.iterator();
    }

    public ReconhecimentoDS() {
        super();
    }
    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */

    public boolean next() throws JRException {
        valorAtual = proximo.hasNext() ? proximo.next() : null;
        irParaProximoprograma = (valorAtual != null);
        return irParaProximoprograma;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    @Override
    public Object getFieldValue(JRField campo) throws JRException {
        Object valor = null;
        numberFormat.setMaximumFractionDigits(4);
        Reconhecimento reconhecimento = (Reconhecimento) valorAtual;
        if ("percentual".equals(campo.getName())) {
            double v = Double.parseDouble(reconhecimento.getIndiceReconhecimento().replace(",", "."));
            numberFormat.setMaximumFractionDigits(4);
            v = (v * 100);
            valor = numberFormat.format(v) + "%";
        } else if ("codigo".equals(campo.getName())) {
            valor = reconhecimento.getAmostra().getIdamostra();
        } else if ("cientifico".equals(campo.getName())) {
            valor = reconhecimento.getAmostra().getDoenca().getNomecientifico().trim();
        } else if ("popular".equals(campo.getName())) {
            valor = reconhecimento.getAmostra().getDoenca().getDescricao().trim();
        } else if ("imagem".equals(campo.getName())) {
            Image im = reconhecimento.getAmostra().getImagem().getImage();
            valor = im;
        } else if ("imgAnliz".equals(campo.getName())) {
            Image im = reconhecimento.getImagem().getImage();
            valor = im;
        } else {
            System.out.println("campo nao encontrado: " + campo.getName());
        }
        return valor;
    }
}
