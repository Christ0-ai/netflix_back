package com.netflix.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String ACCEPT_LANGUAGE_PARAM_KEY = "AcceptLanguageHeader";
    private static final String ACCEPT_LANGUAGE_REF = "#/components/parameters/" + ACCEPT_LANGUAGE_PARAM_KEY;
    private static final String SCHEME_NAME = "basicAuth";
    @Bean
    public OpenAPI customOpenAPI() {
        Parameter acceptLanguage = new Parameter()
                .in("header")
                .name("Accept-Language")
                .required(false)
                .description("Locale de réponse (ex: fr-FR, en-US, es-ES)")
                .schema(new StringSchema()
                        ._default("en-US")
                        ._enum(java.util.List.of("fr-FR", "br-FR", "en-US", "es-ES", "de-DE", "it-IT", "ja-JP", "zh-CN", "ru-RU"))
                );

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                        )
                        .addParameters(ACCEPT_LANGUAGE_PARAM_KEY, acceptLanguage))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .info(new Info()
                        .title("Movies Review API")
                        .version("1.0.0")
                        .description("API REST for movies review platform")
                        .contact(new Contact()
                                .name("Netflix V2")
                                .email("contact@netflixv2.com")
                                .url("https://www.netflixv2.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }

    @Bean
    public OperationCustomizer addAcceptLanguageHeaderToAllOperations() {
        return (operation, handlerMethod) -> {
            // évite les doublons si jamais un endpoint le déclare déjà
            boolean alreadyPresent = operation.getParameters() != null
                    && operation.getParameters().stream()
                    .anyMatch(p -> "Accept-Language".equalsIgnoreCase(p.getName()));

            if (!alreadyPresent) {
                operation.addParametersItem(new Parameter().$ref(ACCEPT_LANGUAGE_REF));
            }
            return operation;
        };
    }

}
