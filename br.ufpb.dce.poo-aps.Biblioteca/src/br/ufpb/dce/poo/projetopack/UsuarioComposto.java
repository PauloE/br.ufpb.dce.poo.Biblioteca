package br.ufpb.dce.poo.projetopack;

import java.util.List;

public abstract class UsuarioComposto implements Usuario {
	private List<Emprestimo> emprestimos;
	private String nome;
	private String matricula;
	private String cpf;
	private String periodoIngresso;
	private String departamento;
	private String curso;
	
	public UsuarioComposto(String nome, String matricula, String cpf){
		this.nome = nome;
		this.matricula = matricula;
		this.cpf = cpf;
	}
	
	public void adicionarEmprestimo(Emprestimo e){
		this.emprestimos.add(e);
	}
	public void removerEmprestimo(Emprestimo emprestimo){
		for(Emprestimo e: this.emprestimos){
			if(e.equals(emprestimo)){
				this.emprestimos.remove(e);
				break;
			}
		}
	}
	
	public void setPeriodoIngresso(String periodo){
		this.periodoIngresso = periodo;
	}
	
	public void setCurso(String curso){
		this.curso = curso;
	}
	
	public String getCurso(){
		return this.curso;
	}
	public String getNome(){
		return this.nome;
	}
	public String getMatricula(){
		return this.matricula;
	}
	public String getCPF(){
		return this.cpf;
	}
	public List<Emprestimo> getEmprestimos(){
		return this.emprestimos;
	}
	
	public String getPeriodoIngresso(){
		return this.periodoIngresso;
		
	}
	
	public String getDepartamento(){
		return this.departamento;
	}
	
	public void setDepartamento(String departamento){
		this.departamento = departamento;
	}
	
	public abstract int getQuantDiasEmprestimo();

}
