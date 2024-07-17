package com.alura.literaturaChallengue.Model;

import java.util.List;

public class Book {

    private String title;
    private List<String> authors;
    private List<String> languages;
    private int downloadCount;

    public Book( String title, List<String> authors, List<String> languages, int downloadCount) {
        this.title = title;
        this.authors = authors;
        this.languages = languages;
        this.downloadCount = downloadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return
                "Titulo : " + title + '\'' +
                "Autor : " + authors +
                "Idioma : " + languages +
                "Descargas : " + downloadCount;
    }
}
