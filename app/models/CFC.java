package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.db.jpa.GenericModel;

public class CFC extends GenericModel{

	@Id
	@GeneratedValue
	public int cfc_codigo;
	public int Estabelecimento; //Tamanho 4 - N
	public String Data; //Tamanho 8 - D
	public int Numero; //Tamanho 3 - N
	public int COOReducao; //Tamanho 6 - N 
	public int COOInicial; //Tamanho  6 - N
	public int COOFinal; //Tamanho  6 - N
	public int ContadorReducao; //Tamanho  6 - N
	public int 	NumeroCRO; //Tamanho  6 - N
	public double VendaBruta; //Tamanho 15 - V
	public String TotalizacaoGeral; //Tamanho  15 - V
	public double Cancelamentos; //Tamanho  15 - V
	public double Descontos; //Tamanho  15 - V
	public int Servico; //Tamanho 2 - N
	public String Observacao; //Tamanho  - T
	public String Fatura; //Tamanho 1 - C
	public List<Pedido> listas = new ArrayList<Pedido>();
		
	
}
