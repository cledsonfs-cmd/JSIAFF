/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.rn.emater.DataSources;

import br.gov.rn.emater.Classes.Doenca;
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
public class ListaDoencasDS implements JRDataSource {

    private Iterator proximo;
    private Object valorAtual;
    private boolean irParaProximoprograma = true;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    public ListaDoencasDS(List lista) {
        super();
        this.proximo = lista.iterator();
    }

    public ListaDoencasDS() {
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
        Doenca doenca = (Doenca) valorAtual;
        if ("iddoenca".equals(campo.getName())) {
            valor = doenca.getIddoenca();
        } else if ("descricao".equals(campo.getName())) {
            valor = doenca.getDescricao();
        } else if ("nomecientifico".equals(campo.getName())) {
            valor = doenca.getNomecientifico();
        } else if ("datacadastro".equals(campo.getName())) {
            valor = simpleDateFormat.format(doenca.getDatacadastro());
        } else if ("login".equals(campo.getName())) {
            valor = doenca.getUsuario().getLogin();
        } else {
            System.out.println("campo nao encontrado: " + campo.getName());
        }
        return valor;
    }
}
