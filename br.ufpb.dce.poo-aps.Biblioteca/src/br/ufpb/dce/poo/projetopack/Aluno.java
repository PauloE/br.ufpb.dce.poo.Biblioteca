package br.ufpb.dce.poo.projetopack;

public class Aluno extends UsuarioComposto {

	
	
	public Aluno(String nome, String matricula, String cpf, String curso, String periodoIngresso){
		super(nome, matricula, cpf);
		super.setCurso(curso);
		super.setPeriodoIngresso(periodoIngresso);
	}
	
	public void adicionarEmprestimo(Emprestimo emprestimo){
		this.adicionarEmprestimo(emprestimo);
	}


	public void removerEmprestimo(Emprestimo emprestimo) {
		for(Emprestimo e: super.getEmprestimos()){
			if(e.equals(emprestimo)){
				super.getEmprestimos().remove(e);
				break;
			}
		}
		
	}

	public int getQuantDiasEmprestimo() {
		return Configuracao.getInstance().getDiasEmprestimoAluno();
	}
	
	
}