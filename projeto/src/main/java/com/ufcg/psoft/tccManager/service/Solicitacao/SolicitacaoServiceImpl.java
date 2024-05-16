package com.ufcg.psoft.tccManager.service.Solicitacao;

import com.ufcg.psoft.tccManager.dto.solicitacao.SolicitacaoResponseDTO;
import com.ufcg.psoft.tccManager.model.Aluno;
import com.ufcg.psoft.tccManager.model.Professor;
import com.ufcg.psoft.tccManager.model.TemaTCC;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import com.ufcg.psoft.tccManager.repository.ProfessorRepository;
import com.ufcg.psoft.tccManager.repository.SolicitacaoRepository;
import com.ufcg.psoft.tccManager.repository.TemaTCCRepository;
import com.ufcg.psoft.tccManager.service.orientacaoTCC.OrientacaoTccService;
import com.ufcg.psoft.tccManager.service.professor.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ufcg.psoft.tccManager.exception.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SolicitacaoServiceImpl implements SolicitacaoService {

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Autowired
    OrientacaoTccService orientacaoTccService;

    @Autowired
    ProfessorService professorService;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    @Override
    public void criarSolicitacao(Aluno aluno, TemaTCC tema, Professor professor) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setAluno(aluno);
        solicitacao.setProfessor(professor);
        solicitacao.setTemaTcc(tema);

        this.solicitacaoRepository.save(solicitacao);
        professorService.receberNotificacao(tema.getTitulo(), aluno.getNomeCompleto(), aluno.getEmail(), professor);
    }

    private void checaTemaSolicitacaoCadastradoPorProf(TemaTCC tema, Professor prof){
        if (!prof.getTemasTcc().contains(tema)){
            throw new SolicitacaoCadastradoPorAlunoException();
        }
    }

    @Override
    public void analisaSolicitacaoTemaProfessor(Long idOrientacao, Boolean respostaSolicitacaoOrientacao, Perfil perfil, String mensagemResposta) throws Exception {
        Solicitacao solicitacao = solicitacaoRepository.findById(idOrientacao).orElseThrow(SolicitacaoNaoExisteException::new);

        Professor prof = solicitacao.getProfessor();
        TemaTCC tema = solicitacao.getTemaTcc();
        checaPerfil(perfil);
        checaTemaSolicitacaoCadastradoPorProf(tema, prof);
        if(respostaSolicitacaoOrientacao){
            mudaStatusTemaTcc(solicitacao);
            printarNotificacaoOrientacaoAceita(solicitacao,mensagemResposta);
            printarNotificacaoOrientacaoAceitaCoordenador(solicitacao,mensagemResposta);
        } else {
            printarNotificacaoOrientacaoRecusada(solicitacao, mensagemResposta);
        }
        temaTCCRepository.save(solicitacao.getTemaTcc());
        solicitacaoRepository.save(solicitacao);

    }

    private void mudaStatusTemaTcc(Solicitacao solicitacao){
        if(solicitacao.getTemaTcc().getStatus().equals(Status.ALOCADO)){
            throw new SolicitacaoJaRespondidaExcepetion();
        }
        solicitacao.getTemaTcc().setStatus(Status.ALOCADO);
    }
    private void checaPerfil(Perfil perfil){
        if (!perfil.equals(Perfil.PROFESSOR) ){
            throw new AcessoNegadoException();
        }
    }

    private void printarNotificacaoOrientacaoAceitaCoordenador(Solicitacao orientacao, String mensagem) {
        System.out.println("------------------------------------------------------------------------------------------\n"
                + "| Ha uma nova Solicitacao de TCC aprovado    !                     \n|"
                + "| a solicitacao de Orientacao feita pelo Aluno:    \n"
                + "| " + orientacao.getAluno().toString() + ",            \n"
                + "| foi aceita pelo orientador.                          \n"
                + "| " + orientacao.getProfessor().toString() + ",        \n"
                + "| Mensagem do Orientador:                              \n"
                + "| *" + mensagem +                                       "*\n"
                + "|"              +                                     "\n");


    }


    private void printarNotificacaoOrientacaoAceita(Solicitacao orientacao, String mensagem) {
        System.out.println("------------------------------------------------------------------------------------------\n"
                + "| Voce tem uma nova notificacao!                    \n|"
                + "| " + orientacao.getAluno().toString() +          ",\n"
                + "| a solicitacao de Orientacao:                      \n"
                + "| foi aceita pelo seu orientador.                   \n"
                + "| " + orientacao.getTemaTcc().getStatus() +        "\n"
                + "| Mensagem do Orientador:                           \n"
                + "|  *" + mensagem +                                "*\n"
                + "|"              +                                  "\n"
                + "|                                                   \n");

    }



    private void printarNotificacaoOrientacaoRecusada(Solicitacao orientacao, String mensagem) {
        System.out.println("------------------------------------------------------------------------------------------\n"
                + "| Voce tem uma nova notificacao!                   \n|"
                + "| " + orientacao.getAluno().toString() + ",        \n"
                + "| a solicitacao de Orientacao:                     \n"
                + "| foi negada pelo seu orientador.                  \n"
                + "| " + orientacao.getTemaTcc().getStatus() +       "\n"
                + "| Mensagem do Orientador:                          \n"
                + "|" + mensagem +                                   "\n"
                + "|" +                                              "\n"
                + "|                                                  \n");
    }

    @Override
    public void analisaSolicitacaoTemaAluno(Long idSolicitacao, Boolean respostaSolicitacao, String mensagem) throws Exception {

        Solicitacao solicitacao = solicitacaoRepository.findById(idSolicitacao)
                .orElseThrow(SolicitacaoNaoExisteException::new);
        Aluno aluno = solicitacao.getAluno();
        Professor professor = solicitacao.getProfessor();
        TemaTCC tema = solicitacao.getTemaTcc();

        checaTema(aluno, tema);
        checaValidadeSolicitacao(solicitacao);
        checaSolicitacaoRespondida(solicitacao, tema);

        if (respostaSolicitacao) {
            tema.setStatus(Status.ALOCADO);
            printarNotificacaoOrientacaoAceitaCoordenador(solicitacao, mensagem);
            printarNotificacaoOrientacaoAceita(solicitacao, mensagem);
            orientacaoTccService.criarOrientacao(aluno, tema, professor);
        }
        else {
            tema.setStatus(Status.NOVO);
            printarNotificacaoOrientacaoRecusada(solicitacao, mensagem);
        }
        temaTCCRepository.save(tema);
        solicitacaoRepository.save(solicitacao);
    }

    private void checaTema(Aluno aluno, TemaTCC temaSolicitacao) {

        if (!aluno.getTemasTcc().contains(temaSolicitacao)) {
            throw new TemaNaoCadastradoPeloAlunoException();
        }
    }

    private void checaValidadeSolicitacao(Solicitacao solicitacao) throws Exception {
        if (solicitacao == null) {
            throw new SolicitacaoNaoExisteException();
        }
    }

    private void checaSolicitacaoRespondida(Solicitacao solicitacao, TemaTCC temaSolicitado) {
        if (!temaSolicitado.getStatus().equals(Status.PENDENTE)) {
            throw new SolicitacaoJaRespondidaExcepetion();
        }
    }

    @Override
    public List<SolicitacaoResponseDTO> listarSolicitacoesTemaTCCProf(UUID idProf){
        Optional<Professor> profOptional = professorRepository.findById(idProf);
        if(profOptional.isEmpty()){
            throw new ProfessorNaoExisteException();
        }
        List<Solicitacao> solicitacoes = solicitacaoRepository.getSolicitacoesTccByProfessor_IdAndTemaTcc_Status(idProf, Status.NOVO);

        return solicitacoes.stream().map(SolicitacaoResponseDTO::new).collect(Collectors.toList());

    }



}
