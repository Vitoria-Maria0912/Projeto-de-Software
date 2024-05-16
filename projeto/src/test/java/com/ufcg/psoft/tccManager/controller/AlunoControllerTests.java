package com.ufcg.psoft.tccManager.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.*;
import com.ufcg.psoft.tccManager.dto.professor.*;
import com.ufcg.psoft.tccManager.dto.aluno.*;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import com.ufcg.psoft.tccManager.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.tccManager.exception.TccErrorType;
import com.ufcg.psoft.tccManager.model.Aluno;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Alunos")
public class AlunoControllerTests {

        final String URI_ALUNOS = "/alunos";

        @Autowired
        MockMvc driver;

        @Autowired
        AlunoRepository alunoRepository;

        @Autowired
        AreaDeEstudoRepository areaDeEstudoRepository;


        @Autowired
        ProfessorRepository professorRepository;

        Professor professor;

        ObjectMapper objectMapper = new ObjectMapper();

        Aluno aluno;

        AlunoPostPutRequestDTO alunoPostPutRequestDTO;

        ProfessorResponseNomeEmailDTO professorResponseNomeEmailDTO;

        @BeforeEach
        void setup() {

                // Object Mapper suporte para LocalDateTime
                objectMapper.registerModule(new JavaTimeModule());
                alunoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                        .nomeCompleto("Silva")
                        .email("silva@ccc")
                        .matricula("121210111")
                        .periodoConclusao("2025.2")
                        .build();
                aluno = alunoRepository.save(Aluno.builder()
                        .nomeCompleto(alunoPostPutRequestDTO.getNomeCompleto())
                        .email(alunoPostPutRequestDTO.getEmail())
                        .matricula(alunoPostPutRequestDTO.getMatricula())
                        .periodoConclusao(alunoPostPutRequestDTO.getPeriodoConclusao())
                        .build());

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

        }

        @AfterEach
        void tearDown() {
                alunoRepository.deleteAll();
                professorRepository.deleteAll();
        }

        @Nested
        @DisplayName("Conjunto de casos de verificação dos fluxos de criação")
        class AlunoCriacao {
                @Test
                @DisplayName("Quando criamos um novo aluno com dados válidos")
                void quandoCriarAlunoValido() throws Exception {
                        // Arrange
                        // nenhuma necessidade além do setup()

                        // Act
                        String responseJsonString = driver.perform(post(URI_ALUNOS)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                        .andExpect(status().isCreated()) // Codigo 201
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        Aluno resultado = objectMapper.readValue(responseJsonString, Aluno.AlunoBuilder.class).build();

                        // Assert
                        assertAll(
                                () -> assertNotNull(resultado.getId()),
                                () -> assertEquals(alunoPostPutRequestDTO.getNomeCompleto(),
                                        resultado.getNomeCompleto()));
                }

                @Test
                @DisplayName("Quando criamos um novo aluno com nome inválido")
                void quandoCriarAlunoNomeInvalido() throws Exception {
                        // Arrange

                        objectMapper.registerModule(new JavaTimeModule());
                        Aluno alunoInvalido = alunoRepository.save(Aluno.builder()
                                .nomeCompleto("")
                                .email("silva@ccc")
                                .matricula("121210111")
                                .periodoConclusao("2025.2")
                                .build());
                        AlunoPostPutRequestDTO alunoInvalidoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                                .nomeCompleto(alunoInvalido.getNomeCompleto())
                                .email(alunoInvalido.getEmail())
                                .matricula(alunoInvalido.getMatricula())
                                .periodoConclusao(alunoInvalido.getPeriodoConclusao())
                                .build();

                        // Act
                        String responseJsonString = driver.perform(post(URI_ALUNOS)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoInvalidoPostPutRequestDTO)))
                                        .andExpect(status().isBadRequest()) // Codigo 400
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                        // Assert
                        assertAll(
                                () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()));
                }

                @Test
                @DisplayName("Quando criamos um novo aluno com email inválido")
                void quandoCriarAlunoEmailInvalido() throws Exception {
                        // Arrange

                        objectMapper.registerModule(new JavaTimeModule());
                        Aluno alunoInvalido = alunoRepository.save(Aluno.builder()
                                        .nomeCompleto("Silva")
                                        .email("")
                                        .matricula("121210111")
                                        .periodoConclusao("2025.2")
                                        .build());
                        AlunoPostPutRequestDTO alunoInvalidoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                                        .nomeCompleto(alunoInvalido.getNomeCompleto())
                                        .email(alunoInvalido.getEmail())
                                        .matricula(alunoInvalido.getMatricula())
                                        .periodoConclusao(alunoInvalido.getPeriodoConclusao())
                                        .build();

                        // Act
                        String responseJsonString = driver.perform(post(URI_ALUNOS)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoInvalidoPostPutRequestDTO)))
                                        .andExpect(status().isBadRequest()) // Codigo 400
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                        // Assert
                        assertAll(
                                () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()));
                }

                @Test
                @DisplayName("Quando criamos um novo aluno com matrícula inválido")
                void quandoCriarAlunoMatriculaInvalida() throws Exception {
                        // Arrange

                        objectMapper.registerModule(new JavaTimeModule());
                        Aluno alunoInvalido = alunoRepository.save(Aluno.builder()
                                        .nomeCompleto("Silva")
                                        .email("silva@ccc")
                                        .matricula("           ")
                                        .periodoConclusao("2025.2")
                                        .build());
                        AlunoPostPutRequestDTO alunoInvalidoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                                        .nomeCompleto(alunoInvalido.getNomeCompleto())
                                        .email(alunoInvalido.getEmail())
                                        .matricula(alunoInvalido.getMatricula())
                                        .periodoConclusao(alunoInvalido.getPeriodoConclusao())
                                        .build();

                        // Act
                        String responseJsonString = driver.perform(post(URI_ALUNOS)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoInvalidoPostPutRequestDTO)))
                                        .andExpect(status().isBadRequest()) // Codigo 400
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                        // Assert
                        assertAll(
                                        () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()));
                }

                @Test
                @DisplayName("Quando criamos um novo aluno com período inválido")
                void quandoCriarAlunoPeriodoInvalido() throws Exception {
                        // Arrange

                        objectMapper.registerModule(new JavaTimeModule());
                        Aluno alunoInvalido = alunoRepository.save(Aluno.builder()
                                        .nomeCompleto("")
                                        .email("silva@ccc")
                                        .matricula("121210111")
                                        .periodoConclusao("         ")
                                        .build());
                        AlunoPostPutRequestDTO alunoInvalidoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                                        .nomeCompleto(alunoInvalido.getNomeCompleto())
                                        .email(alunoInvalido.getEmail())
                                        .matricula(alunoInvalido.getMatricula())
                                        .periodoConclusao(alunoInvalido.getPeriodoConclusao())
                                        .build();

                        // Act
                        String responseJsonString = driver.perform(post(URI_ALUNOS)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoInvalidoPostPutRequestDTO)))
                                        .andExpect(status().isBadRequest()) // Codigo 400
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                        // Assert
                        assertAll(
                                        () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()));
                }

                @Test
                @DisplayName("Quando criamos um novo aluno completamente inválido")
                void quandoCriarAlunoInvalido() throws Exception {
                        // Arrange

                        objectMapper.registerModule(new JavaTimeModule());
                        Aluno alunoInvalido = alunoRepository.save(Aluno.builder()
                                        .nomeCompleto("")
                                        .email("")
                                        .matricula("")
                                        .periodoConclusao("")
                                        .build());
                        AlunoPostPutRequestDTO alunoInvalidoPostPutRequestDTO = AlunoPostPutRequestDTO.builder()
                                        .nomeCompleto(alunoInvalido.getNomeCompleto())
                                        .email(alunoInvalido.getEmail())
                                        .matricula(alunoInvalido.getMatricula())
                                        .periodoConclusao(alunoInvalido.getPeriodoConclusao())
                                        .build();

                        // Act
                        String responseJsonString = driver.perform(post(URI_ALUNOS)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoInvalidoPostPutRequestDTO)))
                                        .andExpect(status().isBadRequest()) // Codigo 400
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                        // Assert
                        assertAll(
                                        () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()));
                }
        }

        @Nested
        @DisplayName("Conjunto de casos de verificação dos fluxos de remoção")
        class AlunoRemocao {
                @Test
                @DisplayName("Quando excluímos um aluno válido")
                void quandoExcluimosAlunoValido() throws Exception {
                        // Arrange
                        // nenhuma necessidade além do setup()


                        assertEquals(1, alunoRepository.count());

                        // Act
                        String responseJsonString = driver.perform(delete(URI_ALUNOS + "/" + aluno.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR"))
                                .andExpect(status().isNoContent()) // Codigo 204
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

                        // Assert

                        assertTrue(responseJsonString.isBlank());
                }

                @Test
                @DisplayName("Removendo aluno existente no banco, sem ser coordenador")
                        // não deve remover , perfil inválido
                void removerAlunoPerfilInvalido() throws Exception {

                        assertEquals(1, alunoRepository.count());

                        // Act
                        String responseJsonString = driver.perform(delete(URI_ALUNOS + "/" + aluno.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "ALUNO"))
                                .andExpect(status().isBadRequest())
                                .andReturn().getResponse().getContentAsString();

                        // Assert
                        assertEquals(1, alunoRepository.count());
                }

                @Test
                @DisplayName("Removendo aluno inexistente no banco")
                        // não remove nada
                void removerAlunoInexistente() throws Exception {
                        // Aluno não existe
                        // Arrange
                        assertEquals(1, alunoRepository.count());

                        // Act
                        String responseJsonString = driver.perform(delete(URI_ALUNOS + "/" + 0000000)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR"))
                                .andExpect(status().isBadRequest() )
                                .andReturn().getResponse().getContentAsString();

                        // Assert
                        assertEquals(1, alunoRepository.count()); // A contagem de alunos não deve mudar, já que a remoção foi
                        // proibida
                }
        }

        @Nested
        @DisplayName("Conjunto de casos de verificação dos fluxos de buscas")
        class AlunoBuscas {
                @Test
                @DisplayName("Quando buscamos por todos alunos salvos")
                void quandoBuscamosPorTodosAlunosSalvos() throws Exception {
                        // Arrange
                        // Vamos ter 3 clientes no banco
                        Aluno aluno2 = Aluno.builder()
                                        .nomeCompleto("Almeida")
                                        .email("almeida@ccc")
                                        .matricula("121210222")
                                        .periodoConclusao("2025.1")
                                        .build();
                        Aluno aluno3 = Aluno.builder()
                                        .nomeCompleto("Lima")
                                        .email("lima@ccc")
                                        .matricula("121210333")
                                        .periodoConclusao("2024.2")
                                        .build();
                        alunoRepository.saveAll(Arrays.asList(aluno2, aluno3));

                        // Act
                        String responseJsonString = driver.perform(get(URI_ALUNOS)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                        .andExpect(status().isOk()) // Codigo 200
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        List<Aluno> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
                        });

                        // Assert
                        assertAll(
                                        () -> assertEquals(3, resultado.size()));
                }

                @Test
                @DisplayName("Quando buscamos um aluno salvo pelo id")
                void quandoBuscamosPorUmAlunoSalvo() throws Exception {
                        // Arrange
                        // nenhuma necessidade além do setup()

                        // Act
                        String responseJsonString = driver.perform(get(URI_ALUNOS + "/" + aluno.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                        .andExpect(status().isOk()) // Codigo 200
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        AlunoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
                        });

                        // Assert
                        assertAll(
                                        () -> assertEquals(aluno.getId(), resultado.getId()),
                                        () -> assertEquals(aluno.getNomeCompleto(), resultado.getNomeCompleto()));
                }

                @Test
                @DisplayName("Quando buscamos um aluno inexistente")
                void quandoBuscamosPorUmAlunoInexistente() throws Exception {
                        // Arrange
                        // nenhuma necessidade além do setup()

                        UUID idInexistente = UUID.randomUUID();

                        // Act
                        String responseJsonString = driver.perform(get(URI_ALUNOS + "/" + idInexistente)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR")
                                        .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                        .andExpect(status().isBadRequest()) // Codigo 400
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                        // Assert
                        assertAll(
                                        () -> assertEquals("O aluno consultado nao existe!", resultado.getMessage()));
                }
                
                @Test
                @DisplayName("Quando excluímos um aluno válido")
                void quandoExcluimosAlunoValido() throws Exception {
                        // Arrange
                        // nenhuma necessidade além do setup()
                 

                        assertEquals(1, alunoRepository.count());

                        // Act
                        String responseJsonString = driver.perform(delete(URI_ALUNOS + "/" + aluno.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "COORDENADOR"))
                                        .andExpect(status().isNoContent()) // Codigo 204
                                        .andDo(print())
                                        .andReturn().getResponse().getContentAsString();

                        // Assert
                     
                        assertTrue(responseJsonString.isBlank());
                }

                @Test
                @DisplayName("Removendo aluno existente no banco, sem ser coordenador")
                // não deve remover , perfil inválido
                void removerAlunoPerfilInvalido() throws Exception {

                        assertEquals(1, alunoRepository.count());

                        // Act
                        String responseJsonString = driver.perform(delete(URI_ALUNOS + "/" + aluno.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("perfil", "ALUNO"))
                                        .andExpect(status().isBadRequest())
                                        .andReturn().getResponse().getContentAsString();

                        // Assert
                        assertEquals(1, alunoRepository.count());
                }
        }

        @Nested
        @DisplayName("Conjunto de casos de verificação dos fluxos de alterações")
        class AlunoAlteracoes {
                // Testes de alteração nos dados
                @Test
                @DisplayName("Quando alteramos o nome do aluno com nome válidos")
                void quandoAlteramosNomeDoAlunoValido() throws Exception {
                        // Arrange
                        alunoPostPutRequestDTO.setNomeCompleto("carmem");

                // Act
                String responseJsonString = driver.perform(put(URI_ALUNOS + "/" + aluno.getId())
                                .param("perfil", "COORDENADOR")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                .andExpect(status().isOk())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

                Aluno resultado = objectMapper.readValue(responseJsonString, Aluno.class);

                // Assert
                assertEquals("carmem", resultado.getNomeCompleto());
        }

        @Test
        @DisplayName("Quando alteramos vários dados do aluno com informações válidas.")
        void quandoAlteramosDadosDoAlunoValido() throws Exception {
                // arrange
                alunoPostPutRequestDTO.setNomeCompleto("severino");
                alunoPostPutRequestDTO.setEmail("email@ccc");
                alunoPostPutRequestDTO.setPeriodoConclusao("2026.1");
                ;
                alunoPostPutRequestDTO.setMatricula("54545454");

                String responseJsonString = driver.perform(put(URI_ALUNOS + "/" + aluno.getId())
                                .param("perfil", "COORDENADOR")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                .andExpect(status().isOk())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

                Aluno resultado = objectMapper.readValue(responseJsonString, Aluno.class);

                assertAll(
                                () -> assertEquals("severino", resultado.getNomeCompleto()),
                                () -> assertEquals("email@ccc", resultado.getEmail()),
                                () -> assertEquals("2026.1", resultado.getPeriodoConclusao()),
                                () -> assertEquals("54545454", resultado.getMatricula()));
        }

        @Test
        @DisplayName("Quando alteramos o dados do aluno com o perfil inválido") //
        void quandoAlteramosDadosDoAlunoComPerfilInvalido() throws Exception {
                // Arrange

                AlunoPostPutRequestDTO alunoPostPutRequestDTO = AlunoPostPutRequestDTO.builder().nomeCompleto("Carmen")
                                .build();

                // Act
                String responseJsonString = driver.perform(put(URI_ALUNOS + "/" + aluno.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("perfil", "PROFESSOR")
                                .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                .andExpect(status().isBadRequest()) // COdigo 404
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

                TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                // Assert
                assertAll(
                                () -> assertEquals("Erros de validacao encontrados",
                                                resultado.getMessage()));
        }

        @Test
        @DisplayName("Quando alteramos o nome do aluno com nome inválido")
        void quandoAlteramosNomeDoAlunoParaNuloInvalido() throws Exception {
                // Arrange
                alunoPostPutRequestDTO.setNomeCompleto("");

                // Act
                String responseJsonString = driver.perform(put(URI_ALUNOS + "/" + aluno.getId())
                                .param("perfil", "COORDENADOR")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

                TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                // Assert
                assertEquals("Erros de validacao encontrados", resultado.getMessage());
        }

        @Test
        @DisplayName("Quando alteramos o email do aluno com nome inválido")
        void quandoAlteramosEmailDoAlunoParaNuloInvalido() throws Exception {
                // Arrange
                alunoPostPutRequestDTO.setEmail("");

                // Act
                String responseJsonString = driver.perform(put(URI_ALUNOS + "/" + aluno.getId())
                                .param("perfil", "COORDENADOR")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(alunoPostPutRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

                TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                // Assert
                assertEquals("Erros de validacao encontrados", resultado.getMessage());
        }

        @Test
        @Transactional
        @DisplayName("Quando informamos uma área de interesse do aluno")
        void informarAreaDeInteresseAlunoTest() throws  Exception{

                AreaDeEstudo area = areaDeEstudoRepository.save(AreaDeEstudo.builder().nome("Inteligencia Artificial").build());


                String responseJsonString = driver.perform(put(URI_ALUNOS + "/interesse" + "/" + aluno.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("idArea", String.valueOf(area.getId())))
                                .andExpect(status().isOk()) // Codigo 200
                                .andDo(print())
                                .andReturn().getResponse().getContentAsString();

                assertEquals(aluno.getAreasDeInteresse().size(), 1);
        }

        @Test
        @Transactional
        @DisplayName("Quando informamos uma área de interesse inválida do aluno")
        void informarAreaDeInteresseInvalidaAlunoTest() throws  Exception{



                String responseJsonString = driver.perform(put(URI_ALUNOS + "/interesse" + "/" + aluno.getId())
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
        @DisplayName("Quando informamos uma área de interesse inválida do aluno")
        void informarAreaDeInteresseAlunoInvalidoTest() throws  Exception{

                AreaDeEstudo area = areaDeEstudoRepository.save(AreaDeEstudo.builder().nome("Inteligencia Artificial").build());
                UUID idInexistente = UUID.randomUUID();

                String responseJsonString = driver.perform(put(URI_ALUNOS + "/interesse" + "/" + idInexistente)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("idArea", String.valueOf(area.getId())))
                        .andExpect(status().isBadRequest()) //Codigo 400
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);


                assertEquals("O aluno consultado nao existe!", resultado.getMessage());


        }


        @Test
        @Transactional
        @DisplayName("Quando procuramos professores pelas areas de estudo do aluno")
        void quandoBuscarProfessorPelaAreaValido() throws Exception {
                //setando mesma area de interesse para aluno e professor
                AreaDeEstudo area1 = areaDeEstudoRepository.save(AreaDeEstudo.builder().nome("Inteligencia Artificial").build());
                HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
                areasDeInteresse.add(area1);

                aluno.setAreasDeInteresse(areasDeInteresse);
                professor.setAreasDeInteresse(areasDeInteresse);

                assertEquals(aluno.getAreasDeInteresse(), professor.getAreasDeInteresse());

                String responseJsonString = driver.perform(get(URI_ALUNOS + "/professoresDisponiveis/" + aluno.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(professorResponseNomeEmailDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                List<ProfessorResponseNomeEmailDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

                assertEquals(1, resultado.size());
        }

        @Test
        @Transactional
        @DisplayName("Quando procuramos professores pelas areas de estudo do aluno e não há nenhum")
        void quandoBuscarProfessorPelaAreaVazio() throws Exception {
                //setando mesma area de interesse para aluno e professor
                AreaDeEstudo area1 = areaDeEstudoRepository.save(AreaDeEstudo.builder().nome("Inteligencia Artificial").build());
                HashSet<AreaDeEstudo> areasDeInteresse = new HashSet<>();
                areasDeInteresse.add(area1);

                aluno.setAreasDeInteresse(areasDeInteresse);

                assertEquals(0, professor.getAreasDeInteresse().size());
                assertEquals(1, aluno.getAreasDeInteresse().size());

                String responseJsonString = driver.perform(get(URI_ALUNOS + "/professoresDisponiveis/" + aluno.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(professorResponseNomeEmailDTO)))
                        .andExpect(status().isOk()) // Codigo 200
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                List<ProfessorResponseNomeEmailDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

                assertEquals(0, resultado.size());
        }

        @Test
        @Transactional
        @DisplayName("Quando procuramos professores pelas areas de estudo do aluno, porém não há nenhuma área cadastrada")
        void quandoBuscarProfessorPelaAreaInvalido() throws Exception {
                //apenas setup, pois o aluno não terá nenhuma área cadastrada

                String responseJsonString = driver.perform(get(URI_ALUNOS + "/professoresDisponiveis/" + aluno.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(professorResponseNomeEmailDTO)))
                        .andExpect(status().isBadRequest())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

                assertEquals("Nao ha nenhuma area de estudo cadastrada para esse usuario!", resultado.getMessage());
        }
        }
}
