package io.grpc.grpcswagger.openapi.v2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Swagger Documentation Model
 * Contains details for OpenAPI version 2 documentation.
 *
 * @author liuzhengyang
 */
@Data
public class SwaggerV2Documentation {
    private String swagger = "2.0"; // OpenAPI version
    private InfoObject info;
    private List<String> produces = Collections.singletonList("application/json");
    private List<String> consumes = Collections.singletonList("application/json");
    private String basePath = "/";
    private String host = "localhost:8088"; // TODO Swagger host
    private List<String> schemes = Collections.singletonList("http");
    private Map<String, DefinitionType> definitions;
    private Map<String, PathItem> paths;
    private String endPoint;
}
