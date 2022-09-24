package hu.ponte.hr.controller;

import hu.ponte.hr.model.entity.ImageMeta;
import hu.ponte.hr.services.ImageStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
@RequestMapping("api/images")
@AllArgsConstructor
@Slf4j
public class ImagesController {

    private ImageStore imageStore;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
		log.debug("List all image");
        return imageStore.getImages();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) {
        log.debug("Get image preview");
        ImageMeta imageMeta = imageStore.getImageMeta(id);
        response.setContentType(imageMeta.getMimeType());
        response.setContentLength((int) imageMeta.getSize());

        try {
            FileCopyUtils.copy(imageStore.getImageToPreview(id), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error("Failed to load image preview! Error message: {}", e.getMessage());
        }
    }

}
