package br.ufpb.dce.poo.projetopack;

public class EmprestimoInexistenteException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmprestimoInexistenteException(String mensagem){
		super(mensagem);
	}
}
