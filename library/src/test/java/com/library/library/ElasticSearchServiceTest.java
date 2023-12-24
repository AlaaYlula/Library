package com.library.library;

import com.library.library.ElasticSearchQuery.ElasticSearchQuery;
import com.library.library.Entity.Logs;
import com.library.library.Entity.levelEnum.Level;
import com.library.library.Service.ElasticSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ElasticSearchServiceTest {

    @InjectMocks
    ElasticSearchService elasticSearchService;

    @Mock
    ElasticSearchQuery elasticSearchQuery;

    @Mock
    private RequestAttributes requestAttributes;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        RequestContextHolder.setRequestAttributes(requestAttributes);
        this.mockMvc = MockMvcBuilders.standaloneSetup(elasticSearchService).build();
    }

    @Test
    public void testSearchByLevel() throws NoSuchFieldException {
        String level = "warn";
        List<Logs> logs = new ArrayList<>();
        Logs logs1 = new Logs("warn logs", Level.WARN,new Date());
        logs.add(logs1);
        when(elasticSearchQuery.getDocumentLogByLevel(level)).thenReturn(logs);

        List<Logs> returnLogs = elasticSearchService.searchByLevel(level);
        assertEquals(logs,returnLogs);
        assertEquals(logs.get(0).getMessage(),returnLogs.get(0).getMessage());
    }



}
