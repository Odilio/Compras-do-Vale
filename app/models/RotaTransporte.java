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



@Table(name = "rotatrasnporte")
@Entity
public class RotaTransporte extends GenericModel{

	@Id
	@GeneratedValue()
	public Integer rot_codigo;
	public Integer ped_codigo;
	public Integer usu_codigo;
	public String rot_nome;
	public String rot_nome_completo;
	public String rot_data_partida;
	public String rot_data_chegada;
	public String rot_endereco;
	public String rot_cep;
	public String rot_bairro;
	public String rot_cidade_origem;
	public Integer end_codigo_origem;
	public String rot_cidade_destino;
	public Integer end_codigo_destino;
	public String rot_uf;
	public Integer rot_numero;
	public String rot_complemento;
	public String rot_observacao;
	public boolean rot_executacao;
	public boolean rot_concluido;
}
