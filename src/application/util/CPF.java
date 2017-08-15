package application.util;

import java.util.ArrayList;
import java.util.List;

public class CPF {
	private static final int NUMERO_DIGITOS_CPF = 11;
	private static final int NUMERO_DIGITOS_VERIFICADOR = 2;
	

	private static int geraNumAleatorio(){
	    int numero = (int) (Math.random() * 10);
	 
	    return numero;
	}
	
	private static List<Integer> geraCPFParcial(){
		List<Integer> cpfParcial = new ArrayList<Integer>();
		
	    for(int i = 0; i < (NUMERO_DIGITOS_CPF - NUMERO_DIGITOS_VERIFICADOR); i++){
	    	cpfParcial.add(geraNumAleatorio());
	    }
	    
	    return cpfParcial;
	}
	
	private static Integer geraDigitoVerificadorCpf(List<Integer> cpfParcial) {
		int soma = 0;
		int pesoIncial = cpfParcial.size() + 1;
		
		for (Integer digito : cpfParcial) {
			soma += pesoIncial * digito;
			pesoIncial--;
		}
		
		int restoDivisao = soma % 11;
		
		if(restoDivisao < 2) {
			return 0;
		} else {
			return 11 - restoDivisao;
		}
	}
	
	public static String create() {
		List<Integer> cpf = geraCPFParcial();
		
		cpf.add(geraDigitoVerificadorCpf(cpf));
		cpf.add(geraDigitoVerificadorCpf(cpf));
		
		StringBuilder texto = new StringBuilder();
		
		for (Integer integer : cpf) {
			texto.append(integer);
		}
		
		return texto.toString();
	}
	
	public static boolean isValid(String cpf) {
		if(cpf == null) {
			return false;
		}
		
		if(cpf.trim().length() != NUMERO_DIGITOS_CPF) {
			return false;
		}
		
		List<Integer> cpfParcial = new ArrayList<Integer>();
		
		for(int i = 0; i < (NUMERO_DIGITOS_CPF - NUMERO_DIGITOS_VERIFICADOR); i++){
			cpfParcial.add(Character.getNumericValue(cpf.charAt(i)));
		}

		StringBuilder digitosVerificador = new StringBuilder();
		for(int i = 0; i < NUMERO_DIGITOS_VERIFICADOR; i++){
			int digitoVerificador = geraDigitoVerificadorCpf(cpfParcial);
			
			digitosVerificador.append(digitoVerificador);
			cpfParcial.add(digitoVerificador);
		}
		
		return digitosVerificador.toString().equals(cpf.substring(NUMERO_DIGITOS_CPF-NUMERO_DIGITOS_VERIFICADOR, NUMERO_DIGITOS_CPF));
		
	}
}
