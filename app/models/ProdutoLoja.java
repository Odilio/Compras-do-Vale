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
@Table(name = "produtoloja")
public class ProdutoLoja extends GenericModel{

	@Id
	@GeneratedValue
	public Integer pro_codigo;
	public String pro_titulo_reduzido ;
	public String pro_categoria ;
	public Integer pro_prioridade;
	public Integer pro_cod_produtor;
	public Integer pro_cod_produto;
	public String pro_caminho_ima_thumb;
	public double pro_preco;
	public double pro_valor_premio;
}
