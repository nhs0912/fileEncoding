package com.justone.fileencoding.upload.controller;

import com.justone.fileencoding.upload.controller.dto.SaveItemRequest;
import com.justone.fileencoding.upload.domain.Item;
import com.justone.fileencoding.upload.domain.UploadFile;
import com.justone.fileencoding.upload.repository.ItemRepository;
import com.justone.fileencoding.upload.service.FileStoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private static final Charset EUC_KR = Charset.forName("euc-kr");
    private final ItemRepository itemRepository;
    private final FileStoreService fileStoreService;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute Item form) {
        return "item-form";
    }


    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute SaveItemRequest form, RedirectAttributes redirectAttributes,
                           HttpServletRequest request) throws IOException {

        List<UploadFile> storeFiles = fileStoreService.storeFiles(form.getFiles());
        String myIp = request.getRemoteAddr();
        log.info("myIp = {}", myIp);
        //db 저장
        Item item = new Item();
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

//    @GetMapping("/attach/{id}/{fileName}")
//    public ResponseEntity<Resource> downloadAttach(@PathVariable Long id, @PathVariable String fileName) throws MalformedURLException {
//        UrlResource urlResource = new UrlResource("file:" + fileStoreService.getFullPath(fileName));
//
//        UploadFile originalFile = findUploadFile(id, fileName);
//        String encodeUploadFileName = UriUtils.encode(originalFile.getSaveName(), StandardCharsets.UTF_8);
//
//        String contentDisposition = "attachment; fileName = \"" + encodeUploadFileName + "\"";
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(urlResource);
//    }

//    @GetMapping("/attach/{id}/{fileName}/euckr")
//    public ResponseEntity<Resource> downloadUTF8ToEucKrAttach(@PathVariable Long id, @PathVariable String fileName) throws IOException {
//        String originalFullPath = fileStoreService.getFullPath(fileName);
//        UploadFile uploadFile = findUploadFile(id, fileName);
//        UrlResource urlResource = new UrlResource("file:" + originalFullPath);
//        changeUTF8EncodingFile(originalFullPath, uploadFile);
//
//        String downloadFileName = uploadFile.getOriginalName() + "_eucKr" + "." + uploadFile.getExtendedName();
//        String encodeUploadFileName = UriUtils.encode(downloadFileName, StandardCharsets.UTF_8);
//
//        String contentDisposition = "attachment; fileName = \"" + encodeUploadFileName + "\"";
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(urlResource);
//    }


    private void changeUTF8EncodingFile(String saveFileFullPath, UploadFile uploadFile) throws IOException {

        //원본 파일 경로
        Path path = Paths.get(saveFileFullPath);
        String utf8Text = Files.readString(path, StandardCharsets.UTF_8);

        //UTF-8 to EUC-KR
        String eucKrText = new String(utf8Text.getBytes(), EUC_KR);



    }

//    private UploadFile findUploadFile(Long id, String fileName) {
//        Item item = itemRepository.findById(id);
//        List<UploadFile> uploadFiles = item.getFiles();
//        for (UploadFile uploadFile : uploadFiles) {
//            if (uploadFile.fullSaveFileName().equals(fileName)) {
//                return uploadFile;
//            }
//        }
//        throw new IllegalArgumentException("파일 이름이 잘못되었습니다.");
//    }
}
