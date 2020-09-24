/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;



@Entity
@Table(name = "itemtabelapreco")
public class ItemTabelaPreco extends GenericModel{

	@Id
	@GeneratedValue
	public Integer itp_codigo;
	public String itp_titulo ;
	public String itp_referencia ;
	public Integer itp_cod_usuario;
	public Integer itp_cod_produto;
	public Integer itp_tabela_preco;
	public String itp_publicado;
	public double itp_preco;
	public double itp_preco_compra;
	public Date itp_data_hora;
	public Date itp_ultima_atualizacao;
}
