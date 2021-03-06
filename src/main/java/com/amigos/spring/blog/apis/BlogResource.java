package com.amigos.spring.blog.apis;

import com.amigos.spring.blog.dtos.BlogDTO;
import com.amigos.spring.blog.models.Blog;
import com.amigos.spring.blog.services.interfaces.BlogService;
import com.amigos.spring.blog.utils.BlogsData;
import com.amigos.spring.blog.utils.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/blogs")
public class BlogResource {

    @Autowired
    private BlogService blogService;

    //https://www.baeldung.com/spring-request-param
    @GetMapping("")
    @Cacheable(value = GlobalConstants.REDIS_BLOGS_HASH)
    public BlogsData getAllBlogs(@RequestParam(name = "page", defaultValue = GlobalConstants.DEFAULT_STARTING_PAGE, required = false) Integer page,
                                 @RequestParam(name = "size", defaultValue = GlobalConstants.DEFAULT_PAGE_SIZE, required = false) Integer size,
                                 @RequestParam(name= "sort", defaultValue = GlobalConstants.DEFAULT_SORTING_FIELD) String sortField,
                                 @RequestParam(name= "direction", defaultValue = GlobalConstants.DEFAULT_SORTING_DIRECTION) String sortDirection
                                     ) {
        return blogService.getAllBlogs(page, size, sortField, sortDirection);
    }

    @GetMapping("/search")
    @Cacheable(value = GlobalConstants.REDIS_BLOGS_HASH, key = "#searchTerm")
    public List<BlogDTO> searchBlogs(@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm) {
        List<BlogDTO> blogDTOList = blogService.searchBlogsBySearchTerm("%" + searchTerm + "%");
        return blogDTOList;
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogDTO> getBlogById(@PathVariable("blogId") Long blogId) {
        BlogDTO blogsById = blogService.getBlogById(blogId);
        return new ResponseEntity<>(blogsById, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}/{blogId}")
    public ResponseEntity<Map> deleteBlogById(@PathVariable("userId") Long userId,
                                              @PathVariable("blogId") Long blogId) {
        Boolean isDeleted = blogService.deleteBlog(userId, blogId);
        return new ResponseEntity<>(Map.of("isBlogDeleted", isDeleted), HttpStatus.OK);
    }
    @GetMapping("/categories/{blogCategoryId}")
    public ResponseEntity<List<BlogDTO>> getAllBlogsByBlogCategoryId(@PathVariable("blogCategoryId") Long blogCategoryId) {
        List<BlogDTO> allByBlogCategory = blogService.getAllByBlogCategory(blogCategoryId);
        return new ResponseEntity<>(allByBlogCategory, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BlogDTO>> getAllBlogsByUserId(@PathVariable("userId") Long customerUserId) {
        List<BlogDTO> allBlogsByUser = blogService.getAllBlogsByUser(customerUserId);
        return new ResponseEntity<>(allBlogsByUser, HttpStatus.OK);
    }

    @PostMapping("/create/{userId}/{categoryId}")
    public ResponseEntity<BlogDTO> createBlog(@Valid @RequestBody Blog blog,
                                              @PathVariable("userId") Long userId,
                                              @PathVariable("categoryId") Long categoryId) {
        BlogDTO createdBlogDTO = blogService.createBlog(blog, userId, categoryId);
        return new ResponseEntity<>(createdBlogDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}/{blogId}")
    public ResponseEntity<BlogDTO> updateBlog(@Valid @RequestBody Blog blog,
                                              @PathVariable("userId") Long userId,
                                              @PathVariable("blogId") Long blogId) {
        BlogDTO blogDTO = blogService.updateBlog(blog, userId, blogId);
        return new ResponseEntity<>(blogDTO, HttpStatus.CREATED);
    }

    @PostMapping("/image/upload/{blogId}")
    public ResponseEntity uploadFeaturedImage(@RequestParam("featuredImage") MultipartFile file,
                                              @PathVariable("blogId") Long blogId) {
        BlogDTO blogDTO = blogService.uploadFeaturedImageForBlog(blogId, file);
        return new ResponseEntity(blogDTO, HttpStatus.OK);
    }

    @GetMapping("/image/get/{imageName}")
    public ResponseEntity retrieveImageURLByImageName(@PathVariable("imageName") String imageName)  {
        String fileDownloadUri = blogService.getImageURLByImageName(imageName);
        return ResponseEntity.ok(Map.of("blogFeaturedImageDownloadURL", fileDownloadUri));
    }
}
