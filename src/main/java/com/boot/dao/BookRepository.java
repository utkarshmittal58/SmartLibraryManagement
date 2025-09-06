package com.boot.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boot.Entites.Book;
import com.boot.Entites.User;

public interface BookRepository extends JpaRepository<Book, Integer>{
 
	@Query("from Book as b where b.user.id=:userid")
	public Page<Book> findBooksByUser(@Param("userid")int userid,Pageable pageable);
	
	
	public List<Book> findByNameContainingAndUser(String name,User user);
}
