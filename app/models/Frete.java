package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="frete")
public class Frete extends GenericModel{

	@Id
	@GeneratedValue
	public int fre_codigo;
	public String fre_valor;
	public String fre_valor_RETIRADA;
	public String fre_valor_VALEC;
	public String fre_valor_PAC;
	public String fre_valor_SEDEX;
}
