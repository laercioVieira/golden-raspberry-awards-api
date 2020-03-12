package br.com.laersondev.goldenraspberryawardsapi.dto;

import java.io.Serializable;
import java.text.MessageFormat;

public class MovieDeleteResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;

    public MovieDeleteResponse(final int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} [id: {1}]", //
                getClass().getSimpleName(), getId());
    }

}
