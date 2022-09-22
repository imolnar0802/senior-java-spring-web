package hu.ponte.hr.controller;


import hu.ponte.hr.model.entity.ImageMeta;
import hu.ponte.hr.services.ImageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@RestController()
@RequestMapping("api/images")
public class ImagesController {

    @Autowired
    private ImageStore imageStore;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
		return imageStore.getImages();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) {
        ImageMeta imageMeta = imageStore.getImage(id);
        response.setContentType(imageMeta.getMimeType());
        response.setContentLength((int) imageMeta.getSize());
        final BufferedInputStream bif = new BufferedInputStream(
                new ByteArrayInputStream(Base64.getDecoder().decode(
                        imageMeta.getDigitalSign().getBytes(StandardCharsets.UTF_8))
                )
        );
        try {
            FileCopyUtils.copy(bif, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
