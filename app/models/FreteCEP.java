package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="fretecep")
public class FreteCEP extends GenericModel{

	@Id
	@GeneratedValue
	public int frc_codigo;
	public String frc_nome;
	public String frc_valor;
	public String frc_CEP_estoque;
	public String frc_CEP;
	public int frc_dias;
	public String frc_tipo_frete;
}
