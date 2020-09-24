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
@Table(name = "depositomercadoria")
public class DepositoMercadoria extends GenericModel{

	@Id
	@GeneratedValue
	public Integer dpm_codigo;
	public String dpm_titulo ;
	public String dpm_referencia ;
	public Integer dpm_codigo_mercadoria ;
	public Integer dpm_quantidade;
	public Integer dpm_cod_deposito;
	public Integer dpm_cod_parceiro;
	
	}
