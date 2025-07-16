package com.tallerwebi.presentacion.dto;

public class ResultadoGenerarImagenIA {
    private String task_id;
    private String task_status;
    private String[] generated;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String[] getGenerated() {
        return generated;
    }

    public void setGenerated(String[] generated) {
        this.generated = generated;
    }
}
