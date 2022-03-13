package com.training.redditclone.services;

import com.training.redditclone.entities.Post;
import com.training.redditclone.exceptions.PostNotFoundException;
import com.training.redditclone.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private PostRepository postRepository;
    private AuthService authService;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("No post found with the given id"));
    }

    public List<Post> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        return posts;
    }

    public Post update(Post post) {
        return postRepository.save(post);
    }

    public void delete(Long id){
        postRepository.deleteById(id);
    }


}
