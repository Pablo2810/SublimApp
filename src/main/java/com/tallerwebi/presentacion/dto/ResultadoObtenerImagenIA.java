package com.tallerwebi.presentacion.dto;

import java.util.List;

public class ResultadoObtenerImagenIA {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        public List<String> generated;
        public String task_id;
        public String status;
        public List<Boolean> has_nsfw;

        public List<String> getGenerated() {
            return generated;
        }

        public void setGenerated(List<String> generated) {
            this.generated = generated;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Boolean> getHas_nsfw() {
            return has_nsfw;
        }

        public void setHas_nsfw(List<Boolean> has_nsfw) {
            this.has_nsfw = has_nsfw;
        }
    }
}
