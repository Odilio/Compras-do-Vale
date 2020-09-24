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
@Table(name = "produto")
public class Produto extends GenericModel{

	@Id
	@GeneratedValue
	public Integer pro_codigo;
	public String pro_titulo ;
	public String pro_titulo_reduzido ;
	public String pro_referencia ;
	public String pro_texto;
	public Integer pro_cod_usuario;
	public String pro_nome_usuario;
	public String pro_categoria ;
	public String pro_genero;
	public String pro_sub_categoria ;
	public int pro_cod_grupo_produto;
	public boolean pro_espelhado ;
	public boolean pro_habilitar_opcoes ;
	public Integer pro_prioridade;
	public String pro_descricao ;
	public String pro_cor ;
	public String pro_estilo ;
	public Integer pro_quantidade;
	public int pro_quantidade_estoque;
	public String pro_status;
	public int pro_cod_marca;
	public String pro_marca;
	public String pro_peso;
	public boolean pro_usado;
	public String pro_email_produtor;
	public String pro_nome_produtor;
	public Integer pro_cod_produtor;
	@Transient
	public Blob pro_imagem;
	public String pro_caminho_ima;
	@Transient
	public  Blob pro_imagem2;
	public String pro_caminho_ima2;
	@Transient
	public  Blob pro_imagem3;
	public String pro_caminho_ima3;
	public String pro_caminho_ima_thumb;
	public String pro_publicado;
	public double pro_preco;
	public double pro_preco_compra;
	public double pro_valor_premio;
	public int pro_porcentagem_premio;
	public Date pro_data_hora;
	public Date pro_ultima_atualizacao;
}
