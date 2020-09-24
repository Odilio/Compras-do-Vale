package controllers;

import play.*;
import play.cache.Cache;
import play.data.validation.Valid;
import play.db.jpa.Blob;
import play.i18n.Lang;
import play.i18n.Messages;
import play.libs.Crypto;
import play.libs.Images;
import play.libs.Mail;
import play.libs.OAuth2;
import play.modules.paginate.ValuePaginator;
import play.mvc.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.microtripit.mandrillapp.lutung.view.MandrillUserInfo;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;

import models.*;
import notifiers.Notifier;
public class Admin extends Application {
	
		@Before
		static void checkUser() {
			
			if (connected() == null) {
				flash.error("Please log in first");
				Application.logar("");
			}
			 if(connected().usu_role != 1)
				 Application.logar("");
			 
		}
		
		 public static Connection conn;
		 
		 public static void index() {
			 List<Pedido> pedidos = new ArrayList<Pedido>();
			 Pedido a = new Pedido();
		
			 try {
					Class.forName("org.postgresql.Driver");
					 conn = DriverManager.getConnection("jdbc:postgresql://107.170.0.146/vendasdb?user=vendas&password=vendas99337903");
					//Statement stm = conn.createStatement();
					PreparedStatement preparedStatement  = conn.prepareStatement(" SELECT ped_codigo,ped_quantidade,ped_valor FROM  pedido WHERE EXTRACT(MONTH FROM datapedido) = ?");
					preparedStatement.setInt(1, 2);
					ResultSet rs = preparedStatement.executeQuery();//stm.executeQuery(" SELECT ped_codigo,ped_quantidade,ped_valor FROM  pedido WHERE EXTRACT(MONTH FROM datapedido) = 2");
					
					while (rs.next()) {
						a.ped_codigo = rs.getInt("ped_codigo");	
						a.ped_data = rs.getString("ped_data");
						a.ped_quantidade = rs.getInt("ped_quantidade");
						a.ped_valor = rs.getString("ped_valor");	
						
						pedidos.add(a); 
						a = new Pedido();
					}
					
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 render(pedidos);
		    }

		 public static void addProduto(Produto produto) {
				Usuario ze = renderArgs.get("user", Usuario.class);
		
				int eixo_x = 420;
				int eixo_y = 360;
				
				/*if (produto.find("byPro_referencia", produto.pro_referencia).first() != null){
					flash.error("Referencia já existe!");
					cadastrarProduto(new Produto()) ;
				}*/
				
			    
				Parceiro parceiro = Parceiro.find("byPar_codigo", produto.pro_cod_produtor).first();	
				
			    produto.pro_email_produtor = parceiro.par_email;
			    produto.pro_nome_produtor = "Compras do Vale";
				produto.pro_status = "ativo";
				produto.pro_cod_usuario = ze.usu_codigo;
				produto.pro_nome_usuario = "Compras do Vale";
				DateTime dat = new DateTime();
				if (produto.pro_data_hora == null)
					produto.pro_data_hora = dat.toDate();
					produto.pro_ultima_atualizacao = dat.toDate();
				
				if (produto.pro_imagem != null) {
					File outputFile = Play.getFile("/public/images/itens/" + produto.pro_referencia  + "num1" + ".jpg");
					Images.resize(produto.pro_imagem.getFile(), outputFile, eixo_x, eixo_y);
					produto.pro_caminho_ima  = "/public/images/itens/" + produto.pro_referencia + "num1" + ".jpg";
					File outputFile4 = Play.getFile("/public/images/itens-thumb/" + produto.pro_referencia  + "num1 (Copiar)" + ".jpg");
					Images.resize(produto.pro_imagem.getFile(), outputFile4, 195, 167);
					produto.pro_caminho_ima_thumb  = "/public/images/itens-thumb/" + produto.pro_referencia + "num1 (Copiar)" + ".jpg";
			
		 		}
			
				if (produto.pro_imagem2 != null) {
					File outputFile2 = Play.getFile("/public/images/itens/" + produto.pro_referencia  + "num2" + ".jpg");
					Images.resize(produto.pro_imagem2.getFile(), outputFile2, eixo_x, eixo_y);
					produto.pro_caminho_ima2  = "/public/images/itens/" + produto.pro_referencia + "num2" + ".jpg";
			
				}
			
				if (produto.pro_imagem3 != null) {
					File outputFile3 = Play.getFile("/public/images/itens/" + produto.pro_referencia  + "num3" + ".jpg");
					Images.resize(produto.pro_imagem3.getFile(), outputFile3, eixo_x, eixo_y);
					produto.pro_caminho_ima3  = "/public/images/itens/" + produto.pro_referencia + "num3" + ".jpg";
			
				}
				
				int size = produto.pro_titulo.length();
				if (size > 24 )
					produto.pro_titulo_reduzido = produto.pro_titulo.substring(0,24);
				else 
					produto.pro_titulo_reduzido = produto.pro_titulo;
				produto.save();
				DepositoMercadoria deposito = DepositoMercadoria.find("byDpm_codigo_mercadoria",	produto.pro_codigo).first();
			    if(deposito == null) {
			    	deposito = new DepositoMercadoria();
			    	deposito.dpm_cod_deposito = 1;
			    	deposito.dpm_cod_parceiro = produto.pro_cod_usuario;
			    	deposito.dpm_referencia = produto.pro_referencia;
			    	deposito.dpm_codigo_mercadoria = produto.pro_codigo;
			    	deposito.dpm_quantidade = produto.pro_quantidade_estoque;
			    	deposito.save();
			    }
			    
			    ItemTabelaPreco itemTabelaPreco = ItemTabelaPreco.find("byItp_cod_produto",	produto.pro_codigo).first();
			    if(itemTabelaPreco == null) {
			    	itemTabelaPreco = new ItemTabelaPreco();
			    	itemTabelaPreco.itp_cod_produto = produto.pro_codigo;
			    	itemTabelaPreco.itp_preco = produto.pro_preco;
			    	itemTabelaPreco.itp_tabela_preco = 1;
			    	itemTabelaPreco.itp_cod_usuario = ze.usu_codigo;
			    	itemTabelaPreco.save();
			    }
			    
			    produto.pro_valor_premio = produto.pro_preco / produto.pro_porcentagem_premio;
			    //adiciona produto a loja
			    ProdutoLoja produtoloja = new ProdutoLoja();
			    produtoloja.pro_caminho_ima_thumb = produto.pro_caminho_ima_thumb;
			    produtoloja.pro_categoria = produto.pro_categoria;
			    produtoloja.pro_cod_produtor = produto.pro_cod_usuario;
			    produtoloja.pro_cod_produto = produto.pro_codigo;
			    produtoloja.pro_preco = produto.pro_preco;
			    produtoloja.pro_titulo_reduzido = produto.pro_titulo_reduzido;
			    produtoloja.pro_valor_premio = produto.pro_valor_premio;
			    produtoloja.pro_prioridade = 1;
			    
			    produtoloja.save();
				Cache.set("produto_" + produto.pro_codigo, produto);
				
				List<Produto> produtos = Produto.find("order by pro_prioridade asc").fetch();
			    Cache.set("produtos", produtos);
			    
				cadastrarProduto(new Produto()) ;
				
				}
			
		 
		 public static void editarProduto(Produto produto) {
				
				cadastrarProduto(produto);
			}
		 
		 
		 public static void catalogo(Produto produto) {
			 List<Produto> listaProdutos = Produto.find("order by pro_codigo asc").fetch();
			 	
			 render(listaProdutos);
			}
		 
		 public static void catalogoTabela(Produto produto) {
			 List<Produto> listaProdutos = Produto.find("order by pro_codigo asc").fetch();
			 	
			 render(listaProdutos);
			}
		 
	public static void gerarMarca() {
			 Marca marca = new Marca();
			 List<Produto> produtos = Produto.find("order by pro_prioridade asc").fetch();
			 for (Produto produto : produtos) {
				 marca =    Marca.find("byMar_nome", produto.pro_marca).first() ;
						produto.pro_cod_marca = marca.mar_codigo;
				 	
						produto.save();
			 }
			
			}
		
	public static void updateProdutosLoja() {
		List<Produto> produtos = Produto.find("pro_titulo_reduzido like '%apinha%' " ).fetch();
		  ProdutoLoja produtoloja = new ProdutoLoja();
		  for (Produto produto : produtos) {
			    produtoloja.pro_caminho_ima_thumb = produto.pro_caminho_ima_thumb;
			    produtoloja.pro_categoria = produto.pro_categoria;
			    produtoloja.pro_cod_produtor = produto.pro_cod_usuario;
			    produtoloja.pro_cod_produto = produto.pro_codigo;
			    produtoloja.pro_preco = produto.pro_preco;
			    produtoloja.pro_titulo_reduzido = produto.pro_titulo_reduzido;
			    produtoloja.pro_valor_premio = produto.pro_valor_premio;
			    produtoloja.pro_prioridade = 1;
			    produtoloja.save();
			    produtoloja = new ProdutoLoja();
		  }
	}
		 public static void updateProdutos() {
				List<Produto> produtos = Produto.find("order by pro_prioridade asc").fetch();
			    Cache.set("produtos", produtos);	
			    for (Produto produto : produtos) {
				   Cache.set("produto_" + produto.pro_codigo, produto);
				}
			   Cache.set("contacontador",  Pedido.count("byPed_statusAndUsu_codigo", "status1",renderArgs.get("user", Usuario.class).usu_codigo));
			   Long lista = Cart.count("byUsu_codigoAndCar_checkout",
						renderArgs.get("user", Usuario.class).usu_codigo, false);
			    Cache.set("cartcontador", lista, "300mn");
			admin();
			}
	
		 public static void atualizaDataCarts() {
				List<Pedido> produtos = Pedido.all().fetch();
				Cart cart = new Cart() ;
			    for (Pedido pedido : produtos) {
			    	cart = Cart.find("byPed_codigo", pedido.ped_codigo).first();
			    	if (cart != null){
			    	cart.ped_data = pedido.ped_data;
			    	cart.save();
			    	}
				}
			admin();
			}
		 
		 public static void cadastrarProduto(Produto produto) {
			 List<Parceiro> parceiros = Parceiro.all().fetch();	
			 List<GrupoProduto> listaGrupos = GrupoProduto.all().fetch();
				render(produto,parceiros,listaGrupos);
			}

		 public static void mostrarPedido(Integer codigo) {
			 Pedido pedido = Pedido.find("byPed_codigo", codigo).first();	
			 pedido.ped_status = Messages.get(pedido.ped_status);
			 List<Cart> cart = Cart.find("byPed_codigo", pedido.ped_codigo).fetch();
			 Endereco endereco = Endereco.find("byUsu_codigo", pedido.usu_codigo).first();
				render(pedido,cart,endereco);
			}
		 
		 
		 public static void mostrarCliente(Integer codigo) {
			 List<Pedido> pedidos = Pedido.find("byUsu_codigo", codigo).fetch();
				for (Pedido pedido : pedidos) {
					pedido.ped_status = Messages.get(pedido.ped_status);
				}
			 Endereco endereco = Endereco.find("byUsu_codigo", codigo).first();
				render(endereco,pedidos);
			}
		 
		 public static void admin() {		 
			 Double valorTotal = 0.0;
			 Double valorCustoTotal = 0.0;
			 Integer quantidade = 0;
			 Double valorAReceber = 0.0;
			 Double valorRecebido = 0.0;
			 DecimalFormat fmt = new DecimalFormat("0.00");
			 List<Produto> listaProdutos = Cache.get("produtos", List.class);
			    if(listaProdutos == null) {
			    	listaProdutos = Produto.find("order by pro_prioridade asc").fetch();
			        Cache.set("produtos", listaProdutos);
			    }
			 for (Produto produto : listaProdutos) {
				 quantidade = quantidade + produto.pro_quantidade_estoque;
				 valorTotal = valorTotal + (produto.pro_preco * produto.pro_quantidade_estoque);
				 //valorCustoTotal = valorCustoTotal + produto.pro_preco;
				 valorCustoTotal = valorCustoTotal + (produto.pro_preco_compra * produto.pro_quantidade_estoque);
			 }
			 List<Titulo> titulos = Titulo.find("byTit_parceiro", 1).fetch();
			 for (Titulo titulo : titulos) {
			   if (titulo.tit_estado.equalsIgnoreCase("aberto"))
				valorAReceber += titulo.tit_valor;
			   if (titulo.tit_estado.equalsIgnoreCase("confirmado"))
				valorRecebido += titulo.tit_valor;
		     }
			valorTotal = Double.parseDouble(fmt.format(valorTotal).replace(",", "."));
			//valorCustoTotal = Double.parseDouble(fmt.format(valorCustoTotal));
			long qtdProdutos = Produto.count();
			long qtdPedidos = Pedido.count();
			long qtdVendas = Pedido.count("byPed_status","status4");
			long qtdCategorias = Categoria.count();
			long qtdUsuarios = Usuario.count();
		    	render(qtdVendas,qtdProdutos,qtdCategorias,qtdUsuarios,qtdPedidos,quantidade,valorTotal,valorCustoTotal,valorAReceber,valorRecebido);
		    }
		 
		 public static void listaProdutos() {
			 List<Produto> listaProdutos = Produto.all().fetch();	
			 	
			 render(listaProdutos);
		    }
		 
		 public static void listaClientes() {
			 List<Usuario> listaProdutos = Usuario.all().fetch();	
			 	
			 render(listaProdutos);
		    }
		 
		 
		 public static void listaDepositos() {
			 List<Deposito> listaProdutos = Deposito.find("order by dep_codigo ").fetch();	
			 	
			 render(listaProdutos);
		    }
		 
		 public static void listaDepositoMercadoria() {
			 List<DepositoMercadoria> lista = DepositoMercadoria.all().fetch();	
			 ValuePaginator listaProdutos = new ValuePaginator(lista);
			 listaProdutos.setPageSize(12);
			 render(listaProdutos);
		    }
		 
		 public static void listaTitulos(List<Titulo> listaProdutos) {
			 	listaProdutos = Titulo.find("order by ped_codigo desc").fetch();	
			 	
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(12);
			 render(lista);
		    }
	
		 
		 public static void cancelarTitulo(Titulo titulo) {
		     Titulo ped  = Titulo.find("byTit_codigo", titulo.tit_codigo).first();
		   	 ped.tit_estado = "cancelado";
		   	 ped.save();
		   	listaTitulos(null);
		   }
		 
		 
		   public static void itensPedidos(Integer codigo) {
				List<Cart> listaProdutos = Cart.find("byPed_codigo", codigo).fetch();
				for (Cart pedido : listaProdutos) {
					pedido.car_status = Messages.get(pedido.car_status);
				}
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(12);
				
				render(lista);
			}
		   		 
		   public static void movimentacoes(Integer codigo) {
				List<Cart> listaProdutos = Cart.find("byPro_codigo", codigo).fetch();
				for (Cart pedido : listaProdutos) {
					pedido.car_status = Messages.get(pedido.car_status);
				}
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(12);
				
				render(lista);
			}
		   
			  public static void aguardandoPagamento() {
				List<Pedido> listaProdutos = Pedido.find("byPed_status", "status1").fetch();
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(7);
				System.out.println(Messages.get("status1"));
				render(lista);
			}
		    
		    public static void aguardandoEnvio() {
		  		List<Cart> listaProdutos = Cart.find("byCar_status", "status2").fetch();
		  		ValuePaginator lista = new ValuePaginator(listaProdutos);
		  		lista.setPageSize(7);
		  		
		  		render(lista);
		  	}
		    
		    public static void enviados() {
		  		List<Cart> listaProdutos = Cart.find("byCar_status", "status3").fetch();
		  		ValuePaginator lista = new ValuePaginator(listaProdutos);
		  		lista.setPageSize(7);
		  		
		  		render(lista);
		  	}
		  
		  
		  public static void listaVendas() {
				List<Pedido> listaProdutos = Pedido.find("byPed_status" , "status5").fetch();
		    	
		    	for (Pedido pedido : listaProdutos) {
		    	
					pedido.ped_status = Messages.get(pedido.ped_status);
				}
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(7);
				
				render(lista);
			}
 
			public static void alterarStatusPedido() {
				
				render();
			}
			
    public static void pedidos() {
    	List<Pedido> listaProdutos = Pedido.find("order by ped_codigo desc").fetch();
    	
    	for (Pedido pedido : listaProdutos) {
    	
			pedido.ped_status = Messages.get(pedido.ped_status);
		}
		ValuePaginator lista = new ValuePaginator(listaProdutos);
		lista.setPageSize(7);
		
		render(lista);
	}

    
	public static void novaCategoria() {
		List<Categoria> categorias = Categoria.all().fetch();
		render(categorias);
	}
	
	public static void addCategoria(Categoria categoria) {
		categoria.save();
		novaCategoria() ;
	}
	
	public static void cadastrarGrupo() {
		List<GrupoProduto> grupos = GrupoProduto.all().fetch();
		render(grupos);
	}
	
	public static void addGrupo(GrupoProduto grupo) {
		grupo.save();
		cadastrarGrupo() ;
	}
	
	public static void cadastrarFreteCEP() {
		List<FreteCEP> freteCEPs = FreteCEP.all().fetch();
		render(freteCEPs);
	}
	
	public static void addFreteCEP(FreteCEP freteCEP) {
		freteCEP.save();
		cadastrarFreteCEP() ;
	}
	
	public static void cadastrarOpcao() {
		List<Opcao> opcoes = Opcao.all().fetch();
		render(opcoes);
	}
	
	public static void cadastrarMarca() {
		List<Marca> marcas = Marca.all().fetch();
		render(marcas);
	}
	
	
	public static void addMarca(Marca marca) {
		marca.save();
		cadastrarMarca() ;
	}

	public static void criarFornecedor() {
		List<Fornecedor> fornecedores = Fornecedor.all().fetch();
		render(fornecedores);
	}
	
	public static void addFornecedor(Fornecedor fornecedor) {
		fornecedor.save();
		criarFornecedor() ;
	}
	
	public static void criarDeposito() {
		List<Deposito> depositos = Deposito.find("order by dep_codigo ").fetch();
		render(depositos);
	}
	
	public static void addDeposito(Deposito deposito) {
		deposito.save();
		criarDeposito() ;
	}
	
	public static void cadastrarParceiro() {
		List<Parceiro> parceiros = Parceiro.all().fetch();
		ValuePaginator lista = new ValuePaginator(parceiros);
		lista.setPageSize(12);
		render(lista);
	}
	
	
	public static void addParceiro(Parceiro parceiro) {
		Date data = new Date();
		Deposito deposito = new Deposito();
		Deposito depositoDefeito = new Deposito();
		Usuario usuParceiro = new Usuario();
		
		usuParceiro.usu_data_cadastro = formatador.format(data);
		usuParceiro.usu_role = 2;
		usuParceiro.usu_email = parceiro.par_email;
		usuParceiro.usu_nome_completo = parceiro.par_nome;
		usuParceiro.usu_nome = parceiro.par_login;
		Crypto a = new Crypto();
        String senha = a.encryptAES("123456");
		usuParceiro.usu_senha = senha;
		
		if (parceiro.par_imagem != null) {
			File outputFile = Play.getFile("/public/images/logoparceiros/" + parceiro.par_codigo  + "num1" + ".jpg");
			Images.resize(parceiro.par_imagem.getFile(), outputFile, 500, 500);
			parceiro.par_caminho_ima  = "/public/images/logoparceiros/" + parceiro.par_codigo + "num1" + ".jpg";
	
 		}
		
		usuParceiro.save();
		
		
		Endereco endereco = new Endereco();
		endereco.end_celular = parceiro.par_celular;
		endereco.end_cep = parceiro.par_cep;
		endereco.end_email = parceiro.par_email;
		endereco.end_cidade = parceiro.par_cidade;
		endereco.end_endereco1 = parceiro.par_endereco;
		endereco.end_endereco2 = parceiro.par_complemento;
		endereco.end_estado = parceiro.par_uf;
		endereco.end_nome_completo = parceiro.par_nome;
		endereco.end_nome = parceiro.par_login;
		endereco.usu_codigo = usuParceiro.usu_codigo;
		endereco.save();
		
		deposito.dep_parceiro = usuParceiro.usu_codigo;
		depositoDefeito.dep_parceiro = usuParceiro.usu_codigo;
		
		deposito.dep_titulo= "Estoque Principal";
		depositoDefeito.dep_titulo = "Estoque Defeito";
		
		deposito.dep_defeito = false;
		depositoDefeito.dep_defeito = true;
		
		deposito.dep_cod_deposito = 1;
		depositoDefeito.dep_cod_deposito = 2;
		deposito.save();
		depositoDefeito.save();
		
		parceiro.par_codigo_deposito = deposito.dep_cod_deposito;
		parceiro.usu_codigo = usuParceiro.usu_codigo; 
		parceiro.par_back_caminho_ima = "/public/images/capaparceiro.jpg";
		parceiro.save();
		cadastrarParceiro();
	}
	
	
	public static void criarPedido() {
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
		List<Cart> lista = new ArrayList();
		if(pedido != null) {
			lista = Cart.find("ped_codigo = ? ", pedido.ped_codigo).fetch();
			render(lista,pedido);
		}
		
		pedido = new Pedido();
		Date data = new Date();
	    pedido.ped_data = formatador.format(data);
 	    pedido.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
 	    pedido.ped_nome_usuario = renderArgs.get("user", Usuario.class).usu_nome_completo;
 	    pedido.ped_estado = "aberto";
 	    pedido.ped_checkout = false;
 	    pedido.save();
		render(lista,pedido);
	}
	
	public static void criarTransferencia() {
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
		List<Cart> lista = new ArrayList();
		if(pedido != null) {
			lista = Cart.find("ped_codigo = ?1 ", pedido.ped_codigo).fetch();
			render(lista,pedido);
		}
		
		pedido = new Pedido();
		Date data = new Date();
	    pedido.ped_data = formatador.format(data);
 	    pedido.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
 	    pedido.ped_nome_usuario = renderArgs.get("user", Usuario.class).usu_nome_completo;
 	    pedido.ped_estado = "aberto";
 	    pedido.ped_checkout = false;
 	    pedido.save();
		render(lista,pedido);
	}
	
	
	public static void addItemPedido(String rev_texto,String pro_quantidade) {
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
        if(pedido == null) {
        	pedido = new Pedido();
        	Date data = new Date();
		    pedido.ped_data = formatador.format(data);
	 	    pedido.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
	 	    pedido.ped_nome_usuario = renderArgs.get("user", Usuario.class).usu_nome_completo;
	 	    pedido.ped_estado = "aberto";
	 	    pedido.ped_checkout = false;
	 	    pedido.save();
        }
		rev_texto = "%"+rev_texto+"%";
		Produto produto = Produto.find("pro_referencia like ?", rev_texto).first();
		produto.pro_quantidade = Integer.parseInt(pro_quantidade);
		Cart cart = new Cart();
		cart.car_referencia = produto.pro_referencia;
		cart.car_preco = produto.pro_preco;
		cart.car_caminho_ima = produto.pro_caminho_ima;
		cart.car_titulo = produto.pro_titulo;
		cart.car_quantidade = produto.pro_quantidade;
		cart.par_codigo= produto.pro_cod_produtor;
		cart.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
		cart.ped_codigo= pedido.ped_codigo;
		cart.save();
		renderJSON(cart);
	}
	
	  public static void cancelarItem(Integer codigo) {
		   	 Cart cart  = Cart.find("byCar_codigo", codigo).first();
		   	 cart.delete();
		     criarPedido();
		   }
	  
	  public static void confirmaTitulo(Titulo titulo) {
			Date data = new Date();
			 DecimalFormat fmt = new DecimalFormat("0.00");
			Titulo tituloAtual = Titulo.find("byTit_codigo", titulo.tit_codigo).first();
			 Pedido pedidoAtual = Pedido.find("byPed_codigo", titulo.ped_codigo).first();
			 pedidoAtual.ped_forma = titulo.tit_forma;
			 pedidoAtual.ped_data_confirma = formatador.format(data);
			 pedidoAtual.ped_estado = "confirmado";
		 	 pedidoAtual.ped_status = "status2";
		 	 pedidoAtual.ped_checkout = true;
			 pedidoAtual.save();
	 		 
			 tituloAtual.tit_forma = titulo.tit_forma;
			 tituloAtual.tit_data_liquidacao = formatador.format(data);
			 tituloAtual.tit_estado = "confirmado";
			 
			 titulo.save(); 
			 if (titulo.tit_forma.equalsIgnoreCase("Paypal"))
			 {
				 Titulo pagar = new Titulo();
				 
				 pagar.tit_data_emissao = formatador.format(data);
				 pagar.tit_cliente = pedidoAtual.usu_codigo;
				 pagar.tit_forma = pedidoAtual.ped_forma;
				 pagar.ped_codigo = pedidoAtual.ped_codigo;
				 pagar.tit_valor = Double.parseDouble(fmt.format((tituloAtual.tit_valor * 0.07) )) ;
				 pagar.tit_tipo = "pagar";
				 pagar.tit_estado = "confirmado";
					 
				 pagar.save();
			 }
			 
			 Cart cart = Cart.find("byCar_codigo", titulo.car_codigo).first();
			 DepositoMercadoria dpm = DepositoMercadoria.find("byDpm_codigo_mercadoria", cart.pro_codigo).first();
			 dpm.dpm_quantidade  = dpm.dpm_quantidade - cart.car_quantidade;
			 dpm.save();
			 
			 Produto produto = Produto.find("byPro_codigo", cart.pro_codigo).first();
			 produto.pro_quantidade_estoque  = produto.pro_quantidade_estoque - cart.car_quantidade;
			 produto.save();
			 listaTitulos(null);
		   }
	  	  
	  
	  public static void confirmacaoPedido(Pedido pedido) {
			 Date data = new Date();
		     Pedido pedidoAtual = Pedido.find("byPed_codigo", pedido.ped_codigo).first();
			 pedidoAtual.ped_forma = pedido.ped_forma;
			 pedido.ped_data_confirma = formatador.format(data);
		 	 pedido.ped_estado = "confirmado";
		 	 pedido.ped_status = "status1";
		 	 pedido.ped_checkout = true;
		 	 pedidoAtual.ped_quantidade = (int) Cart.count("byPed_codigo",	pedido.ped_codigo);
			 pedidoAtual.save();
	 			
			 Titulo pagar = new Titulo();
			 Titulo receber = new Titulo();
			 
			 pagar.tit_data_emissao = formatador.format(data);
			 receber.tit_data_emissao = formatador.format(data);
			 
			 pagar.tit_cliente = pedido.usu_codigo;
			 receber.tit_cliente = pedido.usu_codigo;
			 
			 pagar.tit_parceiro = pedido.usu_codigo;
			 
			 pagar.tit_forma = pedido.ped_forma;
			 receber.tit_forma = pedido.ped_forma;
			 
			 pagar.ped_codigo = pedido.ped_codigo;
			 receber.ped_codigo = pedido.ped_codigo;
			 
			 pagar.tit_valor = Double.parseDouble(pedido.ped_valor)/10 ;
			 receber.tit_valor = Double.parseDouble(pedido.ped_valor)/10 * 9;
			 
			 pagar.tit_tipo = "pagar";
			 receber.tit_tipo = "receber";
			 
			 pagar.tit_estado = "aberto";
			 receber.tit_estado = "aberto";
			 
			 pagar.save();
			 receber.save();
			
			 
			 if (pedido.ped_forma.equalsIgnoreCase("Paypal"))
			 {
				 
			 }
	    	 criarPedido();
		   }
	  
	  
	  public static void confirmacaoPagamento(Integer codigo) {
			 Pedido pedido = Pedido.find("byPed_codigo", codigo).first();
			 
			 //debitar itens dos estoque após confirmar
			 /*List<Cart> listaProdutos = Cart.find("byPed_codigo", codigo).fetch();
	    	    Produto pro = Produto.find("byPro_codigo ", "").first();*/
	 			pedido.ped_status = Messages.get(pedido.ped_status);
		     render(pedido);
		   }
	  
	  public static void buscaTitulo(Titulo titulo) {
			List<Titulo> titulos = Titulo.all().fetch();
			System.out.println("tit_estado:" + titulo.tit_estado);
			System.out.println("mudou a  " + titulos.size());
			listaTitulos(titulos);
		
		   }
	  
	  
		public static void listarGrupoProduto() {
			List<GrupoProduto> grupoProduto = GrupoProduto.all().fetch();
	       
			renderJSON(grupoProduto);
		}
	  
	  
	  public static void statusProdutoEntregue(String rev_text) {
		  String arr []  = rev_text.split("-");
		  RotaTransporte rota = new RotaTransporte();
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  rota = RotaTransporte.find("byPed_codigo", pedido.ped_codigo).first();
				  if(rota != null) rota.delete();
				  pedido.ped_status = "status4"  ;
				  pedido.save();
			  }
		}
		  
		   }
	
	  public static void statusProdutoCancelado(String rev_text) {
		  String arr []  = rev_text.split("-");
		  RotaTransporte rota = new RotaTransporte();
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();	
			  if (pedido != null){
				  rota = RotaTransporte.find("byPed_codigo", pedido.ped_codigo).first();
				  if(rota != null) rota.delete();
				  pedido.ped_status = "status5"  ;
				  pedido.save();
			  }
		}
		  
		   }
	 
	  
	  public static void statusProdutoParaEnvio(String rev_text) {
		  RotaTransporte rota = new RotaTransporte();
		  Date data = new Date();
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(data); // Objeto Date() do usuário
		  cal.add(cal.DAY_OF_MONTH, +6);
		  String ped_data = formatador.format(cal.getTime());
		  String arr []  = rev_text.split("-");
		  Pedido pedido = null;
		  Endereco endereco = new Endereco();
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  pedido.ped_status = "status2";
				  pedido.save();
				  endereco =  Endereco.find("byUsu_codigo", pedido.usu_codigo).first();
				  criarRotas(rota, data, ped_data, pedido, endereco);
				  criarTitulos(pedido);
			  }
		}
		  
		   }

	  public static void statusProdutoEnviado(String rev_text) {
		  String arr []  = rev_text.split("-");
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  pedido.ped_status = "status3"  ;
				  pedido.save();
			  }
		}
		  
		   }
	
	  public static void statusProdutoAguardandoPagamento(String rev_text) {
		  String arr []  = rev_text.split("-");
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  pedido.ped_status ="status1" ;
				  pedido.save();
			  }
		}
		  
		   }
	  
		public static void productDetails(Integer codigo ) {
			Produto produto = Cache.get("produto_" + codigo, Produto.class);
		    if(produto == null) {
			 produto = Produto.find("byPro_codigo",
					codigo).first();
			 Cache.set("produto_" + codigo, produto, "30mn");
		    }
		    render(produto);
		}
		
		
		private static void criarRotas(RotaTransporte rota, Date data, String ped_data, Pedido pedido, Endereco endereco) {
			rota.ped_codigo = pedido.ped_codigo;
			  rota.rot_nome = pedido.ped_nome_usuario;
			  rota.usu_codigo = pedido.usu_codigo;
			  rota.rot_nome_completo = pedido.ped_nome_usuario;
			  rota.rot_cidade_origem = "Tabuleiro do Norte";
			  rota.rot_cidade_destino = endereco.end_cidade;
			  rota.end_codigo_destino = endereco.end_codigo;
			  rota.rot_cep = endereco.end_cep;
			  rota.rot_endereco = endereco.end_endereco1;
			  rota.rot_complemento = endereco.end_endereco2;
			  rota.rot_data_partida = formatador.format(data);
			  rota.rot_data_chegada = ped_data;
			  rota.save();
		}
		
		
	public static void criarTitulos(Pedido pedido ) {
			double freteTitulo =  Double.parseDouble(pedido.ped_frete);
			List<Cart> lista = Cart.find("byPed_codigoAndCar_finalizado",
					pedido.ped_codigo,false).fetch();
			Titulo tituloFrete = new Titulo();
			tituloFrete.tit_data_emissao =pedido.ped_data_confirma;
			tituloFrete.tit_estado = "aberto";
			tituloFrete.tit_tipo = "receber";
			tituloFrete.ped_codigo = pedido.ped_codigo;
			tituloFrete.car_codigo = 0;
			tituloFrete.tit_cliente = pedido.usu_codigo;
			tituloFrete.tit_valor = freteTitulo;
			tituloFrete.tit_parceiro = 1;
			tituloFrete.save();
		 
			Titulo receber = new Titulo();
			Titulo pagar = new Titulo();
 		
	     for (Cart cart2 : lista) {
	    	 	 receber.tit_data_emissao = pedido.ped_data_confirma;
    			 receber.tit_estado = "aberto";
    			 receber.tit_tipo = "receber";
    			 receber.ped_codigo = pedido.ped_codigo;
    			 receber.car_codigo = cart2.car_codigo;
    			 receber.tit_cliente = pedido.usu_codigo;
    			 receber.tit_valor = cart2.car_preco;
    			 receber.tit_parceiro = cart2.par_codigo;
    			 receber.save();
    		 
    		 if (cart2.par_codigo != 1){
    			 
    			 pagar.tit_data_emissao = formatador.format(pedido.ped_data_confirma);
    			 pagar.tit_estado = "aberto";
    			 pagar.tit_tipo = "pagar";
    			 pagar.ped_codigo = pedido.ped_codigo;
    			 pagar.car_codigo = cart2.car_codigo;
    			 pagar.tit_cliente = pedido.usu_codigo;
    			 pagar.tit_valor = (cart2.car_preco/10) * 9;
    			 pagar.tit_parceiro = cart2.par_codigo;
    			 pagar.save();
    			 pagar = new Titulo();
    		 }
    		 
    		 receber = new Titulo();
    		
		}
	}
}