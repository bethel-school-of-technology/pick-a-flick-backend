package com.pickaflick.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickaflick.models.Tag;

public interface ITagRepo extends JpaRepository<Tag, Long> {

}
