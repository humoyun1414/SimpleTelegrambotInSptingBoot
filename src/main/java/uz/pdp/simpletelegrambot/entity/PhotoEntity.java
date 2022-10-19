package uz.pdp.simpletelegrambot.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "photos")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//
//    @Lob
//    @Column(name = "content")
//    private byte[] content;

    @Column(columnDefinition = "TEXT")
    private String fileId;

//    @JsonSetter("content")
//    public void setContent(String content) throws UnsupportedEncodingException {
//        this.content = Base64.decode(Arrays.toString(content.getBytes(StandardCharsets.UTF_8)));
//    }
//
//    public byte[] getContent() {
//        return content;
//    }

}
