package hu.ponte.hr.controller;

import hu.ponte.hr.model.entity.ImageMeta;
import hu.ponte.hr.services.ImageStore;
import lombok.AllArgsConstructor;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("api/images")
@AllArgsConstructor
public class ImagesController {

    private ImageStore imageStore;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
		return imageStore.getImages();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) {
        ImageMeta imageMeta = imageStore.getImageMeta(id);
        response.setContentType(imageMeta.getMimeType());
        response.setContentLength((int) imageMeta.getSize());

        try {
            FileCopyUtils.copy(imageStore.getImageToPreview(id), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
