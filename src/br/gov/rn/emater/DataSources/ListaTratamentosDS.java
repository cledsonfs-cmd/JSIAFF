/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.rn.emater.DataSources;

import br.gov.rn.emater.Classes.Tratamento;
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
public class ListaTratamentosDS implements JRDataSource {

    private Iterator proximo;
    private Object valorAtual;
    private boolean irParaProximoprograma = true;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    public ListaTratamentosDS(List lista) {
        super();
        this.proximo = lista.iterator();
    }

    public ListaTratamentosDS() {
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
        Tratamento tratamento = (Tratamento) valorAtual;
        if ("idtratamento".equals(campo.getName())) {
            valor = tratamento.getIdtratamento();
        } else if ("descricao".equals(campo.getName())) {
            valor = tratamento.getDescricao();
        } else if ("datacadastro".equals(campo.getName())) {
            valor = simpleDateFormat.format(tratamento.getDatacadastro());
        } else if ("usuario".equals(campo.getName())) {
            valor = tratamento.getUsuario().getLogin();
        } else {
            System.out.println("campo nao encontrado: " + campo.getName());
        }
        return valor;
    }

}
