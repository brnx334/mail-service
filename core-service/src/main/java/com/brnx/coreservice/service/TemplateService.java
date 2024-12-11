package com.brnx.coreservice.service;

import com.brnx.coreservice.mapper.TemplateMapper;
import com.brnx.coreservice.model.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateMapper templateMapper;

    public Template createTemplate(Template template) {
        try {
            templateMapper.insertTemplate(template);
            return template;
        } catch (Exception e) {
            throw new RuntimeException("Error when adding a template: " + e.getMessage(), e);
        }
    }

    public Template getTemplateById(Long id) {
        Template template = templateMapper.selectTemplateById(id);
        if (template.getTemplateText() == null) {
            throw new IllegalArgumentException("Template with ID " + id + " not found");
        }
        return template;
    }

    public void deleteTemplateById(Long id) {
        if (!templateMapper.isTemplateExistsById(id)) {
            throw new IllegalArgumentException("Unable to delete a template. Template with ID " + id + " doesn't exists");
        }
        try {
            templateMapper.deleteTemplateById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error when deleting template with id " + id + ": " + e.getMessage(), e);
        }
    }

    public boolean isTemplateExists(Long id) {
        try {
            return templateMapper.isTemplateExistsById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error when checking is template with id {" + id + "} - exists:" +
                    e.getMessage(), e);
        }

    }
}
