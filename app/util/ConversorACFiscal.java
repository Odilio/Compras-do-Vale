package util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class ConversorACFiscal {

	/**
	 * Converte uma data para o formato AAAAMMDD.
	 * 
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static String DataParaString(Date data) throws ParseException {
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");

		StringBuilder nowYYYYMMDD = new StringBuilder(
				dateformatYYYYMMDD.format(data));

		return nowYYYYMMDD.toString();
	}

	/**
	 * Converte um booleano para "S" (true) ou "N" (false).
	 * 
	 * @param bool
	 * @return
	 */
	public static String BoolParaString(boolean bool) {
		return bool ? "S" : "N";
	}

	/**
	 * Converte um inteiro para String, completando com ) a esquerda de
	 * acordo com o tamanho da formata��o
	 * 
	 * @param num - numero a ser formatado
	 * @param format - numero de "casas" que deve ter o numero formatado
	 * @return
	 */
	public static String IntParaString(int num, int format) {

		String strNum = num + "";
		while (strNum.length() < format) {
			strNum = "0" + strNum;
		}
		return strNum;
	}
	
	/**
	 * Converte um long para String, completando com ) a esquerda de
	 * acordo com o tamanho da formata��o
	 * 
	 * @param num - numero a ser formatado
	 * @param format - numero de "casas" que deve ter o numero formatado
	 * @return
	 */
	public static String LongParaString(long num, int format) {

		String strNum = num + "";
		while (strNum.length() < format) {
			strNum = "0" + strNum;
		}
		return strNum;
	}

	/**
	 * 
	 * @param valor
	 * @param casasDecimais
	 * @return
	 * @throws Exception
	 */
	public static String DoubleParaString(double valor, int tamanho, int casasDecimais)
			throws NumeroCasasDecimaisInvalidoException {

//		if (casasDecimais <= 0)
//			throw new NumeroCasasDecimaisInvalidoException();
//
//		String formato = "#.";
//
//		for (int i = 0; i < casasDecimais; ++i) {
//			formato += "#";
//		}
//
//		DecimalFormat df = new DecimalFormat(formato);
//		String numeroFormatado = df.format(valor);
//		
//		numeroFormatado = numeroFormatado.replace(",", ".");
		String numeroFormatado = "" + valor;
		if (numeroFormatado.substring(numeroFormatado.indexOf("."),numeroFormatado.length()).length()-1 > casasDecimais){
			numeroFormatado = numeroFormatado.substring(0,numeroFormatado.indexOf(".")+3) ;
		
		}
		while(numeroFormatado.substring(numeroFormatado.indexOf(".")).length()-1 < casasDecimais)
		{
			numeroFormatado = numeroFormatado + "0";
		}
		
		while(numeroFormatado.length() < tamanho)
		{
			numeroFormatado = "0" + numeroFormatado;
		}
		
		return numeroFormatado;
	}
	
	public static String StringParaString(String num, int format) {
		String text ="";
		num = " " + num;
		if (num.length() > format){
			for (int i = 0; i < format; i++) {
				text += num.charAt(i);
			}
			
			return text;
		}
			
		String strNum = num + "";
		while (strNum.length() < format) {
			strNum = " " + strNum;
		}
		return strNum;
	}
	
	public static String StrParaString(String num, int format) {
		String text ="";
		num = "0" + num;
		if (num.length() > format){
			for (int i = 0; i < format; i++) {
				text += num.charAt(i);
			}
			
			return text;
		}
			
		String strNum = num + "";
		while (strNum.length() < format) {
			strNum = "0" + strNum;
		}
		return strNum;
	}
}
