/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.rn.emater.DataSources;

import br.gov.rn.emater.Classes.Agente;
import java.awt.Image;
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
public class ListaAgentesDS implements JRDataSource {

    private Iterator proximo;
    private Object valorAtual;
    private boolean irParaProximoprograma = true;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    public ListaAgentesDS(List lista) {
        super();
        this.proximo = lista.iterator();
    }

    public ListaAgentesDS() {
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
        Agente agente = (Agente) valorAtual;
        if ("idagente".equals(campo.getName())) {
            valor = agente.getIdagente();
        } else if ("descricao".equals(campo.getName())) {
            valor = agente.getDescricao();
        } else if ("agente".equals(campo.getName())) {
            valor = agente.getUsuario().getLogin();
        } else if ("datacadastro".equals(campo.getName())) {
            valor = simpleDateFormat.format(agente.getDatacadastro());
        } else if ("nomecientifico".equals(campo.getName())) {
            valor = agente.getNomecientifico();
        } else if ("imagem".equals(campo.getName())) {
            Image im = agente.getImagem().getImage();
            valor = im;

        } else if ("usuario".equals(campo.getName())) {
            valor = agente.getUsuario().getLogin();
        } else {
            System.out.println("campo nao encontrado: " + campo.getName());
        }
        return valor;
    }
}
