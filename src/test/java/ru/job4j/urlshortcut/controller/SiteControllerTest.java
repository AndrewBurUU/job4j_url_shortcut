package ru.job4j.urlshortcut.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.verify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.*;

import ru.job4j.urlshortcut.Job4jUrlShortcutApplication;
import ru.job4j.urlshortcut.service.*;

@SpringBootTest(classes = Job4jUrlShortcutApplication.class)
@AutoConfigureMockMvc
@Transactional
class SiteControllerTest {

    @MockBean
    private SpringSiteService siteService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void whenFindAllThenReturnAll() throws Exception {
        this.mockMvc.perform(get("/site/"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}