package application.util;

import java.util.ArrayList;
import java.util.List;

public class NIP {
	private static final int NUMERO_DIGITOS_NIP = 8;
	private static final int NUMERO_DIGITOS_VERIFICADOR = 1;
	

	private static int geraNumAleatorio(){
	    int numero = (int) (Math.random() * 10);
	 
	    return numero;
	}
	
	private static List<Integer> geranipParcial(){
		List<Integer> nipParcial = new ArrayList<Integer>();
		
	    for(int i = 0; i < (NUMERO_DIGITOS_NIP - NUMERO_DIGITOS_VERIFICADOR); i++){
	    	nipParcial.add(geraNumAleatorio());
	    }
	    
	    return nipParcial;
	}
	
	private static Integer geraDigitoVerificadornip(List<Integer> nipParcial) {
		int soma = 0;
		int pesoIncial = nipParcial.size() + 1;
		
		for (Integer digito : nipParcial) {
			soma += pesoIncial * digito;
			pesoIncial--;
		}
		
		int restoDivisao = soma % 11;

		return (11 - restoDivisao) % 10;
	}
	
	public static String create() {
		List<Integer> nip = geranipParcial();
		
		nip.add(geraDigitoVerificadornip(nip));
		
		StringBuilder texto = new StringBuilder();
		
		for (Integer integer : nip) {
			texto.append(integer);
		}
		
		return texto.toString();
	}
	
	public static boolean isValid(String nip) {
		if(nip == null) {
			return false;
		}
		
		if(nip.trim().length() != NUMERO_DIGITOS_NIP) {
			return false;
		}
		
		List<Integer> nipParcial = new ArrayList<Integer>();
		
		for(int i = 0; i < (NUMERO_DIGITOS_NIP - NUMERO_DIGITOS_VERIFICADOR); i++){
			nipParcial.add(Character.getNumericValue(nip.charAt(i)));
		}

		StringBuilder digitosVerificador = new StringBuilder();
		for(int i = 0; i < NUMERO_DIGITOS_VERIFICADOR; i++){
			int digitoVerificador = geraDigitoVerificadornip(nipParcial);
			
			digitosVerificador.append(digitoVerificador);
			nipParcial.add(digitoVerificador);
		}
		
		return digitosVerificador.toString().equals(nip.substring(NUMERO_DIGITOS_NIP-NUMERO_DIGITOS_VERIFICADOR, NUMERO_DIGITOS_NIP));
		
	}
}
