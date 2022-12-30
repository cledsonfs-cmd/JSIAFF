/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.rn.emater.DataSources;

import br.gov.rn.emater.Classes.Doenca;
import br.gov.rn.emater.Classes.Planta;
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
public class ListaPlantasDS implements JRDataSource {

    private Iterator proximo;
    private Object valorAtual;
    private boolean irParaProximoprograma = true;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    public ListaPlantasDS(List lista) {
        super();
        this.proximo = lista.iterator();
    }

    public ListaPlantasDS() {
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
        Planta planta = (Planta) valorAtual;
        if ("idplanta".equals(campo.getName())) {
            valor = planta.getIdplanta();
        } else if ("nomepopular".equals(campo.getName())) {
            valor = planta.getNomepopular();
        } else if ("nomecientifico".equals(campo.getName())) {
            valor = planta.getNomecientifico();
        } else if ("datacadastro".equals(campo.getName())) {
            valor = simpleDateFormat.format(planta.getDataCadastro());
        } else if ("login".equals(campo.getName())) {
            valor = planta.getUsuario().getLogin().trim();
        } else if ("descricao".equals(campo.getName())) {
            valor = planta.getGenero().getDescricao();
        } else {
            System.out.println("campo nao encontrado: " + campo.getName());
        }
        return valor;
    }
}
