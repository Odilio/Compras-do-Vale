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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;



@Table(name = "parceiro")
@Entity
public class Parceiro extends GenericModel{

	@Id
	@GeneratedValue()
	public Integer par_codigo;
	public Integer usu_codigo;
	public Integer par_codigo_deposito;
	public String par_senha;
	public String par_nome;
	public String par_nome_completo;
	public String par_frase;
	public String par_cpf_cnpj;
	public String par_celular;
	public String par_email;
	public String par_rg;
	public Date par_data_alteracao;
	public Date par_data_cadastro;
	public String par_endereco;
	public String par_cep;
	public String par_bairro;
	public String par_login;
	public String par_cidade;
	public String par_uf;
	public Integer par_numero;
	public String par_complemento;
	public String par_observacao;
	public String par_cor_background;
	@Transient
	public Blob par_imagem;
	public String par_caminho_ima;
	@Transient
	public Blob par_back_imagem;
	public String par_back_caminho_ima;
}
