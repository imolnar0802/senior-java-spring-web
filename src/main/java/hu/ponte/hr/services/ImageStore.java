package hu.ponte.hr.services;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import hu.ponte.hr.model.entity.ImageMeta;
import hu.ponte.hr.model.repository.ImageMetaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageStore {

    private ImageMetaRepository imageMetaRepository;

    public List<ImageMeta> getImages() {
        return imageMetaRepository.findAll();
    }

    public ImageMeta getImage(String id) {
        Optional<ImageMeta> image = imageMetaRepository.findById(id);
        return image.orElse(null);
    }

    public void saveImage(MultipartFile file) {
        try {
            ImageMeta imageMeta = ImageMeta.builder()
                    .name(file.getName())
                    .digitalSign(Base64.getEncoder().encodeToString(file.getBytes()))
                    .size(file.getSize())
                    .mimeType(file.getContentType())
                    .build();

            imageMetaRepository.saveAndFlush(imageMeta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
