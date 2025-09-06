package com.boot.Controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.boot.Entites.Book;
import com.boot.Entites.User;
import com.boot.Helper.Message;
import com.boot.dao.BookRepository;
import com.boot.dao.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@ModelAttribute
	public void commonData(Model m,Principal principal)
	{
		String username=principal.getName();
		User user=userRepository.getUserByUserName(username);
		m.addAttribute("user",user);
	}
	
	@RequestMapping("/dashboard")
	public String userDashboard(Model m)
	{
		m.addAttribute("title","User DashBoard - Smart Library Management");
		return "user/user_dashboard";
	}
	
	@GetMapping("/addBook")
	public String addBook(Model m)
	{
		m.addAttribute("title","Add Book - Smart Library Management");
		m.addAttribute("book",new Book());
		return "user/addBook";
	}
	
	@PostMapping("/addBookForm")
	public String addBookForm(@Valid @ModelAttribute Book book,BindingResult result,@RequestParam("inputImage") MultipartFile file,Model m,Principal principal,HttpSession session)
	{
		try {
			
			if(result.hasErrors())
			{
				m.addAttribute("book",book);
				return "user/addBook";
			}
			
			m.addAttribute("title","Add Book - Smart Library Management");
			String username=principal.getName();
			User user=this.userRepository.getUserByUserName(username);
			
			if(!file.isEmpty()){
				book.setImage(file.getOriginalFilename());
				File f=new ClassPathResource("static/image").getFile();
				Path path=Paths.get(f.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			}
			else {
				book.setImage("default.png");
			}
			book.setUser(user);
			user.getBooks().add(book);
			this.userRepository.save(user);
			m.addAttribute("book",new Book());
			session.setAttribute("message",new Message("Successfully Added","alert-success"));
			return "user/addBook";
			
		} catch (Exception e) {
			m.addAttribute("title","Add Book - Smart Library Management");
			m.addAttribute("book",book);
			session.setAttribute("message",new Message("Something Went Wrong!!!"+e.getMessage(),"alert-danger"));
			return "user/addBook";
		}
		
		
		
	}
    
	@RequestMapping("/viewBooks/{page}")
	public String viewBooks(@PathVariable("page") int page,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model m,Principal principal)
	{
		m.addAttribute("title","Display Books - Smart Library Management");
		String username=principal.getName();
		User user=this.userRepository.getUserByUserName(username);
		
		Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortField).ascending() : Sort.by(sortField).descending(); 
		
		Pageable pageable=PageRequest.of(page,5,sort);
		
		Page<Book> books=this.bookRepository.findBooksByUser(user.getId(),pageable);
		
		m.addAttribute("books", books);
	    m.addAttribute("currentPage",page);
	    m.addAttribute("totalPages",books.getTotalPages());
	    
	    m.addAttribute("sortField",sortField);
	    m.addAttribute("sortDir",sortDir);
	    m.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");
		
		
		
		return "user/viewBooks";
	}
	
	@RequestMapping("/{id}/book")
	public String showBookDetail(@PathVariable("id")int id,Model m,Principal principal)
	{
		
		Book book=this.bookRepository.findById(id).get();
		
		String username=principal.getName();
		
		User user=this.userRepository.getUserByUserName(username);
		
		if(user.getId()==book.getUser().getId()) {
			m.addAttribute("book",book);
			m.addAttribute("title",book.getName());
		}
			
		return "user/bookDetail";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteBook(@PathVariable("id")int id,Model m,HttpSession session) throws IOException
	{
		Book book=this.bookRepository.findById(id).get();
		
		File delfile=new ClassPathResource("static/image").getFile();
		File f1=new File(delfile,book.getImage());
		f1.delete();
		
		book.setUser(null);
		this.bookRepository.delete(book);
		
		session.setAttribute("message",new Message("Book Deleted SuccessFully..","alert-success"));
		return "redirect:/user/viewBooks/0?sortField=Bid&sortDir=asc";
	}
	
	@RequestMapping("/update/{id}")
	public String updateBook(@PathVariable("id")int id,Model m)
	{
		m.addAttribute("title","Update Book");
		Book book=this.bookRepository.findById(id).get();
		m.addAttribute("book",book);
		return "user/updateForm";
	}
	
	@PostMapping("/updateProcess/")
	public String updateFormProcess(@Valid @ModelAttribute Book book,BindingResult result,@RequestParam("inputImage") MultipartFile file,Model m,Principal principal,HttpSession session)
	{
		try {
			String username=principal.getName();
		    User user=this.userRepository.getUserByUserName(username);
			Book OldBook=this.bookRepository.findById(book.getBid()).get();
			m.addAttribute("title","Update Book - Smart Library Management");
			
			if(result.hasErrors())
			{
				m.addAttribute("book",book);
				return "user/updateForm";
			}
			if(!file.isEmpty()){
				
				File delfile=new ClassPathResource("static/image").getFile();
				File f1=new File(delfile,OldBook.getImage());
				f1.delete();

				File f=new ClassPathResource("static/image").getFile();
				Path path=Paths.get(f.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				book.setImage(file.getOriginalFilename());
			}
			else {
				book.setImage(OldBook.getImage());
			}
			book.setUser(user);
			this.bookRepository.save(book);
			session.setAttribute("message",new Message("Your Book is Updated!!!","alert-success"));
			return "redirect:/user/"+book.getBid()+"/book";
	
		} catch (Exception e) {
			
			m.addAttribute("title","Update Book - Smart Library Management");
			m.addAttribute("book",book);
			session.setAttribute("message",new Message("Something Went Wrong!!!"+e.getMessage(),"alert-danger"));
			return "user/updateForm";
			
		}
	}
	
	@GetMapping("/profile")
	public String profile(Model m)
	{
		m.addAttribute("title","Profile - Smart Library Management");
		return "user/profile";
	}
	
	@RequestMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query,Principal principal)
	{
		String username=principal.getName();
		User user=this.userRepository.getUserByUserName(username);
		List<Book> books=this.bookRepository.findByNameContainingAndUser(query,user);
		return ResponseEntity.ok(books);
	}
}
