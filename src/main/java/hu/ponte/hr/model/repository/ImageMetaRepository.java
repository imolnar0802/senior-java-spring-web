package hu.ponte.hr.model.repository;

import hu.ponte.hr.model.entity.ImageMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetaRepository extends JpaRepository<ImageMeta, String>, JpaSpecificationExecutor<ImageMeta> {
}
