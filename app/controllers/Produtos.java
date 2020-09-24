package controllers;

import java.io.File;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import models.Cart;
import models.Categoria;
import models.Endereco;
import models.Frete;
import models.Marca;
import models.Pedido;
import models.Produto;
import models.Titulo;
import models.Usuario;
import play.Play;
import play.cache.Cache;
import play.i18n.Messages;
import play.libs.Images;
import play.mvc.Before;
import util.calculoFreteCorreio;

public class Produtos extends Application {
	
	@Before
	static void checkUser() {
		
		if (connected() == null) {
			flash.error("Por Favor, efetue seu login primeiro");
			Application.logar("");
		}
	}

	
	public static void index() {
	
		render();
	}
	
	public static void alterarStatusPedido() {
		
		render();
	}
	
	public static void cart() {
		List<Cart> lista = Cart.find("byUsu_codigoAndCar_checkout",
				renderArgs.get("user", Usuario.class).usu_codigo, false).fetch();
		double total = 0;
		for (Cart cart : lista) {
			total += cart.car_preco;
		}
		
		
	    Cache.set("cartcontador", lista.size(), "300mn");
		
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
		 Endereco endereco = Endereco.find("byUsu_codigo", renderArgs.get("user", Usuario.class).usu_codigo).first();
		render(lista,total,endereco,pedido);
	}
	
	public static void cart2() {
		List<Cart> lista = Cart.find("byUsu_codigoAndCar_checkout",
				renderArgs.get("user", Usuario.class).usu_codigo, false).fetch();
		double total = 0;
		for (Cart cart : lista) {
			total += cart.car_preco;
		}
		
		
	    Cache.set("cartcontador", lista.size(), "300mn");
		
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
		 Endereco endereco = Endereco.find("byUsu_codigo", renderArgs.get("user", Usuario.class).usu_codigo).first();
		render(lista,total,endereco,pedido);
	}
	
	public static void addCart(Produto produto) {
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
        if(pedido == null) {
        	pedido = new Pedido();
        	Date data = new Date();
		    pedido.ped_data = formatador.format(data);
	 	    pedido.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
	 	    pedido.ped_codigo_digitador = pedido.usu_codigo; 
	 	    pedido.ped_nome_usuario = renderArgs.get("user", Usuario.class).usu_nome_completo;
	 	    pedido.ped_estado = "aberto";
	 	    pedido.ped_checkout = false;
	 	    pedido.save();
        }
		Cart cart = new Cart();
		cart.car_referencia = produto.pro_referencia;
		cart.car_preco = produto.pro_preco;
		cart.pro_codigo = produto.pro_codigo;
		cart.par_codigo = produto.pro_cod_produtor;
		cart.car_caminho_ima = produto.pro_caminho_ima_thumb;
		cart.car_titulo = produto.pro_titulo;
		cart.car_codigo_afiliado = produto.pro_cod_usuario;
		cart.car_quantidade = produto.pro_quantidade;
		cart.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
		cart.ped_codigo= pedido.ped_codigo;
		cart.ped_data = pedido.ped_data;
		cart.save();
		
		cart() ;
	}
	
	public static void removeItemCart(Cart cart) {
		cart.delete();
		cart() ;
	}
	
	public static void boleto() {
		
		cart() ;
	}
	
	
	public static void checkout(Integer codigo) {
		
		List<Cart> lista = Cart.find("byPed_codigoAndCar_checkoutAndCar_finalizado",
				codigo, true,false).fetch();
        Endereco endereco = Endereco.find("byUsu_codigo", renderArgs.get("user", Usuario.class).usu_codigo).first();
        Frete fre_valor = new Frete();
        Integer pedido =0 ;
    	if (lista.size() > 0){
       
    		pedido = lista.get(0).ped_codigo;
    	
    	Pedido ped_frete = Pedido.find("byPed_codigo", pedido).first();
    	fre_valor.fre_valor = ped_frete.ped_frete;
    	
    	}
    	
	render(lista,endereco,fre_valor,pedido);
}
	
	public static void makeCheckout(Endereco endereco) {
		Date data = new Date();
		Integer codigoPedido = params.get("ped_codigo", Integer.class);
		String tipoFrete = params.get("ped_codigo_frete", String.class);
		Pedido pedido = Pedido.find("byPed_codigo",codigoPedido).first();
		pedido.ped_data_confirma = formatador.format(data);
		pedido.ped_tipo_frete = tipoFrete;
 	    pedido.ped_estado = "confirmado";
 	    pedido.ped_status = "status1";
 	    pedido.ped_checkout = true;
 	   
 		Double valor = 0.0;
 		
 	    endereco.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo; 
 		endereco.save();
		
 		List<Cart> lista = Cart.find("byPed_codigoAndCar_finalizado",
				pedido.ped_codigo,false).fetch();
 		
 	    calculoFreteCorreio frete =  new calculoFreteCorreio();
        DecimalFormat fmt = new DecimalFormat("0.00");
        pedido.ped_quantidade  = lista.size();
     
        if (tipoFrete.equalsIgnoreCase("SEDEX"))
        pedido.ped_frete = Double.parseDouble((frete.calcularFrete(endereco.end_cep,pedido.ped_quantidade,"40010")).replace(",", "."))+ "";
        if (tipoFrete.equalsIgnoreCase("PAC"))
            pedido.ped_frete = Double.parseDouble((frete.calcularFrete(endereco.end_cep,pedido.ped_quantidade,"41106")).replace(",", "."))+ "";
        if (tipoFrete.equalsIgnoreCase("VALE"))
            pedido.ped_frete = "4.00";
        if (tipoFrete.equalsIgnoreCase("RETIRADA"))
            pedido.ped_frete = "0.00";
        
 		
	     for (Cart cart2 : lista) {
        	valor += cart2.car_preco;
    		cart2.car_checkout = true;
    		cart2.car_status = "status1";
    		cart2.save();
    		
		}
     
	     Cache.delete("cartcontador_"+endereco.usu_codigo );
        
        pedido.ped_valor_itens = valor+"";
      
        pedido.ped_valor = ((valor)+Double.parseDouble(pedido.ped_frete))+"";
      	pedido.ped_valor = fmt.format(Double.parseDouble(pedido.ped_valor));
    	pedido.save();
        
   		 
     	Cache.set("contacontador_"+renderArgs.get("user", Usuario.class).usu_codigo,  Pedido.count("byPed_statusAndUsu_codigo", "status1",renderArgs.get("user", Usuario.class).usu_codigo));
		checkout(pedido.ped_codigo);
	}
	
	public static String removerAcentos(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}


public static void calcularFrete(String rev_texto, String tipoFrete) {
		Frete fre_valor = new Frete();
		calculoFreteCorreio frete = new calculoFreteCorreio();
		
		if (tipoFrete.equalsIgnoreCase("SEDEX")){
			fre_valor.fre_valor = frete.calcularFrete(rev_texto, 1, "40010");
			fre_valor.fre_valor_SEDEX =fre_valor.fre_valor;
			fre_valor.fre_valor_PAC = frete.calcularFrete(rev_texto, 1, "41106");
		}
		if (tipoFrete.equalsIgnoreCase("PAC")){
			fre_valor.fre_valor = frete.calcularFrete(rev_texto, 1, "41106");
			fre_valor.fre_valor_SEDEX =frete.calcularFrete(rev_texto, 1, "40010");
			fre_valor.fre_valor_PAC = fre_valor.fre_valor;
		}
		if (tipoFrete.equalsIgnoreCase("VALE")){
			fre_valor.fre_valor = "4.00";
			fre_valor.fre_valor_SEDEX =frete.calcularFrete(rev_texto, 1, "40010");
			fre_valor.fre_valor_PAC = frete.calcularFrete(rev_texto, 1, "41106");
		}
		if (tipoFrete.equalsIgnoreCase("RETIRADA")){
			fre_valor.fre_valor = "0.00";
			fre_valor.fre_valor_SEDEX =frete.calcularFrete(rev_texto, 1, "40010");
			fre_valor.fre_valor_PAC = frete.calcularFrete(rev_texto, 1, "41106");
		}
	
		renderJSON(fre_valor);

	}


	public static void aumentarQuantidade(String plus, String qtd) {
		int id = Integer.parseInt(plus);
		Cart cart = Cart.find("byCar_codigo",id).first();
		cart.car_quantidade = Integer.parseInt(qtd)+1;
		cart.save();
	renderJSON(plus);
	
}


}
