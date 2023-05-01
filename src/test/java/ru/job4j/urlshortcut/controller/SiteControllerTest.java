package ru.job4j.urlshortcut.controller;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;
import org.springframework.transaction.annotation.*;

import ru.job4j.urlshortcut.Job4jUrlShortcutApplication;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.model.*;
import ru.job4j.urlshortcut.service.*;

import java.util.*;

@SpringBootTest(classes = Job4jUrlShortcutApplication.class)
@AutoConfigureMockMvc
@Transactional
class SiteControllerTest {

    Site site1 = new Site(1, "mail.ru", "andrew1@mail.ru", "secret");
    ShortCut shortCut1 = new ShortCut(1, "https://job4j.ru/profile/exercise/106/task-view/532", "7782ba4", 0);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private SpringSiteService siteService;

    @MockBean SpringShortCutService shortCutService;

    @Test
    @WithMockUser
    public void whenFindAllThenReturnAll() throws Exception {
        List<Site> records = new ArrayList<>(Arrays.asList(site1));
        Mockito.when(siteService.findAll()).thenReturn(records);

        this.mockMvc.perform(get("/site/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].urlAddress", is(site1.getUrlAddress())));
    }

    @Test
    @WithMockUser
    public void whenRegistrationThenReturnAuthorization() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO(
                site1.getUrlAddress(), true, site1.getLogin(), site1.getPassword());

        Mockito.when(siteService.registration(registrationDTO)).thenReturn(registrationDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/site/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(registrationDTO));

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.urlAddress", is(registrationDTO.getUrlAddress())));
    }

    @Test
    @WithMockUser
    public void whenConvertThenReturnCode() throws Exception {
        ConvertDTO convertDTO = new ConvertDTO(shortCut1.getUrlLink(), shortCut1.getLinkCode());

        Mockito.when(shortCutService.convert(convertDTO)).thenReturn(convertDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/site/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(convertDTO));

        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.code", is(convertDTO.getCode())));
    }

    @Test
    @WithMockUser
    public void whenStatisticThenReturnOk() throws Exception {
        StatisticDTO record = new StatisticDTO(shortCut1.getUrlLink(), shortCut1.getCallCounter());
        Collection<StatisticDTO> records = new ArrayList<>(Arrays.asList(record));
        Mockito.when(shortCutService.getStatistic()).thenReturn(records);

        this.mockMvc.perform(get("/site/statistic"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].url", is(shortCut1.getUrlLink())));
    }

    @Test
    @WithMockUser
    public void whenRedirectThenReturnURL() throws Exception {
        String code = shortCut1.getLinkCode();
        Mockito.when(shortCutService.redirect(code)).thenReturn(Optional.of(shortCut1));

        this.mockMvc.perform(get(String.format("/site/redirect/%s", code)))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

}