package br.ufpb.dce.poo.projetopack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Calendar;
import br.ufpb.dce.poo.ExceptionsProject.*;

public class Biblioteca {
	
	private Configuracao configuracao = Configuracao.getInstance();
	private List<Livro> livros;
	private List<Emprestimo> emprestimosAtivos;
	private List<Usuario> usuarios;
	private static Biblioteca singleton;
	
	private Biblioteca() {
		this.livros = new LinkedList<Livro>();
		this.emprestimosAtivos = new LinkedList<Emprestimo>();
		this.usuarios = new LinkedList<Usuario>();
	}
	
	public static Biblioteca getInstance(){
		if (singleton == null){
			singleton = new Biblioteca();
		}
		return singleton;
	}
	
	public void cadastrarUsuario (Usuario u) throws UsuarioJaExisteException{
		if(this.usuarios.contains(u)){
			throw new UsuarioJaExisteException ("O usuario ja existe");
		}
		this.usuarios.add(u);
	}
	
	public void cadastrarLivro(Livro livro){
		for (Livro lv: this.livros){
			if (lv.getCodigo().equals(livro.getCodigo())){
				lv.setQuantidade(lv.getQuantidade()+livro.getQuantidade());
			}
			
		}
		this.livros.add(livro);
	}
	
	public Usuario getUsuario(String mat) throws UsuarioInexistenteException{
		
		for (Usuario u: this.usuarios){
			if (u.getMatricula().equals(mat)){
				return u;
			}
		}
		throw new UsuarioInexistenteException ("Este usuario não existe.");
	}
	
	public Livro getLivro (String codLivro) throws LivroInexistenteException{
		for (Livro l: livros){
			if(l.getCodigo().equals(codLivro)){
				return l;
			}
		}
		throw new LivroInexistenteException ("Este livro nao esta cadastrado");
	}
	
	public List<Emprestimo> listarEmprestimosEmAtraso(){
		
		Calendar DiaDeHoje = Calendar.getInstance();
		List<Emprestimo> atrasos = new LinkedList<Emprestimo>();
		for (Emprestimo e: this.emprestimosAtivos){
			if (e.getDataDevolucao().before(DiaDeHoje)){
				atrasos.add(e);
			}
		}

		return atrasos;
		
	}
	
	public double calcularMulta(Usuario u){
		
		long diasAtraso = 0;
		for (Emprestimo e: this.listarEmprestimosEmAtraso()){
			if (e.getUsuario().getMatricula().equals(u.getMatricula())){
				diasAtraso += diasEntre(e.getDataDevolucao(), Calendar.getInstance());
			}
			
		}
		
		return diasAtraso * this.configuracao.getValorMulta();
		
	}
	
	public long diasEntre(Calendar a, Calendar b){ 
		
		int tempoDia = 1000 * 60 * 60 * 24;
		Calendar dInicial = a;
		Calendar dFinal = b;
		long diferenca = dFinal.getTimeInMillis() - dInicial.getTimeInMillis();
		return diferenca / tempoDia;
		
	}
	
	public void emprestarLivro (Usuario usuario, Livro livro) throws MaximoDeLivrosEmprestadosException, UsuarioEmAtrasoException, QuantidadeDeLivrosInsuficienteException, ListaDeAtrasoInexistenteException{
		
		int quantidadeMaxEmprestimo = 3;
		if (usuario.getEmprestimos().size() == quantidadeMaxEmprestimo){
			throw new MaximoDeLivrosEmprestadosException ("Usuario atingiu limite de emprestimos");
		}
		if(this.listarEmprestimosEmAtraso().contains(usuario)){
			throw new UsuarioEmAtrasoException("O usuario esta com devoluçao em atraso.");
		}
		for (Livro l: this.livros){
			if (l.getCodigo().equals(livro.getCodigo()) && l.getQuantidade() == 1){
				throw new QuantidadeDeLivrosInsuficienteException("Quantidade de livros insuficiente para emprestimo.");
			}
		}
		
		Calendar diaDevolucao = Calendar.getInstance();
		diaDevolucao.add(Calendar.DAY_OF_YEAR, usuario.getQuantDiasEmprestimo());
		Emprestimo novoEmprestimo = new Emprestimo (usuario, livro, Calendar.getInstance(), diaDevolucao);
		for (Livro lv: this.livros){
			if (lv.getCodigo().equals(livro.getCodigo())){
				lv.setQuantidade(lv.getQuantidade()-1);
			}
		}
		this.emprestimosAtivos.add(novoEmprestimo);
		usuario.adicionarEmprestimo(novoEmprestimo);		
			
	}
	
	public void devolverLivro (Usuario usuario, Livro livro)throws EmprestimoInexistenteException{
		boolean emprestou = false;
		for (Emprestimo emprestimoUsuario: usuario.getEmprestimos()){
			if (emprestimoUsuario.getLivro().getCodigo().equals(livro.getCodigo())){
				for (Emprestimo emprestimoBiblioteca: this.emprestimosAtivos){
					if (emprestimoBiblioteca.getLivro().getCodigo().equals(livro.getCodigo()) && emprestimoBiblioteca.getUsuario().getMatricula().equals(usuario.getMatricula())){
						this.emprestimosAtivos.remove(emprestimoBiblioteca);
						usuario.getEmprestimos().remove(emprestimoBiblioteca);
						emprestou = true;
						for (Livro lv : this.livros){
							if (lv.getCodigo().equals(livro.getCodigo())){
								lv.setQuantidade(lv.getQuantidade()+1);
							}
						}
					}
				}
			}
		}
		if (emprestou == false){
			throw new EmprestimoInexistenteException ("O usuario nao possui o emprestimo referente.");
		}
		
	}
	
	
	//##########################       PERSISTENCIA DE ARQUIVOS      ############################################
	

	
	public void gravarUsuariosEmAquivo(String nomeArquivoAluno, String nomeArquivoProfessor) throws IOException {
		BufferedWriter gravadorAluno = null;
		BufferedWriter gravadorProfessor = null;
		try{
			gravadorAluno = new BufferedWriter(new FileWriter(nomeArquivoAluno));
			gravadorProfessor = new BufferedWriter(new FileWriter(nomeArquivoProfessor));
			for (Usuario usuario: this.usuarios){
				if(usuario.getQuantDiasEmprestimo() == 10){
					gravadorAluno.write(usuario.getNome());
					gravadorAluno.newLine();
					gravadorAluno.write(usuario.getMatricula());
					gravadorAluno.newLine();
					gravadorAluno.write(usuario.getCPF());
					gravadorAluno.newLine();
					gravadorAluno.write(usuario.getPeriodoIngresso());
					gravadorAluno.newLine();
					gravadorAluno.write(usuario.getCurso());
					gravadorAluno.newLine();
				}else{
					gravadorProfessor.write(usuario.getNome());
					gravadorProfessor.newLine();
					gravadorProfessor.write(usuario.getMatricula());
					gravadorProfessor.newLine();
					gravadorProfessor.write(usuario.getCPF());
					gravadorProfessor.newLine();
					gravadorProfessor.write(usuario.getDepartamento());
					gravadorProfessor.newLine();
				}
			}
		}
		finally{
			if(gravadorAluno != null && gravadorProfessor !=null){
				gravadorAluno.close();
				gravadorProfessor.close();
			}
		}
	}
	
	public void carregarAlunosDeArquivo(String nomeArquivoAluno) throws UsuarioJaExisteException, IOException{
		BufferedReader leitorAluno = null;
		
		try{
			leitorAluno = new BufferedReader(new FileReader(nomeArquivoAluno));
			String nomeAluno = null;

			do{
				nomeAluno = leitorAluno.readLine();
				if(nomeAluno != null){
					String matricula = leitorAluno.readLine();
					String cpf = leitorAluno.readLine();
					String curso = leitorAluno.readLine();
					String periodoIngresso = leitorAluno.readLine();
					Usuario u = new Aluno(nomeAluno, matricula, cpf, curso, periodoIngresso);
					this.cadastrarUsuario(u);
				}
			}while(nomeAluno != null);
		}
		finally{
			if(leitorAluno == null){
				leitorAluno.close();
			}
		}
	}
	
	public void carregarProfessoresDeArquivo(String nomeArquivoProfessor) throws UsuarioJaExisteException, IOException{ 
		BufferedReader leitorProfessor = null; 
		try{ 
			leitorProfessor = new BufferedReader(new FileReader (nomeArquivoProfessor)); 
			String nomeProfessor = null; 
			
			do{ 
				nomeProfessor = leitorProfessor.readLine();
				if(nomeProfessor!= null){  
					String matriculaProfessor = leitorProfessor.readLine(); 
					String cpfProfessor = leitorProfessor.readLine(); 
					String departamento = leitorProfessor.readLine(); 
					Usuario u = new Professor(nomeProfessor, matriculaProfessor, cpfProfessor, departamento); 
					this.cadastrarUsuario(u); 
				} 
			} while(nomeProfessor != null); 
			
		} finally{ 
			if(leitorProfessor!=null){ 
				leitorProfessor.close(); 
			} 
		} 
	}
	
	public void gravarLivrosEmArquivo(String nomeArquivo) throws IOException{
		BufferedWriter gravadorLivro = null;
		
		try{
			gravadorLivro = new BufferedWriter(new FileWriter(nomeArquivo));		
			for(Livro l: this.livros){
				gravadorLivro.write(l.getNome());
				gravadorLivro.newLine();
				gravadorLivro.write(l.getCodigo());
				gravadorLivro.newLine();
				gravadorLivro.write(l.getAutor());
				gravadorLivro.newLine();
				gravadorLivro.write(l.getClassificacao());
				gravadorLivro.newLine();
				gravadorLivro.write(l.getQuantidade());
				gravadorLivro.newLine();
			}		
		}
		finally{
			if(gravadorLivro != null){
				gravadorLivro.close();
			}
		}
	}
	
	public void carregarLivrosDeArquivo(String arquivoLivro)throws IOException {

		BufferedReader leitor = null; 
		try { 
			leitor = new BufferedReader(new FileReader(arquivoLivro)); 
			String nomeDoLivro = null; 
			
			do { 
				nomeDoLivro = leitor.readLine();
				if (nomeDoLivro != null) {
					String codigo = leitor.readLine(); 
					String autor = leitor.readLine(); 
					String classificacao = leitor.readLine(); 
					int quantidade = leitor.read(); 
					Livro l = new Livro (nomeDoLivro, autor, codigo, quantidade,classificacao); 
					this.cadastrarLivro(l); 
				} 
			} while (nomeDoLivro!= null); 
			
		} finally { 
			if (leitor != null) { 
				leitor.close(); 
			}
		}
	}
	
	public void gravarEmprestimosEmArquivo(String nomeArquivo) throws IOException {
		BufferedWriter gravador = null;
		try {
			gravador = new BufferedWriter(new FileWriter("C:\\date\\datas.txt"));
			for (Emprestimo e: this.emprestimosAtivos){
				gravador.write(e.getUsuario().getMatricula() +"\n");
				gravador.write(e.getLivro().getCodigo() +"\n");
				
				gravador.write(e.getDataEmprestimo().get(Calendar.DAY_OF_MONTH)+"\n");
				gravador.write(e.getDataEmprestimo().get(Calendar.MONTH) +"\n");
				gravador.write(e.getDataEmprestimo().get(Calendar.YEAR) +"\n");
				
				gravador.write(e.getDataDevolucao().get(Calendar.DAY_OF_MONTH) +"\n");
				gravador.write(e.getDataDevolucao().get(Calendar.MONTH) +"\n");
				gravador.write(e.getDataDevolucao().get(Calendar.YEAR) +"\n");
			}
		} finally {
			if (gravador!=null){
				gravador.close();
			}
		}		
	}
	
	public void carregarEmprestimosDeArquivo(String nomeArquivo) throws IOException, EmprestimoJaExisteException, UsuarioInexistenteException, LivroInexistenteException{
		BufferedReader leitor = null;
		try {
			leitor = new BufferedReader(new FileReader(nomeArquivo));
			String matricula = null;
			String codigoLivro;
			
			String diaEmprestimo;
			String mesEmprestimo;
			String anoEmprestimo;
			
			String diaDevolucao;
			String mesDevolucao;
			String anoDevolucao;
			
			
			do {
				matricula = leitor.readLine(); 
				codigoLivro = leitor.readLine();
				
				diaEmprestimo = leitor.readLine();
				mesEmprestimo = leitor.readLine();
				anoEmprestimo = leitor.readLine();
				
				diaDevolucao = leitor.readLine();
				mesDevolucao = leitor.readLine();
				anoDevolucao = leitor.readLine();
				
				Usuario usuario = this.getUsuario(matricula); 
				Livro livro = this.getLivro(codigoLivro); 
				
				Calendar dataEmprestimo = Calendar.getInstance();
				dataEmprestimo.set(Integer.parseInt(diaEmprestimo),Integer.parseInt(mesEmprestimo) , Integer.parseInt(anoEmprestimo));
				
				Calendar dataDevolucao = Calendar.getInstance();
				dataDevolucao.set(Integer.parseInt(anoDevolucao), Integer.parseInt(mesDevolucao), Integer.parseInt(diaDevolucao));
				
				Emprestimo emprestimo = new Emprestimo(usuario, livro, dataEmprestimo, dataDevolucao);
				
				usuario.adicionarEmprestimo(emprestimo);
				this.emprestimosAtivos.add(emprestimo);
				
			} 
			while(matricula != null); 
		} 
		finally {
			if (leitor!=null){
				leitor.close();
			}
		}
		
	}




	
}



