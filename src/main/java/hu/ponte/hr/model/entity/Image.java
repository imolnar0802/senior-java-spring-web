package hu.ponte.hr.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image {

    @Id
    @Column(name = "imageMeta")
    String imageMetaId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "imageMeta")
    ImageMeta imageMeta;

    @Column(name = "content", length = 1250000)
    private byte[] content;
}
