package com.ufcg.psoft.tccManager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.tccManager.dto.professor.ProfessorPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.professor.ProfessorResponseDTO;
import com.ufcg.psoft.tccManager.dto.temaTCC.TemaTCCPostPutRequestDTO;
import com.ufcg.psoft.tccManager.dto.temaTCC.TemaTCCResponseDTO;
import com.ufcg.psoft.tccManager.exception.TccErrorType;
import com.ufcg.psoft.tccManager.model.Aluno;
import com.ufcg.psoft.tccManager.model.AreaDeEstudo;
import com.ufcg.psoft.tccManager.model.TemaTCC;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.model.Professor;
import com.ufcg.psoft.tccManager.repository.*;
import com.ufcg.psoft.tccManager.service.Solicitacao.SolicitacaoService;
import com.ufcg.psoft.tccManager.service.temaTCC.TemaTCCService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Professores")
public class ProfessorControllerTests {

    final String URI_PROFESSORES = "/professores";

    @Autowired
    MockMvc driver;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProfessorRepository professorRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Professor professor;

    ProfessorPostPutRequestDTO professorPostPutRequestDTO;

    @Autowired
    AlunoRepository alunoRepository;
    @Autowired
    TemaTCCService temaTCCService;
    @Autowired
    AreaDeEstudoRepository areaDeEstudoRepository;
    @Autowired
    SolicitacaoService solicitacaoService;

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        HashSet<String> laboratorios = new HashSet<>();
        laboratorios.add("SPLAB");
        professor = professorRepository.save(Professor.builder()
                .perfil(Perfil.COORDENADOR)
                .nomeCompleto("Silva")
                .email("silva@ccc")
                .laboratorios(laboratorios)
                .quota(10)
                .build()
        );
        professorPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                .perfil(professor.getPerfil())
                .nomeCompleto(professor.getNomeCompleto())
                .email(professor.getEmail())
                .laboratorios(professor.getLaboratorios())
                .quota(professor.getQuota())
                .build();
    }

    @AfterEach
    void tearDown() {
        professorRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos criação")
    class ProfessorCriacao {

        @Test
        @DisplayName("Quando criamos um novo professor com dados válidos")
        void quandoCriarProfessorValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Professor resultado = objectMapper.readValue(responseJsonString, Professor.ProfessorBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(professorPostPutRequestDTO.getNomeCompleto(), resultado.getNomeCompleto())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo professor com perfil inválido")
        void PerfilAlunoCriandoProfessor() throws Exception {
            // Arrange
            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("SPLAB");
            ProfessorPostPutRequestDTO professorInvalidoPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("Silva")
                    .email("silva@ccc")
                    .laboratorios(laboratorios)
                    .quota(10)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(professorInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo professor com perfil inválido")
        void PerfilProfessorCriandoProfessor() throws Exception {
            // Arrange
            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("SPLAB");
            ProfessorPostPutRequestDTO professorInvalidoPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("Silva")
                    .email("silva@ccc")
                    .laboratorios(laboratorios)
                    .quota(10)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(professorInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo professor com nome inválido")
        void quandoCriarProfessorNomeInvalido() throws Exception {
            // Arrange

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("SPLAB");
            ProfessorPostPutRequestDTO professorInvalidoPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("")
                    .email("silva@ccc")
                    .laboratorios(laboratorios)
                    .quota(10)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(professorInvalidoPostPutRequestDTO)))
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
        @DisplayName("Quando criamos um novo professor sem laboratórios")
        void quandoCriarProfessorSemLaboratorios() throws Exception {
            // Arrange

            ProfessorPostPutRequestDTO professorInvalidoPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("Silva")
                    .email("silva@ccc")
                    .laboratorios(null)
                    .quota(10)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorInvalidoPostPutRequestDTO)))
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
        @DisplayName("Quando criamos um novo professor com quota negativa")
        void quandoCriarProfessorComQuotaNegativa() throws Exception {
            // Arrange

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("SPLAB");
            ProfessorPostPutRequestDTO professorInvalidoPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("Silva")
                    .email("silva@ccc")
                    .laboratorios(laboratorios)
                    .quota(-1)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorInvalidoPostPutRequestDTO)))
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
        @DisplayName("Quando criamos um novo professor com quota 0")
        void quandoCriarProfessorComQuotaZero() throws Exception {
            // Arrange

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("SPLAB");
            ProfessorPostPutRequestDTO professorInvalidoPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("Silva")
                    .email("silva@ccc")
                    .laboratorios(laboratorios)
                    .quota(0)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Professor resultado = objectMapper.readValue(responseJsonString, Professor.ProfessorBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(professorPostPutRequestDTO.getNomeCompleto(), resultado.getNomeCompleto())
            );
        }

        @Test
        @DisplayName("Quando criamos um novo professor completamente inválido")
        void quandoCriarProfessorInvalido() throws Exception {
            // Arrange

            HashSet<String> laboratorios = new HashSet<>();
            ProfessorPostPutRequestDTO professorInvalidoPostPutRequestDTO = ProfessorPostPutRequestDTO.builder()
                    .perfil(null)
                    .nomeCompleto("")
                    .email("")
                    .laboratorios(laboratorios)
                    .quota(-1)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorInvalidoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos de remoção")
    class ProfessorRemocao {
        @Test
        @DisplayName("Removendo professor existente no banco")
        void removerProfessorValido() throws Exception {
            // Arrange

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("Brain");

            Professor professor2 = professorRepository.save(Professor.builder()
                    .nomeCompleto("Novo Professor")
                    .email("novoprofessor@ccc")
                    .laboratorios(laboratorios)
                    .quota(3)
                    .build()
            );

            assertEquals(2, professorRepository.count());

            // Act
            String responseJsonString = driver.perform(delete(URI_PROFESSORES + "/" + professor2.getId())
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent()) // Codigo 200 pois retorna mensagem de confirmação
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertEquals("", responseJsonString);
            assertEquals(1, professorRepository.count());
        }

        @Test
        @DisplayName("Removendo professor existente no banco")
        void removerProfessorInexistente() throws Exception {

            assertEquals(1, professorRepository.count());
            UUID idInexistente = UUID.randomUUID();

            // Act
            String responseJsonString = driver.perform(delete(URI_PROFESSORES + "/" + idInexistente)
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals(1, professorRepository.count());
            assertEquals("O professor consultado nao existe!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando tentamos remover um professor com perfil inválido (professor)")
        void quandoRemovemosProfessorComPerfilInvalido() throws Exception {
            // Arrange

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("Brain");

            Professor professor2 = professorRepository.save(Professor.builder()
                    .nomeCompleto("Novo Professor")
                    .email("novoprofessor@ccc")
                    .laboratorios(laboratorios)
                    .quota(3)
                    .build()
            );

            assertEquals(2, professorRepository.count());

            // Act
            String responseJsonString = driver.perform(delete(URI_PROFESSORES + "/" + professor2.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) //COdigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage()),
                    () -> assertEquals(2, professorRepository.count())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos de alteração")
    class ProfessorAlteracoes {
        @Test
        @DisplayName("Quando alteramos o nome do professor com nome válidos")
        void quandoAlteramosNomeDoProfessorValido() throws Exception {
            // Arrange
            professorPostPutRequestDTO.setNomeCompleto("Novo Nome Professor");

            // Act
            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + professor.getId())
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Professor resultado = objectMapper.readValue(responseJsonString, Professor.class);

            // Assert
            assertEquals("Novo Nome Professor", resultado.getNomeCompleto());
        }

        @Test
        @DisplayName("Quando alteramos vários dados do professor com informações válidas.")
        void quandoAlteramosDadosDoProfessorValido() throws Exception {
            // arrange
            professorPostPutRequestDTO.setNomeCompleto("Novo Nome");
            professorPostPutRequestDTO.setEmail("novoemail@ccc");
            HashSet<String> novoLaboratorio = new HashSet<>();
            novoLaboratorio.add("LIAD");
            professorPostPutRequestDTO.setLaboratorios(novoLaboratorio);
            professorPostPutRequestDTO.setQuota(3);

            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + professor.getId())
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Professor resultado = objectMapper.readValue(responseJsonString, Professor.class);

            HashSet<String> laboratorioResposta = new HashSet<>();
            laboratorioResposta.add("LIAD");

            assertAll(
                    () -> assertEquals("Novo Nome", resultado.getNomeCompleto()),
                    () -> assertEquals("novoemail@ccc", resultado.getEmail()),
                    () -> assertEquals(laboratorioResposta, resultado.getLaboratorios()),
                    () -> assertEquals(3, resultado.getQuota())
            );
        }

        @Test
        @DisplayName("Quando alteramos o dados do professor com nome perfil inválido (aluno)")
        void quandoAlteramosDadosDoProfessorComPerfilInvalido() throws Exception {
            // Arrange
            professorPostPutRequestDTO.setNomeCompleto("Novo Nome Professor");

            // Act
            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + professor.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) //COdigo 404
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos o nome do professor com nome inválido")
        void quandoAlteramosNomeDoProfessorParaNuloInvalido() throws Exception {
            // Arrange
            professorPostPutRequestDTO.setNomeCompleto("");

            // Act
            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + professor.getId())
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos o email do professor com nome inválido")
        void quandoAlteramosEmailDoProfessorParaNuloInvalido() throws Exception {
            // Arrange
            professorPostPutRequestDTO.setEmail("");

            // Act
            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + professor.getId())
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos os laboratórios do professor para nulo")
        void quandoAlteramosLaboratoriosDoProfessorParaNulo() throws Exception {
            // Arrange
            professorPostPutRequestDTO.setLaboratorios(null);

            // Act
            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + professor.getId())
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos os laboratórios do professor para vazio")
        void quandoAlteramosLaboratoriosDoProfessorParaVazio() throws Exception {
            // Arrange
            HashSet<String> laboratorioInvalido = new HashSet<>();
            professorPostPutRequestDTO.setLaboratorios(laboratorioInvalido);

            // Act
            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + professor.getId())
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("Professor deve pertencer a, pelo menos um, laboratorio!", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos quota do professor com dados inválidos.")
        void quandoAlteramosQuotaDoProfessorInvalido() throws Exception {
            // Arrange
            professorPostPutRequestDTO.setQuota(null);

            // Act
            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + professor.getId())
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
        }


        @Test
        @DisplayName("Quando alteramos vários dados de um professor inexistente")
        void quandoAlteramosDadosDeProfessorInexistente() throws Exception {
            // arrange
            professorPostPutRequestDTO.setNomeCompleto("Nome");
            professorPostPutRequestDTO.setEmail("email@ccc");
            HashSet<String> novoLaboratorio = new HashSet<>();
            novoLaboratorio.add("LIAD");
            professorPostPutRequestDTO.setLaboratorios(novoLaboratorio);
            professorPostPutRequestDTO.setQuota(3);

            UUID idInexistente = UUID.randomUUID();


            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/" + idInexistente)
                            .param("perfil", "COORDENADOR")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            assertEquals("O professor consultado nao existe!", resultado.getMessage());

        }

        @Test
        @DisplayName("Quando alteramos a quota do professor válida")
        void quandoAtualizamosQuotaValido() throws Exception {

            assertEquals(professor.getQuota(), 10);

            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/atualizarQuota/" + professor.getId())
                            .param("perfil", "PROFESSOR")
                            .param("novaQuota", "3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Professor resultado = objectMapper.readValue(responseJsonString, Professor.class);

            assertEquals(resultado.getQuota(), 3);
        }

        @Test
        @DisplayName("Quando alteramos a quota do professor negativa (inválida)")
        void quandoAtualizamosQuotaNegativaInvalido() throws Exception {

            assertEquals(professor.getQuota(), 10);

            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/atualizarQuota/" + professor.getId())
                            .param("perfil", "PROFESSOR")
                            .param("novaQuota", "-3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            assertEquals("A quota inserida eh invalida.", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos a quota do professor com perfil inválido")
        void quandoAtualizamosQuotaPerfilInvalido() throws Exception {

            assertEquals(professor.getQuota(), 10);

            String responseJsonString = driver.perform(put(URI_PROFESSORES + "/atualizarQuota/" + professor.getId())
                            .param("perfil", "COORDENADOR")
                            .param("novaQuota", "3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage());
        }

    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos de remoção")
    class ProfessorBuscas {
        @Test
        @DisplayName("Quando buscamos por todos professores salvos")
        void quandoBuscamosPorTodosProfessoresSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 professores no banco
            HashSet<String> laboratorio1 = new HashSet<>();
            HashSet<String> laboratorio2 = new HashSet<>();
            laboratorio1.add("LSD");
            laboratorio2.add("LACINA");
            Professor professor2 = Professor.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("Joao")
                    .email("joao@ccc")
                    .laboratorios(laboratorio1)
                    .quota(5)
                    .build();

            Professor professor3 = Professor.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("Luiz")
                    .email("luiz@ccc")
                    .laboratorios(laboratorio2)
                    .quota(5)
                    .build();

            professorRepository.saveAll(Arrays.asList(professor2, professor3));

            // Act
            String responseJsonString = driver.perform(get(URI_PROFESSORES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Professor> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos um professor salvo pelo id")
        void quandoBuscamosPorUmProfessorSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_PROFESSORES + "/" + professor.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            ProfessorResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

            // Assert
            assertAll(
                    () -> assertEquals(professor.getId(), resultado.getId()),
                    () -> assertEquals(professor.getNomeCompleto(), resultado.getNomeCompleto())
            );
        }

        @Test
        @DisplayName("Quando buscamos um professor inexistente")
        void quandoBuscamosPorUmProfessorInexistente() throws Exception {
            // Arrange
            UUID idInexistente = UUID.randomUUID();

            // Act
            String responseJsonString = driver.perform(get(URI_PROFESSORES + "/" + idInexistente)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(professorPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O professor consultado nao existe!", resultado.getMessage())
            );
        }
    }

        @Test
        @Transactional
        @DisplayName("Quando informamos uma área de interesse do professor")
        void informarAreaDeInteresseProf() throws Exception{

                AreaDeEstudo area = areaDeEstudoRepository.save(AreaDeEstudo.builder().nome("Inteligencia Artificial").build());

                String responseJsonString = driver.perform(put(URI_PROFESSORES + "/interesseProf" + "/" + professor.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("idArea", String.valueOf(area.getId())))
                                .andExpect(status().isOk()) // Codigo 200
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

                assertEquals(professor.getAreasDeInteresse().size(), 1);
        }

        @Test
        @Transactional
        @DisplayName("Quando informamos uma área de interesse inexistente")
        void informarAreaDeInteresseInvalidaProf() throws Exception{

                String responseJsonString = driver.perform(put(URI_PROFESSORES + "/interesseProf" + "/" + professor.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("idArea", String.valueOf("9999999")))
                        .andExpect(status().isBadRequest()) //Codigo 400
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);
                assertEquals("A area de estudo consultada nao existe!", resultado.getMessage());
        }

        @Test
        @Transactional
        @DisplayName("Quando informamos uma área de interesse com ID de professor inválido")
        void informarAreaDeInteresseIdProfessorInvalido() throws Exception {

                UUID idInexistente = UUID.randomUUID();
                AreaDeEstudo area = areaDeEstudoRepository.save(AreaDeEstudo.builder().nome("Inteligencia Artificial").build());

                String responseJsonString = driver.perform(put(URI_PROFESSORES + "/interesseProf" + "/" + idInexistente)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("idArea", String.valueOf(area.getId())))
                        .andExpect(status().isBadRequest()) // Código 400
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);
                assertEquals("O professor consultado nao existe!", resultado.getMessage());
        }

    @Nested
    @DisplayName("Conjunto de casos de verificação do recebimento de notificações")
    class ProfessorNotificacoes {

        @Test
        @Transactional
        @DisplayName("Quando é solicitado orientação em um tema de TCC de professor")
        void testEnviarNotificacaoProfessorNovaSolicitacaoTemaProf() throws Exception {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(outputStream));

            AreaDeEstudo area = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                    .nome("Inteligencia Artificial").build());
            Set<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);

            Aluno aluno = alunoRepository.save(Aluno.builder()
                    .nomeCompleto("Silva")
                    .email("silva@ccc")
                    .matricula("121210111")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeEstudo)
                    .build());
            TemaTCCPostPutRequestDTO temaTCCPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Analise da Inteligencia Artificial aplicada a educacao")
                    .descricao("Explorando o uso da IA no ambito da educacao sob uma perspectiva etica")
                    .areasDeEstudo(areasDeEstudo)
                    .build();

            //professor cria tema
            TemaTCCResponseDTO temaTccResponseDTO = temaTCCService.criar(temaTCCPostPutRequestDTO, professor.getId());
            TemaTCC temaTcc = modelMapper.map(temaTccResponseDTO, TemaTCC.class);
            solicitacaoService.criarSolicitacao(aluno, temaTcc, professor);

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertTrue(printOutput.contains("| Professor " + professor.getNomeCompleto() + ", \n")),
                    () -> assertTrue(printOutput.contains("| No tema: " + temaTcc.getTitulo() + "\n")),
                    () -> assertTrue(printOutput.contains("| Pelo aluno: " + aluno.getNomeCompleto() + "   Email:" + aluno.getEmail() + "\n"))
            );
        }

        @Test
        @Transactional
        @DisplayName("Quando é solicitado orientação em um tema de TCC de aluno")
        void testEnviarNotificacaoProfessorNovaSolicitacaoTemaAluno() throws Exception {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(outputStream));

            AreaDeEstudo area = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                    .nome("Inteligencia Artificial").build());
            Set<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);

            Aluno aluno = alunoRepository.save(Aluno.builder()
                    .nomeCompleto("Silva")
                    .email("silva@ccc")
                    .matricula("121210111")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeEstudo)
                    .build());
            TemaTCCPostPutRequestDTO temaTCCPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Analise da Inteligencia Artificial aplicada a educacao")
                    .descricao("Explorando o uso da IA no ambito da educacao sob uma perspectiva etica")
                    .areasDeEstudo(areasDeEstudo)
                    .build();

            // aluno cria tema
            TemaTCCResponseDTO temaTccResponseDTO = temaTCCService.criar(temaTCCPostPutRequestDTO, aluno.getId());
            TemaTCC temaTcc = modelMapper.map(temaTccResponseDTO, TemaTCC.class);
            solicitacaoService.criarSolicitacao(aluno, temaTcc, professor);

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertTrue(printOutput.contains("| Professor " + professor.getNomeCompleto() + ", \n")),
                    () -> assertTrue(printOutput.contains("| No tema: " + temaTcc.getTitulo() + "\n")),
                    () -> assertTrue(printOutput.contains("| Pelo aluno: " + aluno.getNomeCompleto() + "   Email:" + aluno.getEmail() + "\n"))
            );
        }


        @Test
        @Transactional
        @DisplayName("Quando é solicitado orientação em um tema de TCC, apenas aquele professor específico recebe a mensagem.")
        void testEnviarNotificacaoProfessorNovaSolicitacaoProfessorCorreto() throws Exception {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setOut(new PrintStream(outputStream));

            AreaDeEstudo area = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                    .nome("Inteligencia Artificial").build());
            Set<AreaDeEstudo> areasDeEstudo = new HashSet<>();
            areasDeEstudo.add(area);

            HashSet<String> laboratorios = new HashSet<>();
            laboratorios.add("LSD");

            Aluno aluno = alunoRepository.save(Aluno.builder()
                    .nomeCompleto("Silva")
                    .email("silva@ccc")
                    .matricula("121210111")
                    .periodoConclusao("2025.2")
                    .areasDeInteresse(areasDeEstudo)
                    .build());
            TemaTCCPostPutRequestDTO temaTCCPostPutRequestDTO = TemaTCCPostPutRequestDTO.builder()
                    .titulo("Analise da Inteligencia Artificial aplicada a educacao")
                    .descricao("Explorando o uso da IA no ambito da educacao sob uma perspectiva etica")
                    .areasDeEstudo(areasDeEstudo)
                    .build();
            Professor professor2 = professorRepository.save(Professor.builder()
                    .perfil(Perfil.PROFESSOR)
                    .nomeCompleto("Marcus")
                    .email("marcus@ccc")
                    .laboratorios(laboratorios)
                    .quota(10)
                    .build()
            );

            // o professor2 cria tema
            TemaTCCResponseDTO temaTccResponseDTO = temaTCCService.criar(temaTCCPostPutRequestDTO, professor2.getId());
            TemaTCC temaTcc = modelMapper.map(temaTccResponseDTO, TemaTCC.class);
            solicitacaoService.criarSolicitacao(aluno, temaTcc, professor2);

            String printOutput = outputStream.toString();
            System.setOut(System.out);

            assertAll(
                    () -> assertFalse(printOutput.contains("| Professor " + professor.getNomeCompleto() + ", \n")), //a notificacao nao contem o nome do professor que nao foi solicitado
                    () -> assertTrue(printOutput.contains("| Professor " + professor2.getNomeCompleto() + ", \n")),
                    () -> assertTrue(printOutput.contains("| No tema: " + temaTcc.getTitulo() + "\n")),
                    () -> assertTrue(printOutput.contains("| Pelo aluno: " + aluno.getNomeCompleto() + "   Email:" + aluno.getEmail() + "\n"))
            );
        }

    }
}
        