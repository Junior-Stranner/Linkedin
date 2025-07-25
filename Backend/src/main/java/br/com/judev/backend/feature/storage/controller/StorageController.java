package br.com.judev.backend.feature.storage.controller;

import br.com.judev.backend.feature.storage.service.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    private final StorageService storageService;

    // Injeta a instância do serviço de armazenamento
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Endpoint para servir arquivos (ex: imagens) salvos no diretório.
     * A URL será: GET /api/v1/storage/{filename}
     */
    @GetMapping("/{filename}")
    public ResponseEntity<StreamingResponseBody> serveFile(@PathVariable String filename) throws IOException {
        // Descobre o tipo MIME com base na extensão do arquivo
        MediaType mediaType = storageService.getMediaType(filename);

        // Obtém o stream do arquivo diretamente do disco
        FileInputStream resource = storageService.getFileInputStream(filename);

        // Cria um corpo de resposta baseado em stream, para evitar carregar todo o arquivo na memória
        StreamingResponseBody stream = outputStream -> {
            try (resource) {
                int nRead;
                byte[] data = new byte[1024]; // buffer de leitura

                // Lê o arquivo em blocos e escreve diretamente na saída da resposta
                while ((nRead = resource.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, nRead);
                    outputStream.flush(); // garante envio imediato
                }
            }
        };

        // Retorna a resposta HTTP com o conteúdo do arquivo
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"") // permite visualizar diretamente no navegador
                .contentType(mediaType) // ex: image/jpeg, image/png
                .body(stream); // corpo da resposta será o streaming do arquivo
    }
}
