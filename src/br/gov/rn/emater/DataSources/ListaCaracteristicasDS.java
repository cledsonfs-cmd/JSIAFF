/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.rn.emater.DataSources;

import br.gov.rn.emater.Classes.Caracteristica;
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
public class ListaCaracteristicasDS implements JRDataSource {

    private Iterator proximo;
    private Object valorAtual;
    private boolean irParaProximoprograma = true;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    public ListaCaracteristicasDS(List lista) {
        super();
        this.proximo = lista.iterator();
    }

    public ListaCaracteristicasDS() {
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
        Caracteristica caracteristica = (Caracteristica) valorAtual;
        if ("idcaracteristica".equals(campo.getName())) {
            valor = caracteristica.getIdcaracteristica();
        } else if ("descricao".equals(campo.getName())) {
            valor = caracteristica.getDescricao();
        } else if ("agente".equals(campo.getName())) {
            valor = caracteristica.getUsuario().getLogin();
        } else if ("datacadastro".equals(campo.getName())) {
            valor = simpleDateFormat.format(caracteristica.getDatacadastro());
        } else if ("usuario".equals(campo.getName())) {
            valor = caracteristica.getUsuario().getLogin();
        } else {
            System.out.println("campo nao encontrado: " + campo.getName());
        }
        return valor;
    }

}
