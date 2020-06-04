package com.kalah.controller;

import com.kalah.domain.GameDTO;
import com.kalah.domain.NewGameDTO;
import com.kalah.service.KalahPlayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 */

@RunWith(SpringRunner.class)
@WebMvcTest
public class KalahGameControllerTest {

    private static final String URL = "http://localhost:8080/games/1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KalahPlayService gameService;

    @Test
    public void testCreateNewGame() throws Exception {
        NewGameDTO newGame = new NewGameDTO(1, URL);
        given(gameService.createNewGame()).willReturn(newGame);

        mockMvc.perform(post("/games"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.uri").value(newGame.getUri()));
    }

    @Test
    public void testChoosePit() throws Exception {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(1);
        gameDTO.setUrl(URL);
        given(gameService.choosePit(1, 3)).willReturn(gameDTO);

        mockMvc.perform(put("/games/1/pits/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.url").value(gameDTO.getUrl()));
    }
}
