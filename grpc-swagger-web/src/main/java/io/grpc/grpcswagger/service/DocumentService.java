package io.grpc.grpcswagger.service;

import io.grpc.grpcswagger.openapi.v2.DefinitionType;
import io.grpc.grpcswagger.openapi.v2.PathItem;

import org.springframework.stereotype.Service;

import io.grpc.grpcswagger.openapi.v2.DocumentRegistry;
import io.grpc.grpcswagger.openapi.v2.SwaggerV2Documentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Swagger doc API service
 * Updates documentation based on API host and endPoint.
 *
 * @author liuzheng
 */
@Service
public class DocumentService {

    public SwaggerV2Documentation getDocumentation(String service, String apiHost, String endPoint) {
        SwaggerV2Documentation swaggerV2Documentation = DocumentRegistry.getInstance().get(service);
        if (swaggerV2Documentation != null) {
            swaggerV2Documentation.setHost(apiHost);
            swaggerV2Documentation.setEndPoint(endPoint);

            sortPaths(swaggerV2Documentation);
        }
        return swaggerV2Documentation;
    }


    /**
     * @param endPoint address of gRPC service
     * @return docs list, based on endPoint
     */
    public List<SwaggerV2Documentation> getDocsByEndPoint(String endPoint) {
        return DocumentRegistry.getInstance().getAll().stream()
                .filter(doc -> endPoint.equals(doc.getEndPoint()))
                .collect(Collectors.toList());
    }

    /**
     * Split SwaggerV2Documentation in one document.
     * @param documents merger list
     * @return merged doc
     */
    public SwaggerV2Documentation combineDocuments(List<SwaggerV2Documentation> documents) {
        if (documents == null || documents.isEmpty()) {
            return null;
        }

        SwaggerV2Documentation combined = documents.get(0);

        Map<String, DefinitionType> combinedDefinitions = new HashMap<>();
        documents.forEach(doc -> {
            if (doc.getDefinitions() != null) {
                combinedDefinitions.putAll(doc.getDefinitions());
            }
        });
        combined.setDefinitions(combinedDefinitions);

        Map<String, PathItem> combinedPaths = new HashMap<>();
        documents.forEach(doc -> {
            if (doc.getPaths() != null) {
                combinedPaths.putAll(doc.getPaths());
            }
        });
        combined.setPaths(combinedPaths);

        sortPaths(combined);
        addTags(combined);

        return combined;
    }

    public void addTags(SwaggerV2Documentation documentation) {
        if (documentation.getPaths() != null) {
            documentation.getPaths().forEach((path, pathItem) -> {
                String[] pathParts = path.split("\\.");
                String serviceName = (pathParts.length > 2) ? pathParts[2].toLowerCase() : "default";
                if (pathItem.getPost() != null) {
                    pathItem.getPost().addTag(serviceName);
                }
            });
        }
    }



    /**
     * Asc sort
     * @param documentation Swagger doc
     */
    public void sortPaths(SwaggerV2Documentation documentation) {
        if (documentation.getPaths() != null) {
            Map<String, PathItem> sortedPaths = new TreeMap<>(documentation.getPaths());
            documentation.setPaths(sortedPaths);
        }
    }
}
