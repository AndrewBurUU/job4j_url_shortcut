package ru.job4j.urlshortcut.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.verify;

import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.*;

import ru.job4j.urlshortcut.Job4jUrlShortcutApplication;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.model.*;
import ru.job4j.urlshortcut.repository.*;
import ru.job4j.urlshortcut.service.*;

@SpringBootTest(classes = Job4jUrlShortcutApplication.class)
@AutoConfigureMockMvc
@Transactional
class SiteControllerTest {

    @MockBean
    private SiteRepository siteRepository;

    @MockBean
    private SpringSiteService siteService;

    @MockBean
    private SpringShortCutService shortCutService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void whenFindAllThenReturnAll() throws Exception {
        this.mockMvc.perform(get("/site/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    @WithMockUser
    public void whenRegistrationThenReturnAuthorization() throws Exception {
        this.mockMvc.perform(post("/site/registration")
                .param("urlAddress", "job4j.ru"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
        ArgumentCaptor<RegistrationDTO> argument = ArgumentCaptor.forClass(RegistrationDTO.class);
        verify(siteService).registration(argument.capture());
        assertThat(argument.getValue().getUrlAddress()).isEqualTo("job4j.ru");
    }

    @Disabled
    @Test
    @WithMockUser
    public void whenConvertThenReturnCode() throws Exception {
        this.mockMvc.perform(post("/site/convert")
                        .param("url", "https://job4j.ru/profile/exercise/106/task-view/532"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
        ArgumentCaptor<ConvertDTO> argument = ArgumentCaptor.forClass(ConvertDTO.class);
        verify(shortCutService).convert(argument.capture());
        assertThat(argument.getValue().getUrl()).isEqualTo("https://job4j.ru/profile/exercise/106/task-view/532");
    }

    @Test
    @WithMockUser
    public void whenStatisticThenReturnOk() throws Exception {
        this.mockMvc.perform(get("/site/statistic"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenRedirectThenReturnOk() throws Exception {
        String code = "7782ba4";
        this.mockMvc.perform(get(String.format("/site/redirect/%s", code)))
                .andDo(print())
                .andExpect(status().isOk());
    }

}