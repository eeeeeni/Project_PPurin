// package com.ppurin.ppurin.gallery;

// import java.io.File;
// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.multipart.MultipartFile;

// import jakarta.servlet.http.HttpSession;

// @Controller
// @RequestMapping("/gallery")
// public class postController {

//     private final postService postService;

//     @Value("${upload.dir}")
//     private String uploadDir;

//     public postController(postService postService) {
//         this.postService = postService;
//     }

//     // 게시글 작성 페이지 (관리자만)
//     @GetMapping("/write")
//     public String write(HttpSession session, Model model) {
//         if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
//             return "gallery/write";
//         }
//         return "redirect:/gallery";
//     }

//     @PostMapping("/write")
//     public String savePost(@ModelAttribute postDTO postDto, 
//      @RequestPart("image") MultipartFile image) {
//     try {
//         // 파일 저장 로직
//         String rootPath = System.getProperty("user.dir"); // 프로젝트 루트 경로
//         String fullUploadDir = rootPath + "/" + uploadDir;

//         File directory = new File(fullUploadDir);
//         if (!directory.exists()) {
//             directory.mkdirs();
//         }

//         String savedFilePath = fullUploadDir + "/" + image.getOriginalFilename();
//         image.transferTo(new File(savedFilePath));

//         postDto.setImageUrl("/" + uploadDir + "/" + image.getOriginalFilename());

//         // content 기본 값 설정
//         if (postDto.getContent() == null || postDto.getContent().isEmpty()) {
//             postDto.setContent("Default content"); // 기본 값
//         }

//         // DTO를 엔티티로 변환 후 저장
//         postService.savePost(postDto.toEntity());

//     } catch (IOException e) {
//         e.printStackTrace();
//         return "error";
//     }

//     return "redirect:/gallery";
// }

//     // 게시글 수정 페이지 (관리자만)
//     @GetMapping("/edit/{id}")
//     public String edit(@PathVariable Long id, HttpSession session, Model model) {
//         if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
//             model.addAttribute("post", postService.getPostById(id));
//             return "gallery/edit";
//         }
//         return "redirect:/gallery";
//     }

//     // 게시글 수정 처리 (관리자만 필요)
//     @PostMapping("/edit/{id}")
//     public String updatePost(
//         @PathVariable Long id,
//         @ModelAttribute postDTO postDto,
//         @RequestParam("image") MultipartFile imageFile
//     ) {
//         try {
//             if (imageFile != null && !imageFile.isEmpty()) {
//                 // 파일 저장 경로 설정
//                 File uploadPath = new File(uploadDir);
//                 if (!uploadPath.exists()) {
//                     uploadPath.mkdirs(); // 경로가 없으면 생성
//                 }

//                 String filePath = uploadDir + "/" + imageFile.getOriginalFilename();
//                 File destFile = new File(filePath);

//                 // 파일 저장
//                 imageFile.transferTo(destFile);

//                 // 저장된 파일 경로를 DTO에 설정
//                 postDto.setImageUrl("/assets/upload/" + imageFile.getOriginalFilename());
//             }

//             // 업데이트 로직
//             postService.updatePost(id, postDto.toEntity());
//             return "redirect:/gallery";
//         } catch (IOException e) {
//             e.printStackTrace();
//             return "redirect:/gallery/edit/" + id + "?error=uploadFailed"; // 에러 시 리다이렉트
//         }
//     }

//     // 게시글 세부 내용 보기
//     @GetMapping("/inner/{id}")
//     public String viewPost(@PathVariable Long id, Model model) {
//         model.addAttribute("post", postService.getPostById(id));
//         return "gallery/inner";
//     }
// }

package com.ppurin.ppurin.gallery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Arrays;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/gallery")
public class postController {

    private final postService postService;

    @Value("${upload.dir}")
    private String uploadDir;

    public postController(postService postService) {
        this.postService = postService;
    }

    // 게시글 작성 페이지 (관리자만)
    @GetMapping("/write")
    public String write(HttpSession session, Model model) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "gallery/write";
        }
        return "redirect:/gallery";
    }

    // 게시글 작성 처리
    @PostMapping("/write")
    public String savePost(@ModelAttribute postDTO postDto, @RequestPart("image") MultipartFile image) {
        try {
            // 파일 저장 경로 생성
            String rootPath = System.getProperty("user.dir");
            String fullUploadDir = rootPath + "/" + uploadDir;

            File directory = new File(fullUploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String savedFilePath = fullUploadDir + "/" + image.getOriginalFilename();
            image.transferTo(new File(savedFilePath));

            postDto.setImageUrl("/" + uploadDir + "/" + image.getOriginalFilename());

            // content 기본 값 설정
            if (postDto.getContent() == null || postDto.getContent().isEmpty()) {
                postDto.setContent("Default content"); // 기본 내용
            }

            // DTO를 엔티티로 변환 후 저장
            postService.savePost(postDto.toEntity());

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        return "redirect:/gallery";
    }

    // 갤러리 페이지
    @GetMapping
    public String gallery(Model model) {
        // postEntity 리스트를 postDTO 리스트로 변환
        List<postDTO> posts = postService.getAllPosts().stream().map(entity -> {
            postDTO dto = new postDTO(entity.getTitle(), entity.getContent(), entity.getImageUrl(), entity.getDate(), entity.getTags());
            if (entity.getTags() != null) {
                dto.setTagList(Arrays.asList(entity.getTags().split(",")));
            }
            return dto;
        }).toList();
    
        model.addAttribute("posts", posts);
        return "gallery/gallery";
    }

    // 게시글 세부 내용 보기
    @GetMapping("/inner/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "gallery/inner";
    }
}