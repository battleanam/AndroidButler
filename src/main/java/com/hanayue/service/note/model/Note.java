package com.hanayue.service.note.model;

import lombok.Data;

@Data
public class Note {
    private String id;
    private String userId;
    private int noteId;
    private String title;
    private String content;
    private long noteTime;
    private long createTime;
}
