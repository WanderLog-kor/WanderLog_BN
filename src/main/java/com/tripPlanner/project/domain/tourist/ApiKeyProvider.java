package com.tripPlanner.project.domain.tourist;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "api.service")
public class ApiKeyProvider {
    private List<String> keys;  // api.service.keys에 대응되는 값이 리스트로 주입됩니다.

    private final Random random = new Random();

    public String getRandomApiKey() {
        return keys.get(random.nextInt(keys.size())); // 랜덤으로 API 키 선택
    }
}