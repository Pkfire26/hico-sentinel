package com.hico.customrepo;

import java.util.List;
import com.hico.models.Teacher;

public interface TeacherRepositoryCustom {

	List<Teacher> findAllTeachersForSchool(String id);

}

