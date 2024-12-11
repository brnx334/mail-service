package com.brnx.coreservice.mapper;

import com.brnx.coreservice.model.Template;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TemplateMapper {
    @Insert("INSERT INTO templates (template_name, template_text) VALUES (#{templateName}, #{templateText})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertTemplate(Template template);

    @Select("SELECT id, template_name, template_text FROM templates WHERE id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "template_name", property = "templateName"),
            @Result(column = "template_text", property = "templateText")
    })
    Template selectTemplateById(@Param("id") Long id);

    @Delete("DELETE FROM templates WHERE id = #{id}")
    void deleteTemplateById(Long id);

    @Select("SELECT EXISTS(SELECT 1 FROM templates WHERE id = #{id})")
    boolean isTemplateExistsById(Long id);
}
