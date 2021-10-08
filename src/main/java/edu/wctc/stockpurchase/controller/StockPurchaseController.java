package edu.wctc.stockpurchase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.exception.ResourceNotFoundException;
import edu.wctc.stockpurchase.service.StockPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/stockpurchases")
public class StockPurchaseController {

    private StockPurchaseService stockPurchaseService;

    @Autowired
    public StockPurchaseController(StockPurchaseService sps) { this.stockPurchaseService = sps; }

    @GetMapping
    public List<StockPurchase> getStockPurchase() {return stockPurchaseService.getAllStock(); }

    @PostMapping
    public StockPurchase createStockPurchase(@RequestBody StockPurchase newStockPurchase) {
        newStockPurchase.setId(0);
        return stockPurchaseService.save(newStockPurchase);
    }

    @DeleteMapping("/{stockPurchaseId")
    public String deleteStockPurchase(@PathVariable String stockPurchaseId) {
        try {
            int id = Integer.parseInt(stockPurchaseId);
            stockPurchaseService.delete(id);
            return "Stock deleted: ID " + stockPurchaseId;
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock ID must be a number", e);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    e.getMessage(), e);
        }
    }

    @PatchMapping("/{stockPurchaseId}")
    public StockPurchase patchStockPurchase(@PathVariable String stockPurchaseId,
                                @RequestBody JsonPatch patch) {
        try {
            int id = Integer.parseInt(stockPurchaseId);
            return stockPurchaseService.patch(id, patch);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock ID must be a number", e);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    e.getMessage(), e);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid patch format: " + e.getMessage(), e);
        }
    }

    @PutMapping
    public StockPurchase updateStockPurchase(@RequestBody StockPurchase stockPurchase) {
        try {
            return stockPurchaseService.update(stockPurchase);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    e.getMessage(), e);
        }
    }

    @GetMapping("/{stockPurchaseId}")
    public StockPurchase getStockPurchase(@PathVariable String stockPurchaseId) {
        try {
            int id = Integer.parseInt(stockPurchaseId);
            return stockPurchaseService.getStockPurchase(id);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Student ID must be a number", e);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    e.getMessage(), e);
        }
    }
}
