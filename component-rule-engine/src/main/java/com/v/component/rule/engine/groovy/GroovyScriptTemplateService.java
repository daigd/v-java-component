package com.v.component.rule.engine.groovy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : DaiGD
 * @createtime :  2020年08月24日 16:27
 * @description : groovy 脚本模板服务方法
 */
@Service
@Slf4j
public class GroovyScriptTemplateService implements InitializingBean
{
    private static final Map<String, String> SCRIPT_TEMPLATE_MAP = new ConcurrentHashMap<>();

    public String getScript(String fileName)
    {
        String template = SCRIPT_TEMPLATE_MAP.get(fileName);
        if(StringUtils.isEmpty(template))
        {
            throw new RuntimeException(String.format("请添加脚本模板: %s 到resources目录下", fileName));
        }
        return template;
    }

    private void scriptTemplate() throws IOException
    {

        final String path = "classpath*:*.groovy_template";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Arrays.stream(resolver.getResources(path)).forEach(resource -> {
            try
            {
                String fileName = resource.getFilename();
                InputStream input = resource.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(reader);
                StringBuilder template = new StringBuilder();

                for(String line; (line = br.readLine()) != null; )
                {
                    template.append(line).append("\n");
                }

                SCRIPT_TEMPLATE_MAP.put(fileName, template.toString());
                log.info("load script template :{}", resource.getURL());
            }
            catch (Exception e)
            {
                log.error("read rule script file failed", e);
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.scriptTemplate();
    }
}
