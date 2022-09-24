package hu.ponte.hr.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.NoSuchElementException;

import hu.ponte.hr.InitJpaRespositories;
import hu.ponte.hr.config.H2Config;
import hu.ponte.hr.config.KeysConfig;
import hu.ponte.hr.model.entity.Image;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = {
        ImageStore.class,
        SignService.class,

        H2Config.class
})
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = KeysConfig.class)
class ImageStoreTest extends InitJpaRespositories {

    private Image image1;
    private Image image2;
    private Image image3;

    @Autowired
    SignService signService;

    @Autowired
    ImageStore imageStore;

    @BeforeEach
    void setUp() {
        image1 = generateImage("Teszt_image_1", "image/png", "teszt_digital_sign_1", "teszt_content1");
        image2 = generateImage("Teszt_image_2", "image/jpeg", "teszt_digital_sign_2", "teszt_content2");
        image3 = generateImage("Teszt_image_3", "image/webp", "teszt_digital_sign_3", "teszt_content3");
    }

    @AfterEach
    void tearDown() {
        clearRepositories();
    }

    @Test
    void getImagesTest() {
        assertNotNull(imageStore.getImages(), "Get all images from DB");
        assertEquals(3, imageStore.getImages().size(), "Count images in DB");

        clearRepositories();

        assertEquals(0, imageStore.getImages().size());
    }

    @Test
    void getImageMetaTest() {
        assertEquals("Teszt_image_2", imageStore.getImageMeta(image2.getImageMetaId()).getName(), "Get ImageMeta name");
        assertEquals("image/webp", imageStore.getImageMeta(image3.getImageMetaId()).getMimeType(), "Get ImageMeta mimetype");
        assertEquals(1024L, imageStore.getImageMeta(image2.getImageMetaId()).getSize(), "Get ImageMeta size");
        }

    @Test
    void getImageMetaExceptionTest() {
        var exception = assertThrows(NoSuchElementException.class, () -> imageStore.getImageMeta("nonExistsId"));
        assertEquals("ImageMeta not found with id: nonExistsId" ,exception.getMessage(), "Find non exists ImageMeta");
    }

    @Test
    void getImageToPreviewTest() throws IOException {
        var bf = new BufferedInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(image1.getContent())));
        assertNotNull(imageStore.getImageToPreview(image1.getImageMetaId()), "Get image to preview");
        assertEquals(bf.read(), imageStore.getImageToPreview(image1.getImageMetaId()).read(), "Check image preview");
    }

    @Test
    void getImageToPreviewExceptionTest() {
        var exception = assertThrows(NoSuchElementException.class, () -> imageStore.getImageToPreview("nonExistsId"));
        assertEquals("Image not found with id: nonExistsId" ,exception.getMessage(), "Find non exists Image");
    }

    @Test
    void saveImageTest() {
        assertEquals(3, imageRepository.findAll().size(), "Count image before save");

        MockMultipartFile file = new MockMultipartFile("image", "newImage.jpeg", "image/jpeg", "mock content".getBytes());
        imageStore.saveImage(file);

        assertEquals(4, imageRepository.findAll().size(), "Count image after save");
    }

    @Test
    void saveImageExceptionTest() {
        assertEquals(3, imageRepository.findAll().size(), "Count image before save");

        assertThrows(RuntimeException.class, () -> imageStore.saveImage(null));

        assertEquals(3, imageRepository.findAll().size(), "Count image before save");
    }
}
