package com.project.befree.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;
import java.util.List;


@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Place {

    private Long pid; // 같은 날 place의 순서
    private String title;
    private String contentId;
    private String contentTypeId;
    @Builder.Default
    private int days = 1;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] facilities;
    private String mapx;
    private String mapy;

    public void changePid(Long pid) {
        this.pid = pid;
    }

    // 직렬화 및 역직렬화 메서드 추가
    public List<String> getFacilities() {
        if (this.facilities == null) {
            return null;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(this.facilities);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (List<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFacilities(List<String> facilities) {
        if (facilities == null) {
            this.facilities = null;
        } else {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(facilities);
                this.facilities = baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                this.facilities = null;
            }
        }
    }
}
