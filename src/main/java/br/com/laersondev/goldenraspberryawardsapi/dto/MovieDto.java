package br.com.laersondev.goldenraspberryawardsapi.dto;

import java.io.Serializable;
import java.text.MessageFormat;

public class MovieDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private int year;

    public int getYear() {
        return year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} [id: {1}, title: {2}, year: {3}]", //
                getClass().getSimpleName(), id, title, year);
    }

}
