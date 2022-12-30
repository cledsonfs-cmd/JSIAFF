/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.rn.emater.DataSources;

import br.gov.rn.emater.Classes.Usuario;
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
public class ListaUsuarioDS implements JRDataSource {

    private Iterator proximo;
    private Object valorAtual;
    private boolean irParaProximoprograma = true;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    public ListaUsuarioDS(List lista) {
        super();
        this.proximo = lista.iterator();
    }

    public ListaUsuarioDS() {
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
        Usuario usuario = (Usuario) valorAtual;
        if ("idusuario".equals(campo.getName())) {
            valor = usuario.getIdUsuario();
        } else if ("login".equals(campo.getName())) {
            valor = usuario.getLogin();
        } else if ("acesso".equals(campo.getName())) {
            if (usuario.isAdministrador()) {
                valor = "Administrador";
            } else if (usuario.isOperador()) {
                valor = "Operador";
            } else if (usuario.isEspecialista()) {
                valor = "Especialista";
            } else if (usuario.isUsuario()) {
                valor = "Usuário";
            }
        } else if ("datacadastro".equals(campo.getName())) {
            valor = simpleDateFormat.format(usuario.getDatacadastro());
        } else {
            System.out.println("campo nao encontrado: " + campo.getName());
        }
        return valor;
    }
}
