package com.activity.bookappmanager;

import java.util.List;

public class BookResponse {
    public List<Item> items;

    public static class Item {
        public VolumeInfo volumeInfo;
    }

    public static class VolumeInfo {
        public String title;
        public List<String> authors;
        public String description;
        public ImageLinks imageLinks;

        // Getter methods
        public String getTitle() {
            return title;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public String getDescription() {
            return description;
        }

        public ImageLinks getImageLinks() {
            return imageLinks;
        }
    }

    public static class ImageLinks {
        public String thumbnail;
    }
}
