package com.example.category.controller;

import com.example.category.entity.Category;
import com.example.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "5") int size,
                       Model model) {
        Page<Category> categoryPage = categoryService.findAll(keyword, page, size);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "category/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "category/form";
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        model.addAttribute("category", category);
        return "category/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("category") Category category,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "category/form";
        }
        category.setId(id);
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        model.addAttribute("category", category);
        return "category/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }
}


