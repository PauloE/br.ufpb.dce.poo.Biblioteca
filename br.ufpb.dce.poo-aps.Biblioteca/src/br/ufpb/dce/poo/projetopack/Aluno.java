package br.ufpb.dce.poo.projetopack;

public class Aluno extends UsuarioComposto {

	
	
	public Aluno(String nome, String matricula, String cpf, String curso, String periodoIngresso){
		super(nome, matricula, cpf);
		super.setCurso(curso);
		super.setPeriodoIngresso(periodoIngresso);
	}

	public int getQuantDiasEmprestimo() {
		return Configuracao.getInstance().getDiasEmprestimoAluno();
	}
	
	
}
