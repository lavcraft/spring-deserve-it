package spring.deserve.it.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.deserve.it.api.PlayerQualifier;
import spring.deserve.it.api.Spider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SpiderConfig {

    @Autowired
    private List<Spider> allSpiders;


    @Bean
    public List<Spider> spiders() {
        return allSpiders.stream()
                      .filter(spider -> spider.getClass().isAnnotationPresent(PlayerQualifier.class))
                      .collect(Collectors.toList());
    }


    @Bean
    public Map<String,Integer> playersMap(){
        return spiders().stream()
                      .collect(Collectors.toMap(
                              spider -> {
                                  PlayerQualifier qualifier = spider.getClass().getAnnotation(PlayerQualifier.class);
                                  return qualifier.playerName();
                              },
                              spider -> 0,
                              (existing, replacement) -> existing // на случай дубликатов ключей, оставляем первый
                      ));
    }
}
