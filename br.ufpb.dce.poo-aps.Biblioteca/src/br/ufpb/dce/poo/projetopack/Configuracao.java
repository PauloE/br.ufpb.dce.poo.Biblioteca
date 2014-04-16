package br.ufpb.dce.poo.projetopack;

public class Configuracao {
	private static Configuracao singleton;
	
	private double valorMulta;
	private final int diasEmprestimoProf = 30;
	private final int diasEmprestimoAluno = 10;
	
	private Configuracao (){
		this.valorMulta = 0.5;

	}
		
	public static Configuracao getInstance(){
		if (Configuracao.singleton == null){
			Configuracao.singleton = new Configuracao();
		}
		return Configuracao.singleton;
	}
	
	public int getDiasEmprestimoProfessor(){
		return this.diasEmprestimoProf;
	}
	
	public int getDiasEmprestimoAluno(){
		return this.diasEmprestimoAluno;
	}
	
	public void setValorMulta (double valor){
		this.valorMulta = valor;
	}
	
	public double getValorMulta (){
		return this.valorMulta;
	}
	

	
	
}
