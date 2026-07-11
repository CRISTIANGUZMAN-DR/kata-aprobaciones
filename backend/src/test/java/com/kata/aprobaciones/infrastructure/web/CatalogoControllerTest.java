package com.kata.aprobaciones.infrastructure.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.kata.aprobaciones.infrastructure.web.controller.CatalogoController;

@WebMvcTest(CatalogoController.class)
class CatalogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_retornar200ConCuatroTipos_when_seConsultaCatalogo() throws Exception {
        mockMvc.perform(get("/api/catalogo/tipos").header("X-Usuario", "jperez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0]").value("DESPLIEGUE"));
    }

    @Test
    void should_retornar400_when_faltaHeaderXUsuario() throws Exception {
        mockMvc.perform(get("/api/catalogo/tipos"))
                .andExpect(status().isBadRequest());
    }
}
