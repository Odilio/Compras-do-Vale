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
@Table(name = "deposito")
public class Deposito extends GenericModel{

	@Id
	@GeneratedValue
	public Integer dep_codigo;
	public String dep_titulo ;
	public Integer dep_cod_deposito;
	public Integer dep_parceiro;
	public boolean dep_defeito;
	public boolean dep_admin;
	}
