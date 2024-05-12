package com.likelion12th.shop.controller;

import com.likelion12th.shop.dto.ItemFormDto;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final ItemService itemService;

    // 상품 업로드
    @PostMapping("/new")
    public ResponseEntity<Long> createItem(@RequestPart(name = "itemFormDto") ItemFormDto itemFormDto,
                                           @RequestPart(name = "itemImg", required = false) MultipartFile itemImg) {
        if (itemImg == null) {

            try {
                Long itemId = itemService.saveItem(itemFormDto);

                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            try {
                Long itemId = itemService.saveItem(itemFormDto, itemImg);

                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    // 전체 상품 조회
    @GetMapping
    public ResponseEntity<List<ItemFormDto>> getItems() {
        return ResponseEntity.ok().body(itemService.getItems());
    }

    // 특정 상품 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemFormDto> getItemById(@PathVariable Long itemId) {
        try {
            ItemFormDto itemFormDto = itemService.getItemById(itemId);

            return ResponseEntity.ok().body(itemFormDto);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemFormDto>> searchItemsByName(@RequestParam String itemName) {
        try {
            List<ItemFormDto> itemFormDtos = itemService.getItemsByName(itemName);
            return ResponseEntity.ok().body(itemFormDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}