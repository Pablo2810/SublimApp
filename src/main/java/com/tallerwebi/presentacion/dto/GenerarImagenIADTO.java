package com.tallerwebi.presentacion.dto;

public class GenerarImagenIADTO {
    public String prompt;
    public int structure_strength;
    public int adherence;
    public int hdr;
    public String resolution;
    public String aspect_ratio;
    public String model;
    public int creative_detailing;
    public String engine;
    public boolean fixed_generation;
    public boolean filter_nsfw;

    public GenerarImagenIADTO(String prompt, String model) {
        this.prompt = prompt;
        this.filter_nsfw = true;
        this.fixed_generation = false;
        this.engine = "automatic";
        this.creative_detailing = 35;
        this.model = model;
        this.aspect_ratio = "square_1_1";
        this.resolution = "1k";
        this.hdr = 50;
        this.adherence = 50;
        this.structure_strength = 50;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getStructure_strength() {
        return structure_strength;
    }

    public void setStructure_strength(int structure_strength) {
        this.structure_strength = structure_strength;
    }

    public int getAdherence() {
        return adherence;
    }

    public void setAdherence(int adherence) {
        this.adherence = adherence;
    }

    public int getHdr() {
        return hdr;
    }

    public void setHdr(int hdr) {
        this.hdr = hdr;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getAspect_ratio() {
        return aspect_ratio;
    }

    public void setAspect_ratio(String aspect_ratio) {
        this.aspect_ratio = aspect_ratio;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCreative_detailing() {
        return creative_detailing;
    }

    public void setCreative_detailing(int creative_detailing) {
        this.creative_detailing = creative_detailing;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public boolean isFixed_generation() {
        return fixed_generation;
    }

    public void setFixed_generation(boolean fixed_generation) {
        this.fixed_generation = fixed_generation;
    }

    public boolean isFilter_nsfw() {
        return filter_nsfw;
    }

    public void setFilter_nsfw(boolean filter_nsfw) {
        this.filter_nsfw = filter_nsfw;
    }
}
