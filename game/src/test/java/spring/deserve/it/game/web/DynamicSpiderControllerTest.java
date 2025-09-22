package spring.deserve.it.game.web;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import spring.deserve.it.game.*;
import spring.deserve.it.game.dynamic.SpiderClassLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@Import({
        SpiderClassLoader.class,
        GameMaster.class,
        SpiderConfig.class,
        PaperSpider.class
})
@MockitoBean(types = {HistoricalService.class})
public class DynamicSpiderControllerTest {
    @Autowired ApplicationContext          applicationContext;
    @Autowired MockMvc                     mockMvc;
    @Autowired Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    /**
     * Test for input string Base64 encoded from:
     *
     * <pre>
     * {@code
     * package strategy0;
     * import com.sbr.api.SpiderStrategy;
     * import com.sbr.api.Thing;
     *
     * public class BlazinglyFastSpider implements SpiderStrategy {
     *     public BlazinglyFastSpider() {
     *         System.out.println("I'm fast and alive");
     *     }
     *
     *     @Override
     *     public Thing gamble() {
     *         return Thing.PAPER;
     *     }
     * }
     * }</pre>
     */
    @Test
    @SneakyThrows
    void should_load_new_spider_strategy_scneario() {
        //class from comment
        String encodedByteCode = "yv66vgAAAEIAKAoAAgADBwAEDAAFAAYBABBqYXZhL2xhbmcvT2JqZWN0AQAGPGluaXQ+AQADKClWCQAIAAkHAAoMAAsADAEAEGphdmEvbGFuZy9TeXN0ZW0BAANvdXQBABVMamF2YS9pby9QcmludFN0cmVhbTsIAA4BABJJJ20gZmFzdCBhbmQgYWxpdmUKABAAEQcAEgwAEwAUAQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYJABYAFwcAGAwAGQAaAQARY29tL3Nici9hcGkvVGhpbmcBAAVQQVBFUgEAE0xjb20vc2JyL2FwaS9UaGluZzsHABwBAB1zdHJhdGVneTAvQmxhemluZ2x5RmFzdFNwaWRlcgcAHgEAGmNvbS9zYnIvYXBpL1NwaWRlclN0cmF0ZWd5AQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBAB9Mc3RyYXRlZ3kwL0JsYXppbmdseUZhc3RTcGlkZXI7AQAGZ2FtYmxlAQAVKClMY29tL3Nici9hcGkvVGhpbmc7AQAKU291cmNlRmlsZQEAGEJsYXppbmdseUZhc3RTcGlkZXIuamF2YQAhABsAAgABAB0AAAACAAEABQAGAAEAHwAAAD8AAgABAAAADSq3AAGyAAcSDbYAD7EAAAACACAAAAAOAAMAAAAHAAQACAAMAAkAIQAAAAwAAQAAAA0AIgAjAAAAAQAkACUAAQAfAAAALgABAAEAAAAEsgAVsAAAAAIAIAAAAAYAAQAAAA0AIQAAAAwAAQAAAAQAIgAjAAAAAQAmAAAAAgAn";

        //when
        MvcResult mvcResult = mockMvc.perform(post("/api/spiders")
                                                      .contentType("application/json")
                                                      .content(
                                                              //language=json
                                                              String.format(
                                                                      """
                                                                              {
                                                                               "byteCode": "%s"
                                                                              }
                                                                              """,
                                                                      encodedByteCode
                                                              )
                                                      ))
                                     .andExpect(status().isOk())
                                     .andExpect(jsonPath("$.beanName").exists())
                                     .andReturn();
        mockMvc.perform(post("/api/spiders")
                                .contentType("application/json")
                                .content(
                                        //language=json
                                        String.format(
                                                """
                                                        {
                                                         "byteCode": "%s"
                                                        }
                                                        """,
                                                encodedByteCode
                                        )
                                ))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.beanName").exists())
               .andReturn();

        var spiderResponseBody = jackson2ObjectMapperBuilder.build()
                                                            .readValue(
                                                                    mvcResult.getResponse()
                                                                             .getContentAsString(),
                                                                    DynamicSpiderController.SpiderResponseBody.class
                                                            );
        //then
        Object bean = applicationContext.getBean(spiderResponseBody.getBeanName());

        assertAll(
                () -> assertThat(bean).isInstanceOf(AbstractSpider.class),
                () -> assertThat(bean.getClass().getCanonicalName()).startsWith("org.dynamic.")
        );

        GameMaster gameMaster = applicationContext.getBean(GameMaster.class);
        assertThat(gameMaster.getSpiders().stream()
                                     .anyMatch(spider -> spider.getClass().getCanonicalName().contains("org.dynamic")))
                .as("Should contain new bean as one of spiders at GameMaster")
                .isTrue();
    }

    @Test
    @SneakyThrows
    void should_return_error_when_bytecode_is_invalid_or_class_cant_create() {
        String encodedByteCode = "ys66sdfsdfsdvgAAAEIAKAoAAgADBwAEDAAFAAYBABBqYXZhL2xhbmcvT2JqZWN0AQAGPGluaXQ+AQADKClWCQAIAAkHAAoMAAsADAEAEGphdmEvbGFuZy9TeXN0ZW0BAANvdXQBABVMamF2YS9pby9QcmludFN0cmVhbTsIAA4BABJJJ20gZmFzdCBhbmQgYWxpdmUKABAAEQcAEgwAEwAUAQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYJABYAFwcAGAwAGQAaAQARY29tL3Nici9hcGkvVGhpbmcBAAVQQVBFUgEAE0xjb20vc2JyL2FwaS9UaGluZzsHABwBAB1zdHJhdGVneTAvQmxhemluZ2x5RmFzdFNwaWRlcgcAHgEAGmNvbS9zYnIvYXBpL1NwaWRlclN0cmF0ZWd5AQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBAB9Mc3RyYXRlZ3kwL0JsYXppbmdseUZhc3RTcGlkZXI7AQAGZ2FtYmxlAQAVKClMY29tL3Nici9hcGkvVGhpbmc7AQAKU291cmNlRmlsZQEAGEJsYXppbmdseUZhc3RTcGlkZXIuamF2YQAhABsAAgABAB0AAAACAAEABQAGAAEAHwAAAD8AAgABAAAADSq3AAGyAAcSDbYAD7EAAAACACAAAAAOAAMAAAAHAAQACAAMAAkAIQAAAAwAAQAAAA0AIgAjAAAAAQAkACUAAQAfAAAALgABAAEAAAAEsgAVsAAAAAIAIAAAAAYAAQAAAA0AIQAAAAwAAQAAAAQAIgAjAAAAAQAmAAAAAgAn";

        //when
        mockMvc.perform(post("/api/spiders")
                                .contentType("application/json")
                                .content(
                                        //language=json
                                        String.format(
                                                """
                                                        {
                                                         "byteCode": "%s"
                                                        }
                                                        """,
                                                encodedByteCode
                                        )
                                ))
               .andExpect(status().is4xxClientError());

    }
}
