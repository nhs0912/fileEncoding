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

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
        String myIp = request.getRemoteAddr();
        log.info("myIp = {}", myIp);
        //db 저장
        Item item = new Item();
        item.setFiles(storeFiles);
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

    private String findOriginalName(Long id, String fileName) {
        Item item = itemRepository.findById(id);
        List<UploadFile> uploadFiles = item.getFiles();
        for (UploadFile uploadFile : uploadFiles) {
            if (uploadFile.getSaveName().equals(fileName)) {
                return uploadFile.getOriginalName();
            }
        }
        throw new IllegalArgumentException("파일 이름이 잘못되었습니다.");
    }
}
