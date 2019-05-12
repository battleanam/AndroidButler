package com.hanayue.service.note.dao;

import com.hanayue.service.common.dao.BaseDao;
import com.hanayue.service.note.model.Note;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteDao extends BaseDao<Note> {
}
