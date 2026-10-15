package com.tienda;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class StorageConfig {

    // Ruta de la carpeta de Firebase
    @Value("${firebase.json.path}")
    private String jsonPath;

    // Nombre del archivo JSON
    @Value("${firebase.json.file}")
    private String jsonFile;

    @Bean
    public Storage storage() throws IOException {

        // Credenciales para conectar con Firebase
        GoogleCredentials credentials;

        // Buscamos si existe una ruta de Firebase en las variables de entorno
        String credentialsPath =
                System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        // Si existe usamos el archivo secreto de Render
        if (credentialsPath != null && !credentialsPath.isBlank()) {

            // Abrimos el archivo secreto de Firebase
            try (InputStream input = Files.newInputStream(
                    Path.of(credentialsPath))) {

                // Cargamos las credenciales de Render
                credentials = GoogleCredentials.fromStream(input);
            }

        } else {

            // Si no existe la variable usamos el JSON local de NetBeans

            // Obtenemos la ruta del archivo desde application.properties
            String filePath = jsonPath + "/" + jsonFile;

            // Buscamos el JSON dentro de resources
            ClassPathResource resource =
                    new ClassPathResource(filePath);

            // Abrimos el archivo local
            try (InputStream input = resource.getInputStream()) {

                // Cargamos las credenciales locales
                credentials = GoogleCredentials.fromStream(input);
            }
        }

        // Creamos la conexion con Firebase Storage
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}