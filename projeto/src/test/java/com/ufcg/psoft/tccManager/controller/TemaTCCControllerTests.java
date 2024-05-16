package com.ufcg.psoft.tccManager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.tccManager.dto.aluno.AlunoPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.area.AreaDeEstudoPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.professor.ProfessorPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.temaTCC.TemaTCCPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.temaTCC.TemaTCCResponseDTO;
import com.ufcg.psoft.tccManager.exception.AcessoNegadoException;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.exception.TccErrorType;
import com.ufcg.psoft.tccManager.exception.UsuarioNaoExisteException;
import com.ufcg.psoft.tccManager.model.Aluno;
import com.ufcg.psoft.tccManager.model.AreaDeEstudo;
import com.ufcg.psoft.tccManager.model.Professor;
import com.ufcg.psoft.tccManager.model.TemaTCC;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import com.ufcg.psoft.tccManager.repository.*;
import com.ufcg.psoft.tccManager.service.temaTCC.TemaTCCService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Temas de TCC")
public class TemaTCCControllerTests {

    final String URI_TEMAS = "/temasTCC";

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
    SolicitacaoRepository solicitacaoRepository;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    AlunoPostPutRequestDTO alunoPostPutRequestDTO;

    ProfessorPostPutRequestDTO professorPostPutRequestDTO;

    Aluno aluno;

    Professor professor;

    AreaDeEstudo area;

    AreaDeEstudo area1;

    @Autowired
    TemaTCCService temaTCCService;

    @BeforeEach
    void setup() {

        area = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                .nome("Inteligencia Artificial").build());

        area1 = areaDeEstudoRepository.save(AreaDeEstudo.builder().nome("ANALISE").build());

        HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
        areasDeEstudo.add(area);

        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        temaTCCPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                .titulo("Analise da Inteligencia Artificial aplicada a educacao")
                .descricao("Explorando o uso da IA no ambito da educacao sob uma perspectiva etica")
                .areasDeEstudo(areasDeEstudo)
                .build();
        this.temaTcc1 = temaTCCRepository.save(TemaTCC.builder()
                .titulo(temaTCCPostPutRequestDTO.getTitulo())
                .descricao((temaTCCPostPutRequestDTO.getDescricao()))
                .areasDeEstudo(temaTCCPostPutRequestDTO.getAreasDeEstudo())
                .build());

        objectMapper.registerModule(new JavaTimeModule());
        temaTCCPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                .titulo("IA : aplicada a estatistica")
                .descricao("estudo e análise")
                .areasDeEstudo(areasDeEstudo)
                .build();
        this.temaTcc2 = temaTCCRepository.save(TemaTCC.builder()
                .titulo(temaTCCPostPutRequestDTO.getTitulo())
                .descricao((temaTCCPostPutRequestDTO.getDescricao()))
                .areasDeEstudo(temaTCCPostPutRequestDTO.getAreasDeEstudo())
                .build());

        Set<TemaTCC> temasTcc = new HashSet<>();
            temasTcc.add(temaTcc1);

        alunoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                .nomeCompleto("Silva")
                .email("silva@ccc")
                .matricula("121210111")
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
                .nomeCompleto("Joelson Marques")
                .email("joelson@ccc")
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

    }

    @AfterEach
    void tearDown() {
        alunoRepository.deleteAll();
        professorRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de criação dos temas de TCC por Aluno")
    class CriacaoTemaTCCAluno {

        @Test
        @Transactional
        @DisplayName("Quando um aluno cria um novo tema de TCC válido")
        void quandoAlunoCriaTemaTCCValidoTest() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("id", String.valueOf(aluno.getId()))
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TemaTCC resultado = objectMapper.readValue(responseJsonString, TemaTCC.TemaTCCBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(temaTCCPostPutRequestDTO.getTitulo(),
                            resultado.getTitulo()),
                    () -> assertEquals(aluno.getTemasTcc().size(), 1));

        }

        @Test
        @Transactional
        @DisplayName("Quando um aluno cria um novo tema de TCC titulo inválido")
        void quandoAlunoCriaTemaTCCTituloInvalidoTest() throws Exception {
            // Arrange
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);
            TemaTCCPostPutRequestDTO temaTCCInvalidoPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("")
                    .descricao("Explorando o uso da IA no ambito da educacao sob uma perspectiva etica")
                    .areasDeEstudo(areasDeEstudo)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(temaTCCInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(aluno.getTemasTcc().size(), 0)
            );
        }

        @Test
        @DisplayName("Quando um aluno cria um novo tema de TCC com areas invalidas")
        void quandoAlunoCriaTemaTCCComAreasInvalidasTest() throws Exception {
            // Arrange

            TemaTCCPostPutRequestDTO temaTCCInvalidoPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Sistema de Atendimento ao Consumidor")
                    .descricao("Explorando as melhores ferramentas para implementar um novo sistema de atendimento ao consumdor")
                    .areasDeEstudo(null)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(temaTCCInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(aluno.getTemasTcc().size(), 0)
            );
        }

        @Test
        @Transactional
        @DisplayName("Quando um aluno cria um novo tema de TCC com descricao inválida")
        void quandoAlunoCriaTemaTCCDescricaoInvalidoTest() throws Exception {
            // Arrange
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);
            TemaTCCPostPutRequestDTO temaTCCInvalidoPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Sistema de Atendimento ao Consumidor")
                    .descricao("")
                    .areasDeEstudo(areasDeEstudo)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(temaTCCInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals(aluno.getTemasTcc().size(), 0)
            );
        }

        @Test
        @Transactional
        @DisplayName("Quando um aluno cria um segundo tema novo de TCC válido")
        void quandoAlunoCriaUmSegundoTemaTCCValidoTest() throws Exception {
            // Arrange
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);
            TemaTCCPostPutRequestDTO temaTCCNovo = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Inteligencia Artifical na Educacao")
                    .descricao("Explorando o uso da IA no ambito da educacao sob uma perspectiva etica")
                    .areasDeEstudo(areasDeEstudo)
                    .build();


            // Act
            driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("id", String.valueOf(aluno.getId()))
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            String responseJsonStringNovo = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("id", String.valueOf(aluno.getId()))
                            .content(objectMapper.writeValueAsString(temaTCCNovo)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TemaTCC resultado = objectMapper.readValue(responseJsonStringNovo, TemaTCC.TemaTCCBuilder.class).build();
            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(temaTCCNovo.getTitulo(),
                            resultado.getTitulo()),
                    () -> assertEquals(aluno.getTemasTcc().size(), 2));

        }
    }
    @Nested
    @DisplayName("Conjunto de casos de verificação de criação dos temas de TCC por Professor")
    class CriacaoTemaTCCProfessor {
        @Test
        @DisplayName("Quando um professor cria um novo tema de TCC válido")
        void quandoProfessorCriaTemaTCCValidoTest() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("id", String.valueOf(professor.getId()))
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TemaTCC resultado = objectMapper.readValue(responseJsonString, TemaTCC.TemaTCCBuilder.class).build();
            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(temaTCCPostPutRequestDTO.getTitulo(),
                            resultado.getTitulo()));

        }

        @Test
        @Transactional
        @DisplayName("Quando um professor cria um segundo tema de TCC válido")
        void quandoProfessorCriaSegundoTemaTCCValidoTest() throws Exception {
            // Arrange
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);
            TemaTCCPostPutRequestDTO temaTCCNovo = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Desenvolvimento de um Jogo Educativo para o Ensino de Programacao para Criancas")
                    .descricao("Esse projeto desenvolver um jogo educativo interativo e envolvente destinado ao ensino de" +
                            " conceitos basicos de programacaoo para crianças. O jogo sera projetado de forma a tornar o aprendizado de programacao acessivel" +
                            " e divertido para crianças em idade escolar, utilizando uma abordagem lúdica e amigavel.")
                    .areasDeEstudo(areasDeEstudo)
                    .build();

            // Act
            driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("id", String.valueOf(aluno.getId()))
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("id", String.valueOf(professor.getId()))
                            .content(objectMapper.writeValueAsString(temaTCCNovo)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TemaTCC resultado = objectMapper.readValue(responseJsonString, TemaTCC.TemaTCCBuilder.class).build();
            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(temaTCCNovo.getTitulo(),
                            resultado.getTitulo()),
                    () -> assertEquals(professor.getTemasTcc().size(), 2));

        }

        @Test
        @DisplayName("Quando checamos o status de um tema válido criado por professor")
        void quandoChecaStatusDeTemaValidoCriadoPorProfessorTest() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("id", String.valueOf(professor.getId()))
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TemaTCC resultado = objectMapper.readValue(responseJsonString, TemaTCC.TemaTCCBuilder.class).build();
            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(resultado.getStatus(), Status.valueOf("NOVO")));

        }


        @Test
        @DisplayName("Quando um professor cria um novo tema de TCC titulo inválido")
        void quandoProfessorCriaTemaTCCTituloInvalidoTest() throws Exception {
            // Arrange
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);
            TemaTCCPostPutRequestDTO temaTCCInvalidoPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("")
                    .descricao("Explorando o uso da IA no ambito da educacao sob uma perspectiva etica")
                    .areasDeEstudo(areasDeEstudo)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(temaTCCInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um professor cria um novo tema de TCC com descricao invalida")
        void quandoProfessorCriaTemaTCCDescricaoInvalidoTest() throws Exception {
            // Arrange
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);
            TemaTCCPostPutRequestDTO temaTCCInvalidoPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Inteligencia Artifical na Educacao")
                    .descricao("")
                    .areasDeEstudo(areasDeEstudo)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(temaTCCInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando um professor cria um novo tema de TCC já existente")
        void quandoProfessorCriaTemaTCCjaexistente() throws Exception {
            // Arrange
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);
            TemaTCCPostPutRequestDTO temaTCCInvalidoPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Analise da Inteligencia Artificial aplicada a educacao")
                    .descricao("Explorando o uso da IA no ambito da educacao sob uma perspectiva etica")
                    .areasDeEstudo(areasDeEstudo)
                    .build();
            // Act
            MvcResult result = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(temaTCCInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Verifica se a resposta é um código de erro 400
                    .andReturn();

            // Assert
            String responseBody = result.getResponse().getContentAsString();
            if (!responseBody.isEmpty()) {
                assertTrue(responseBody.contains("TemaTcc ja cadastrado pelo professor!"));
            }
        }

        @Nested
        @Transactional
        @DisplayName("Conjunto de casos de verificação dos fluxos de notificações")
        class NotificacarAlunosAoCriarTemaTCC {

            @Test
            @DisplayName("Quando é cadastrado um tema de TCC, por um professor, em uma área de interesse de algum aluno")
            void testEnviarNotificacaoAlunoNovoTema() throws Exception {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                System.setOut(new PrintStream(outputStream));

                AlunoPostPutRequestDTO alunoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                        .nomeCompleto("Silva")
                        .email("silva@ccc")
                        .matricula("121210111")
                        .periodoConclusao("2025.2")
                        .areasDeInteresse(temaTCCPostPutRequestDTO.getAreasDeEstudo())
                        .build();
                Aluno aluno = alunoRepository.save(Aluno.builder()
                        .nomeCompleto(alunoPostPutRequestDTO.getNomeCompleto())
                        .email(alunoPostPutRequestDTO.getEmail())
                        .matricula(alunoPostPutRequestDTO.getMatricula())
                        .periodoConclusao(alunoPostPutRequestDTO.getPeriodoConclusao())
                        .areasDeInteresse(alunoPostPutRequestDTO.getAreasDeInteresse())
                        .build());

                temaTCCService.criar(temaTCCPostPutRequestDTO, professor.getId());

                String printOutput = outputStream.toString();
                System.setOut(System.out);

                assertAll(
                        () -> assertTrue(printOutput.contains("Voce tem uma nova notificacao!")),
                        () -> assertTrue(printOutput.contains(aluno.getNomeCompleto())),
                        () -> assertTrue(printOutput.contains("um novo tema de TCC: " + temaTCCPostPutRequestDTO.getTitulo()))
                );
            }

            @Test
            @DisplayName("Quando é cadastrado um tema de TCC, por um professor, mas nenhum aluno tem interesse nas áreas relacionadas")
            void testNaoEnviarNotificacaoAlunoNovoTema() throws Exception {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                System.setOut(new PrintStream(outputStream));

                AreaDeEstudo areaDeEstudo = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                        .nome("Musica")
                        .build()
                );
                AreaDeEstudoPostPutRequestDTO areaDeEstudoPostPutRequestDTO = AreaDeEstudoPostPutRequestDTO.builder()
                        .nome(areaDeEstudo.getNome())
                        .build();

                HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
                areasDeEstudo.add(areaDeEstudo);

                TemaTCCPostPutRequestDTO temaTCCPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                        .titulo("Analise da Inteligencia Artificial aplicada a musica")
                        .descricao("Explorando o uso da IA no ambito da musica sob uma perspectiva etica")
                        .areasDeEstudo(areasDeEstudo)
                        .build();
                TemaTCC temaTcc = temaTCCRepository.save(TemaTCC.builder()
                        .titulo(temaTCCPostPutRequestDTO.getTitulo())
                        .descricao((temaTCCPostPutRequestDTO.getDescricao()))
                        .areasDeEstudo(temaTCCPostPutRequestDTO.getAreasDeEstudo())
                        .build());

                temaTCCService.criar(temaTCCPostPutRequestDTO, professor.getId());

                String printOutput = outputStream.toString();
                System.setOut(System.out);

                assertFalse(printOutput.contains("Voce tem uma nova notificacao!"));
            }

            @Test
            @DisplayName("Quando é cadastrado um tema de TCC, por um aluno, nenhum aluno é notificado")
            void testNaoEnviarNotificacaoNovoTemaCadastradoPorAluno() throws Exception {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                System.setOut(new PrintStream(outputStream));

                temaTCCService.criar(temaTCCPostPutRequestDTO, aluno.getId());

                String printOutput = outputStream.toString();

                System.setOut(System.out);

                assertFalse(printOutput.contains("Voce tem uma nova notificacao!"));
            }

            @Test
            @DisplayName("Quando é cadastrado um tema de TCC, por um aluno, nenhum aluno é notificado")
            void testNaoEnviarNotificacaoNovoTemaCadastradoIdInvalido() throws Exception {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                System.setOut(new PrintStream(outputStream));

                assertThrows(UsuarioNaoExisteException.class, () -> {
                    temaTCCService.criar(temaTCCPostPutRequestDTO, UUID.randomUUID());
                });

                }
        }
    }


    @Nested
    @DisplayName("Conjunto de casos de verificação de listagem")
    class ListarTemasTCC {

        @Test
        @Transactional
        @DisplayName("Quando aluno lista os TemasTcc , sem nenhum professor que tenha cadastrado um Tema Tcc")
        void quandoAlunolistaTemasTccsemNenhumProfessorTerCadastrado() throws Exception {
            // Arrange
            // 3 professores, nenhum há TemasTcc cadastrado.
            professorRepository.deleteAll();

            HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
            areasDeInteresse.add(area);

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSI");

            Professor professor1 = Professor.builder()
                    .nomeCompleto("Jac")
                    .email("Jac@ccc")
                    .quota(3)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .build();
            Professor professor2 = Professor.builder()
                    .nomeCompleto("Karen")
                    .email("karen@ccc")
                    .quota(7)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .build();
            Professor professor3 = Professor.builder()
                    .nomeCompleto("Tavares")
                    .email("tav@ccc")
                    .quota(4)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .build();
            professorRepository.saveAll(Arrays.asList(professor1, professor2, professor3));
            // Act
            String responseJsonString = driver.perform(get(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Set<TemaTCC> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(0, resultado.size()));
        }

        @Test
        @DisplayName("Quando aluno lista os TemasTcc , com todos professores que tenha cadastrado um Tema Tcc")
        void quandoAlunolistaTemasTcccomTodosProfessorTerCadastrado() throws Exception {
            // Arrange
            // 3 professores que todos têm TemasTcc cadastrados, contando com o já salvo pelo setUp
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);

            Set<TemaTCC> temasTcc = new HashSet<>();
            temasTcc.add(temaTcc1);

            HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
            areasDeInteresse.add(area);

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSI");

            Professor professor1 = Professor.builder()
                    .nomeCompleto("Jac")
                    .email("Jac@ccc")
                    .quota(3)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            Professor professor2 = Professor.builder()
                    .nomeCompleto("Karen")
                    .email("karen@ccc")
                    .quota(7)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            professorRepository.saveAll(Arrays.asList(professor1, professor2));
            // Act
            String responseJsonString = driver.perform(get(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Set<TemaTCC> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size()));


        }

        @Test
        @DisplayName("Quando aluno lista TemasTcc cadastrados por professores, mas não há nenhum professor no banco")
        void quandoAlunolistaTemasTccSemNenhumProfessor() throws Exception {
            // Arrange
            // nenhum professor no banco.
            Set<TemaTCC> temasTcc = new HashSet<>();
            temasTcc.add(temaTcc1);

            professorRepository.deleteAll();

            // Act
            String responseJsonString = driver.perform(get(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Set<TemaTCC> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(0, resultado.size()));


        }

        @Test
        @DisplayName("Quando o perfil PROFESSOR tenta , listar TemasTcc cadastrados por professores")
        void quandoProfessorlistaTemasTccCadastradosPorProfessor() throws Exception {
            // Arrange
            // 3 professores que todos têm TemasTcc cadastrados
            Set<TemaTCC> temasTcc = new HashSet<>();
            temasTcc.add(temaTcc1);

            HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
            areasDeInteresse.add(area);

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSI");

            Professor professor1 = Professor.builder()
                    .nomeCompleto("Jac")
                    .email("Jac@ccc")
                    .quota(3)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            Professor professor3 = Professor.builder()
                    .nomeCompleto("Tavares")
                    .email("tav@ccc")
                    .quota(4)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            professorRepository.saveAll(Arrays.asList(professor1, professor3));
            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR"))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();


        }

        @Test
        @DisplayName("Quando professor lista os TemasTcc, sem nenhum aluno que tenha cadastrado um Tema Tcc")
        void quandoProfessorListaTemasTccSemNenhumAlunoTerCadastrado() throws Exception {
            // Arrange
            // 3 alunos, nenhum há TemasTcc cadastrado.

            HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
            areasDeInteresse.add(area);

            Aluno aluno1 = Aluno.builder()
                    .nomeCompleto("Jac")
                    .email("Jac@ccc")
                    .matricula("18339857")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeInteresse)
                    .build();
            Aluno aluno2 = Aluno.builder()
                    .nomeCompleto("Karen")
                    .email("karen@ccc")
                    .matricula("18339857")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeInteresse)
                    .build();
            Aluno aluno3 = Aluno.builder()
                    .nomeCompleto("Tavares")
                    .email("tav@ccc")
                    .matricula("18339857")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeInteresse)
                    .build();
            alunoRepository.saveAll(Arrays.asList(aluno1, aluno2, aluno3));
            // Act
            String responseJsonString = driver.perform(get(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Set<TemaTCC> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(0, resultado.size()));

        }

        @Test
        @DisplayName("Quando professor lista os TemasTcc, com todos alunos que tenha cadastrado um Tema Tcc")
        void quandoProfessorListaTemasTccComTodosAlunosTendoCadastrado() throws Exception {
            // Arrange
            // 3 alunos que todos têm TemasTcc cadastrados
            HashSet<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);

            Set<TemaTCC> temasTcc = new HashSet<>();
            temasTcc.add(temaTcc1);

            HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
            areasDeInteresse.add(area);

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSI");

            Aluno aluno1 = Aluno.builder()
                    .nomeCompleto("Jac")
                    .email("Jac@ccc")
                    .matricula("18339857")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            Aluno aluno2 = Aluno.builder()
                    .nomeCompleto("Karen")
                    .email("karen@ccc")
                    .matricula("18339857")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            Aluno aluno3 = Aluno.builder()
                    .nomeCompleto("Tavares")
                    .email("tav@ccc")
                    .matricula("18339857")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            alunoRepository.saveAll(Arrays.asList(aluno1, aluno2, aluno3));
            // Act
            String responseJsonString = driver.perform(get(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Set<TemaTCC> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size()));
        }

        @Test
        @DisplayName("Quando professor lista TemasTcc cadastrados por alunos, mas não há nenhum aluno no banco")
        void quandoProfessorListaTemasTccSemNenhumAluno() throws Exception {
            // Arrange
            // nenhum professor no banco.
            Set<TemaTCC> temasTcc = new HashSet<>();
            temasTcc.add(temaTcc1);

            // Act
            String responseJsonString = driver.perform(get(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Set<TemaTCC> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(0, resultado.size()));

        }

        @Test
        @DisplayName("Quando o perfil ALUNO tenta, listar TemasTcc cadastrados por alunos")
        void quandoAlunoListaTemasTccCadastradosPorAluno() throws Exception {

            Set<TemaTCC> temasTcc = new HashSet<>();
            temasTcc.add(temaTcc1);

            HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
            areasDeInteresse.add(area);

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSI");

            Professor professor1 = Professor.builder()
                    .nomeCompleto("Jac")
                    .email("Jac@ccc")
                    .quota(3)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            Professor professor3 = Professor.builder()
                    .nomeCompleto("Tavares")
                    .email("tav@ccc")
                    .quota(4)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            professorRepository.saveAll(Arrays.asList(professor1, professor3));
            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO"))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();
        }

        @Test
        @DisplayName("Quando o perfil ALUNO tenta, listar TemasTcc cadastrados por alunos")
        void quandoCoordenadorTentaListarTemasTccCadastradosPorAlunoOuProfessor() throws Exception {

            Set<TemaTCC> temasTcc = new HashSet<>();
            temasTcc.add(temaTcc1);

            HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
            areasDeInteresse.add(area);

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSI");

            Professor professor1 = Professor.builder()
                    .nomeCompleto("Jac")
                    .email("Jac@ccc")
                    .quota(3)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            Professor professor3 = Professor.builder()
                    .nomeCompleto("Tavares")
                    .email("tav@ccc")
                    .quota(4)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .temasTcc(temasTcc)
                    .build();
            professorRepository.saveAll(Arrays.asList(professor1, professor3));
            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR"))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();
        }


        @Test
        @Transactional
        @DisplayName("Quando Professor busca por temasTcc cadastrados por ele")
        void quandoProfessorBuscaPorTemasTccCadastrados() throws Exception {
                //Professor tem dois temas cadastrados
               
        Set<TemaTCC> temasTcc = new HashSet<>();
        temasTcc.add(temaTcc1);
        temasTcc.add(temaTcc2);
        professor.setTemasTcc(temasTcc);
        
                
        professorRepository.save(professor);
                       
                String responseJsonString = driver.perform(get(URI_TEMAS + "/listaTemaTccProfessor" + "/" + professor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("perfil", "PROFESSOR")
                        .param("id", String.valueOf(professor.getId()))
                        .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                Set<TemaTCCResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});
                       assertAll(
                
                    () -> assertEquals(temasTcc.size(),
                            resultado.size()));
        }

        @Test
        @Transactional
        @DisplayName("Quando é passado um idProfessor ,sem TemasTcc a listar")
        void quandoPassaUmIdProfesdorSemTemasTcc() throws Exception {
                //Professor não tem nenhum temaTcc cadastrado
        Set<TemaTCC> temasTcc = new HashSet<>();
        HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
            areasDeInteresse.add(area);

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSI");

            Professor professor1 = Professor.builder()
                    .nomeCompleto("Jac")
                    .email("Jac@ccc")
                    .quota(3)
                    .laboratorios(laboratorios)
                    .areasDeInteresse(areasDeInteresse)
                    .build();


            // Act
            String responseJsonString = driver.perform(get(URI_TEMAS + "/listaTemaTccProfessor" + "/" + professor1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .param("id", String.valueOf(professor1.getId()))
                            .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

        }

        @Test
        @Transactional
        @DisplayName("Quando é passado um idAluno nao autorizado ,para listar TemaTcc de Professor")
        void quandoPassaUmPerfilNaoAutorizadoParaListarTemaTccProfessor() throws Exception {
                //Professor tem dois temas cadastrados, mas perfil a ser acessado é ALUNO
               
        Set<TemaTCC> temasTcc = new HashSet<>();
        temasTcc.add(temaTcc1);
        temasTcc.add(temaTcc2);
        professor.setTemasTcc(temasTcc);
        
        
                        // Act
                String responseJsonString = driver.perform(get(URI_TEMAS + "/listaTemaTccProfessor" + "/" + professor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("perfil", "ALUNO")
                        .param("id", String.valueOf(professor.getId()))
                        .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                        .andExpect(status().isBadRequest()) // Codigo 200
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

        }

        @Test
        @Transactional
        @DisplayName("Quando é passado um idProfessor inválido ,para listar TemaTcc de Professor")
        void quandoPassaUmIdProfessorInvalidoParaListarTemaTccProfessor() throws Exception {
                //Professor tem dois temas cadastrados, mas perfil a ser acessado é ALUNO
               
        Set<TemaTCC> temasTcc = new HashSet<>();
        temasTcc.add(temaTcc1);
        temasTcc.add(temaTcc2);
        professor.setTemasTcc(temasTcc);
        UUID random = UUID.randomUUID();
        
                        // Act
                String responseJsonString = driver.perform(get(URI_TEMAS + "/listaTemaTccProfessor" + "/" + random)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("perfil", "ALUNO")
                        .param("id", String.valueOf(professor.getId()))
                        .content(objectMapper.writeValueAsString(temaTCCPostPutRequestDTO)))
                        .andExpect(status().isBadRequest()) // Codigo 200
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação para solicitação de orientação em temas de tcc" )
    class SolicitarOrientacaoTCC {

        @Test
        @jakarta.transaction.Transactional
        @DisplayName("Quando criamos uma solicitação para orientação em tema de tcc criado pelo estudante.")
        void quandoSolicitarOrientacaoEmTemaDoAlunoValido() throws Exception {

            aluno.getTemasTcc().add(temaTcc2);

            assertEquals(1, aluno.getTemasTcc().size());
            assertEquals(2, temaTCCRepository.count());
            assertEquals(0, solicitacaoRepository.count());

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS + "/orientacaoTemaAluno")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idAluno", String.valueOf(aluno.getId()))
                            .param("idProfessor", String.valueOf(professor.getId()))
                            .param("idTema", String.valueOf(temaTcc2.getId())))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            assertEquals(1, solicitacaoRepository.count());
            assertEquals(temaTcc2.getStatus(), Status.valueOf("PENDENTE"));
        }

        @Test
        @jakarta.transaction.Transactional
        @DisplayName("Quando criamos uma solicitação para orientação em tema de tcc que não existe")
        void quandoSolicitarOrientacaoEmTemaInexistenteDeAlunoInvalido() throws Exception {

            assertEquals(0, aluno.getTemasTcc().size());
            assertEquals(2, temaTCCRepository.count());
            assertEquals(0, solicitacaoRepository.count());

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS + "/orientacaoTemaAluno")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idAluno", String.valueOf(aluno.getId()))
                            .param("idProfessor", String.valueOf(professor.getId()))
                            .param("idTema", String.valueOf(9999999)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("O tema consultado nao existe!", resultado.getMessage());
        }

        @Test
        @jakarta.transaction.Transactional
        @DisplayName("Quando criamos uma solicitação para orientação de um professor que não existe.")
        void quandoSolicitarOrientacaoEmTemaDeAlunoComProfessorInexistenteInvalido() throws Exception {

            aluno.getTemasTcc().add(temaTcc2);

            assertEquals(1, aluno.getTemasTcc().size());
            assertEquals(2, temaTCCRepository.count());
            assertEquals(0, solicitacaoRepository.count());

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS + "/orientacaoTemaAluno")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idAluno", String.valueOf(aluno.getId()))
                            .param("idProfessor", "999f9999-9999-9999-99f9-9ff99ff9f999")
                            .param("idTema", String.valueOf(temaTcc2.getId())))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("O professor consultado nao existe!", resultado.getMessage());
        }

        /*Testes referentes à US12, método de solicitar orientação para tema proposto pelo professor.*/

        @Test
        @jakarta.transaction.Transactional
        @DisplayName("Quando criamos uma solicitação para orientação em tema de tcc criado pelo professor.")
        void quandoSolicitaOrientacaoEmTemaDoProfessorValido() throws Exception {

            assertEquals(1, professor.getTemasTcc().size());
            assertEquals(2, temaTCCRepository.count());
            assertEquals(0, solicitacaoRepository.count());

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS + "/orientacaoTemaProf")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idAluno", String.valueOf(aluno.getId()))
                            .param("idTema", String.valueOf(temaTcc1.getId()))
                            .param("idProfessor", String.valueOf(professor.getId())))

                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            assertEquals(1, solicitacaoRepository.count());
        }

        @Test
        @jakarta.transaction.Transactional
        @DisplayName("Quando criamos uma solicitação para orientação em tema de tcc que não existe")
        void quandoSolicitarOrientacaoEmTemaInexistenteDeProf() throws Exception {

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS + "/orientacaoTemaProf")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idAluno", String.valueOf(aluno.getId()))
                            .param("idTema", String.valueOf(9999999))
                            .param("idProfessor", String.valueOf(professor.getId())))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("O tema consultado nao existe!", resultado.getMessage());
        }

        @Test
        @jakarta.transaction.Transactional
        @DisplayName("Quando criamos uma solicitação para orientação de um tema cadastrado por um professor e o professor não existe.")
        void quandoSolicitarOrientacaoEmTemaProfComProfessorInexistente() throws Exception {

            assertEquals(2, temaTCCRepository.count());
            assertEquals(0, solicitacaoRepository.count());

            // Act
            String responseJsonString = driver.perform(post(URI_TEMAS + "/orientacaoTemaProf")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("idAluno", String.valueOf(aluno.getId()))
                            .param("idTema", String.valueOf(temaTcc1.getId()))
                            .param("idProfessor", String.valueOf("999f9999-9999-9999-99f9-9ff99ff9f999")))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("O professor consultado nao existe!", resultado.getMessage());
        }
    }

    @Nested
    @Transactional
    @DisplayName("Conjunto de casos de verificação do relatório")
    class GerarRelatorioDoSistema {

        @Test
        @DisplayName("Quando o coordenador solicita um relatório do sistema")
        void testCoordenadorSolicitaRelatorioDoSistema() throws Exception {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(outputStream));

            temaTCCService.gerarRelatorio(Perfil.COORDENADOR);

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertTrue(printOutput.contains("Relatorio do Sistema")),
                    () -> assertTrue(printOutput.contains("Temas alocados")),
                    () -> assertTrue(printOutput.contains("Temas nao alocados")),
                    () -> assertTrue(printOutput.contains("Alunos sem orientador")),
                    () -> assertTrue(printOutput.contains("Professores sem orientandos"))
            );
        }

        @Test
        @DisplayName("Quando um aluno tenta solicitar um relatório do sistema")
        void testAlunoTentaSolicitarRelatorioDoSistema() throws Exception {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(outputStream));

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertThrows(AcessoNegadoException.class,
                    () -> temaTCCService.gerarRelatorio(Perfil.ALUNO));
        }

        @Test
        @DisplayName("Quando um professor tenta solicitar um relatório do sistema")
        void testProfessorTentaSolicitarRelatorioDoSistema() throws Exception {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(outputStream));

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertThrows(AcessoNegadoException.class,
                    () -> temaTCCService.gerarRelatorio(Perfil.PROFESSOR));
        }

        @Test
        @DisplayName("Exibe relatório completo")
        void testRelatorioDoSistema() throws Exception {

            AreaDeEstudo areasDeEstudoWeb = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                    .nome("Desenvolvimento Web").build());

            AreaDeEstudo areasDeEstudoCyberSeguranca = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                    .nome("Cyber Seguranca").build());

            AreaDeEstudo areasDeEstudoBancoDados = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                    .nome("Banco de Dados")
                    .build());

            AreaDeEstudo areasDeEstudoEngSoftware = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                    .nome("Engenharia de Software")
                    .build());

            TemaTCCPostPutRequestDTO tema1 = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Estrategias de Otimização em Aplicações Web")
                    .descricao("Analise de tecnicas para otimizacao de performance em sites e aplicativos web")
                    .areasDeEstudo(new HashSet<>(Arrays.asList(areasDeEstudoWeb)))
                    .build();

            TemaTCC tema1Alocado = temaTCCRepository.save(TemaTCC.builder()
                    .titulo(tema1.getTitulo())
                    .descricao(tema1.getDescricao())
                    .areasDeEstudo(tema1.getAreasDeEstudo())
                    .build());

            TemaTCCPostPutRequestDTO tema2 = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Medidas para Fortalecimento da Cyber Segurança")
                    .descricao("Investigação de métodos e tecnologias para aumentar a segurança cibernética")
                    .areasDeEstudo(new HashSet<>(Arrays.asList(areasDeEstudoCyberSeguranca)))
                    .build();

            TemaTCC tema2Alocado = temaTCCRepository.save(TemaTCC.builder()
                    .titulo(tema2.getTitulo())
                    .descricao(tema2.getDescricao())
                    .areasDeEstudo(tema2.getAreasDeEstudo())
                    .build());

            TemaTCCPostPutRequestDTO tema3 = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Design Responsivo em Desenvolvimento Web")
                    .descricao("Exploração das melhores práticas em design responsivo para websites e aplicativos")
                    .areasDeEstudo(new HashSet<>(Arrays.asList(areasDeEstudoWeb)))
                    .build();

            TemaTCC tema3Alocado = temaTCCRepository.save(TemaTCC.builder()
                    .titulo(tema3.getTitulo())
                    .descricao(tema3.getDescricao())
                    .areasDeEstudo(tema3.getAreasDeEstudo())
                    .build());

            TemaTCCPostPutRequestDTO tema4 = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Refatoração de Código para Manutenção de Software")
                    .descricao("Estudo sobre práticas de refatoração de código para melhorar a manutenção de software")
                    .areasDeEstudo(new HashSet<>(Arrays.asList(areasDeEstudoEngSoftware)))
                    .build();

            TemaTCC tema4Alocado = temaTCCRepository.save(TemaTCC.builder()
                    .titulo(tema4.getTitulo())
                    .descricao(tema4.getDescricao())
                    .areasDeEstudo(tema4.getAreasDeEstudo())
                    .build());

            TemaTCCPostPutRequestDTO tema5 = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Métodos de Backup e Recuperação em Banco de Dados")
                    .descricao("Avaliação de métodos de backup e recuperação em sistemas de banco de dados")
                    .areasDeEstudo(new HashSet<>(Arrays.asList(areasDeEstudoBancoDados)))
                    .build();

            TemaTCC tema5Alocado = temaTCCRepository.save(TemaTCC.builder()
                    .titulo(tema5.getTitulo())
                    .descricao(tema5.getDescricao())
                    .areasDeEstudo(tema5.getAreasDeEstudo())
                    .build());

            Aluno aluno1 = alunoRepository.save(Aluno.builder()
                    .nomeCompleto("Arthur Alves")
                    .email("arthur.alves@ccc")
                    .matricula("121210111")
                    .periodoConclusao("2025.2")
                    .build());

            Aluno aluno2 = alunoRepository.save(Aluno.builder()
                    .nomeCompleto("Bruna Ferreira")
                    .email("bruna@ccc")
                    .matricula("121210112")
                    .periodoConclusao("2025.1")
                    .build());

            Aluno aluno3 = alunoRepository.save(Aluno.builder()
                    .nomeCompleto("Adalberto Oliveira")
                    .email("adalberto.oliveira@ccc")
                    .matricula("121210113")
                    .periodoConclusao("2024.2")
                    .build());

            Aluno aluno4 = alunoRepository.save(Aluno.builder()
                    .nomeCompleto("Daniela Pereira")
                    .email("daniela@ccc")
                    .matricula("121210114")
                    .periodoConclusao("2024.1")
                    .build());

            Aluno aluno5 = alunoRepository.save(Aluno.builder()
                    .nomeCompleto("Eduardo Santos")
                    .email("eduardo@ccc")
                    .matricula("121210115")
                    .periodoConclusao("2025.2")
                    .build());

            Professor professor1 = professorRepository.save(Professor.builder()
                    .nomeCompleto("Samara Silva")
                    .email("samara.silva@ccc")
                    .laboratorios(new HashSet<>(Arrays.asList("NuFuturo", "Brain")))
                    .quota(8)
                    .temasTcc(new HashSet<>(Arrays.asList(tema1Alocado)))
                    .areasDeInteresse(new HashSet<>(Arrays.asList(areasDeEstudoWeb)))
                    .build());

            Professor professor2 = professorRepository.save(Professor.builder()
                    .nomeCompleto("Ivanildo Silva")
                    .email("ivanildo.silva@ccc")
                    .laboratorios(new HashSet<>(Arrays.asList("Lacina")))
                    .quota(8)
                    .temasTcc(new HashSet<>(Arrays.asList(tema2Alocado)))
                    .areasDeInteresse(new HashSet<>(Arrays.asList(areasDeEstudoCyberSeguranca)))
                    .build());

            Professor professor3 = professorRepository.save(Professor.builder()
                    .nomeCompleto("Lívia Sampaio")
                    .email("livia.sampaio@ccc")
                    .laboratorios(new HashSet<>(Arrays.asList("NuFuturo", "Lacina")))
                    .quota(6)
                    .temasTcc(new HashSet<>(Arrays.asList(tema3Alocado)))
                    .areasDeInteresse(new HashSet<>(Arrays.asList(areasDeEstudoWeb)))
                    .build());

            Professor professor4 = professorRepository.save(Professor.builder()
                    .nomeCompleto("Patricia Costa")
                    .email("patricia.costa@ccc")
                    .laboratorios(new HashSet<>(Arrays.asList("SPLAB", "Lacina")))
                    .quota(10)
                    .temasTcc(new HashSet<>(Arrays.asList(tema4Alocado)))
                    .areasDeInteresse(new HashSet<>(Arrays.asList(areasDeEstudoEngSoftware)))
                    .build());

            Professor professor5 = professorRepository.save(Professor.builder()
                    .nomeCompleto("Tiago Massoni")
                    .email("tiago.massoni@ccc")
                    .laboratorios(new HashSet<>(Arrays.asList("SPLAB", "LSI")))
                    .quota(9)
                    .temasTcc(new HashSet<>(Arrays.asList(tema5Alocado)))
                    .areasDeInteresse(new HashSet<>(Arrays.asList(areasDeEstudoBancoDados)))
                    .build());

            temaTCCService.solicitarOrientacaoTemaProfessor(aluno1.getId(), tema1Alocado.getId(), professor1.getId());
            temaTCCService.solicitarOrientacaoTemaProfessor(aluno2.getId(), tema2Alocado.getId(), professor2.getId());
            temaTCCService.solicitarOrientacaoTemaProfessor(aluno3.getId(), tema3Alocado.getId(), professor3.getId());
            temaTCCService.solicitarOrientacaoTemaProfessor(aluno4.getId(), tema4Alocado.getId(), professor4.getId());
            temaTCCService.solicitarOrientacaoTemaProfessor(aluno5.getId(), tema5Alocado.getId(), professor5.getId());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(outputStream));

            temaTCCService.gerarRelatorio(Perfil.COORDENADOR);

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertTrue(printOutput.contains("Relatorio do Sistema")),
                    () -> assertTrue(printOutput.contains("Temas alocados")),
                    () -> assertTrue(printOutput.contains("Temas nao alocados")),
                    () -> assertTrue(printOutput.contains("Alunos sem orientador")),
                    () -> assertTrue(printOutput.contains("Professores sem orientandos"))
            );
        }
    }
}

