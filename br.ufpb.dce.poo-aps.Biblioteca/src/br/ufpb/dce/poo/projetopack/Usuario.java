package br.ufpb.dce.poo.projetopack;

import java.util.List;

public interface Usuario {
	public int getQuantDiasEmprestimo();
	public void adicionarEmprestimo(Emprestimo e);
	public void removerEmprestimo(Emprestimo e);
	public String getNome();
	public String getMatricula();
	public String getCPF();
	public List<Emprestimo> getEmprestimos();
	public String getPeriodoIngresso();
	public String getCurso();
	public String getDepartamento();
}