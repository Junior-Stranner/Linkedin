import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class StorageService {

    // Diretório base onde os arquivos serão armazenados
    private final Path rootLocation = Path.of("uploads");

    // Construtor que cria a pasta "uploads" caso ela ainda não exista
    public StorageService() {
        if (!rootLocation.toFile().exists()) {
            rootLocation.toFile().mkdir();
        }
    }

    // Salva a imagem após validar tipo, tamanho e extensão
    public String saveImage(MultipartFile file) throws IOException {
        validateFile(file); // Validação de segurança

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + fileExtension; // Nome único para evitar conflitos
        Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName)); // Salva o arquivo
        return fileName;
    }

    // Retorna um FileInputStream da imagem solicitada (para download ou visualização)
    public FileInputStream getFileInputStream(String filename) throws IOException {
        Path file = rootLocation.resolve(filename).normalize();

        // Proteção contra path traversal (ex: "../../../../etc/passwd")
        if (!file.startsWith(rootLocation)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        if (!file.toFile().exists()) {
            throw new IllegalArgumentException("File not found");
        }

        return new FileInputStream(file.toFile());
    }

    // Exclui um arquivo, se ele existir
    public void deleteFile(String fileName) throws IOException {
        Path file = rootLocation.resolve(fileName).normalize();
        if (!file.startsWith(rootLocation)) {
            throw new IllegalArgumentException("Invalid file path");
        }
        Files.deleteIfExists(file);
    }

    // Retorna o tipo MIME com base na extensão do arquivo
    public MediaType getMediaType(String filename) {
        String extension = getFileExtension(filename);
        return switch (extension.toLowerCase()) {
            case ".png" -> MediaType.IMAGE_PNG;
            case ".jpg", ".jpeg" -> MediaType.IMAGE_JPEG;
            case ".gif" -> MediaType.IMAGE_GIF;
            default -> MediaType.APPLICATION_OCTET_STREAM; // Padrão para arquivos desconhecidos
        };
    }

    // Valida se o arquivo é uma imagem válida e dentro do tamanho permitido
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        if (!isImage(file.getContentType())) {
            throw new IllegalArgumentException("File is not an image");
        }

        if (isFileTooLarge(file)) {
            throw new IllegalArgumentException("File is too large");
        }
    }

    // Retorna a extensão do arquivo, ex: ".png"
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

    // Verifica se o tipo de conteúdo é de uma imagem
    private boolean isImage(String contentType) {
        return contentType != null && contentType.toLowerCase().startsWith("image");
    }

    // Verifica se o arquivo excede o limite de 10MB
    private boolean isFileTooLarge(MultipartFile file) {
        return file.getSize() > 10 * 1024 * 1024; // 10MB
    }
}
