package org.homonoia.echo.bot.plugins.core;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.interpret.RenderResult;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.Post;
import org.homonoia.echo.bot.annotations.RespondTo;
import org.homonoia.echo.bot.event.MattermostEvent;
import org.homonoia.echo.documentation.DocumentationProcessor;
import org.homonoia.echo.documentation.annotations.EchoDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * Copyright (c) 2015-2017 Homonoia Studios.
 *
 * @author alexparlett
 * @since 17/03/2017
 */
@Component
@ConditionalOnProperty(prefix = "plugins.core", name = "friendly", havingValue = "true", matchIfMissing = true)
public class HelpPlugin {

    @Autowired
    private MattermostClient mattermostClient;

    @Autowired
    private Jinjava jinjava;

    @Autowired
    private DocumentationProcessor documentationProcessor;

    @Value("#{T(org.apache.commons.io.FileUtils).readFileToString(T(org.springframework.util.ResourceUtils).getFile('classpath:templates/help.template.md'), T(java.nio.charset.StandardCharsets).UTF_8)}")
    private String helpTemplate;

    @RespondTo(regex = "#root contains '\\b(Help)'")
    public void handleDirectHelp(MattermostEvent event) {
        Map<String, MultiValueMap<String, EchoDoc>> context = singletonMap("docs", documentationProcessor.getEchoDocumentation());
        RenderResult render = jinjava.renderForResult(helpTemplate, context);

        Post post = new Post();
        post.setChannelId(event.getPayload().getChannelId());
        post.setMessage(render.getOutput());

        mattermostClient.createPost(post);
    }
}
