package net.yazsoft.entities;

import lombok.Data;

import java.util.List;

@Data
public class AlbumsImagesDto {
    private Albums album;
    private Images image;
    private Boolean isAlbum;
}
