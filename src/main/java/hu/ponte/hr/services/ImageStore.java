package hu.ponte.hr.services;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import hu.ponte.hr.model.entity.Image;
import hu.ponte.hr.model.entity.ImageMeta;
import hu.ponte.hr.model.repository.ImageMetaRepository;
import hu.ponte.hr.model.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageStore {

    private ImageMetaRepository imageMetaRepository;
    private ImageRepository imageRepository;

    private SignService signService;

    public List<ImageMeta> getImages() {
        return imageMetaRepository.findAll();
    }

    public ImageMeta getImageMeta(String id) {
        Optional<ImageMeta> imageMeta = imageMetaRepository.findById(id);
        return imageMeta.orElseThrow(() -> new NoSuchElementException("ImageMeta not found with id: " + id));
    }

    public BufferedInputStream getImageToPreview(String id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Image not found with id: " + id));
        return new BufferedInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(image.getContent())));
    }

    public void saveImage(MultipartFile file) {
        try {
            ImageMeta imageMeta = ImageMeta.builder()
                    .name(file.getName())
                    .digitalSign(signService.sign(new String(file.getBytes())))
                    .size(file.getSize())
                    .mimeType(file.getContentType())
                    .build();

            Image image = Image.builder()
                    .imageMetaId(imageMeta.getId())
                    .imageMeta(imageMeta)
                    .content(Base64.getEncoder().encode(file.getBytes()))
                    .build();

            imageRepository.saveAndFlush(image);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
