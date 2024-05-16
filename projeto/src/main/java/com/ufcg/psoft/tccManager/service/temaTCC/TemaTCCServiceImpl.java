package com.ufcg.psoft.tccManager.service.temaTCC;

import com.ufcg.psoft.tccManager.dto.temaTCC.*;
import com.ufcg.psoft.tccManager.exception.*;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.*;
import com.ufcg.psoft.tccManager.repository.*;
import com.ufcg.psoft.tccManager.service.orientacaoTCC.OrientacaoTccService;
import com.ufcg.psoft.tccManager.service.Solicitacao.SolicitacaoService;
import com.ufcg.psoft.tccManager.service.aluno.AlunoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TemaTCCServiceImpl implements TemaTCCService {

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Autowired
    OrientacaoTccRepository orientacaoTccRepository;

    @Autowired
    AlunoService alunoService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    OrientacaoTccService orientacaoTccService;

    @Autowired
    SolicitacaoService solicitacaoService;

    @Override
    public TemaTCCResponseDTO criar(TemaTCCPostPutRequestDTO temaTCCPostPutRequestDTO, UUID id) throws Exception {
        Perfil perfil = checarID(id);
        if (perfil.equals(Perfil.ALUNO) || perfil.equals(Perfil.PROFESSOR)) {
            TemaTCC temaTCC = modelMapper.map(temaTCCPostPutRequestDTO, TemaTCC.class);
            temaTCC.setStatus(Status.NOVO);
            temaTCCRepository.save(temaTCC);
            adicionarTemaTCC(temaTCC, id);
            if (perfil.equals(Perfil.PROFESSOR)) {
                enviarNotificacao(temaTCCPostPutRequestDTO.getTitulo(),
                        (temaTCCPostPutRequestDTO.getAreasDeEstudo()).iterator());
            }
            return modelMapper.map(temaTCC, TemaTCCResponseDTO.class);
        } else {
            throw new AcessoNegadoException();
        }
    }

    @Override
    public Perfil checarID(UUID id) throws Exception {
        Perfil perfil;
        if (alunoRepository.existsById(id)){
            perfil = Perfil.ALUNO;
        } else if (professorRepository.existsById(id)) {
            perfil = Perfil.PROFESSOR;
        } else {
            throw new UsuarioNaoExisteException();
        }
        return perfil;
    }

    @Override
    public List<TemaTCCResponseDTO> listarTemasCadastrados(Perfil perfil) throws Exception {
        if (checaPerfil(perfil, Perfil.ALUNO)) {
            return listarTemasCadastradosPorProfessores();
        } else if (checaPerfil(perfil, Perfil.PROFESSOR)) {
            return listarTemasCadastradosPorAlunos();
        } else {
            throw new AcessoNegadoException();
        }
    }

    private List<TemaTCCResponseDTO> listarTemasCadastradosPorProfessores() {
        List<Professor> professores = professorRepository.getProfessoresTemasTCCIsNotNull();
        List<TemaTCC> temasCadastrados = new ArrayList<>();
        for (Professor professor : professores) {
            temasCadastrados.addAll(professor.getTemasTcc());
        }
        return temasCadastrados.stream().map(TemaTCCResponseDTO::new).collect(Collectors.toList());
    }

    private List<TemaTCCResponseDTO> listarTemasCadastradosPorAlunos() {
        List<Aluno> alunos = alunoRepository.getAlunosTemasTCCIsNotNull();
        List<TemaTCC> temasCadastrados = new ArrayList<>();
        for (Aluno aluno : alunos) {
            temasCadastrados.addAll(aluno.getTemasTcc());
        }
        return temasCadastrados.stream().map(TemaTCCResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public void adicionarTemaTCC(TemaTCC temaTCC, UUID id) throws Exception {
        Perfil perfil = checarID(id);

        if(checaPerfil(perfil, Perfil.ALUNO)){
            Aluno aluno = alunoRepository.getAlunoById(id);
            aluno.getTemasTcc().add(temaTCC);
            alunoRepository.save(aluno);
        } else {
            Professor professor = professorRepository.getProfessorById(id);
            professor.getTemasTcc().add(temaTCC);
            professorRepository.save(professor);
        }
    }

    @Override
    public void enviarNotificacao(String titulo, Iterator<AreaDeEstudo> iterator){
        if (iterator.hasNext()) {
            AreaDeEstudo area = iterator.next();
            this.alunoService.receberNotificacao(titulo, area);
            enviarNotificacao(titulo, iterator);
        }
    }

    @Override
    public List<TemaTCCResponseDTO> listarTemasCadastradosPeloProfessor(Perfil perfil, UUID id) {
         if (checaPerfil(perfil, Perfil.PROFESSOR)) {
            Professor professor = professorRepository.findById(id).orElseThrow(ProfessorNaoExisteException::new);
            Set<TemaTCC> temasCadastrados = professor.getTemasTcc();
           if(temasCadastrados.isEmpty()) throw new ProfessorSemTemasTccCadastradosException();
            return temasCadastrados.stream().map(TemaTCCResponseDTO::new).collect(Collectors.toList());
        } else {
            throw new AcessoNegadoException();
        }
    }

    private boolean checaPerfil(Perfil perfil, Perfil perfilEsperado) {
        return (perfilEsperado.equals(perfil));
    }
    @Transactional
    @Override
    public void gerarRelatorio(Perfil perfil) throws Exception {
        if (perfil.equals(Perfil.COORDENADOR)) {

            List<OrientacaoTcc> orientacoesTcc = this.orientacaoTccRepository.findAll();
            List<Aluno> alunosSemOrientador = this.orientacaoTccRepository.getAlunosSemOrientador();
            List<TemaTCC> temasNaoAlocados= this.orientacaoTccRepository.getTemasNaoAlocados();
            List<Professor> professoresSemOrientandos = this.orientacaoTccRepository.getProfessoresSemOrientandos();

            System.out.println("|----------------------------------------------------------------------------------------|\n"
                             + "                                   Relatorio do Sistema                                  \n"
                             + "|----------------------------------------------------------------------------------------|\n"
                             + "                                      Temas alocados:                                     \n"
                             + "|----------------------------------------------------------------------------------------|\n");
            exibeTemasAlocados(orientacoesTcc.iterator());
            System.out.println("|----------------------------------------------------------------------------------------|\n"
                             + "                                Alunos sem orientador:                                    \n"
                             + "|----------------------------------------------------------------------------------------|\n");
            exibeAlunosNaoAlocados(alunosSemOrientador.iterator());
            System.out.println("|----------------------------------------------------------------------------------------|\n"
                             + "                                  Temas nao alocados:                                     \n"
                             + "|----------------------------------------------------------------------------------------|\n");
            exibeTemasNaoAlocados(temasNaoAlocados.iterator());
            System.out.println("|----------------------------------------------------------------------------------------|\n"
                             + "                              Professores sem orientandos:                                \n"
                             + "|----------------------------------------------------------------------------------------|\n");
            exibeProfessoresNaoAlocados(professoresSemOrientandos.iterator());
            System.out.println("|----------------------------------------------------------------------------------------|");
        } else {
            throw new AcessoNegadoException();
        }
    }

    private void exibeTemasAlocados(Iterator<OrientacaoTcc> iterator) {
        if (iterator.hasNext()) {
            OrientacaoTcc orientacaoTcc = iterator.next();
            System.out.println( "| Titulo: " + orientacaoTcc.getTemaTcc().getTitulo() + "\n" +
                                "| Status: " + orientacaoTcc.getTemaTcc().getStatus() + "\n" +
                                "| Aluno responsavel: " + orientacaoTcc.getAluno().getNomeCompleto() + "\n" +
                                "| Matricula do aluno: " + orientacaoTcc.getAluno().getMatricula() + "\n" +
                                "| Professor responsavel: " + orientacaoTcc.getProfessor().getNomeCompleto() + "\n" +
                                "| Laboratorio(s) que o professor(a) participa: " + orientacaoTcc.getProfessor().getLaboratorios().toString() + "\n" +
                                "| Area(s) de estudo: " + orientacaoTcc.getTemaTcc().getAreasDeEstudo().toString() + "\n"
            );
            this.exibeTemasAlocados(iterator);
        }
    }

    private void exibeTemasNaoAlocados(Iterator<TemaTCC> iterator) {
        if (iterator.hasNext()) {
            TemaTCC temaTcc = iterator.next();
            System.out.println("| Titulo: " + temaTcc.getTitulo() + "\n" +
                               "| Descricao: " + temaTcc.getDescricao()+ "\n" +
                               "| Status: " + temaTcc.getStatus() + "\n" +
                               "| Area(s) de estudo: " + temaTcc.getAreasDeEstudo().toString() + "\n"
            );
            this.exibeTemasNaoAlocados(iterator);
        }
    }

    private void exibeAlunosNaoAlocados(Iterator<Aluno> iterator) {
        if (iterator.hasNext()) {
            Aluno aluno = iterator.next();
            System.out.println("| Nome completo: " + aluno.getNomeCompleto() + "\n" +
                               "| Matricula: " + aluno.getMatricula() + "\n" +
                               "| Tema(s) de TCC: " + aluno.getTemasTcc().toString() + "\n" +
                               "| Area(s) de interesse: " + aluno.getAreasDeInteresse().toString() + "\n"
            );
            this.exibeAlunosNaoAlocados(iterator);
        }
    }

    private void exibeProfessoresNaoAlocados(Iterator<Professor> iterator) {
        if (iterator.hasNext()) {
            Professor professor = iterator.next();
            System.out.println("| Nome completo: " + professor.getNomeCompleto() + "\n" +
                               "| Quota: " + professor.getQuota() + "\n" +
                               "| Laboratorio(s) que o professor(a) participa: " + professor.getLaboratorios().toString() + "\n" +
                               "| Tema(s) de TCC: " + professor.getTemasTcc().toString() + "\n" +
                               "| Area(s) de interesse: " + professor.getAreasDeInteresse().toString() + "\n"
            );
            this.exibeProfessoresNaoAlocados(iterator);
        }
    }

    @Override
    public void solicitarOrientacaoTemaTccAluno(UUID idAluno, UUID idProfessor, Long idTema) {
        Aluno aluno = this.alunoRepository.getAlunoById(idAluno);
        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(ProfessorNaoExisteException::new);
        TemaTCC temaSelecionado = this.encontraTemaCadastradoPeloAluno(aluno, idTema);

        checaValidadeTema(temaSelecionado);
        temaSelecionado.setStatus(Status.PENDENTE);
        solicitacaoService.criarSolicitacao(aluno, temaSelecionado, professor);
    }

    private TemaTCC encontraTemaCadastradoPeloAluno(Aluno aluno, Long idTema) {
        Set<TemaTCC> temasAluno = aluno.getTemasTcc();
        TemaTCC temaSelecionado = null;

        for (TemaTCC tema : temasAluno) {
            if (tema != null && tema.getId().equals(idTema)) temaSelecionado = tema;
        }

        return temaSelecionado;
    }

    @Override
    public void solicitarOrientacaoTemaProfessor(UUID idAluno, Long idTema, UUID idProfessor) {
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(AlunoNaoExisteException::new);

        Professor professor = professorRepository.findById(idProfessor)
                .orElseThrow(ProfessorNaoExisteException::new);

        TemaTCC tema = this.encontraTemaCadastradoPeloProf(professor, idTema);

        checaValidadeTema(tema);
        solicitacaoService.criarSolicitacao(aluno, tema, professor);
    }

    private TemaTCC encontraTemaCadastradoPeloProf(Professor professor, Long idTema) {
        Set<TemaTCC> temasProfessor = professor.getTemasTcc();
        TemaTCC temaEscolhido = null;

        for (TemaTCC tema : temasProfessor) {
            if (tema != null && tema.getId().equals(idTema)) temaEscolhido = tema;
        }

        return temaEscolhido;
    }

    private void checaValidadeTema(TemaTCC tema) {
        if (tema == null) {
            throw new TemaNaoExisteException();
        }
    }
}
