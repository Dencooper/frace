package vn.dencooper.fracejob.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import vn.dencooper.fracejob.domain.dto.response.file.UploadFileResponse;
import vn.dencooper.fracejob.exception.AppException;
import vn.dencooper.fracejob.exception.ErrorCode;
import vn.dencooper.fracejob.service.FileService;
import vn.dencooper.fracejob.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    @Value("${frace.upload-file.base-uri}")
    @NonFinal
    String baseURI;

    FileService fileService;

    @PostMapping
    @ApiMessage("Upload single file")
    public ResponseEntity<UploadFileResponse> uploadSingleFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, AppException {

        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.EMPTY_FILE);
        }

        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValidExtension = allowedExtensions.stream()
                .anyMatch(ext -> file.getOriginalFilename().toLowerCase().endsWith("." + ext));
        if (!isValidExtension) {
            throw new AppException(ErrorCode.EXTENSIONS_FILE);
        }

        fileService.createDirectory(baseURI + folder);

        String fileName = fileService.store(file, folder);
        UploadFileResponse res = new UploadFileResponse(fileName, Instant.now());

        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    @ApiMessage("Download a file")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder)
            throws AppException, URISyntaxException, FileNotFoundException {
        if (fileName == null || folder == null) {
            throw new AppException(ErrorCode.MISSING_PARAM);
        }

        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new AppException(ErrorCode.FILE_NOTFOUND);
        }

        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
