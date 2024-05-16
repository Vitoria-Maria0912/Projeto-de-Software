package com.ufcg.psoft.tccManager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ufcg.psoft.tccManager.dto.area.AreaDeEstudoResponseDTO;
import org.junit.jupiter.api.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.tccManager.dto.area.AreaDeEstudoPostPutRequestDTO;
import com.ufcg.psoft.tccManager.exception.TccErrorType;
import com.ufcg.psoft.tccManager.model.AreaDeEstudo;
import com.ufcg.psoft.tccManager.repository.AreaDeEstudoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Área de Estudos")
public class AreaDeEstudoControllerTests {
    
    final String URI_AREAS = "/areasEstudo";

    @Autowired
    MockMvc driver;

    @Autowired
    AreaDeEstudoRepository areaDeEstudoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    AreaDeEstudo areaDeEstudo;

    AreaDeEstudoPostPutRequestDTO areaDeEstudoPostPutRequestDTO;

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        areaDeEstudo = areaDeEstudoRepository.save(AreaDeEstudo.builder()
                .nome("Informatica na Educacao")
                .build()
        );
        areaDeEstudoPostPutRequestDTO = AreaDeEstudoPostPutRequestDTO.builder()
                .nome(areaDeEstudo.getNome())
                .build();
    }

    @AfterEach
    void tearDown() {
        areaDeEstudoRepository.deleteAll();
    }


    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos de criação")
    class AreaDeEstudoCriacao {
        @Test
        @DisplayName("Quando criamos uma nova area com dados válidos")
        void quandoCriarAreaValida() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_AREAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            AreaDeEstudo resultado = objectMapper.readValue(responseJsonString, AreaDeEstudo.AreaDeEstudoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(areaDeEstudoPostPutRequestDTO.getNome(), resultado.getNome())
            );

        }

        @Test
        @DisplayName("Quando criamos uma nova area com string vazia")
        void quandoCriarAreaNomeVazio() throws Exception {
            // Arrange
            objectMapper.registerModule(new JavaTimeModule());
            areaDeEstudoPostPutRequestDTO = AreaDeEstudoPostPutRequestDTO.builder()
                    .nome("")
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_AREAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();


            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome obrigatorio", resultado.getErrors().get(0))
            );

        }

        @Test
        @DisplayName("Quando criamos uma nova area com nome nulo")
        void quandoCriarAreaNomeNulo() throws Exception {
            // Arrange
            objectMapper.registerModule(new JavaTimeModule());
            areaDeEstudoPostPutRequestDTO = AreaDeEstudoPostPutRequestDTO.builder()
                    .nome(null)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_AREAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();


            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome obrigatorio", resultado.getErrors().get(0))
            );

        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos de alterações")
    class AreaDeEstudoAlteracoes {
        // Testes referentes a Atualização das Áreas.
        @Test
        @DisplayName("Quando alteramos a area com dados válidos")
        void quandoAlteramosAreaValido() throws Exception {
            // Arrange
            Long areaId = areaDeEstudo.getId();
            // Act
            String responseJsonString = driver.perform(put(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            AreaDeEstudo resultado = objectMapper.readValue(responseJsonString, AreaDeEstudo.AreaDeEstudoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), areaId),
                    () -> assertEquals(areaDeEstudoPostPutRequestDTO.getNome(), resultado.getNome())
            );
        }

        @Test
        @DisplayName("Quando alteramos a area com dados válidos, mas perfil errado")
        void quandoAlteramosAreaValidoPerfilErrado() throws Exception {
            // Arrange
            Long areaId = areaDeEstudo.getId();
            // Act
            String responseJsonString = driver.perform(put(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos uma area inexistente")
        void quandoAlteramosAreaInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(put(URI_AREAS + "/" + 99999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("A area de estudo consultada nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando alteramos o nome da area para um nulo")
        void quandoAlteramosNomeDaAreaNulo() throws Exception {
            // Arrange
            areaDeEstudoPostPutRequestDTO.setNome(null);

            // Act
            String responseJsonString = driver.perform(put(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome obrigatorio", resultado.getErrors().get(0))
            );
        }

        @Test
        @DisplayName("Quando alteramos o nome da area para um vazio")
        void quandoAlteramosNomeDaAreaVazio() throws Exception {
            // Arrange
            areaDeEstudoPostPutRequestDTO.setNome("");

            // Act
            String responseJsonString = driver.perform(put(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Erros de validacao encontrados", resultado.getMessage()),
                    () -> assertEquals("Nome obrigatorio", resultado.getErrors().get(0))
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos de busca")
    class AreaDeEstudoBuscas {
        @Test
        @DisplayName("Quando buscamos o numero de todas as áreas cadastradas.")
        void quandoBuscamosPorTodasAreasCadastradas() throws Exception {
            // Arrange
            // Cria-se mais três áreas para ter no banco.
            AreaDeEstudo area1 = AreaDeEstudo.builder()
                    .nome("Sistemas Operacionais")
                    .build();
            AreaDeEstudo area2 = AreaDeEstudo.builder()
                    .nome("Inteligência Artificial")
                    .build();
            AreaDeEstudo area3 = AreaDeEstudo.builder()
                    .nome("Circuitos Integrados")
                    .build();
            areaDeEstudoRepository.saveAll(Arrays.asList(area1, area2, area3));

            // Act
            String responseJsonString = driver.perform(get(URI_AREAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<AreaDeEstudo> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(4, resultado.size())
            );
        }

        @Test
        @DisplayName("Quando buscamos o numero de todas as áreas cadastradas com perfil errado.")
        void quandoBuscamosPorTodasAreasCadastradasPerfilErrado() throws Exception {
            // Arrange
            // Cria-se mais três áreas para ter no banco.
            AreaDeEstudo area1 = AreaDeEstudo.builder()
                    .nome("Sistemas Operacionais")
                    .build();
            AreaDeEstudo area2 = AreaDeEstudo.builder()
                    .nome("Inteligência Artificial")
                    .build();
            AreaDeEstudo area3 = AreaDeEstudo.builder()
                    .nome("Circuitos Integrados")
                    .build();
            areaDeEstudoRepository.saveAll(Arrays.asList(area1, area2, area3));

            // Act
            String responseJsonString = driver.perform(get(URI_AREAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos uma area salva pelo id")
        void quandoBuscamosPorUmaAreaSalva() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            AreaDeEstudoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(areaDeEstudo.getId().longValue(), resultado.getId().longValue()),
                    () -> assertEquals(areaDeEstudo.getNome(), resultado.getNome())
            );
        }

        @Test
        @DisplayName("Quando buscamos uma area salva pelo id com perfil errado")
        void quandoBuscamosPorUmaAreaSalvaPeloIdPerfilErrado() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos uma area inexistente")
        void quandoBuscamosPorUmaAreaInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_AREAS + "/" + 999999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("A area de estudo consultada nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando buscamos uma area inexistente com perfil errado")
        void quandoBuscamosPorUmaAreaInexistentePerfilErrado() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_AREAS + "/" + 999999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR")
                            .content(objectMapper.writeValueAsString(areaDeEstudoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos de remoção")
    class AreaDeEstudoRemocao {
        @Test
        @DisplayName("Quando excluímos uma área de estudo salva")
        void quandoExcluimosAreaDeEstudoValida() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR"))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());
        }


        @Test
        @DisplayName("Quando excluímos uma área inexistente")
        void quandoExcluimosAreaDeEstudoInexistente() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_AREAS + "/" + 999999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "COORDENADOR"))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("A area de estudo consultada nao existe!", resultado.getMessage())
            );
        }

        @Test
        @DisplayName("Quando excluímos uma área salva passando perfil de aluno")
        void quandoExcluimosAreaDeEstudoComPerfilAluno() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "ALUNO"))
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
        @DisplayName("Quando excluímos uma área salva passando perfil de professor")
        void quandoExcluimosAreaDeEstudoComPerfilProfessor() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_AREAS + "/" + areaDeEstudo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("perfil", "PROFESSOR"))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            TccErrorType resultado = objectMapper.readValue(responseJsonString, TccErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("Usuario nao autorizado realizar esta acao!", resultado.getMessage())
            );
        }
    }
}