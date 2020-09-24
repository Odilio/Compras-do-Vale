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

import play.db.jpa.GenericModel;



@Table(name = "fornecedor")
@Entity
public class Fornecedor extends GenericModel{

	@Id
	@GeneratedValue()
	public Integer for_codigo;

	public String for_nome;
	public String for_cpf_cnpj;
	public String for_telefone_fixo;
	public String for_celular;
	public String for_email;
	public String for_rg;
	public Date for_data_alteracao;
	public Date for_data_cadastro;
	public String for_cep;
	public String for_bairro;
	public Integer for_numero;
	public String for_complemento;
	public String for_observacao;


}
