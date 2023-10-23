package com.justone.fileencoding.upload.controller;

import com.justone.fileencoding.upload.controller.dto.SaveItemRequest;
import com.justone.fileencoding.upload.domain.Item;
import com.justone.fileencoding.upload.domain.UploadFile;
import com.justone.fileencoding.upload.repository.ItemRepository;
import com.justone.fileencoding.upload.service.FileStoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final FileStoreService fileStoreService;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute Item form) {
        return "item-form";
    }


    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute SaveItemRequest form, RedirectAttributes redirectAttributes,
                           HttpServletRequest request) throws IOException {

        log.info("form = {}", form);
        List<UploadFile> storeFiles = fileStoreService.storeFiles(form.getFiles());
//        List<UploadFile> storeEucKrFiles = fileStoreService.storeFiles(form.getFiles());
        String myIp = request.getRemoteAddr();
        log.info("myIp = {}", myIp);
        //db 저장
        Item item = new Item();
        item.setFiles(storeFiles);
//        item.setFiles(storeEucKrFiles);
        item.setClientIp(myIp);
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        log.info("items view 시작");
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    @GetMapping("/attach/{id}/{fileName}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long id, @PathVariable String fileName) throws MalformedURLException {
        UrlResource urlResource = new UrlResource("file:" + fileStoreService.getFullPath(fileName));

        String originalName = findOriginalName(id, fileName);
        String encodeUploadFileName = UriUtils.encode(originalName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; fileName = \"" + encodeUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    @GetMapping("/attach/{id}/{fileName}/euckr")
    public ResponseEntity<Resource> downloadUTF8ToEucKrAttach(@PathVariable Long id, @PathVariable String fileName) throws IOException {
        String eucKrFullPath = fileStoreService.getFullPath(fileName);
        String originalFullPath = fileStoreService.getFullPath(fileName);
        UrlResource urlResource = new UrlResource("file:" + eucKrFullPath);
        changeUTF8EncodingFile(originalFullPath, fileName);

        String originalName = findOriginalName(id, fileName);
        String encodeUploadFileName = UriUtils.encode(originalName, "UTF-8");

        String contentDisposition = "attachment; fileName = \"" + encodeUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    private void changeUTF8EncodingFile(String saveFileFullPath, String fileName) throws IOException {

        String eucKrFullPath = fileStoreService.getFullPath(fileName); //euc_kr 변환파일 저장 경로

        //원본 파일 경로
        File file = new File(saveFileFullPath);
        FileInputStream inputStream = new FileInputStream(file);

        //UTF-8 to EUC-KR
        byte[] bytes = inputStream.readAllBytes();

        for (byte aByte : bytes) {
            log.info(aByte+" ");
        }
        //UTF-8로 decode한다.
        String beforeConvertedText = new String(bytes, StandardCharsets.UTF_8);
        String afterConvertedText = new String(beforeConvertedText.getBytes(Charset.forName("EUC-KR")), Charset.forName("EUC-KR"));
        log.info("beforeConvertedText = {}", beforeConvertedText);
        log.info("afterConvertedText = {}", afterConvertedText);
        log.info("eucKr = {}" , Charset.forName("EUC-KR"));
        File outputFile = new File(eucKrFullPath);
        Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), Charset.forName("EUC-KR"));
//        log.info("encoding 검사 outputFile: {}" , FileEncodingDe);

        writer.write(afterConvertedText);
        writer.close();

    }


    private String findOriginalName(Long id, String fileName) {
        Item item = itemRepository.findById(id);
        List<UploadFile> uploadFiles = item.getFiles();
        for (UploadFile uploadFile : uploadFiles) {
            if (uploadFile.fullSaveFileName().equals(fileName)) {
                return uploadFile.fullOriginalFileName();
            }
        }
        throw new IllegalArgumentException("파일 이름이 잘못되었습니다.");
    }
}
