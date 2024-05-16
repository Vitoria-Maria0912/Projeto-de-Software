package com.ufcg.psoft.tccManager.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.tccManager.dto.aluno.AlunoPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.professor.ProfessorPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.solicitacao.SolicitacaoPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.temaTCC.TemaTCCPostPutRequestDTO;
import com.ufcg.psoft.tccManager.exception.TccErrorType;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import com.ufcg.psoft.tccManager.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Solicitacao de Orientacoes")
public class SolicitacaoControllerTest {

    final String URI_SOL = "/solicitacoesOrientacoes";

    @Autowired
    MockMvc driver;

    @Autowired
    TemaTCCRepository temaTCCRepository;

    TemaTCCPostPutRequestDTO temaTCCPostPutRequestDTO;

    TemaTCC temaTcc1;

    TemaTCC temaTcc2;

    @Autowired
    AreaDeEstudoRepository areaDeEstudoRepository;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    OrientacaoTccRepository orientacaoTccRepository;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    AlunoPostPutRequestDTO alunoPostPutRequestDTO;

    ProfessorPostPutRequestDTO professorPostPutRequestDTO;

    SolicitacaoPostPutRequestDTO solicitacaoPostPutRequestDTO;

    Aluno aluno;

    Professor professor;

    AreaDeEstudo area;

    AreaDeEstudo area1;

    Solicitacao solicitacao;

    @BeforeEach
    void setup() {

        area = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                .nome("jogos digitais").build());

        area1 = areaDeEstudoRepository.save(AreaDeEstudo.builder().nome("ANALISE").build());
        HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
        areasDeEstudo.add(area);
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());

        temaTCCPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                .titulo("Introdução de programação no Ensino Básico através de Jogos")
                .descricao("apresentando conceitos de programação por meio de jogos digitais aos alunos do ensino básico")
                .areasDeEstudo(areasDeEstudo)
                .build();
        this.temaTcc1 = temaTCCRepository.save(TemaTCC.builder()
                .titulo(temaTCCPostPutRequestDTO.getTitulo())
                .descricao((temaTCCPostPutRequestDTO.getDescricao()))
                .areasDeEstudo(temaTCCPostPutRequestDTO.getAreasDeEstudo())
                        .status(Status.NOVO)
                .build());

        objectMapper.registerModule(new JavaTimeModule());
        temaTCCPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                .titulo("JD: impactos")
                .descricao("estudo e análise")
                .areasDeEstudo(areasDeEstudo)
                .build();
        this.temaTcc2 = temaTCCRepository.save(TemaTCC.builder()
                .titulo(temaTCCPostPutRequestDTO.getTitulo())
                .descricao((temaTCCPostPutRequestDTO.getDescricao()))
                .areasDeEstudo(temaTCCPostPutRequestDTO.getAreasDeEstudo())
                        .status(Status.NOVO)
                .build());

        HashSet<TemaTCC> temasTcc = new HashSet<>();
        temasTcc.add(temaTcc1);

        alunoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                .nomeCompleto("juliette")
                .email("juliette@ccc")
                .matricula("121210143")
                .periodoConclusao("2025.2")
                .areasDeInteresse(areasDeEstudo)
                .build();
        aluno = alunoRepository.save(Aluno.builder()
                .nomeCompleto(alunoPostPutRequestDTO.getNomeCompleto())
                .email(alunoPostPutRequestDTO.getEmail())
                .matricula(alunoPostPutRequestDTO.getMatricula())
                .periodoConclusao(alunoPostPutRequestDTO.getPeriodoConclusao())
                .areasDeInteresse(alunoPostPutRequestDTO.getAreasDeInteresse())
                .build());


        HashSet<String> laboratorios = new HashSet<>();
        laboratorios.add("LSD");

        professorPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                .nomeCompleto("boninho jr")
                .email("boninho@ccc")
                .laboratorios(laboratorios)
                .quota(3)
                .areasDeInteresse(areasDeEstudo)
                .build();

        professor = professorRepository.save(Professor.builder()
                .nomeCompleto(professorPostPutRequestDTO.getNomeCompleto())
                .email(professorPostPutRequestDTO.getEmail())
                .laboratorios(professorPostPutRequestDTO.getLaboratorios())
                .quota(professorPostPutRequestDTO.getQuota())
                .areasDeInteresse(areasDeEstudo)
                .temasTcc(temasTcc)
              
                .build());

        solicitacaoPostPutRequestDTO = SolicitacaoPostPutRequestDTO.builder()
                .temaTCC(temaTcc1)
                .aluno(aluno)
                .professor(professor)
                .build();
        solicitacao = solicitacaoRepository.save(Solicitacao.builder()
                .temaTcc(solicitacaoPostPutRequestDTO.getTemaTCC())
                .professor(solicitacaoPostPutRequestDTO.getProfessor())
                .aluno(solicitacaoPostPutRequestDTO.getAluno())
                .build());

    }

    @AfterEach
    void tearDown() {
        alunoRepository.deleteAll();
        professorRepository.deleteAll();
        solicitacaoRepository.deleteAll();
        orientacaoTccRepository.deleteAll();
    }
    @Nested
    @DisplayName("Conjunto de casos de análise de Solicitação de Orientação do TCC, tendo como TemaTcc cadastrados por Professor" )
    class AnalisarSolicitacaoOrientacaoTCC {
        @Test
        @Transactional
        @DisplayName("Quando aprovamos uma Solicitacao de Orientação com TemaTcc cadastrado pelo Professor")
        void quandoAprovamosSolicitacaocomTemaTccProfessor() throws Exception {

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1 ,alunoRepository.count());
            assertEquals(1, solicitacaoRepository.count());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


            System.setOut(new PrintStream(outputStream));


            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/responderSolicitacaoOrientacaoTCCTemaProfessor/" + solicitacao.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacaoOrientacao", String.valueOf(true))
                            .param("perfil", "PROFESSOR")
                            .param("mensagemResposta", "Iremos fazer um belo trabalho"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertTrue(printOutput.contains("foi aceita")),
                    () -> assertTrue(printOutput.contains("ALOCADO")),
                    () -> assertTrue(printOutput.contains(aluno.getNomeCompleto()))
            );

        }

        @Test
        @Transactional
        @DisplayName("Quando analisamos uma Solicitacao de Orientação com TemaTcc cadastrado pelo Professor, mas Perfil errado tenta analisar")
        void quandoAnalisamosSolicitacaocomTemaTccProfessor() throws Exception {

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1 ,alunoRepository.count());
            assertEquals(1, solicitacaoRepository.count());


            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/responderSolicitacaoOrientacaoTCCTemaProfessor/" + solicitacao.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacaoOrientacao", String.valueOf(true))
                            .param("perfil", "ALUNO")
                            .param("mensagemResposta", "Sempre pode contar comigo :)"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage());

        }
        @Test
        @Transactional
        @DisplayName("Quando negamos uma Solicitacao de Orientação com TemaTcc cadastrado pelo Professor")
        void quandoNegamosSolicitacaocomTemaTccProfessor() throws Exception {

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1 ,alunoRepository.count());
            assertEquals(1, solicitacaoRepository.count());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(outputStream));

            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/responderSolicitacaoOrientacaoTCCTemaProfessor/" + solicitacao.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacaoOrientacao", String.valueOf(false))
                            .param("perfil", "PROFESSOR")
                            .param("mensagemResposta", "Podemos ver como a temática será trabalhada , espero retorno"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertTrue(printOutput.contains("foi negada")),
                    () -> assertTrue(printOutput.contains("NOVO")),
                    () -> assertTrue(printOutput.contains(aluno.getNomeCompleto()))
            );


        }
        @Test
        @Transactional
        @DisplayName("Quando analisamos uma Solicitacao de Orientação que não existe")
        void quandoanalisamosSolicitacaoInexistente() throws Exception {

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1 ,alunoRepository.count());
            assertEquals(1, solicitacaoRepository.count());


            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/responderSolicitacaoOrientacaoTCCTemaProfessor/" + 99999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacaoOrientacao", String.valueOf(true))
                            .param("perfil", "PROFESSOR")
                            .param("mensagemResposta", "Podemos ver como a temática será trabalhada , espero retorno"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("A solicitacao nao existe!", resultado.getMessage());



        }
        @Test
        @Transactional
        @DisplayName("Quando aanalisamos uma Solicitacao de Orientação com TemaTcc cadastrado por Aluno")
        void quandoAnalisamosSolicitacaocomTemaTccAluno() throws Exception {

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1 ,alunoRepository.count());
            assertEquals(1, solicitacaoRepository.count());


            AlunoPostPutRequestDTO alunoPostPutRequestDTO1;
            Aluno aluno1;
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);
            TemaTCC temaTcc0;
            TemaTCCPostPutRequestDTO temaTCCPostPutRequestDTO0;
            temaTCCPostPutRequestDTO0 = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Introdução de programação no Ensino Básico através de Jogos")
                    .descricao("apresentando conceitos de programação por meio de jogos digitais aos alunos do ensino básico")
                    .areasDeEstudo(areasDeEstudo)
                    .build();
            temaTcc0 = temaTCCRepository.save(TemaTCC.builder()
                    .titulo(temaTCCPostPutRequestDTO0.getTitulo())
                    .descricao((temaTCCPostPutRequestDTO0.getDescricao()))
                    .areasDeEstudo(temaTCCPostPutRequestDTO0.getAreasDeEstudo())
                    .build());
           HashSet<TemaTCC> temasaluno = new HashSet<>();
            temasaluno.add(temaTcc0);
            alunoPostPutRequestDTO1 = AlunoPostPutRequestDTO.builder()
                    .nomeCompleto("juliette")
                    .email("juliette@ccc")
                    .matricula("121210143")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeEstudo)
                    .temasTcc(temasaluno)
                    .build();
            aluno1 = alunoRepository.save(Aluno.builder()
                    .nomeCompleto(alunoPostPutRequestDTO1.getNomeCompleto())
                    .email(alunoPostPutRequestDTO1.getEmail())
                    .matricula(alunoPostPutRequestDTO1.getMatricula())
                    .periodoConclusao(alunoPostPutRequestDTO1.getPeriodoConclusao())
                    .areasDeInteresse(alunoPostPutRequestDTO1.getAreasDeInteresse())
                    .temasTcc(alunoPostPutRequestDTO1.getTemasTcc())
                    .build());
            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSD");
            ProfessorPostPutRequestDTO professorPostPutRequestDTO0;
            Professor professor0;

            professorPostPutRequestDTO0 = ProfessorPostPutRequestDTO.builder()
                    .nomeCompleto("boninho jr")
                    .email("boninho@ccc")
                    .laboratorios(laboratorios)
                    .quota(3)
                    .areasDeInteresse(areasDeEstudo)
                    .build();

            professor0 = professorRepository.save(Professor.builder()
                    .nomeCompleto(professorPostPutRequestDTO0.getNomeCompleto())
                    .email(professorPostPutRequestDTO0.getEmail())
                    .laboratorios(professorPostPutRequestDTO0.getLaboratorios())
                    .quota(professorPostPutRequestDTO0.getQuota())
                    .areasDeInteresse(areasDeEstudo)
                    .build());
            Solicitacao solicitacao1;
            SolicitacaoPostPutRequestDTO solicitacaoPostPutRequestDTO1;

            solicitacaoPostPutRequestDTO1 = SolicitacaoPostPutRequestDTO.builder()
                    .temaTCC(temaTcc0)
                    .aluno(aluno1)
                    .professor(professor0)
                    .build();
            solicitacao1 = solicitacaoRepository.save(Solicitacao.builder()
                    .temaTcc(solicitacaoPostPutRequestDTO1.getTemaTCC())
                    .professor(solicitacaoPostPutRequestDTO1.getProfessor())
                    .aluno(solicitacaoPostPutRequestDTO1.getAluno())
                    .build());
            solicitacao1.getTemaTcc().setStatus(Status.PENDENTE);



            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/responderSolicitacaoOrientacaoTCCTemaProfessor/" + solicitacao1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacaoOrientacao", String.valueOf(true))
                            .param("perfil", "PROFESSOR")
                            .param("mensagemResposta", "Iremos fazer um belo trabalho"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();


            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("A solicitacao tem TemaTCC cadastrado por Aluno !", resultado.getMessage());

        }
    }

    @Nested
    @DisplayName("Conjunto de casos de listar solicitações de orientação")
    class ListarSolicitacoesOrientacaoTCC{

        @Test
        @Transactional
        @DisplayName("Quando listamos as solicitações de orientação em tema de tcc criado pelo professor.")
        void quandoListaAsSolicitacoesDeOrientacaoComTemaCriadoPeloProf() throws Exception {

            assertEquals(1, professor.getTemasTcc().size());
            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, solicitacaoRepository.count());

            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/solicitacaoTemaProf" + "/" + professor.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(solicitacaoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            List<Solicitacao> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });
            // Assert
                assertEquals(1, resultado.size());
        }

        @Test
        @Transactional
        @DisplayName("Quando listamos as solicitações de orientação em tema de tcc criado pelo professor, mesmo o tema da solicitacao tendo sido criada pelo aluno.")
        void quandoTentaListarAsSolicitacoesDeOrientacaoComTemaCriadoPeloProfZerado() throws Exception {

            assertEquals(1, professor.getTemasTcc().size());
            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, solicitacaoRepository.count());

            aluno.getTemasTcc().add(temaTcc2);
            Solicitacao solicitacao1;
            SolicitacaoPostPutRequestDTO solicitacaoPostPutRequestDTO1;

            solicitacaoPostPutRequestDTO1 = SolicitacaoPostPutRequestDTO.builder()
                    .temaTCC(temaTcc2)
                    .aluno(aluno)
                    .professor(professor)
                    .build();
            solicitacao1 = solicitacaoRepository.save(Solicitacao.builder()
                    .temaTcc(solicitacaoPostPutRequestDTO1.getTemaTCC())
                    .professor(solicitacaoPostPutRequestDTO1.getProfessor())
                    .aluno(solicitacaoPostPutRequestDTO1.getAluno())
                    .build());
            solicitacao1.getTemaTcc().setStatus(Status.PENDENTE);



            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/solicitacaoTemaProf" + "/" + professor.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(solicitacaoPostPutRequestDTO1)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Solicitacao> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });
            // Assert
            assertAll(
                    () -> assertEquals(1, resultado.size()),
                    () -> assertEquals(1, aluno.getTemasTcc().size()),
            () ->  assertEquals(2, solicitacaoRepository.count()));
        }

        @Test
        @Transactional
        @DisplayName("Quando listamos as solicitações de orientação em tema de tcc criado por professor inexistente.")
        void quandoListaAsSolicitacoesDeOrientacaoComTemaCriadoPeloProfInexistente() throws Exception {

            assertEquals(1, professor.getTemasTcc().size());
            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, solicitacaoRepository.count());

            UUID randomIDProf = UUID.randomUUID();

            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/solicitacaoTemaProf" + "/" + randomIDProf)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(solicitacaoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O professor consultado nao existe!", resultado.getMessage()),
                    () -> assertEquals(1, solicitacaoRepository.count()));
        }

        @Test
        @Transactional
        @DisplayName("Quando listamos as solicitações de orientação em tema de tcc criado pelo professor tendo duas solicitacoes.")
        void quandoListaAsSolicitacoesDeOrientacaoComTemaCriadoPeloProfComDoisResultados() throws Exception {

            assertEquals(1, professor.getTemasTcc().size());
            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, solicitacaoRepository.count());

            professor.getTemasTcc().add(temaTcc2);
            SolicitacaoPostPutRequestDTO solicitacaoPostPutRequestDTO1;

            solicitacaoPostPutRequestDTO1 = SolicitacaoPostPutRequestDTO.builder()
                    .temaTCC(temaTcc2)
                    .aluno(aluno)
                    .professor(professor)
                    .build();
            solicitacaoRepository.save(Solicitacao.builder()
                    .temaTcc(solicitacaoPostPutRequestDTO1.getTemaTCC())
                    .professor(solicitacaoPostPutRequestDTO1.getProfessor())
                    .aluno(solicitacaoPostPutRequestDTO1.getAluno())
                    .build());

            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/solicitacaoTemaProf" + "/" + professor.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(solicitacaoPostPutRequestDTO1)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            List<Solicitacao> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });
            // Assert
            assertEquals(2, resultado.size());
        }



    }

    @Nested
    @DisplayName("Conjunto de casos de análise de Solicitação de Orientação do TCC quando o Aluno cadastra o tema")
    class AnalisarSolicitacaoOrientacaoTCCTemaAluno {

        @Test
        @Transactional
        @DisplayName("Quando aprovamos uma Solicitacao de Orientação com TemaTcc cadastrado pelo aluno")
        void quandoAprovamosSolicitacaocomTemaTccAluno() throws Exception {

            Solicitacao solicitacao1;
            solicitacaoPostPutRequestDTO = SolicitacaoPostPutRequestDTO.builder()
                    .temaTCC(temaTcc2)
                    .aluno(aluno)
                    .professor(professor)
                    .build();
            solicitacao1 = solicitacaoRepository.save(Solicitacao.builder()
                    .temaTcc(solicitacaoPostPutRequestDTO.getTemaTCC())
                    .professor(solicitacaoPostPutRequestDTO.getProfessor())
                    .aluno(solicitacaoPostPutRequestDTO.getAluno())
                    .build());

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, alunoRepository.count());
            assertEquals(2, solicitacaoRepository.count());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            solicitacao1.getTemaTcc().setStatus(Status.PENDENTE);
            aluno.getTemasTcc().add(temaTcc2);


            System.setOut(new PrintStream(outputStream));

            // Act
            driver.perform(put(URI_SOL + "/responderSolicitacaoTccTemaAluno/" + solicitacao1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacao", String.valueOf(true))
                            .param("mensagem", "vamos nessa"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertTrue(printOutput.contains("foi aceita")),
                    () -> assertEquals(Status.ALOCADO, temaTcc2.getStatus()),
                    () -> assertTrue(printOutput.contains(aluno.getNomeCompleto()))
            );
        }


        @Test
        @Transactional
        @DisplayName("Quando negamos uma Solicitacao de Orientação com TemaTcc cadastrado pelo aluno")
        void quandoNegamosSolicitacaocomTemaTccAluno() throws Exception {

            Solicitacao solicitacao1;
            solicitacaoPostPutRequestDTO = SolicitacaoPostPutRequestDTO.builder()
                    .temaTCC(temaTcc2)
                    .aluno(aluno)
                    .professor(professor)
                    .build();
            solicitacao1 = solicitacaoRepository.save(Solicitacao.builder()
                    .temaTcc(solicitacaoPostPutRequestDTO.getTemaTCC())
                    .professor(solicitacaoPostPutRequestDTO.getProfessor())
                    .aluno(solicitacaoPostPutRequestDTO.getAluno())
                    .build());

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, alunoRepository.count());
            assertEquals(2, solicitacaoRepository.count());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            solicitacao1.getTemaTcc().setStatus(Status.PENDENTE);
            aluno.getTemasTcc().add(temaTcc2);

            System.setOut(new PrintStream(outputStream));

            // Act
            driver.perform(put(URI_SOL + "/responderSolicitacaoTccTemaAluno/" + solicitacao1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacao", String.valueOf(false))
                            .param("mensagem", "infelizmente nao tenho tempo"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertTrue(printOutput.contains("foi negada")),
                    () -> assertEquals(Status.NOVO, temaTcc2.getStatus()),
                    () -> assertTrue(printOutput.contains(aluno.getNomeCompleto()))
            );
        }

        @Test
        @Transactional
        @DisplayName("Quando analisamos uma Solicitacao de Orientação que não existe")
        void quandoAnalisamosSolicitacaoInexistente() throws Exception {

            Solicitacao solicitacao1;
            solicitacaoPostPutRequestDTO = SolicitacaoPostPutRequestDTO.builder()
                    .temaTCC(temaTcc2)
                    .aluno(aluno)
                    .professor(professor)
                    .build();
            solicitacao1 = solicitacaoRepository.save(Solicitacao.builder()
                    .temaTcc(solicitacaoPostPutRequestDTO.getTemaTCC())
                    .professor(solicitacaoPostPutRequestDTO.getProfessor())
                    .aluno(solicitacaoPostPutRequestDTO.getAluno())
                    .build());

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, alunoRepository.count());
            assertEquals(2, solicitacaoRepository.count());
            solicitacao1.getTemaTcc().setStatus(Status.PENDENTE);
            aluno.getTemasTcc().add(temaTcc2);


            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/responderSolicitacaoTccTemaAluno/" + 99999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacao", String.valueOf(true))
                            .param("mensagem", "Tema interessante. Faremos um otimo trabalho!"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);
            // Assert
            assertEquals("A solicitacao nao existe!", resultado.getMessage());
        }

        @Test
        @Transactional
        @DisplayName("Quando chega solicitação já respondida.")
        void quandoSolicitacaoRespondidaTemaTccAluno() throws Exception {

            aluno.getTemasTcc().add(temaTcc2);
            Solicitacao solicitacao1;
            solicitacaoPostPutRequestDTO = SolicitacaoPostPutRequestDTO.builder()
                    .temaTCC(temaTcc2)
                    .aluno(aluno)
                    .professor(professor)
                    .build();
            solicitacao1 = solicitacaoRepository.save(Solicitacao.builder()
                    .temaTcc(solicitacaoPostPutRequestDTO.getTemaTCC())
                    .professor(solicitacaoPostPutRequestDTO.getProfessor())
                    .aluno(solicitacaoPostPutRequestDTO.getAluno())
                    .build());

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, alunoRepository.count());
            assertEquals(2, solicitacaoRepository.count());


            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/responderSolicitacaoTccTemaAluno/" + solicitacao1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacao", String.valueOf(true))
                            .param("mensagem", "Iremos fazer um belo trabalho"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();


            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);
            assertEquals("A solicitacao ja foi respondida!", resultado.getMessage());
        }

        @Test
        @Transactional
        @DisplayName("Quando o tema não foi cadastrado pelo aluno.")
        void quandoTemaNaoFoiCadastradoPeloAluno() throws Exception {

            Solicitacao solicitacao1;
            solicitacaoPostPutRequestDTO = SolicitacaoPostPutRequestDTO.builder()
                    .temaTCC(temaTcc2)
                    .aluno(aluno)
                    .professor(professor)
                    .build();
            solicitacao1 = solicitacaoRepository.save(Solicitacao.builder()
                    .temaTcc(solicitacaoPostPutRequestDTO.getTemaTCC())
                    .professor(solicitacaoPostPutRequestDTO.getProfessor())
                    .aluno(solicitacaoPostPutRequestDTO.getAluno())
                    .build());

            assertEquals(2, temaTCCRepository.count());
            assertEquals(1, alunoRepository.count());
            assertEquals(2, solicitacaoRepository.count());
            solicitacao1.getTemaTcc().setStatus(Status.PENDENTE);


            // Act
            String responseJsonString = driver.perform(put(URI_SOL + "/responderSolicitacaoTccTemaAluno/" + solicitacao1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("respostaSolicitacao", String.valueOf(true))
                            .param("mensagem", "Iremos fazer um belo trabalho"))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);
            assertEquals("O tema consultado nao foi cadastrado por um aluno!", resultado.getMessage());
        }
    }

}
