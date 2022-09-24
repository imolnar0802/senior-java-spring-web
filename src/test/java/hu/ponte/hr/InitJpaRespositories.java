package hu.ponte.hr;

import java.util.Base64;

import hu.ponte.hr.model.entity.Image;
import hu.ponte.hr.model.entity.ImageMeta;
import hu.ponte.hr.model.repository.ImageMetaRepository;
import hu.ponte.hr.model.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class InitJpaRespositories {

    @Autowired
    protected ImageRepository imageRepository;

    @Autowired
    protected ImageMetaRepository imageMetaRepository;

    protected Image generateImage(String name, String mimeType, String digitalSign, String content) {
        ImageMeta imageMeta = ImageMeta.builder()
                .name(name)
                .mimeType(mimeType)
                .size(1024L)
                .digitalSign(Base64.getEncoder().encodeToString(digitalSign.getBytes()))
                .build();

        Image image = Image.builder()
                .imageMetaId(imageMeta.getId())
                .imageMeta(imageMeta)
                .content(Base64.getEncoder().encode(content.getBytes()))
                .build();

        return imageRepository.saveAndFlush(image);
    }

    protected void clearRepositories() {
        imageRepository.deleteAll();
        imageRepository.flush();

        imageMetaRepository.deleteAll();
        imageMetaRepository.flush();
    }
}
