package com.brnx.coreservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class TemplateProcessor {

    public String processTemplate(String template, JsonNode data) {
        try {
            if (template == null || template.isEmpty()) {
                return "0";
            }
            if (data == null || !data.isObject()) {
                return "0";
            }

            String[] tags = getTagsFromTemplate(template);

            if (compareTags(tags, data)) {
                return populateTemplate(template, data);
            } else {
                return "0";
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid input: " + e.getMessage());
            return "Invalid input: " + e.getMessage();
        } catch (Exception e) {
            log.error("An error occurred while processing the template: " + e.getMessage());
            return "An error occurred while processing the template: " + e.getMessage();
        }
    }

    private String[] getTagsFromTemplate(String template) {
        Pattern pattern = Pattern.compile("\\$\\w+");
        Matcher matcher = pattern.matcher(template);

        List<String> tags = new ArrayList<>();
        while (matcher.find()) {
            tags.add(matcher.group());
        }
        return tags.toArray(new String[0]);
    }

    private boolean compareTags(String[] tags, JsonNode data) {
        // Преобразуем Iterator в Stream
        Set<String> dataKeys = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(data.fieldNames(), Spliterator.ORDERED), false
        ).collect(Collectors.toSet());

        // Проверяем, что все tags содержатся в dataKeys
        return Arrays.stream(tags)
                .allMatch(dataKeys::contains);
    }




    private String populateTemplate(String template, JsonNode data) {
        StringBuilder result = new StringBuilder(template);

        data.fields().forEachRemaining(entry -> {
            String key = entry.getKey(); // Формируем ключ вида "$key"
            String value = entry.getValue().asText(); // Получаем значение из JSON

            // Заменяем все вхождения ключа на значение
            int startIndex = 0;
            while ((startIndex = result.indexOf(key, startIndex)) != -1) {
                result.replace(startIndex, startIndex + key.length(), value);
                startIndex += value.length(); // Продвигаемся дальше по строке
            }
        });

        return result.toString();
    }
}
