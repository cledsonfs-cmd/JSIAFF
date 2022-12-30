/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.rn.emater.DataSources;

import br.gov.rn.emater.Classes.Familia;
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
public class ListaFamiliaDS implements JRDataSource {

    private Iterator proximo;
    private Object valorAtual;
    private boolean irParaProximoprograma = true;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    public ListaFamiliaDS(List lista) {
        super();
        this.proximo = lista.iterator();
    }

    public ListaFamiliaDS() {
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
        Familia familia = (Familia) valorAtual;
        if ("idfamilia".equals(campo.getName())) {
            valor = familia.getIdfamilia();
        } else if ("descricao".equals(campo.getName())) {
            valor = familia.getDescricao();
        } else if ("usuario".equals(campo.getName())) {
            valor = familia.getUsuario().getLogin();
        } else if ("datacadastro".equals(campo.getName())) {
            valor = simpleDateFormat.format(familia.getDatacadastro());
        } else {
            System.out.println("campo nao encontrado: " + campo.getName());
        }
        return valor;
    }

}
