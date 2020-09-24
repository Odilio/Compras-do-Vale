package util;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import models.Pagamento;
import models.Usuario;
import br.com.nordestefomento.jrimum.bopepo.BancoSuportado;
import br.com.nordestefomento.jrimum.bopepo.Boleto;
import br.com.nordestefomento.jrimum.bopepo.view.BoletoViewer;
import br.com.nordestefomento.jrimum.domkee.comum.pessoa.endereco.CEP;
import br.com.nordestefomento.jrimum.domkee.comum.pessoa.endereco.Endereco;
import br.com.nordestefomento.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Agencia;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Carteira;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Cedente;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Sacado;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Titulo;


public class GerarBoleto {

	
	public static void gerarBoleto(Pagamento pagamento){
		// TODO Auto-generated method stub

        /*
         * INFORMANDO DADOS SOBRE O CEDENTE.
         */
        Cedente cedente = new Cedente("Bhakti ", "012.379.543-55");

        /*
         * INFORMANDO DADOS SOBRE O SACADO.
         */
        Sacado sacado = new Sacado(pagamento.pag_nome, pagamento.pag_cpf);

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.SP);
        enderecoSac.setLocalidade(pagamento.pag_cidade);
        enderecoSac.setCep(new CEP(pagamento.pag_cep));
        enderecoSac.setBairro(pagamento.pag_bairro);
        enderecoSac.setLogradouro(pagamento.pag_endereco);
        enderecoSac.setNumero(pagamento.pag_numero);
        sacado.addEndereco(enderecoSac);

    /*    
         * INFORMANDO DADOS SOBRE O SACADOR AVALISTA.
         
        SacadorAvalista sacadorAvalista = new SacadorAvalista("FindContent", "00.000.000/0001-91");

        // Informando o endere�o do sacador avalista.
        Endereco enderecoSacAval = new Endereco();
        enderecoSacAval.setUF(UnidadeFederativa.DF);
        enderecoSacAval.setLocalidade("Brasília");
        enderecoSacAval.setCep(new CEP("59000-000"));
        enderecoSacAval.setBairro("Grande Centro");
        enderecoSacAval.setLogradouro("Rua Eternamente Principal");
        enderecoSacAval.setNumero("001");
        sacadorAvalista.addEndereco(enderecoSacAval);
*/
        /*
         * INFORMANDO OS DADOS SOBRE O TITULO.
         */
        
        // Informando dados sobre a conta bancaria do titulo.
        ContaBancaria contaBancaria = new ContaBancaria(BancoSuportado.BANCO_DO_BRASIL.create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(7540, "X"));
        contaBancaria.setCarteira(new Carteira(30));
        contaBancaria.setAgencia(new Agencia(2701, "4"));
        
        Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
        titulo.setNumeroDoDocumento("123456");
        titulo.setNossoNumero("99345678912");
        titulo.setDigitoDoNossoNumero("5");
        titulo.setValor(BigDecimal.valueOf(9.00));
        titulo.setDataDoDocumento(new Date());
        titulo.setDataDoVencimento(new Date());
        titulo.setTipoDeDocumento(TipoDeTitulo.DS_DUPLICATA_DE_SERVICO);
        
        titulo.setDesconto(new BigDecimal(0.00));
        

        /*
         * INFORMANDO OS DADOS SOBRE O BOLETO.
         */
        Boleto boleto = new Boleto(titulo);
        
       /* boleto.setLocalPagamento("Pagavel preferencialmente no Banco do Brasil ou em " +
                        "qualquer Banco ate o Vencimento.");*/
        boleto.setLocalPagamento("Documento inválido, favor não receber " +
                ".");
        boleto.setInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor " +
                        "cobrado não é o esperado, aproveite o DESCONTO!");
        boleto.setInstrucao1("PARA PAGAMENTO 1 até Hoje não cobrar nada!");
        boleto.setInstrucao2("PARA PAGAMENTO 2 até Amanhã Não cobre!");
       

        /*
         * GERANDO O BOLETO BANC�RIO.
         */
        // Instanciando um objeto "BoletoViewer", classe respons�vel pela
        // geração do boleto banc�rio.
        BoletoViewer boletoViewer = new BoletoViewer(boleto);
        
        // Gerando o arquivo. No caso o arquivo mencionado ser� salvo na mesma
        // pasta do projeto. Outros exemplos:
        // WINDOWS: boletoViewer.getAsPDF("C:/Temp/MeuBoleto.pdf");
        // LINUX: boletoViewer.getAsPDF("/home/temp/MeuBoleto.pdf");
        File arquivoPdf = boletoViewer.getPdfAsFile("/home/play-1.2.7/findcontent/public/boleto.pdf");
	}
        

        /**
        * Exibe o arquivo na tela.
        * 
        * @param arquivoBoleto
        */
        private static void mostreBoletoNaTela(File arquivoBoleto) {

            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            
            try {
                    desktop.open(arquivoBoleto);
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
}